<template>
  <div class="lives-page h-full">
    <VirtualListScroller
      v-show="list.length > 0 && showList"
      v-model="loading"
      :finished="finished"
      :finished-text="list.length > 0 ? '' : ''"
      @load="onLoad"
      :list="list"
      :noBottom="true"
    >
      <template #default="{ itemData, index }">
        <div :class="`grid grid-cols-2 ${!index ? 'mt-[24px]' : ''} pb-[16px] pl-[24px] pr-[8px]`">
          <template v-for="subItem in itemData.data" :key="subItem.id">
            <room textColor="#fff" bg="#1F1F20" :data="subItem" />
          </template>
        </div>
      </template>
    </VirtualListScroller>
    <no-data
      text="暂无其他主播"
      class="ml-[1px]"
      :class="{ 'mt-[116px]': !liveId }"
      v-if="list.length == 0 && showList"
      :grayText="true"
      icon="lives"
    />
  </div>
</template>
<script setup>
  import apis from '@/api/home/index';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  import { VirtualListScroller } from '@/components/common/VirtualListScroller';
  let historyId = 0;
  const Room = defineAsyncComponent(() => import('@/components/common/RoomItem.vue'));
  const noData = defineAsyncComponent(() => import('@/components/common/EmptyData.vue'));
  const loadingStatus = ref(true);
  const list = ref([]);
  const loading = ref(true);
  const finished = ref(false);
  const refreshing = ref(false);
  const showList = ref(false);
  const props = defineProps({
    matchId: {
      type: String,
    },
    matchType: {
      type: String,
    },
    liveId: {
      type: String,
    },
  });
  const queryParams = ref({
    current: 1,
    size: 10,
    exceptId: props.liveId,
    matchType: props.matchType,
  });

  const onRefresh = () => {
    // 清空列表数据
    finished.value = false;

    // 重新加载数据
    // 将 loading 设置为 true，表示处于加载状态
    loading.value = true;
    queryParams.value.current = 1;
    showList.value = false;
    onLoad();
  };

  const onLoad = () => {
    if (refreshing.value) {
      list.value = [];
      refreshing.value = false;
    }
    getLives();
  };
  const getLives = () => {
    apis
      .getHomeLiving(queryParams.value)
      .then((res) => {
        if (loadingStatus.value) {
          loadingStatus.value = false;
        }
        let { total, records } = res;
        records &&
          records.map((item) => {
            const { homeTeamName, awayTeamName, matchType } = item;
            if (matchType == 2) {
              item.homeTeamName = awayTeamName;
              item.awayTeamName = homeTeamName;
            }
            return item;
          });

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
        console.log('list', list.value);
        showList.value = true;
        loading.value = false;
        finished.value = total < queryParams.value.size || list.value.length === total;
        queryParams.value.current++;
        console.log('finished.value', finished.value);
        closeLoading();
      })
      .catch((error) => {
        console.log('error', error);
        showList.value = true;
        closeLoading();
      });
  };
  onMounted(() => {
    createLoading({ overlay: false });
    onRefresh();
  });
</script>
<style scoped lang="scss">
  .lives-page {
    .van-tabs__content {
      flex: 1 1 auto;

      .van-tab__panel {
        height: auto;
        padding-left: 24px;
      }
    }
    .van-pull-refresh {
      :deep() {
        .van-pull-refresh__track {
          min-height: calc(var(--device-height) - 680px);
        }
      }
    }
  }
</style>
