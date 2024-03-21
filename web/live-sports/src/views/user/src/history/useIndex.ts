import { getLiveHistoryList } from '@/api/user';
import { createLoading, closeLoading } from '@/components/SysLoading';
import { type SportsMiniCardData } from '@/components/SportsCard/types';
import { formatMiniCardData } from '@/components/SportsCard/useIndex';
import { useUserStore } from '@/store/modules/user';
type HistoryCardList = {
  id: string;
  data: SportsMiniCardData[];
};
let historyId = 0;
export const useIndex = () => {
  const userStore = useUserStore();
  const list = ref<HistoryCardList[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const currentPage = ref(0);
  const pageSize = ref(10);
  const firstLoad = ref(false);
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
    historyId = 0;
    // clearCache();
    initData();
  };
  const onFirstLoad = () => {
    // 清空列表数据
    firstLoad.value = true;
    currentPage.value = 0;
    historyId = 0;
    createLoading({ overlay: false });
    initData(currentPage.value + 1);
  };
  const onClearData = () => {
    // 清空列表数据
    firstLoad.value = false;
    finished.value = false;
    loading.value = false;
    list.value = [];
    historyId = 0;
    // clearCache();
  };
  const initData = async (page = 1) => {
    if (!userStore.token) {
      loading.value = false;
      refreshing.value = false;
      finished.value = true;
      return;
    }
    try {
      error.value = false;
      const response = await getLiveHistoryList({
        current: page,
        size: pageSize.value,
      });
      closeLoading();
      firstLoad.value = false;
      if (refreshing.value) {
        list.value = [];
        refreshing.value = false;
      }
      const data = response.records || [];
      let renderData = formatMiniCardData(data);
      const oldIndex = list.value.length - 1;
      const lastItem = oldIndex >= 0 ? list.value[oldIndex] : ({} as HistoryCardList);
      if (lastItem.data && lastItem.data.length === 1 && renderData.length > 0) {
        lastItem.data.push(renderData[0]);
        renderData = renderData.slice(1);
      }
      const newList: HistoryCardList[] = [];
      let newIndex = 0;
      renderData.forEach((cur) => {
        if (newList[newIndex]) {
          newList[newIndex].data.push(cur);
          newIndex++;
        } else {
          newList[newIndex] = {
            id: historyId + '',
            data: [cur],
          };
          historyId++;
        }
      });
      list.value.push(...newList);
      loading.value = false;
      currentPage.value = page;
      if (page * pageSize.value >= response.total || response.total === undefined) {
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
    onClearData,
    onFirstLoad,
    firstLoad,
  };
};
