import { getMessageList } from '@/api/message';
import { useUserStore } from '@/store/modules/user';
import { buildMessageItem } from '@/views/message/utils/message';
import { MessageCardData } from '@/views/message/utils/types';

export const useSearch = () => {
  const userStore = useUserStore();
  const searchKey = ref('');
  const list = ref<MessageCardData[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const currentPage = ref(0);
  const pageSize = ref(10);
  const onLoad = () => {
    initData(currentPage.value + 1);
  };
  const resetState = () => {
    list.value = [];
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
    try {
      error.value = false;
      if (!userStore.token || searchKey.value === '') {
        resetState();
        return;
      }
      const response = await getMessageList({
        current: page,
        size: pageSize.value,
        userName: searchKey.value,
      });
      if (refreshing.value || page === 1) {
        list.value = [];
        refreshing.value = false;
      }
      const data = response.records || [];
      const renderData = data.map<MessageCardData>((cur) => {
        const anchorId = (cur.anchorId || '') + '';
        return buildMessageItem({
          id: (cur.id || '') + '',
          anchorId: anchorId,
          name: cur.nick,
          cover: cur.avatar,
          message: cur.content,
          readDot: Number(cur.noReadSum),
          createTime: cur.createTime,
          msgType: cur.msgType,
        });
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
  const handleSearch = (key: string) => {
    searchKey.value = key;
    onRefresh();
  };
  watch(
    () => userStore.token,
    () => {
      onRefresh();
    },
  );
  return {
    searchKey,
    list,
    error,
    loading,
    finished,
    refreshing,
    onLoad,
    onRefresh,
    handleSearch,
  };
};
