import { getLiveList } from "@/api/message";
import { ref, type Ref, watch } from "vue";
import {
  MessageCardData,
  MessageReceiveData
} from "@/views/message/utils/types";
import {
  buildMessageItem,
  formatMessageTime,
  isGroupMessage
} from "@/views/message/utils/message";
import { useMessageStore } from "@/store/modules/message";
export const useIndex = ({ currentAnchor }: { currentAnchor: Ref<string> }) => {
  const list = ref<MessageCardData[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const onLoad = async () => {
    loading.value = true;
    await initData();
  };
  const onRefresh = async () => {
    // 清空列表数据
    finished.value = false;
    loading.value = true;
    list.value = [];
    await initData();
  };
  const initData = async () => {
    try {
      error.value = false;
      const response = await getLiveList();
      const data = response || [];
      const renderData = data.map<MessageCardData>(cur => {
        const anchorId = (cur.anchorId || "") + "";
        const fromId = (cur.fromId || "") + "";
        return buildMessageItem({
          id: (cur.id || "") + "",
          anchorId: anchorId,
          sendUserId: fromId,
          name: cur.nick,
          cover: cur.avatar,
          message: cur.content,
          readDot: Number(cur.noReadSum),
          createTime: cur.createTime
        });
      });
      list.value.push(...renderData);
      finished.value = true;
    } catch (err) {
      error.value = true;
      finished.value = true;
    } finally {
      loading.value = false;
      refreshing.value = false;
    }
  };
  const getItem = (anchorId: string) => {
    return list.value.find(cur => cur.anchorId === anchorId);
  };
  let currentLiveItem: MessageCardData | null = null;
  watch(
    currentAnchor,
    (newVal, oldVal) => {
      if (newVal !== oldVal) {
        currentLiveItem = getItem(newVal);
      }
    },
    { immediate: true }
  );
  const messageStore = useMessageStore();
  const messageServer = messageStore.getMessageServer();

  messageServer?.chatEmitter.on("message", res => {
    console.log("resresres", res);
    const data = res.data || ({} as MessageReceiveData["data"]);
    if (!isGroupMessage(data)) {
      return;
    }
    const index = list.value.findIndex(cur => cur.id === data.groupId);
    if (index !== -1) {
      const msgItem = list.value[index];
      // list.value.splice(index, 1);
      // list.value.unshift(msgItem);
      msgItem.message = data.content;
      msgItem.createTime = formatMessageTime(data.createTime);
      msgItem.createTimeText = formatMessageTime(data.createTime);
      if (currentLiveItem && data.groupId === currentLiveItem.id) {
        return;
      }
      list.value[index].readDot += 1;
    } else {
      const anchorId = (data.anchorId || "") + "";
      const newMsg = buildMessageItem({
        id: (data.id || "") + "",
        anchorId,
        sendUserId: (data.from || "") + "",
        name: data.fromNickName,
        cover: data.fromAvatar,
        message: data.content,
        readDot: 1,
        createTime: data.createTime
      });
      list.value.unshift(newMsg);
    }
  });
  messageServer?.chatEmitter.on("clearDot", ({ id, pId }) => {
    if (pId !== currentAnchor.value) return;
    if (id) {
      for (let i = 0; i < list.value.length; i++) {
        if (list.value[i].id === id) {
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
    error,
    loading,
    finished,
    refreshing,
    onLoad,
    onRefresh,
    getItem
  };
};
