import { computed, watchEffect } from "vue";
import { useMessageStore } from "@/store/modules/message";
import { useUserStore } from "@/store/modules/user";
import { useLiveStore } from "@/store/modules/live";
import {
  MessageCommand,
  MessageReceiveData,
  MessageType
} from "@/views/message/utils/types";
import { ChatType, IdentityType } from "@/api/message/enums";
import { MessageHistoryItemData } from "@/api/message/model";

interface Payload {
  /** 接收事件后的处理 */
  onEvent?: (type: MessageCommand) => void;
  /** 接收消息后的处理 */
  onMessage?: (message: LiveMessageItem) => void;
}
interface SendMessagePayload {
  /** 消息内容 */
  content: string;
  /** 消息类型 */
  msgType?: MessageType;
  /** 身份标识 */
  identityType?: IdentityType;
}
export interface LiveMessageItem {
  /** 消息id */
  id: string;
  /** 直播间id */
  roomId: string;
  /** 用户等级 */
  level: string;
  /** 用户昵称 */
  sender: string;
  /** 用户消息 */
  content: string;
  /** 身份标识 */
  identityType: IdentityType;
  /** 消息类型 */
  msgType: MessageType;
  /** 发送时间 */
  createTime: string;
}

export const useLiveRoomSocket = ({ onMessage, onEvent }: Payload = {}) => {
  const server = useMessageStore().getMessageServer();
  const roomId = computed(() => useLiveStore().currentLive.id);
  const userInfo = computed(() => useUserStore().userInfo);

  const sender = async (payload: SendMessagePayload) => {
    const timestamp = Date.now();
    const message = {
      level: payload.identityType === IdentityType.Operator && "3",
      content: payload.content,
      createTime: timestamp,
      from: userInfo.value.id,
      fromAvatar: userInfo.value.avatar,
      fromNickName: userInfo.value.nickname,
      sendId: `${userInfo.value.id}-${timestamp}`,
      groupId: roomId.value,
      cmd: MessageCommand.send,
      chatType: ChatType.Public,
      identityType: payload.identityType || IdentityType.Operator,
      msgType: payload.msgType || MessageType.text
    };
    return server?.sendMessage(message);
  };

  const executor = (res: MessageReceiveData) => {
    onEvent?.(res.command);
    if (!isLiveRoomMessage(roomId.value, res.data)) return;
    onMessage?.(transformMessage(res.data));
  };

  watchEffect(onCleanup => {
    if (!(onMessage || onEvent)) return;
    server?.chatEmitter.on("message", executor);
    onCleanup(() => {
      server?.chatEmitter.off("message", executor);
    });
  });

  return {
    sendMessage: sender
  };
};

const isLiveRoomMessage = (
  roomId: string,
  received: MessageReceiveData["data"]
) => received.chatType === ChatType.Public && received.groupId === roomId;

export const transformMessage = (
  message: MessageHistoryItemData | MessageReceiveData["data"]
): LiveMessageItem => {
  const isWs = "fromNickName" in message;
  return {
    sender: isWs ? message.fromNickName : message.nick || "",
    level: message.level,
    content: message.content,
    identityType: message.identityType,
    msgType: message.msgType,
    id: message.id,
    roomId: message.groupId,
    createTime: String(message.createTime)
  };
};
