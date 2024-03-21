import { getUserList } from "@/api/message";
import defaultUserLogo from "@/assets/message/default_user.png";
import { computed, ref, type Ref } from "vue";
import {
  MessageCardData,
  MessageReceiveData
} from "@/views/message/utils/types";
import { useMessageStore } from "@/store/modules/message";
import {
  buildMessageItem,
  formatUserId,
  isNormalUserSend,
  isPersonalMessage,
  formatMessageTime,
  isCurrentWindow,
  getOriginUserId
} from "@/views/message/utils/message";
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
    await initData();
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
          msgType: cur.msgType
        });
      });
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
    }
  });
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
      list.value.splice(index, 1);
      list.value.unshift(msgItem);
      msgItem.message = data.content;
      msgItem.createTime = formatMessageTime(data.createTime);
      msgItem.createTimeText = formatMessageTime(data.createTime);
      msgItem.msgType = data.msgType;
      if (_isCurrentWindow) {
        return;
      }
      msgItem.readDot += 1;
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
    }
  });
  messageServer?.chatEmitter.on("clearDot", ({ id, pId }) => {
    if (pId !== currentAnchor.value) return;
    if (id) {
      for (let i = 0; i < list.value.length; i++) {
        if (list.value[i].sendUserId === id) {
          list.value[i].readDot = 0;
          break;
        }
      }
    } else {
      for (let i = 0; i < list.value.length; i++) {
        list.value[i].readDot = 0;
      }
    }
  });

  return {
    list,
    currentPage,
    error,
    loading,
    finished,
    disabled,
    refreshing,
    onLoad,
    onRefresh
  };
};
