import { defineStore } from 'pinia';

import apis from '@/api/home/index';
import { getUserAdv } from '@/api/user';

export const useResourceStore = defineStore(
  'resource-status',
  () => {
    const bannerList = ref([] as any);
    const adInfo = reactive<{
      imgUrl: string;
      targetUrl: string;
      id: string;
    }>({
      id: '',
      imgUrl: '',
      targetUrl: '',
    });
    const getBannerList = () => {
      return new Promise(async (resolve) => {
        const bannerRes = await apis.getBanners({});
        bannerList.value = bannerRes;
        resolve(bannerList.value);
      });
    };
    const getAdInfo = () => {
      return new Promise(async (resolve) => {
        const response = await getUserAdv();
        adInfo.imgUrl = response.imgUrl || '';
        adInfo.targetUrl = response.targetUrl || '';
        adInfo.id = (response.id || '') + '';
        resolve(adInfo);
      });
    };
    return {
      bannerList,
      getBannerList,
      getAdInfo,
      adInfo,
    };
  },
  {
    persist: false,
  },
);
