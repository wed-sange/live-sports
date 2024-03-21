<template>
  <page-box>
    <template #header>
      <AppHeader />
      <NavBar title="我的订阅" left-arrow @click-left="onLeftClick"
    /></template>
    <div class="w-full h-full">
      <PullRefresh :disabled="!isScrollTop" v-model="refreshing" @refresh="onRefresh">
        <EmptyData class="pt-[408px] !h-[auto]" v-if="list.length === 0 && !refreshing && finished" />
        <VirtualListScroller
          v-model="loading"
          :finished="finished"
          :finished-text="list.length === 0 ? '' : ''"
          v-model:error="error"
          :refreshing="refreshing"
          :firstLoad="firstLoad"
          error-text="请求失败，点击重新加载"
          @load="onLoad"
          class="pt-[12px]"
          @scroll="onScroll"
          :list="list"
          keyField="matchId"
          :distance="8"
        >
          <template #default="{ itemData }">
            <SportsCard :item="itemData" :showDate="false">
              <template #bottom v-if="itemData.anchorList && itemData.anchorList.length > 0">
                <anchors :anchorList="itemData.anchorList" />
              </template>
            </SportsCard>
          </template>
        </VirtualListScroller>
      </PullRefresh>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import SportsCard from '@/components/SportsCard/index.vue';
  import Anchors from '@/components/SportsCard/Anchors.vue';
  import { useIndex } from './useIndex';
  import { VirtualListScroller, useVirtualListScroller } from '@/components/common/VirtualListScroller';
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
    name: 'UserSubscribe',
  };
</script>

<style lang="scss" scoped>
  .van-pull-refresh {
    height: calc(var(--device-height) - 88px);
  }
</style>
