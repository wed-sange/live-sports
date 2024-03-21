<template>
  <page-box class="history-page">
    <template #header> <AppHeader /><NavBar title="观看历史" left-arrow @click-left="onLeftClick" /></template>
    <div class="w-full h-full">
      <PullRefresh v-model="refreshing" @refresh="onRefresh" :disabled="!isScrollTop">
        <EmptyData class="mt-[0] pt-[408px] !h-[auto]" v-if="list.length === 0 && !refreshing && finished" />
        <VirtualListScroller
          v-model="loading"
          :finished="finished"
          :finished-text="list.length === 0 ? '' : ''"
          v-model:error="error"
          :refreshing="refreshing"
          :firstLoad="firstLoad"
          error-text="请求失败，点击重新加载"
          @load="onLoad"
          @scroll="onScroll"
          :list="list"
          :distance="8"
        >
          <template #default="{ itemData, index }">
            <div :class="`grid grid-cols-2 gap-x-[14px] px-[24px] ${!index ? 'mt-[16px]' : ''}`">
              <template v-for="subItem in itemData.data" :key="subItem.matchId">
                <SportsMiniCard :item="subItem" />
              </template>
            </div>
          </template>
        </VirtualListScroller>
      </PullRefresh>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import SportsMiniCard from '@/components/SportsCard/SportsMiniCard.vue';
  import { VirtualListScroller, useVirtualListScroller } from '@/components/common/VirtualListScroller';

  import { useIndex } from './useIndex';
  import { useUserStore } from '@/store/modules/user';

  const userStore = useUserStore();
  const router = useRouter();
  const onLeftClick = () => {
    router.go(-1);
  };
  const { list, error, loading, finished, refreshing, onLoad, onRefresh, firstLoad, onFirstLoad, onClearData } = useIndex();
  const { isScrollTop, onScroll, onSetScrollTop } = useVirtualListScroller();
  // onActivated(() => {
  //   if (!list || list.value.length == 0) {
  //     onSetScrollTop(true);
  //     onFirstLoad();
  //   }
  // });
  onMounted(() => {
    if (!list || list.value.length == 0) {
      onSetScrollTop(true);
      onFirstLoad();
    }
  });
  watch(
    () => userStore.token,
    () => {
      onClearData();
    },
    {
      deep: true,
    },
  );
</script>
<script lang="ts">
  export default {
    name: 'UserHistory',
  };
</script>

<style lang="scss" scoped>
  .history-page {
    .van-pull-refresh {
      height: 100%;
    }
    .van-pull-refresh__track {
      height: 100%;
      min-height: calc(var(--device-height) - 88px);
    }
  }
</style>
