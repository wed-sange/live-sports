import { getActivityList, getActivityDetail } from '@/api/user';
import { createLoading, closeLoading } from '@/components/SysLoading';
import defaultImgSrc from '@/assets/img/user/default_logo.png';
import dayjs from 'dayjs';
export type ItemData = {
  id: string;
  name: string;
  cover: string;
  publishByTime: string;
  content: string;
};
export const useIndex = () => {
  const list = ref<ItemData[]>([]);
  const error = ref(false);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const currentPage = ref(0);
  const pageSize = ref(10);
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
    initData();
  };
  const initData = async (page = 1) => {
    try {
      error.value = false;
      const response = await getActivityList({
        current: page,
        size: pageSize.value,
      });
      if (refreshing.value) {
        list.value = [];
        refreshing.value = false;
      }
      closeLoading();
      const data = response.records || [];
      const renderData = data.map((cur) => {
        return {
          id: (cur.id || '') + '',
          name: cur.title || '',
          cover: cur.mainPic || defaultImgSrc,
          publishByTime: cur.updateTime ? dayjs(Number(cur.updateTime)).format('YYYY-MM-DD HH:mm') : '',
          content: cur.content || '',
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
export const useDetail = () => {
  const item = reactive<ItemData>({
    id: '',
    name: '',
    cover: '',
    publishByTime: '',
    content: '',
  });
  const initData = async (id: string) => {
    createLoading({ overlay: false });
    try {
      const response = await getActivityDetail(id);

      item.id = (response.id || '') + '';
      item.name = response.title || '';
      item.cover = response.mainPic || defaultImgSrc;
      item.publishByTime = response.updateTime ? dayjs(Number(response.updateTime)).format('YYYY-MM-DD HH:mm') : '';
      item.content = response.content || '';
    } finally {
      closeLoading();
    }
  };
  return {
    item,
    initData,
  };
};
