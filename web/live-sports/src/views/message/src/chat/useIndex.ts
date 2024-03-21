import dayjs from 'dayjs';
import { getMessageHistory } from '@/api/message';
import { uploadChatImg } from '@/api/user';
import { usePageListCache } from '@/hook/usePageListCache';
import { getMessageType } from '@/views/message/utils/useMessage';
import { MessageCommand, MessageType, MessageReceiveData } from '@/views/message/utils/types';
import { isPersonalMessage, isCurrentWindow } from '@/views/message/utils/message';
import { useChat, type ChatCardData } from './useCommon';
import { v4 as uuidv4 } from 'uuid';
import { floatingActionSheet } from '@/components/FloatingActionSheet';

export type SendMessageFunction = (value: string | File, msgType?: MessageType) => Promise<void>;
export type SendImageMessageFunction = (value: string | File) => Promise<void>;
export const useIndex = ({ onReceiveMessage }: { onReceiveMessage?: () => void }) => {
  const route = useRoute();
  // const chatId = (route.query.id || '') + '';
  const anchorId = ref((route.query.anchorId || '') + '');
  const { userStore, messageStore, clearTipCache, updateTipCacheSuffix, createChatMessage, sendMessage, anchorAccount, updateAccountInfo } =
    useChat({
      anchorId,
    });
  const messageServer = messageStore.getMessageServer()!;
  // const messageServer = useMessage({
  //   token: userStore.token,
  //   userId: userStore.userInfo.id,
  // });
  // provide('messageServer', messageServer);
  const uuidCache = new Map<string, ChatCardData>();
  const list = ref<ChatCardData[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const currentOffset = ref('');
  const pageSize = ref(50);
  const { getRenderData, clearCache } = usePageListCache<ChatCardData>();
  const onLoad = async () => {
    await initData(currentOffset.value);
  };
  const onRefresh = () => {
    // 清空列表数据
    finished.value = false;
    loading.value = true;
    clearCache();
    clearTipCache();
    initData();
  };
  // const fakeItem = {
  //   id: 1768165014746044,
  //   fromId: '1722075906916335616',
  //   toId: '1732234292346683392',
  //   anchorId: '1732234292346683392',
  //   cmd: 11,
  //   msgType: 3,
  //   chatType: 2,
  //   avatar: '',
  //   nick: 'Cf831608',
  //   identityType: 0,
  //   level: 6,
  //   content:
  //     'https://d3ty9kq80i16x4.cloudfront.net/chat-space/20240314/oOzLU9tsAGIxs9b8HjSiT.png***https://d3ty9kq80i16x4.cloudfront.net/chat-space/20240314/ps9Ko2TNi3ysQATOKnPaQ.mp4',
  //   groupId: null,
  //   sent: 1,
  //   readable: 1,
  //   userDel: 0,
  //   anchorDel: 0,
  //   createTime: 1710398365097,
  //   updateTime: 1710369683787,
  //   creator: null,
  //   updater: null,
  //   delFlag: 0,
  // };
  const initData = async (offsetId = '') => {
    try {
      error.value = false;
      await updateAccountInfo();
      const response = await getMessageHistory({
        offset: offsetId,
        size: pageSize.value,
        userId: userStore.userInfo.id, // 当前用户id
        searchId: anchorAccount.id, // 对话的用户id
        type: '0', // 消息类型，0-历史消息，1-离线消息
        chatType: '2', // 群聊还是私聊 1-群聊，2-私聊
        searchType: '3', // 查询类型（判断是用户还是直播端，普通用户查看不了被删除的消息）， live-2, APP-3
      });
      if (refreshing.value) {
        list.value = [];
        refreshing.value = false;
      }
      const data = response || [];
      // data.push(fakeItem as any);
      updateTipCacheSuffix(offsetId);
      const renderData = getRenderData(
        data.map<ChatCardData>((cur) => {
          const fromId = (cur.fromId || '') + '';
          const isMe = fromId === userStore.userInfo.id;
          const createByTime = cur.createTime ? dayjs(Number(cur.createTime)) : undefined;
          return createChatMessage({
            msgId: (cur.id || '') + '',
            message: cur.content,
            msgType: getMessageType(cur.msgType, 'value') as MessageType,
            isMe: isMe,
            createTime: createByTime,
          });
        }),
      );
      list.value.unshift(...renderData);
      loading.value = false;
      currentOffset.value = list.value[0].id;
      if (data.length === 0 || data.length < pageSize.value) {
        finished.value = true;
      }
    } catch (err) {
      error.value = true;
    } finally {
      loading.value = false;
      refreshing.value = false;
    }
  };

  // 增加一个uuid缓存池子，当发送消息的时候存入池子
  // 收到消息的时候移除池子
  const pushLocalMessage = ({
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
  }) => {
    const createByDate = createTime || dayjs();
    const item = createChatMessage({
      msgId,
      message,
      msgType,
      createTime: createByDate,
      isMe,
      status,
      file,
    });
    const newLength = list.value.push(item);
    return list.value[newLength - 1];
  };
  const handleMessage = (res: MessageReceiveData) => {
    const data = res.data || {};
    if (!isPersonalMessage(data)) return;
    // 判断消息来源是否属于当前窗口
    if (!isCurrentWindow(data, anchorAccount.id)) return;
    const isMe = data.from === userStore.userInfo.id;
    // if (!isMe) {
    if (isMe && uuidCache.has(data.sendId || '')) {
      const item = uuidCache.get(data.sendId || '')!;
      item.status = 'end';
    } else {
      const createByTime = data.createTime ? dayjs(Number(data.createTime || '')) : undefined;
      pushLocalMessage({
        msgId: data.id,
        message: data.content,
        msgType: data.msgType,
        isMe,
        createTime: createByTime,
      });
    }
    messageServer.sendReadTag({
      cmd: MessageCommand.read, //（整型）: 23
      messageId: data.id, //（字符串）：
      read: 1, //（整型）: 0-未读， 1-已读
      currentId: userStore.userInfo.id, //（String）: 当前用户ID
      channelType: 3, //（整型）: 消息发送平台：2-LIVE, 3-APP
      toId: anchorAccount.id, //（字符串）: 接收人ID
    });
    // }
    messageServer.chatEmitter.emit('clearDot', anchorAccount.id);
    onReceiveMessage && onReceiveMessage();
  };
  messageServer.chatEmitter.on('message', handleMessage);
  onUnmounted(() => {
    messageServer.chatEmitter.off('message', handleMessage);
  });
  const createFileUrl = (file: File) => {
    return URL.createObjectURL(file);
  };
  const revokeFileUrl = (url) => {
    url.indexOf('blob:') === 0 && URL.revokeObjectURL(url);
  };
  const _sendMessage: SendMessageFunction = async (value, msgType?) => {
    const useMsgType = msgType || MessageType.text;
    const isFile = value instanceof File;
    let msgValue = '';
    if (isFile) {
      msgValue = createFileUrl(value);
    } else {
      msgValue = value;
    }
    const _uuid = uuidv4();
    const item = pushLocalMessage({
      msgId: _uuid,
      message: msgValue,
      msgType: useMsgType,
      isMe: true,
      file: isFile ? value : undefined,
    });
    uuidCache.set(_uuid, item);
    item.status = 'sending';
    try {
      if (isFile) {
        const uploadRes = await uploadChatImg({
          file: value,
        });
        if (uploadRes) {
          revokeFileUrl(msgValue);
          msgValue = uploadRes;
          item.message = msgValue;
          // 上传成功清空file
          item.file = undefined;
        } else {
          throw new Error('图片上传失败');
        }
      }
      const response = await sendMessage(
        {
          value: msgValue,
          uuid: _uuid,
        },
        useMsgType,
      );
      if (!response.success) {
        item.status = 'failed';
        uuidCache.delete(_uuid);
      }
    } catch (error) {
      item.status = 'failed';
      uuidCache.delete(_uuid);
    }
  };
  const _sendImageMessage: SendImageMessageFunction = async (value) => {
    _sendMessage(value, MessageType.image);
  };
  const handleStatus = async (item: ChatCardData) => {
    // 存在file则说明文件上传失败，重新发送的时候需要重新上传文件
    floatingActionSheet({
      actions: [{ name: '重新发送', key: 'reload', class: 'primary' }],
      cancelText: '取消',
      closeOnClickAction: true,
      select: (action) => {
        if (action.key === 'reload') {
          _sendMessage(item.file ? item.file : item.message, item.type);
        }
      },
    });
  };
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
    handleStatus,
  };
};
