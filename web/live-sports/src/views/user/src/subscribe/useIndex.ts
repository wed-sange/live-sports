import { getSubscribeList } from '@/api/user';
import { createLoading, closeLoading } from '@/components/SysLoading';
import { type SportsCardData } from '@/components/SportsCard/types';
import { formatCardData } from '@/components/SportsCard/useIndex';
import { usePageListCache } from '@/hook/usePageListCache';
export const useIndex = () => {
  // const dateList = ref<{ key: string; date: string; data: SportsCardData[] }[]>([]);
  const list = ref<SportsCardData[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const firstLoad = ref(false);
  const currentPage = ref(0);
  const pageSize = ref(10);
  const { getRenderData, clearCache } = usePageListCache<SportsCardData>({
    cacheKey: 'matchId',
  });
  const onLoad = () => {
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
  // const getCurrentItem = (key: string, date: string) => {
  //   const index = dateList.value.findIndex((cur) => cur.key === key);
  //   let renderIndex = index;
  //   if (index === -1) {
  //     dateList.value.push({
  //       key,
  //       date,
  //       data: [],
  //     });
  //     renderIndex = dateList.value.length - 1;
  //   }
  //   return dateList.value[renderIndex];
  // };
  const initData = async (page = 1) => {
    try {
      error.value = false;
      const response = await getSubscribeList({
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
      const renderData = getRenderData(formatCardData(data));
      list.value.push(...renderData);
      // renderData.forEach((cur) => {
      //   getCurrentItem(page + '_' + cur.date, cur.date).data.push(cur);
      // });
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
      closeLoading();
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
    onClearData,
    onFirstLoad,
    firstLoad,
  };
};
