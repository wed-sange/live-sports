import { getMemberList, removeMember } from '@/api/message';
import { useUserStore } from '@/store/modules/user';
import defaultLogo from '@/assets/img/user/default_anchor.png';
import { usePageListCache } from '@/hook/usePageListCache';
import { showLoadingToast, showConfirmDialog } from 'vant';
import { createLoading, closeLoading } from '@/components/SysLoading';
export type MemberCardData = {
  id: string;
  anchorId: string;
  name: string;
  cover: string;
  shortName: string;
};

export const useIndex = () => {
  const userStore = useUserStore();
  const searchKey = ref('');
  // const indexDataList = ref([
  //   'A',
  //   'B',
  //   'C',
  //   'D',
  //   'E',
  //   'F',
  //   'G',
  //   'H',
  //   'I',
  //   'J',
  //   'K',
  //   'L',
  //   'M',
  //   'N',
  //   'O',
  //   'P',
  //   'Q',
  //   'R',
  //   'S',
  //   'T',
  //   'U',
  //   'V',
  //   'W',
  //   'X',
  //   'Y',
  //   'Z',
  //   '#',
  // ]);
  const indexList = ref<string[]>([]);
  const list = ref<{ index: string; data: MemberCardData[] }[]>([]);
  const dataRequest = ref(false);
  const isEmpty = ref(true);
  const loading = ref(false);
  const finished = ref(false);
  const refreshing = ref(false);
  const currentPage = ref(0);
  const pageSize = ref(100);
  const { getRenderData, clearCache } = usePageListCache<MemberCardData>();
  const onLoad = () => {
    initData(currentPage.value + 1);
  };
  const resetList = () => {
    indexList.value = [];
    list.value = [];
    isEmpty.value = true;
  };
  const resetState = () => {
    clearCache();
    resetList();
    refreshing.value = false;
    finished.value = true;
  };
  const onRefresh = () => {
    // 清空列表数据
    finished.value = false;
    loading.value = true;
    clearCache();
    initData();
  };
  const initData = async (page = 1) => {
    if (dataRequest.value) {
      return;
    } else {
      dataRequest.value = true;
    }
    if (!list.value || list.value.length == 0) {
      createLoading();
    }
    try {
      if (!userStore.token) {
        resetState();
        return;
      }
      const response = await getMemberList({
        current: page,
        size: pageSize.value,
        userName: searchKey.value,
      });
      if (refreshing.value || page === 1) {
        resetList();
        refreshing.value = false;
      }
      const data = response.records || [];
      const renderData = getRenderData(
        data.map<MemberCardData>((cur) => {
          const anchorId = (cur.anchorId || '') + '';
          return {
            id: anchorId,
            anchorId: anchorId,
            name: cur.nickName || '',
            cover: cur.head || defaultLogo,
            shortName: (cur.shortName || '#').toUpperCase(),
          };
        }),
      );
      // for (let keyI = 0; keyI < indexDataList.value.length; keyI++) {
      //   const curKey = indexDataList.value[keyI];
      //   const index =
      //     list.value.push({
      //       index: curKey,
      //       data: [],
      //     }) - 1;
      //   indexList.value.push(curKey);
      //   for (let i = 0; i < renderData.length; i++) {
      //     const cur = { ...renderData[i] };
      //     // let index = list.value.findIndex((item) => item.index === cur.shortName);
      //     // if (index === -1) {
      //     //   index =
      //     //     list.value.push({
      //     //       index: cur.shortName,
      //     //       data: [],
      //     //     }) - 1;
      //     // }
      //     isEmpty.value = false;
      //     cur.anchorId = curKey + '-' + cur.anchorId;
      //     cur.name = curKey + '-' + cur.name;
      //     list.value[index].data.push(cur);
      //   }
      // }
      for (let i = 0; i < renderData.length; i++) {
        const cur = renderData[i];
        let index = list.value.findIndex((item) => item.index === cur.shortName);
        if (index === -1) {
          indexList.value.push(cur.shortName);
          index =
            list.value.push({
              index: cur.shortName,
              data: [],
            }) - 1;
        }
        list.value[index].data.push(cur);
      }
      list.value.sort((a, b) => (a.index === '#' || a.index > b.index ? 1 : -1));
      indexList.value.sort((a, b) => (a === '#' || a > b ? 1 : -1));
      isEmpty.value = list.value.length <= 0;
      loading.value = false;
      currentPage.value = page;
      // if (page * pageSize.value >= response.total) {
      finished.value = true;
      // }
    } finally {
      dataRequest.value = false;
      loading.value = false;
      refreshing.value = false;
      closeLoading();
    }
  };
  const handleRemove = (item: MemberCardData) => {
    showConfirmDialog({
      message: '确定删除该好友？',
      className: 'custom-dialog-confirm',
    })
      .then(async () => {
        const toast = showLoadingToast({
          duration: 0,
          forbidClick: true,
          message: '加载中...',
        });
        try {
          await removeMember(item.anchorId);
          for (let i = 0; i < list.value.length; i++) {
            const data = list.value[i].data;
            const index = data.findIndex((cur) => cur.anchorId === item.anchorId);
            if (index !== -1) {
              list.value[i].data.splice(index, 1);
              break;
            }
          }
        } finally {
          toast.close();
        }
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
  watch(
    () => userStore.token,
    () => {
      onRefresh();
    },
  );
  onMounted(() => {
    onRefresh();
  });
  onActivated(() => {
    onRefresh();
  });
  return {
    searchKey,
    indexList,
    list,
    isEmpty,
    loading,
    finished,
    refreshing,
    onLoad,
    onRefresh,
    handleRemove,
    handleSearch,
  };
};
