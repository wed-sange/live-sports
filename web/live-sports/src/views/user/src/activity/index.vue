<template>
  <page-box class="activity-page">
    <template #header>
      <AppHeader />
      <NavBar title="活动中心" left-arrow @click-left="onLeftClick"
    /></template>
    <div class="w-full h-full" ref="scrollRef">
      <div class="w-full h-[24px] bg-[#f2f3f7]"></div>
      <PullRefresh v-model="refreshing" @refresh="onRefresh" :disabled="!isScrollTop">
        <EmptyData class="pt-[408px] !h-[auto]" v-if="list.length === 0 && (refreshing || finished)" />
        <VirtualListScroller
          v-model="loading"
          :refreshing="refreshing"
          :finished="finished"
          :finished-text="list.length === 0 ? '' : ''"
          v-model:error="error"
          error-text="请求失败，点击重新加载"
          @load="onLoad"
          @scroll="onScroll"
          :list="list"
          :distance="16"
        >
          <template #default="{ itemData }">
            <ArticleCard :item="itemData" />
          </template>
        </VirtualListScroller>
      </PullRefresh>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import { useRestoreScroll } from '@/plugins/restoreScroll';
  import ArticleCard from './ArticleCard.vue';
  import { useIndex } from './useIndex';
  import { VirtualListScroller, useVirtualListScroller } from '@/components/common/VirtualListScroller';
  const router = useRouter();
  const onLeftClick = () => {
    router.go(-1);
  };
  const { list, error, loading, finished, refreshing, onLoad, onRefresh } = useIndex();
  const scrollRef = ref<HTMLElement>();
  onMounted(() => {
    onLoad();
  });
  useRestoreScroll({
    el: scrollRef,
  });
  const { isScrollTop, onScroll } = useVirtualListScroller();
</script>
<script lang="ts">
  export default {
    name: 'UserActivity',
  };
</script>

<style lang="scss" scoped>
  .activity-page {
    .van-pull-refresh {
      background: white;
      height: calc(var(--device-height) - 88px - 24px);
    }
  }
</style>
