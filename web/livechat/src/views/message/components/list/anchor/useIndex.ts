import { getAnchorList } from "@/api/message";

import { computed, ref, type Ref } from "vue";
import {
  MessageCardData,
  MessageReceiveData
} from "@/views/message/utils/types";
import {
  buildMessageItem,
  formatUserId,
  isNormalUserSend,
  isPersonalMessage,
  formatMessageTime
} from "@/views/message/utils/message";
import { useMessageStore } from "@/store/modules/message";
// import { useUserStore } from "@/store/modules/user";
export const useIndex = ({
  currentUser,
  currentAnchor
}: {
  currentUser: Ref<string>;
  currentAnchor: Ref<string>;
}) => {
  // const userStore = useUserStore();
  const list = ref<MessageCardData[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const currentPage = ref(0);
  const disabled = computed(() => {
    return loading.value || finished.value;
  });
  const pageSize = ref(10);
  const onLoad = async () => {
    loading.value = true;
    await initData(currentPage.value + 1);
  };
  const onRefresh = () => {
    // 清空列表数据
    finished.value = false;
    loading.value = true;
    initData();
  };

  // 重新排序 置顶账号需要在最前面
  const initTopList = (_list: MessageCardData[]) => {
    return [
      ..._list.filter(item => item.setTop),
      ..._list.filter(item => !item.setTop)
    ];
  };
  const initData = async (page = 1) => {
    try {
      error.value = false;
      const response = await getAnchorList({
        current: page,
        size: pageSize.value
      });
      if (page <= 1) {
        list.value = [];
      }
      const data = response.records || [];
      const renderData = data.map<MessageCardData>(cur => {
        const anchorId = (cur.anchorId || "") + "";
        return buildMessageItem({
          id: (cur.id || "") + "",
          sendUserId: "",
          anchorId: anchorId,
          name: cur.nick,
          cover: cur.avatar,
          message: cur.content,
          readDot: Number(cur.noReadSum),
          createTime: cur.createTime,
          msgType: cur.msgType,
          liveStatus: cur.liveStatus,
          setTop: cur.setTop
        });
      });
      // const renderData1 = [];
      // for (let i = 0; i < 20; i++) {
      //   renderData1.push({
      //     id: "awdawd" + i,
      //     anchorId: list.value.length + "awdawd" + i,
      //     name: "cur.nick",
      //     cover: "",
      //     message: "",
      //     readDot: i,
      //     createTime: "",
      //     createTimeText: "",
      //     type: MessageCardType.MESSAGE
      //   });
      // }
      list.value.push(...renderData);
      currentPage.value = page;
      if (data.length === 0 || list.value.length >= response.total) {
        finished.value = true;
      }
    } catch (err) {
      error.value = true;
      finished.value = true;
    } finally {
      loading.value = false;
      refreshing.value = false;
    }
  };
  const messageStore = useMessageStore();
  const messageServer = messageStore.getMessageServer();

  messageServer?.chatEmitter.on("anchorReadMessage", res => {
    const data = res.data;
    if (!data.anchorId) return;
    const index = list.value.findIndex(cur => cur.anchorId === data.anchorId);
    if (index !== -1) {
      const msgItem = list.value[index];
      msgItem.readDot = data.noReadSum;
    } else {
      const anchorId = (data.anchorId || "") + "";
      const newMsg = buildMessageItem({
        id: list.value.length + "_" + anchorId,
        anchorId,
        sendUserId: "",
        name: data.nick,
        cover: data.avatar,
        message: "",
        readDot: data.noReadSum
      });
      list.value.unshift(newMsg);
    }
  });
  messageServer?.chatEmitter.on("message", res => {
    const data = res.data || ({} as MessageReceiveData["data"]);
    if (!isPersonalMessage(data)) return;
    const isCurrentAnchor = data.anchorId === currentAnchor.value;
    // 当前消息是否是普通用户发送true是，false 代表主播运营助手
    const isNormalUser = isNormalUserSend(data);
    // 当前的用户id， isNormalUser为true则聊天的用户id是from，false则是to
    const dataUserId = formatUserId(
      data.anchorId,
      isNormalUser ? data.from : data.to
    );
    const index = list.value.findIndex(cur => cur.anchorId === data.anchorId);
    if (index !== -1) {
      const msgItem = list.value[index];
      // 置顶的消息，位置不做改变
      if (!msgItem.setTop) {
        list.value.splice(index, 1);
        list.value.unshift(msgItem);
      }

      msgItem.message = data.content;
      msgItem.createTime = formatMessageTime(data.createTime);
      msgItem.createTimeText = formatMessageTime(data.createTime);
      msgItem.msgType = data.msgType;

      if (
        !isNormalUser ||
        (isCurrentAnchor && dataUserId === currentUser.value)
      )
        return;
      msgItem.readDot += 1;
    } else {
      const anchorId = (data.anchorId || "") + "";
      const newMsg = buildMessageItem({
        id: (data.id || "") + "",
        anchorId,
        sendUserId: "",
        // isNormalUser为true主播头像则是to，false是from
        name: isNormalUser ? data.toNickName : data.fromNickName,
        cover: isNormalUser ? data.toAvatar : data.fromAvatar,
        message: data.content,
        readDot: 1,
        createTime: data.createTime,
        msgType: data.msgType
      });
      list.value.unshift(newMsg);
    }
  });

  const clearDot = (
    pId: string,
    readDot: number,
    list: Ref<MessageCardData[]>
  ) => {
    if (pId) {
      for (let i = 0; i < list.value.length; i++) {
        if (list.value[i].anchorId === pId) {
          list.value[i].readDot -= readDot || 0;
          break;
        }
      }
    }
  };

  messageServer?.chatEmitter.on("clearDot", ({ pId, readDot }) => {
    clearDot(pId, readDot, list);
  });

  const setLocalToping = (item: MessageCardData) => {
    const index = list.value.findIndex(cur => cur.anchorId === item.anchorId);

    if (index === -1) return;
    const msgItem = list.value[index];
    list.value.splice(index, 1);
    list.value.unshift(msgItem);
  };
  const setLocalUnToping = (item: MessageCardData) => {
    const index = list.value.findIndex(cur => cur.anchorId === item.anchorId);
    if (index !== -1) {
      const msgItem = list.value[index];
      msgItem.setTop = false;
      list.value.splice(index, 1);
      list.value.unshift(msgItem);
    }
  };

  const concatList = computed(() => {
    return initTopList(list.value);
  });

  return {
    list: concatList,
    currentPage,
    error,
    loading,
    finished,
    disabled,
    refreshing,
    onLoad,
    onRefresh,
    setLocalToping,
    setLocalUnToping
  };
};
