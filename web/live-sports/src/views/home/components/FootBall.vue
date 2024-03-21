<template>
  <div class="px-[24px] h-full football" ref="scrollRef" v-show="loaded">
    <VirtualListScroller
      v-show="list.length > 0 && showList"
      v-model="loading"
      :finished="finished"
      :finished-text="list.length > 0 ? LOAD_MORE_TEXT : ''"
      @load="onLoad"
      :list="list"
      :noBottom="true"
    >
      <template #default="{ itemData, index }">
        <div :class="`grid grid-cols-2 pb-[16px] gap-x-[14px] ${!index ? 'mt-[24px]' : index == list.length - 1 ? 'mb-[12px]' : ''}`">
          <template v-for="subItem in itemData.data" :key="subItem.id">
            <room textColor="#37373D" :data="subItem" />
          </template>
        </div>
      </template>
    </VirtualListScroller>
    <EmptyData v-if="list.length == 0 && showList" text="暂无主播" icon="anchor" />
  </div>
</template>
<script setup>
  import apis from '@/api/home/index';
  import { useMessageStore } from '@/store/modules/message';
  import { MessageCommand } from '@/views/message/utils/types';
  import { LOAD_MORE_TEXT } from '../../../../build/constant';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  import { useRestoreScroll } from '@/plugins/restoreScroll';
  import { VirtualListScroller } from '@/components/common/VirtualListScroller';

  const EmptyData = defineAsyncComponent(() => import('@/components/common/EmptyData.vue'));
  const Room = defineAsyncComponent(() => import('@/components/common/RoomItem.vue'));
  const messageStore = useMessageStore();
  const loadingStatus = ref(true);
  const list = ref([]);
  const loading = ref(true);
  const finished = ref(false);
  const refreshing = ref(false);
  const queryParams = ref({ current: 1, size: 10, matchType: 1 });
  const showList = ref(false);
  const scrollRef = ref();
  const loaded = ref(false);
  let historyId = 0;
  useRestoreScroll({
    el: scrollRef,
  });

  // const onRefresh = () => {
  //   refreshing.value = true;
  //   // 清空列表数据
  //   finished.value = true;
  //   loading.value = true;
  //   // 重新加载数据
  //   // 将 loading 设置为 true，表示处于加载状态
  //   queryParams.value.current = 1;
  //   onLoad();
  // };
  const onWsListen = () => {
    const onCheck = () => {
      const messageServer = messageStore.getMessageServer();
      if (!messageServer) {
        setTimeout(() => {
          onCheck();
        }, 1000);
        return;
      }
      messageServer.chatEmitter.on('messageAll', (res) => {
        setTimeout(() => {
          if (res.command === MessageCommand.openLive) {
            onAddLiveItem(res.data);
          } else if (res.command === MessageCommand.closeLive) {
            onRemoveLiveItem(res.data);
          }
        }, 200);
      });
    };
    onCheck();
  };
  const onAddLiveItem = (item) => {
    let allList = [];
    let findData = false;
    if (list.value && list.value.length > 0) {
      list.value.forEach((itemData) => {
        itemData &&
          itemData.data.forEach((childItemData) => {
            if (childItemData.userId + '' == item.anchorId + '') {
              findData = true;
            }
            allList.push(childItemData);
          });
      });
    }
    if (findData) {
      return;
    }
    allList.push({ ...item, userId: item.anchorId, titlePage: item.coverImg });
    allList.sort(function (a, b) {
      return b.hotValue - a.hotValue;
    });
    let newIndex = 0;
    let newList = [];
    allList.forEach((cur) => {
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
    list.value = newList;
  };
  const onRemoveLiveItem = (data) => {
    let newListData = [];
    if (list.value && list.value.length > 0) {
      list.value.forEach((item) => {
        item.data &&
          item.data.forEach((childItem) => {
            if (childItem.userId + '' != data.anchorId + '') {
              newListData.push(childItem);
            }
          });
      });
      let newList = [];
      let newIndex = 0;
      newListData.forEach((cur) => {
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
      list.value = newList;
    }
  };

  const onLoad = () => {
    loading.value = true;
    getLives();
  };
  const getLives = () => {
    apis.getHomeLiving(queryParams.value).then((res) => {
      closeLoading();
      nextTick(() => {
        let { total, records } = res;
        if (loadingStatus.value) {
          loadingStatus.value = false;
        }
        // if (queryParams.value.current == 1) {
        //   list.value = records;
        // } else {
        //   list.value = list.value.concat(records);
        // }
        const oldIndex = list.value.length - 1;
        const lastItem = oldIndex >= 0 ? list.value[oldIndex] : {};
        if (lastItem.data && lastItem.data.length === 1 && records.length > 0) {
          lastItem.data.push(records[0]);
          records = records.slice(1);
        }
        let newIndex = 0;
        const newList = [];
        records.forEach((cur) => {
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
        showList.value = true;
        refreshing.value = false;
        queryParams.value.current++;
        if (queryParams.value.current * queryParams.value.size >= total || total === undefined) {
          finished.value = true;
        }
        setTimeout(() => {
          loaded.value = true;
        }, 200);
      }).catch(() => {
        showList.value = true;
        loaded.value = true;
        finished.value = true;
      });
    });
  };
  onMounted(() => {
    showList.value = false;
    createLoading({ overlay: false });
    getLives();
    onWsListen();
  });
</script>
<style scoped lang="scss">
  .football {
    min-height: calc(100vh - 500px);
    :deep(.vue-recycle-scroller__item-wrapper) {
      margin-top: 24px !important;
      margin-bottom: 12px !important;
    }
  }
</style>
