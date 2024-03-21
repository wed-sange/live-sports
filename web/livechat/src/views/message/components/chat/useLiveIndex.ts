import dayjs from "dayjs";
import { onUnmounted, provide, ref, type Ref, watch } from "vue";
import { ElLoading } from "element-plus";
import { getMessageHistory } from "@/api/message";
import { uploadChatImg } from "@/api/user";
import {
  MessageCommand,
  MessageType,
  MessageReceiveData
} from "@/views/message/utils/types";
import {
  isGroupMessage,
  isNormalUserSend
} from "@/views/message/utils/message";
import { getMessageType } from "@/views/message/utils/useMessage";
import { useGroup, type ChatCardData } from "./useCommon";
import { v4 as uuidv4 } from "uuid";

export type SendMessageFunction = (
  value: string | File,
  msgType?: MessageType
) => Promise<void>;
export type SendFileMessageFunction = (value: string | File) => Promise<void>;
export const useIndex = ({
  onReceiveMessage,
  anchorId
}: {
  onReceiveMessage?: () => void;
  anchorId: Ref<string>;
}) => {
  const {
    userStore,
    messageStore,
    clearTipCache,
    updateTipCacheSuffix,
    createChatMessage,
    sendMessage,
    anchorAccount,
    updateAccountInfo
  } = useGroup({
    anchorId
  });

  const messageServer = messageStore.getMessageServer()!;
  provide("messageServer", messageServer);
  const uuidCache = new Map<string, ChatCardData>();
  const list = ref<ChatCardData[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const currentOffset = ref("");
  const pageSize = ref(20);
  const onLoad = async () => {
    await initData(currentOffset.value);
  };
  const onRefresh = async () => {
    // 清空列表数据

    await updateAccountInfo(true);
    finished.value = false;
    loading.value = true;
    // clearCache();
    clearTipCache();
    list.value = [];
    await initData();
  };
  const initData = async (offsetId = "") => {
    try {
      await updateAccountInfo();
      error.value = false;
      const response = await getMessageHistory({
        offset: offsetId,
        size: pageSize.value,
        // userId: anchorAccount.id, // 当前用户id
        // searchId: userStore.userInfo.id, // 对话的用户id
        groupId: anchorAccount.groupId, // 对话的用户id
        type: "0", // 消息类型，0-历史消息，1-离线消息
        chatType: "1", // 群聊还是私聊 1-群聊，2-私聊
        searchType: "2" // 查询类型（判断是用户还是直播端，普通用户查看不了被删除的消息）， live-2, APP-3
      });
      if (refreshing.value) {
        list.value = [];
        refreshing.value = false;
      }
      const data = response || [];
      updateTipCacheSuffix(offsetId);
      const renderData = data.map<ChatCardData>(cur => {
        const fromId = (cur.fromId || "") + "";
        const isMe = cur.identityType !== 0;
        const createByTime = cur.createTime
          ? dayjs(parseFloat(cur.createTime))
          : undefined;
        return createChatMessage({
          msgId: (cur.id || "") + "",
          message: cur.content,
          msgType: getMessageType(cur.msgType, "value") as MessageType,
          isMe: isMe,
          createTime: createByTime,
          accountId: fromId,
          accountName: cur.nick || "",
          accountAvatar: cur.avatar || "",
          level: cur.level || ""
        });
      });
      list.value.unshift(...renderData);
      loading.value = false;
      currentOffset.value = list.value[0].id;
      if (data.length === 0 || data.length < pageSize.value) {
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

  const pushLocalMessage = ({
    msgId,
    message,
    msgType,
    createTime,
    isMe,
    accountId,
    accountName,
    accountAvatar,
    level,
    status,
    file
  }: {
    msgId: string;
    message: string;
    msgType: MessageType;
    createTime?: dayjs.Dayjs;
    isMe: boolean;
    accountId?: string;
    accountName?: string;
    accountAvatar?: string;
    level?: string;
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
      accountId,
      accountName,
      accountAvatar,
      level,
      status,
      file
    });
    const newLength = list.value.push(item);
    return list.value[newLength - 1];
  };
  const handleMessage = (res: MessageReceiveData) => {
    const data = res.data || ({} as MessageReceiveData["data"]);
    if (!isGroupMessage(data) || data.groupId !== anchorAccount.groupId) {
      return;
    }
    const isMe = data.from === userStore.userInfo.id;
    const isNormalUser = isNormalUserSend(data);
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
        createTime: createByTime,
        accountId: data.from,
        accountName: data.fromNickName,
        accountAvatar: data.fromAvatar,
        level: data.level || ""
      });
      messageServer.sendReadTag({
        cmd: MessageCommand.read, //（整型）: 23
        messageId: data.id, //（字符串）：
        read: 1, //（整型）: 0-未读， 1-已读
        currentId: userStore.userInfo.id, //（String）: 当前用户ID
        channelType: 2, //（整型）: 消息发送平台：2-LIVE, 3-APP
        toId: anchorAccount.id //（字符串）: 接收人ID
      });
    }
    // messageServer.chatEmitter.emit("clearDot", anchorAccount.id);
    onReceiveMessage && onReceiveMessage();
  };
  messageServer.chatEmitter.on("message", handleMessage);
  onUnmounted(() => {
    messageServer.chatEmitter.off("message", handleMessage);
    if (userStore.userInfo.identityType === 3) return;
    messageServer.leaveGroup(anchorAccount.groupId);
  });
  // const createFileUrl = (file: File) => {
  //   return URL.createObjectURL(file);
  // };
  // const revokeFileUrl = url => {
  //   url.indexOf("blob:") === 0 && URL.revokeObjectURL(url);
  // };
  const _sendMessage: SendMessageFunction = async (value, msgType?) => {
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
        // revokeFileUrl(msgValue);
        msgValue = uploadRes;
        // item.message = msgValue;
      } else {
        throw new Error("图片上传失败");
      }
    }
    if (isFile) {
      // msgValue = createFileUrl(value);
    } else {
      msgValue = value;
    }
    const _uuid = uuidv4();
    const item = pushLocalMessage({
      msgId: _uuid,
      message: msgValue,
      msgType: useMsgType,
      isMe: true
      // file: isFile ? value : undefined
    });
    uuidCache.set(_uuid, item);
    item.status = "sending";
    try {
      // if (isFile) {
      //   const loadingInstance = ElLoading.service({ fullscreen: true });
      //   const uploadRes = await uploadChatImg({
      //     file: value
      //   }).finally(() => {
      //     loadingInstance.close();
      //   });
      //   if (uploadRes) {
      //     revokeFileUrl(msgValue);
      //     msgValue = uploadRes;
      //     item.message = msgValue;
      //     // 上传成功清空file
      //     item.file = undefined;
      //   } else {
      //     throw new Error("图片上传失败");
      //   }
      // }
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
    _sendMessage(value, MessageType.image);
  };
  const handleStatus = async (item: ChatCardData) => {
    // 存在file则说明文件上传失败，重新发送的时候需要重新上传文件
    _sendMessage(item.file ? item.file : item.message, item.type);
  };
  watch(
    () => anchorAccount.groupId,
    (newVal, oldVal) => {
      if (userStore.userInfo.identityType === 3) return;
      if (newVal !== oldVal) {
        if (oldVal) {
          messageServer.leaveGroup(oldVal);
        }
        messageServer.joinGroup(newVal);
      }
    }
  );
  return {
    list,
    error,
    loading,
    finished,
    refreshing,
    onLoad,
    onRefresh,
    sendMessage: _sendMessage,
    sendImageMessage: _sendImageMessage,
    anchorAccount,
    handleStatus
  };
};
