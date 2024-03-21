import { defineStore } from 'pinia';

export const useAppStore = defineStore(
  'app-status',
  () => {
    const immersive = ref(false);
    const backAnimation = ref(false);
    const loadStaticResource = ref(false);
    const bannerList = ref([] as any);
    const matchLongClick = ref(false);
    const isMatchScroll = ref(false);
    const isLiveScroll = ref(false);
    const showBar = ref(true);
    const clearRoom = ref(false);
    const routerPaths = ref([
      'Match',
      'HomePage',
      'Message',
      'User',
      'RoomDetail',
      'UserSubscribe',
      'UserFollowing',
      'UserActivity',
      'UserHistory',
    ]);

    const onAddRouterName = (name) => {
      const find = routerPaths.value.find((item) => item === name);
      if (find) {
        return;
      } else {
        routerPaths.value.push(name);
      }
    };
    const onRemoveRouterName = (name) => {
      const findIndex = routerPaths.value.findIndex((item) => item === name);
      if (findIndex != -1) {
        routerPaths.value.splice(findIndex, 1);
      }
    };
    return {
      immersive,
      backAnimation,
      loadStaticResource,
      bannerList,
      matchLongClick,
      isMatchScroll,
      isLiveScroll,
      showBar,
      routerPaths,
      onAddRouterName,
      onRemoveRouterName,
      clearRoom,
    };
  },
  {
    persist: false,
  },
);
