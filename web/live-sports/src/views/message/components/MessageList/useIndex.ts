import { showConfirmDialog } from 'vant';
import { useUserStore } from '@/store/modules/user';
import { useMessageStore } from '@/store/modules/message';
import { MessageCardData } from '@/views/message/utils/types';
import { createLoading, closeLoading } from '@/components/SysLoading';
// 抽离到store
// 利用store维护list数据
// 消息收发监听存放在store里面
// 共用消息处理机制
// 抛弃provide/inject
// 调整消息队列
// 根据当前page和size决定缓存大小和数据处理节点
// 拥有释放当前状态的方法
// 拥有创建 ws和释放ws的方法 也可以创建之后不回收 处于永久挂载状态
export const useIndex = () => {
  const userStore = useUserStore();
  const searchKey = ref('');
  const messageStore = useMessageStore();
  const list = computed(() => messageStore.list);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const currentPage = computed(() => messageStore.currentPage);
  const pageSize = computed(() => messageStore.pageSize);
  const onLoad = () => {
    initData(currentPage.value + 1);
  };
  const resetState = () => {
    messageStore.resetListData();
    refreshing.value = false;
    finished.value = true;
  };
  const onRefresh = () => {
    // 清空列表数据
    finished.value = false;
    loading.value = true;
    initData();
  };
  const initData = async (page = 1) => {
    if (!list.value || list.value.length == 0) {
      createLoading();
    }
    try {
      error.value = false;
      if (!userStore.token) {
        resetState();
        return;
      }
      const response = (await messageStore.initData(page)) as any;
      if (page * pageSize.value >= response.total) {
        finished.value = true;
      }
    } catch (err) {
      error.value = true;
    } finally {
      loading.value = false;
      refreshing.value = false;
      closeLoading();
    }
  };
  const handleRemove = (item: MessageCardData) => {
    showConfirmDialog({
      message: '确定删除该条消息？',
      className: 'custom-dialog-confirm',
    })
      .then(async () => {
        messageStore.removeMessageRecord(item);
        // on confirm
      })
      .catch(() => {
        // on cancel
      });
  };
  const handleSearch = (key: string) => {
    searchKey.value = key;
    onRefresh();
  };

  onMounted(() => {
    messageStore.updateAllMessageTime();
  });
  onActivated(() => {
    messageStore.updateAllMessageTime();
  });
  messageStore.getMessageServerAsync().then((messageServer) => {
    messageServer.chatEmitter.on('onWebsocketUserLogin', (isLogin) => {
      isLogin && initData();
    });
  });
  watch(
    () => userStore.token,
    () => {
      initData();
    },
  );
  return {
    list,
    error,
    loading,
    finished,
    refreshing,
    onLoad,
    onRefresh,
    handleRemove,
    searchKey,
    handleSearch,
  };
};
