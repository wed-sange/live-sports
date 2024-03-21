import dayjs from "dayjs";
import { computed, onUnmounted, provide, watch, ref, type Ref } from "vue";
import { v4 as uuidv4 } from "uuid";
import { ElLoading } from "element-plus";
import { uploadChatImg, uploadChatVideo } from "@/api/user";
import { MessageType, MessageReceiveData } from "@/views/message/utils/types";
import {
  isNormalUserSend,
  isPersonalMessage,
  isCurrentWindow
} from "@/views/message/utils/message";
import { useChat, type ChatCardData } from "./useCommon";
import getVideoCover from "@/utils/getVideoCover";
import { IChatMessage, useChatMessage } from "./useChatMessage";
import { cloneDeep } from "lodash";
import { IVitalId } from "@/api/message/model";
import { useMessageUnReadCountStore } from "@/store/modules/message/unReadCount";
import { changeScollTopForFetchFinished, isSafeRegion } from "./scroll";

export type SendMessageFunction = (
  value: string | File,
  msgType?: MessageType
) => Promise<void>;
export type SendFileMessageFunction = (value: string | File) => Promise<void>;
export type SendVideoMessageFunction = (value: File) => Promise<void>;

export const useIndex = ({
  onReceiveMessage,
  receiveId,
  anchorId
}: {
  onReceiveMessage?: (boolean?) => void;
  receiveId: Ref<string>;
  anchorId: Ref<string>;
}) => {
  const {
    userStore,
    messageStore,
    createChatMessage,
    sendMessage,
    receiveAccount,
    updateAccountInfo
  } = useChat({
    receiveId,
    anchorId
  });
  const messageServer = messageStore.getMessageServer()!;
  const uuidCache = new Map<string, ChatCardData>();
  const list = ref<ChatCardData[]>([]);
  const messageUnReadCountStore = useMessageUnReadCountStore();
  const unreadCount = computed(
    () => messageUnReadCountStore.unReadRecord?.[receiveId.value] || 0
  );
  const error = ref(false);
  const chatLoading = ref(false);
  const finished = ref({});
  const refreshing = ref(false);
  const chatRef = ref<IChatMessage>(null);
  const currentOffset = ref("");
  const antiShakeLock = ref(false);
  const prevOffsetId = ref("");
  /**
   * 懒加载
   *
   * @author
   * @param inquiryMode
   * @return {*}
   */
  const onLazyLoad = inquiryMode => {
    // 防止首次加载触发懒加载
    if (antiShakeLock.value) {
      antiShakeLock.value = false;
      return;
    }
    // 已加载完，就不做请求了
    if (chatRef.value?.finishStatus?.[inquiryMode]) {
      return;
    }
    const arr = list.value;
    const offsetId =
      inquiryMode === "up" ? arr?.[0]?.id : arr[arr.length - 1]?.id;
    if (!offsetId) {
      return;
    }
    // 和上次请求一样，也不用再请求了
    if (prevOffsetId.value === offsetId) {
      return;
    }
    prevOffsetId.value = offsetId;
    chatLoading.value = true;
    chatRef.value.getLazyMessage(offsetId, inquiryMode).finally(() => {
      chatLoading.value = false;
      changeScollTopForFetchFinished(inquiryMode, offsetId);
    });
  };

  /**
   * 初始化加载
   *
   * @author
   */
  const onRefresh = async ({ userId, anchorId }: IVitalId) => {
    // 初始化一堆值。。。其实可以合并到一个ref，因为是老代码，暂时不改了
    antiShakeLock.value = true;
    error.value = false;
    chatLoading.value = true;
    prevOffsetId.value = "";
    finished.value = {};
    list.value = [];

    try {
      const { chat } = useChatMessage({
        userId,
        anchorId,
        extra: {
          createChatMessage
        }
      });
      chat.addListener(data => {
        // 此处应该会重新渲染，想想有没有别的方法
        list.value = cloneDeep(data.messages);
        currentOffset.value = data.messages?.[0]?.id;
      });

      chatRef.value = chat;

      // 初始化用户信息
      await updateAccountInfo(true);
      // 初始化信息列表
      await chatRef.value.create();

      // 聊天到最底
      onReceiveMessage &&
        onReceiveMessage(chatRef.value.finishStatus.down ? true : false);
    } catch (err) {
      error.value = true;
    }

    antiShakeLock.value = false;
    chatLoading.value = false;
  };

  /**
   * 回到最新消息
   *
   * @author
   */
  const handleBackToLatest = async () => {
    await chatRef.value.getNewestMessage();
    onReceiveMessage && onReceiveMessage();
  };

  const pushLocalMessage = ({
    msgId,
    message,
    msgType,
    createTime,
    isMe,
    status,
    file
  }: {
    msgId: string;
    message: string;
    msgType: MessageType;
    createTime?: dayjs.Dayjs;
    isMe: boolean;
    status?: ChatCardData["status"];
    file?: File;
  }) => {
    const createByDate = createTime || dayjs();
    const item = createChatMessage({
      msgId,
      message,
      msgType,
      createTime: createByDate,
      isMe,
      status,
      file
    });
    const newLength = list.value.push(item);
    return list.value[newLength - 1];
  };

  const handleMessage = (res: MessageReceiveData) => {
    const data = res.data || ({} as MessageReceiveData["data"]);
    if (!isPersonalMessage(data)) return;
    const isNormalUser = isNormalUserSend(data);
    // 如果有未读消息条数或者滚动条没有置底，则渲染新的消息
    if ((unreadCount.value || !isSafeRegion()) && isNormalUser) {
      return;
    }
    // 判断消息来源是否属于当前窗口
    const _isCurrentWindow = isCurrentWindow(data, receiveAccount.id);
    if (!_isCurrentWindow) return;
    const isMe = data.from === userStore.userInfo.id;

    if (isMe && uuidCache.has(data.sendId || "")) {
      const item = uuidCache.get(data.sendId || "")!;
      item.status = "end";
    }
    if (!isMe) {
      const createByTime = data.createTime
        ? dayjs(Number(data.createTime || ""))
        : undefined;
      pushLocalMessage({
        msgId: data.id,
        message: data.content,
        msgType: data.msgType,
        isMe: isNormalUser ? false : true,
        createTime: createByTime
      });
    }
    chatRef.value.addUnReadAbleMessage({ messageIds: [data.id] });
    onReceiveMessage && onReceiveMessage();
  };
  messageServer.chatEmitter.on("message", handleMessage);
  onUnmounted(() => {
    messageServer.chatEmitter.off("message", handleMessage);
  });
  const _sendMessage: SendMessageFunction = async (value, msgType?) => {
    if (unreadCount.value) {
      await handleBackToLatest();
    }
    const useMsgType = msgType || MessageType.text;
    const isFile = value instanceof File;
    let msgValue = "";
    if (isFile) {
      const loadingInstance = ElLoading.service({ fullscreen: true });
      const uploadRes = await uploadChatImg({
        file: value
      }).finally(() => {
        loadingInstance.close();
      });
      if (uploadRes) {
        msgValue = uploadRes;
      } else {
        throw new Error("图片上传失败");
      }
    }
    if (!isFile) {
      msgValue = value;
    }
    const _uuid = uuidv4();
    const item = pushLocalMessage({
      msgId: _uuid,
      message: msgValue,
      msgType: useMsgType,
      isMe: true
    });
    uuidCache.set(_uuid, item);
    item.status = "sending";
    try {
      const response = await sendMessage(
        {
          value: msgValue,
          uuid: _uuid
        },
        useMsgType
      );
      if (!response.success) {
        item.status = "failed";
        uuidCache.delete(_uuid);
      }
    } catch (error) {
      item.status = "failed";
      uuidCache.delete(_uuid);
    }
  };

  const _sendVideoMessage: SendVideoMessageFunction = async value => {
    const useMsgType = MessageType.video;
    const isFile = value instanceof File;

    const cover: File = await getVideoCover(value);

    let msgValue = "";
    if (isFile) {
      const loadingInstance = ElLoading.service({ fullscreen: true });
      try {
        const uploadRes = await Promise.all([
          uploadChatImg({
            file: cover
          }),
          uploadChatVideo({
            file: value
          })
        ]);
        if (uploadRes) {
          msgValue = uploadRes.join("***");
          console.log("msgValue", msgValue);
        } else {
          throw new Error("视频上传失败");
        }
      } finally {
        loadingInstance.close();
      }
    }
    if (!isFile) {
      msgValue = value;
    }
    const _uuid = uuidv4();
    const item = pushLocalMessage({
      msgId: _uuid,
      message: msgValue,
      msgType: useMsgType,
      isMe: true
    });
    uuidCache.set(_uuid, item);
    item.status = "sending";
    try {
      const response = await sendMessage(
        {
          value: msgValue,
          uuid: _uuid
        },
        useMsgType
      );
      if (!response.success) {
        item.status = "failed";
        uuidCache.delete(_uuid);
      }
    } catch (error) {
      item.status = "failed";
      uuidCache.delete(_uuid);
    }
  };

  const _sendImageMessage: SendFileMessageFunction = async value => {
    await _sendMessage(value, MessageType.image);
  };
  const handleStatus = async (item: ChatCardData) => {
    // 存在file则说明文件上传失败，重新发送的时候需要重新上传文件
    _sendMessage(item.file ? item.file : item.message, item.type);
  };

  watch(
    unreadCount,
    val => {
      if (chatRef.value?.finishStatus) {
        chatRef.value.finishStatus.down = val ? false : true;
      }
    },
    {
      immediate: true
    }
  );

  provide("messageServer", messageServer);

  return {
    list,
    unreadCount,
    error,
    chatLoading,
    refreshing,
    onLazyLoad,
    onRefresh,
    sendMessage: _sendMessage,
    sendImageMessage: _sendImageMessage,
    sendVideoMessage: _sendVideoMessage,
    receiveAccount,
    handleStatus,
    handleBackToLatest
  };
};
