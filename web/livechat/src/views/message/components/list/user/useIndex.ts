import { getUserList } from "@/api/message";
import defaultUserLogo from "@/assets/message/default_user.png";
import { computed, ref, type Ref } from "vue";
import {
  MessageCardData,
  MessageReceiveData
} from "@/views/message/utils/types";
import { useMessageStore } from "@/store/modules/message";
import { useMessageUnReadCountStore } from "@/store/modules/message/unReadCount";
import {
  buildMessageItem,
  formatUserId,
  isNormalUserSend,
  isPersonalMessage,
  formatMessageTime,
  isCurrentWindow,
  getOriginUserId
} from "@/views/message/utils/message";
import { isSafeRegion } from "../../chat/scroll";
// import { useUserStore } from "@/store/modules/user";
export const useIndex = ({
  currentAnchor,
  currentUser
}: {
  currentAnchor: Ref<string>;
  currentUser: Ref<string>;
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
  const messageUnReadCountStore = useMessageUnReadCountStore();
  const pageSize = ref(10);
  const onLoad = async () => {
    loading.value = true;
    await initData(currentPage.value + 1);
  };
  const onRefresh = async () => {
    // 清空列表数据
    finished.value = false;
    loading.value = true;
    list.value = [];
    // topList.value = [];
    await initData();
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
      const response = await getUserList({
        current: page,
        size: pageSize.value,
        anchorId: currentAnchor.value
      });
      if (page <= 1) {
        list.value = [];
        messageUnReadCountStore.initCount([]);
      }
      const data = response.records || [];
      const renderData = data.map<MessageCardData>(cur => {
        const anchorId = (cur.anchorId || currentAnchor.value) + "";
        const fromId = (cur.fromId || "") + "";
        return buildMessageItem({
          id: (cur.id || "") + "",
          anchorId: anchorId,
          sendUserId: formatUserId(anchorId, fromId),
          name: cur.nick,
          cover: cur.avatar || defaultUserLogo,
          message: cur.content,
          readDot: Number(cur.noReadSum),
          createTime: cur.createTime,
          msgType: cur.msgType,
          setTop: cur.setTop
        });
      });
      list.value.push(...renderData);
      // initTopList(renderData);
      messageUnReadCountStore.initCount(list.value);
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
  const currentUserId = computed(() => {
    return getOriginUserId(currentUser.value);
  });
  const messageStore = useMessageStore();
  const messageServer = messageStore.getMessageServer();

  messageServer?.chatEmitter.on("anchorReadMessage", res => {
    const data = res.data;
    if (!data.anchorId || data.anchorId !== currentAnchor.value) return;
    const userData = data.chatUserInfo;
    if (!userData.userId) return;
    const dataUserId = formatUserId(data.anchorId, userData.userId);
    const index = list.value.findIndex(cur => cur.sendUserId === dataUserId);
    if (index !== -1) {
      const msgItem = list.value[index];
      msgItem.readDot = userData.noReadSum;
      messageUnReadCountStore.setCount(msgItem.sendUserId, msgItem.readDot);
    } else {
      const anchorId = (data.anchorId || "") + "";
      const newMsg = buildMessageItem({
        id: (userData.id || "") + "",
        anchorId,
        sendUserId: dataUserId,
        name: userData.nick,
        cover: userData.avatar || defaultUserLogo,
        message: userData.content,
        readDot: userData.noReadSum,
        createTime: userData.createTime,
        msgType: userData.msgType
      });
      list.value.unshift(newMsg);
      messageUnReadCountStore.setCount(newMsg.sendUserId, newMsg.readDot);
    }
  });
  const unreadCount = computed(
    () => messageUnReadCountStore.unReadRecord?.[currentUser.value] || 0
  );
  messageServer?.chatEmitter.on("message", res => {
    const data = res.data || ({} as MessageReceiveData["data"]);
    if (!isPersonalMessage(data) || data.anchorId !== currentAnchor.value) {
      return;
    }
    const isNormalUser = isNormalUserSend(data);
    // 判断消息来源是否属于当前窗口
    const _isCurrentWindow = isCurrentWindow(data, currentUserId.value);
    const dataUserId = formatUserId(
      data.anchorId,
      isNormalUser ? data.from : data.to
    );
    const index = list.value.findIndex(cur => cur.sendUserId === dataUserId);
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
      // 在当前窗口，显示了未读条数或者在滚动条底部，则不触发
      if (_isCurrentWindow && !unreadCount.value && isSafeRegion()) {
        return;
      }
      msgItem.readDot += 1;
      messageUnReadCountStore.setCount(msgItem.sendUserId, msgItem.readDot);
    } else {
      const anchorId = (data.anchorId || "") + "";
      const newMsg = buildMessageItem({
        id: (data.id || "") + "",
        anchorId,
        sendUserId: dataUserId,
        name: data.fromNickName,
        cover: data.fromAvatar || defaultUserLogo,
        message: data.content,
        readDot: 1,
        createTime: data.createTime,
        msgType: data.msgType
      });
      list.value.unshift(newMsg);
      messageUnReadCountStore.setCount(newMsg.sendUserId, newMsg.readDot);
    }
  });

  const clearDot = (id: string, list: Ref<MessageCardData[]>) => {
    if (id) {
      for (let i = 0; i < list.value.length; i++) {
        if (list.value[i].sendUserId === id) {
          list.value[i].readDot = 0;
          messageUnReadCountStore.setCount(list.value[i].sendUserId, 0);
          break;
        }
      }
    } else {
      for (let i = 0; i < list.value.length; i++) {
        list.value[i].readDot = 0;
        messageUnReadCountStore.setCount(list.value[i].sendUserId, 0);
      }
    }
  };

  messageServer?.chatEmitter.on("clearDot", ({ id, pId }) => {
    if (pId !== currentAnchor.value) return;
    clearDot(id, list);
    // clearDot(id, topList);
  });

  const setLocalToping = (item: MessageCardData) => {
    const index = list.value.findIndex(
      cur => cur.sendUserId === item.sendUserId
    );

    if (index === -1) return;
    const msgItem = list.value[index];
    list.value.splice(index, 1);
    list.value.unshift(msgItem);
  };
  const setLocalUnToping = (item: MessageCardData) => {
    const index = list.value.findIndex(
      cur => cur.sendUserId === item.sendUserId
    );
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
