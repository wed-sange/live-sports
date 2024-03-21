import { getMemberList } from '@/api/message';
import { useUserStore } from '@/store/modules/user';
import defaultLogo from '@/assets/img/user/default_anchor.png';
import { type MemberCardData } from './useIndex';

export const useSearch = () => {
  const userStore = useUserStore();
  const searchKey = ref('');
  const list = ref<MemberCardData[]>([]);
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
      const response = await getMemberList({
        current: page,
        size: pageSize.value,
        userName: searchKey.value,
      });
      if (refreshing.value || page === 1) {
        list.value = [];
        refreshing.value = false;
      }
      const data = response.records || [];
      const renderData = data.map<MemberCardData>((cur) => {
        const anchorId = (cur.anchorId || '') + '';
        return {
          id: anchorId,
          anchorId: anchorId,
          name: cur.nickName || '',
          cover: cur.head || defaultLogo,
          shortName: '未知xxx',
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
