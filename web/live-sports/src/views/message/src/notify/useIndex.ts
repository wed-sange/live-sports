import { getSysNotifyList, readSysNotify } from '@/api/message';
import dayjs from 'dayjs';
import { useUserStore } from '@/store/modules/user';
import { useMessageStore } from '@/store/modules/message';
import { MessageBannedData, MessageFeedbackData } from '@/views/message/utils/types';

export type NotifyCardData = {
  id: string;
  type: string;
  title: string;
  content: string;
  result: string;
  tip: string;
};
export const useIndex = () => {
  const sysTypeList = reactive([
    { label: '反馈结果', key: '1' },
    { label: '禁言通知', key: '2' },
    { label: '解禁通知', key: '3' },
  ]);
  const userStore = useUserStore();
  const messageStore = useMessageStore();
  const messageServer = messageStore.getMessageServer()!;

  const list = ref<NotifyCardData[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const currentPage = ref(0);
  const pageSize = ref(10);
  const resetState = () => {
    list.value = [];
    refreshing.value = false;
    finished.value = true;
  };
  const onLoad = () => {
    initData(currentPage.value + 1);
  };
  const onRefresh = () => {
    // 清空列表数据
    finished.value = false;
    loading.value = true;
    initData();
  };
  const initData = async (page = 1) => {
    try {
      error.value = false;
      if (!userStore.token) {
        resetState();
        return;
      }
      const response = await getSysNotifyList({
        current: page,
        size: pageSize.value,
      });
      if (refreshing.value) {
        list.value = [];
        refreshing.value = false;
      }
      const data = response.records || [];
      const renderData = data.map<NotifyCardData>((cur) => {
        const curType = sysTypeList.find((ctype) => ctype.key === cur.type + '');
        return {
          id: (cur.id || '') + '',
          type: curType?.key || '',
          title: (curType && curType.label) || '',
          content: cur.title || '',
          result: cur.notice || '',
          tip: dayjs(Number(cur.createTime)).format('YYYY-MM-DD HH:mm'),
        };
      });
      list.value.push(...renderData);
      loading.value = false;
      currentPage.value = page;
      if (page * pageSize.value >= response.total) {
        finished.value = true;
      }
    } catch (err) {
      error.value = true;
    } finally {
      loading.value = false;
      refreshing.value = false;
    }
  };
  const onSysNotifyAddSysMessage = (data, type: string) => {
    const curType = sysTypeList.find((ctype) => ctype.key === type + '');
    const newMsg = {
      id: (data.noticeId || '') + '',
      type: curType?.key || '',
      title: (curType && curType.label) || '',
      content: data.title || '',
      result: data.reason || '',
      tip: dayjs(Number(data.createTime)).format('YYYY-MM-DD HH:mm:ss'),
    };
    list.value.unshift(newMsg);
    // console.log('newMsg', newMsg.title, newMsg.id);
    messageServer?.clearDot('0');
    readSysNotify(newMsg.id);
  };
  const handleBanned = (res: MessageBannedData) => {
    const data = res.data || {};
    const sendId = String(data.userId);
    if (sendId === userStore.userInfo.id) {
      userStore.updateUserBanned(true);
      onSysNotifyAddSysMessage(data, '2');
    }
  };
  messageServer.chatEmitter.on('banned', handleBanned);
  const handleUnBanned = (res: MessageBannedData) => {
    const data = res.data || {};
    const sendId = String(data.userId);
    if (sendId === userStore.userInfo.id) {
      userStore.updateUserBanned(false);
      onSysNotifyAddSysMessage(data, '3');
    }
  };
  messageServer.chatEmitter.on('unBanned', handleUnBanned);
  const handleFeedback = (res: MessageFeedbackData) => {
    const data = res.data || {};
    const sendId = String(data.userId);
    if (sendId === userStore.userInfo.id) {
      onSysNotifyAddSysMessage(data, '1');
    }
  };
  messageServer.chatEmitter.on('feedback', handleFeedback);
  onUnmounted(() => {
    messageServer.chatEmitter.off('banned', handleBanned);
    messageServer.chatEmitter.off('unBanned', handleUnBanned);
    messageServer.chatEmitter.off('feedback', handleFeedback);
  });
  return {
    list,
    error,
    loading,
    finished,
    refreshing,
    onLoad,
    onRefresh,
  };
};
