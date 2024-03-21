import { getAnchorFollowingList } from '@/api/user';
import { createLoading, closeLoading } from '@/components/SysLoading';
import { usePageListCache } from '@/hook/usePageListCache';
import defaultLogoSrc from '@/assets/img/user/default_anchor.png';
export type ItemData = {
  name: string;
  id: string;
  fans: number;
  liveId: string;
  matchId: string;
  matchType: string;
  head: string;
  isFocused: boolean;
};
export const useIndex = () => {
  const list = ref<ItemData[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const firstLoad = ref(false);
  const currentPage = ref(0);
  const pageSize = ref(10);
  const { getRenderData, clearCache } = usePageListCache<ItemData>();
  const onLoad = () => {
    if (!currentPage.value) {
      createLoading({ overlay: false });
    }
    initData(currentPage.value + 1);
  };
  const onRefresh = () => {
    // 清空列表数据
    finished.value = false;

    loading.value = true;
    clearCache();
    initData();
  };
  const onFirstLoad = () => {
    // 清空列表数据
    firstLoad.value = true;
    currentPage.value = 0;
    createLoading({ overlay: false });
    initData(currentPage.value + 1);
  };
  const onClearData = () => {
    // 清空列表数据
    firstLoad.value = false;
    finished.value = false;
    loading.value = false;
    list.value = [];
    clearCache();
  };

  const initData = async (page = 1) => {
    try {
      error.value = false;
      const response = await getAnchorFollowingList({
        current: page,
        size: pageSize.value,
      });
      firstLoad.value = false;
      closeLoading();
      if (refreshing.value) {
        list.value = [];
        refreshing.value = false;
      }
      const data = response.records || [];
      const renderData = getRenderData(
        data.map<ItemData>((cur) => {
          return {
            name: cur.nickName || '',
            id: (cur.anchorId || '') + '',
            fans: Number(cur.fans || ''),
            liveId: (cur.liveId || '') + '',
            matchId: (cur.matchId || '') + '',
            matchType: (cur.matchType || '') + '',
            head: cur.head || defaultLogoSrc,
            isFocused: true,
          };
        }),
      );
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
  return {
    list,
    error,
    loading,
    finished,
    refreshing,
    onLoad,
    onRefresh,
    firstLoad,
    onFirstLoad,
    onClearData,
  };
};
