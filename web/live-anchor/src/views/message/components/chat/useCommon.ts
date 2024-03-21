import dayjs from "dayjs";
import { reactive, type Ref, inject } from "vue";
import { ElMessage } from "element-plus";

import defaultUserLogo from "@/assets/message/default_user.png";
import defaultAnchorLogo from "@/assets/message/default_anchor.png";
import { useUserStore } from "@/store/modules/user";
import { MessageCommand, MessageType } from "@/views/message/utils/types";
// import { isCurrentWindow } from "@/views/message/utils/message";
import {
  buildChatMessage,
  useShowDateTip,
  type ChatCardData
} from "@/views/message/utils/chat";
import { useMessageStore } from "@/store/modules/message";
import { type GetItemFun } from "../../replay/useIndex";
export { type ChatCardData };

export type SendMessageValue = {
  value: string;
  uuid?: string;
};

export type SendMessageFunction = (
  value: string | SendMessageValue,
  msgType?: MessageType
) => Promise<any>;
export type SendImageMessageFunction = (
  value: string | SendMessageValue
) => Promise<any>;

export type ChatAccountModel = {
  name: string;
  id: string;
  groupId: string;
  cover: string;
};
const getOnlineImg = (src: string) => {
  return /^https?:\/\/.+/.test(src) ? src : "";
};
export const useCommon = ({
  anchorId,
  receiveId,
  type
}: {
  anchorId: Ref<string>;
  receiveId?: Ref<string>;
  type?: "group";
}) => {
  const userStore = useUserStore();
  const mainPageGetItem = inject("mainPageGetItem") as GetItemFun;

  const messageStore = useMessageStore();
  // 目前这个变量在群聊中未使用
  const receiveAccount = reactive<ChatAccountModel>({
    name: "", // 名称
    id: "", // id
    groupId: "", // id
    cover: defaultUserLogo // 头像
  });
  const initReceiveInfo = async (force?: boolean) => {
    if ((force !== true && receiveAccount.id) || type === "group") return;
    const userItem = mainPageGetItem(receiveId.value, "user");
    receiveAccount.name = userItem.name;
    receiveAccount.id = receiveId.value.split("-")[1];
    receiveAccount.cover = userItem.cover;
  };
  const anchorAccount = reactive<ChatAccountModel>({
    name: "", // 名称
    id: "", // id
    groupId: "", // id
    cover: defaultAnchorLogo // 头像
  });
  const initChatAccountInfo = async (force?: boolean) => {
    if (force !== true && anchorAccount.id) return;
    const anchorItem = mainPageGetItem(anchorId.value, "anchor");
    anchorAccount.name = anchorItem.name;
    anchorAccount.id = anchorId.value;
    if (type === "group") {
      anchorAccount.groupId = anchorItem.id;
    }
    anchorAccount.cover = anchorItem.cover;
  };

  const { clearTipCache, updateTipCacheSuffix, getShowDateTip } =
    useShowDateTip();
  return {
    messageStore,
    userStore,
    clearTipCache,
    updateTipCacheSuffix,
    getShowDateTip,
    receiveAccount,
    initReceiveInfo,
    anchorAccount,
    initChatAccountInfo
  };
};
// 私聊
export const useChat = ({
  receiveId,
  anchorId
}: {
  receiveId: Ref<string>;
  anchorId: Ref<string>;
}) => {
  const {
    userStore,
    messageStore,
    clearTipCache,
    updateTipCacheSuffix,
    getShowDateTip,
    receiveAccount,
    initReceiveInfo,
    anchorAccount,
    initChatAccountInfo
  } = useCommon({
    receiveId,
    anchorId
  });

  const createChatMessage = ({
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
  }): ChatCardData => {
    return buildChatMessage({
      msgId: msgId,
      msgType: msgType,
      message: message,
      createTime: createTime,
      accountId: isMe ? userStore.userInfo.id : receiveAccount.id,
      accountName: isMe ? userStore.userInfo.nickname : receiveAccount.name,
      accountAvatar: isMe ? userStore.userInfo.avatar : receiveAccount.cover,
      isMe,
      tip: getShowDateTip(createTime),
      status,
      file
    });
  };
  const sendMessage: SendMessageFunction = async (value, msgType?) => {
    const useMsgType = msgType || MessageType.text;
    const msgValue = (value as SendMessageValue).value || (value as string);
    try {
      const messageServer = messageStore.getMessageServer()!;
      const response = await messageServer.sendMessage({
        from: userStore.userInfo.id, //（字符串）: 发送者ID
        sendId: (value as SendMessageValue).uuid || "", //（字符串）:  消息发送标识符
        cmd: MessageCommand.send, //（整型）: 11
        anchorId: anchorAccount.id, //（字符串）: 当前聊天主播的id【私聊独有，不管是发送还是接收】
        toAvatar: getOnlineImg(receiveAccount.cover), // 接收者头像（主播、运营、助手均显示主播头像）
        toNickName: receiveAccount.name, // 接收者昵称（主播、运营、助手均显示主播昵称）
        fromAvatar: getOnlineImg(anchorAccount.cover), // 发送者头像（主播、运营、助手均显示主播头像）
        fromNickName: anchorAccount.name, // 发送者昵称（主播、运营、助手均显示主播昵称）
        to: receiveAccount.id, //（字符串）: 接收者ID【私聊独有】
        level: "", // level: 用户等级（发送者为用户时必填）
        msgType: useMsgType, //（字符串）：消息类型，文字：0， 图片：1
        // createTime: dayjs().unix(),
        chatType: "2", //（字符串）: 聊天类型，群聊：1， 私聊：2
        content: msgValue, //(字符串): 消息内容,
        identityType: userStore.userInfo.identityType as 0 | 1 | 2 | 3 // 发送者类型：0-APP普通用户，1-LIVE端主播，2-LIVE端助手，3-LIVE端运营
      });
      if (!response.success) {
        ElMessage.error("消息发送失败");
      }
      return response;
    } catch (error) {
      console.log("error", error);
      ElMessage.error("消息发送失败");
    }
  };
  const sendImageMessage: SendImageMessageFunction = async value => {
    return await sendMessage(value, MessageType.image);
  };
  const updateAccountInfo = (force?: boolean) => {
    initReceiveInfo(force);
    initChatAccountInfo(force);
  };
  return {
    userStore,
    messageStore,
    clearTipCache,
    updateTipCacheSuffix,
    getShowDateTip,
    createChatMessage,
    sendMessage,
    sendImageMessage,
    receiveAccount,
    anchorAccount,
    updateAccountInfo
  };
};
// 群聊
export const useGroup = ({ anchorId }: { anchorId: Ref<string> }) => {
  const {
    userStore,
    messageStore,
    clearTipCache,
    updateTipCacheSuffix,
    getShowDateTip,
    anchorAccount,
    initChatAccountInfo
  } = useCommon({
    anchorId,
    type: "group"
  });

  const createChatMessage = ({
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
  }): ChatCardData => {
    return buildChatMessage({
      msgId: msgId,
      msgType: msgType,
      message: message,
      createTime: createTime,
      accountId: isMe ? anchorAccount.id : accountId || "",
      accountName: isMe ? anchorAccount.name : accountName || "",
      accountAvatar: isMe
        ? anchorAccount.cover
        : accountAvatar || defaultUserLogo,
      isMe,
      level,
      tip: getShowDateTip(createTime),
      status,
      file
    });
  };
  const sendMessage: SendMessageFunction = async (value, msgType?) => {
    const useMsgType = msgType || MessageType.text;
    const msgValue = (value as SendMessageValue).value || (value as string);
    try {
      const messageServer = messageStore.getMessageServer()!;
      const response = await messageServer.sendMessage({
        from: userStore.userInfo.id, //（字符串）: 发送者ID
        sendId: (value as SendMessageValue).uuid || "", //（字符串）:  消息发送标识符
        cmd: MessageCommand.send, //（整型）: 11
        anchorId: "", //（字符串）: 当前聊天主播的id【私聊独有，不管是发送还是接收】
        toAvatar: "", // 接收者头像（主播、运营、助手均显示主播头像）
        toNickName: "", // 接收者昵称（主播、运营、助手均显示主播昵称）
        fromAvatar: getOnlineImg(userStore.userInfo.avatar), // 发送者头像（主播、运营、助手均显示主播头像）
        fromNickName: userStore.userInfo.nickname, // 发送者昵称（主播、运营、助手均显示主播昵称）
        to: "", //（字符串）: 接收者ID【私聊独有】
        level: "", // level: 用户等级（发送者为用户时必填）
        msgType: useMsgType, //（字符串）：消息类型，文字：0， 图片：1
        // createTime: dayjs().unix(),
        chatType: "1", //（字符串）: 聊天类型，群聊：1， 私聊：2
        content: msgValue, //(字符串): 消息内容,
        groupId: anchorAccount.groupId,
        identityType: userStore.userInfo.identityType as 0 | 1 | 2 | 3 // 发送者类型：0-APP普通用户，1-LIVE端主播，2-LIVE端助手，3-LIVE端运营
      });
      if (!response.success) {
        ElMessage.error("消息发送失败");
      }
    } catch (error) {
      console.log("error", error);
      ElMessage.error("消息发送失败");
    }
  };
  const sendImageMessage: SendImageMessageFunction = async value => {
    await sendMessage(value, MessageType.image);
  };
  const updateAccountInfo = (force?: boolean) => {
    initChatAccountInfo(force);
  };
  return {
    userStore,
    messageStore,
    clearTipCache,
    updateTipCacheSuffix,
    getShowDateTip,
    createChatMessage,
    sendMessage,
    sendImageMessage,
    anchorAccount,
    updateAccountInfo
  };
};
