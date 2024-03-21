import dayjs from 'dayjs';
import { showToast } from 'vant';
import { reactive, type Ref } from 'vue';
import defaultLogo from '@/assets/img/user/default_anchor.png';
import { useUserStore } from '@/store/modules/user';
import { useMessageStore } from '@/store/modules/message';
import { MessageCommand, MessageType } from '@/views/message/utils/types';
import { buildChatMessage, useShowDateTip, type ChatCardData } from '@/views/message/utils/chat';
import { initChatAccountInfo as _initChatAccountInfo } from '@/views/message/utils/message';
export { type ChatCardData };
export type SendMessageValue = {
  value: string;
  uuid?: string;
};
export type SendMessageFunction = (value: string | SendMessageValue, msgType?: MessageType) => Promise<any>;
export type SendImageMessageFunction = (value: string | SendMessageValue) => Promise<any>;
export type ChatAccountModel = {
  name: string;
  id: string;
  groupId: string;
  cover: string;
};

const getOnlineImg = (src: string) => {
  return /^https?:\/\/.+/.test(src) ? src : '';
};
export const useCommon = ({ anchorId, type }: { anchorId: Ref<string>; type?: 'group' }) => {
  const userStore = useUserStore();

  const messageStore = useMessageStore();
  const anchorAccount = reactive<ChatAccountModel>({
    name: '', // 名称
    id: '', // id
    groupId: '', // id
    cover: '', // 头像
  });

  const initChatAccountInfo = async (force?: boolean) => {
    if (force !== true && anchorAccount.id) return;
    try {
      const response = await _initChatAccountInfo(anchorId.value);
      anchorAccount.name = response.name;
      anchorAccount.id = anchorId.value;
      anchorAccount.cover = response.cover;
      if (type === 'group') {
        anchorAccount.groupId = '';
      }
      messageStore.updateMessageInfo({
        anchorId: anchorAccount.id,
        name: anchorAccount.name,
        cover: anchorAccount.cover,
      });
      return response;
    } catch (error) {
      throw error;
    }
  };

  const { clearTipCache, updateTipCacheSuffix, getShowDateTip } = useShowDateTip();
  return {
    messageStore,
    userStore,
    clearTipCache,
    updateTipCacheSuffix,
    getShowDateTip,
    anchorAccount,
    initChatAccountInfo,
  };
};
// 私聊
export const useChat = ({ anchorId }: { anchorId: Ref<string> }) => {
  const { userStore, messageStore, clearTipCache, updateTipCacheSuffix, getShowDateTip, anchorAccount, initChatAccountInfo } = useCommon({
    anchorId,
  });

  const createChatMessage = ({
    msgId,
    message,
    msgType,
    createTime,
    isMe,
    status,
    file,
  }: {
    msgId: string;
    message: string;
    msgType: MessageType;
    createTime?: dayjs.Dayjs;
    isMe: boolean;
    status?: ChatCardData['status'];
    file?: File;
  }): ChatCardData => {
    return buildChatMessage({
      msgId: msgId,
      msgType: msgType,
      message: message,
      createTime: createTime,
      accountId: isMe ? userStore.userInfo.id : anchorAccount.id,
      accountName: isMe ? userStore.userInfo.nickname : anchorAccount.name,
      accountAvatar: isMe ? userStore.userInfo.avatar : anchorAccount.cover,
      isMe,
      tip: getShowDateTip(createTime),
      status,
      file,
    });
  };
  const sendMessage: SendMessageFunction = async (value, msgType?) => {
    const useMsgType = msgType || MessageType.text;
    const msgValue = (value as SendMessageValue).value || (value as string);
    try {
      const messageServer = messageStore.getMessageServer()!;
      // if (!messageServer.isLogin()) {
      //   showToast('重连中，请稍候...');
      //   return;
      // }
      const response = await messageServer.sendMessage({
        from: userStore.userInfo.id, //（字符串）: 发送者ID
        sendId: (value as SendMessageValue).uuid || '', //（字符串）:  消息发送标识符
        cmd: MessageCommand.send, //（整型）: 11
        anchorId: anchorAccount.id, //（字符串）: 当前聊天主播的id【私聊独有，不管是发送还是接收】
        toAvatar: getOnlineImg(anchorAccount.cover), // 接收者头像（主播、运营、助手均显示主播头像）
        toNickName: anchorAccount.name, // 接收者昵称（主播、运营、助手均显示主播昵称）
        fromAvatar: getOnlineImg(userStore.userInfo.avatar), // 发送者头像（主播、运营、助手均显示主播头像）
        fromNickName: userStore.userInfo.nickname, // 发送者昵称（主播、运营、助手均显示主播昵称）
        to: anchorAccount.id, //（字符串）: 接收者ID【私聊独有】
        level: userStore.userInfo.level, // level: 用户等级（发送者为用户时必填）
        msgType: useMsgType, //（字符串）：消息类型，文字：0， 图片：1
        // createTime: dayjs().unix(),
        chatType: '2', //（字符串）: 聊天类型，群聊：1， 私聊：2
        content: msgValue, //(字符串): 消息内容,
        identityType: 0, // 发送者类型：0-APP普通用户，1-LIVE端主播，2-LIVE端助手，3-LIVE端运营
      });
      if (!response.success) {
        showToast({
          className: 'custom-toast',
          message: '消息发送失败',
        });
      }
      return response;
    } catch (error) {
      console.log('error', error);
      showToast({
        className: 'custom-toast',
        message: '消息发送失败',
      });
    }
  };
  const sendImageMessage: SendImageMessageFunction = async (value: string) => {
    return await sendMessage(value, MessageType.image);
  };
  const updateAccountInfo = async (force?: boolean) => {
    await initChatAccountInfo(force);
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
    updateAccountInfo,
  };
};
// 群聊
export const useGroup = ({ anchorId, onBeforeSendMessage }: { anchorId: Ref<string>; onBeforeSendMessage?: SendMessageFunction }) => {
  const { userStore, messageStore, clearTipCache, updateTipCacheSuffix, getShowDateTip, anchorAccount, initChatAccountInfo } = useCommon({
    anchorId,
    type: 'group',
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
  }): ChatCardData => {
    return buildChatMessage({
      msgId: msgId,
      msgType: msgType,
      message: message,
      createTime: createTime,
      accountId: isMe ? anchorAccount.id : accountId || '',
      accountName: isMe ? anchorAccount.name : accountName || '',
      accountAvatar: isMe ? anchorAccount.cover : accountAvatar || defaultLogo,
      isMe,
      level,
      tip: getShowDateTip(createTime),
    });
  };
  const sendMessage: SendMessageFunction = async (value: string, msgType?: MessageType) => {
    const useMsgType = msgType || MessageType.text;
    if (onBeforeSendMessage && typeof onBeforeSendMessage === 'function') {
      await onBeforeSendMessage(value, useMsgType);
    }
    try {
      const messageServer = messageStore.getMessageServer()!;
      const response = await messageServer.sendMessage({
        from: userStore.userInfo.id, //（字符串）: 发送者ID
        sendId: '', //（字符串）:  消息发送标识符
        cmd: MessageCommand.send, //（整型）: 11
        anchorId: '', //（字符串）: 当前聊天主播的id【私聊独有，不管是发送还是接收】
        toAvatar: '', // 接收者头像（主播、运营、助手均显示主播头像）
        toNickName: '', // 接收者昵称（主播、运营、助手均显示主播昵称）
        fromAvatar: getOnlineImg(userStore.userInfo.avatar), // 发送者头像（主播、运营、助手均显示主播头像）
        fromNickName: userStore.userInfo.nickname, // 发送者昵称（主播、运营、助手均显示主播昵称）
        to: '', //（字符串）: 接收者ID【私聊独有】
        level: '', // level: 用户等级（发送者为用户时必填）
        msgType: useMsgType, //（字符串）：消息类型，文字：0， 图片：1
        // createTime: dayjs().unix(),
        chatType: '1', //（字符串）: 聊天类型，群聊：1， 私聊：2
        content: value, //(字符串): 消息内容,
        groupId: anchorAccount.groupId,
        identityType: 0, // 发送者类型：0-APP普通用户，1-LIVE端主播，2-LIVE端助手，3-LIVE端运营
      });
      if (!response.success) {
        showToast({
          className: 'custom-toast',
          message: '消息发送失败',
        });
      }
    } catch (error) {
      console.log('error', error);
      showToast({
        className: 'custom-toast',
        message: '消息发送失败',
      });
    }
  };
  const sendImageMessage: SendImageMessageFunction = async (value: string) => {
    await sendMessage(value, MessageType.image);
  };
  const updateAccountInfo = async (force?: boolean) => {
    await initChatAccountInfo(force);
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
    updateAccountInfo,
  };
};
