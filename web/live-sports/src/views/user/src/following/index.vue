<template>
  <page-box>
    <template #header> <AppHeader /><NavBar title="我的关注" left-arrow @click-left="onLeftClick" /></template>
    <div class="w-full h-full">
      <PullRefresh v-model="refreshing" @refresh="onRefresh" :disabled="!isScrollTop">
        <EmptyData class="!h-[auto] pt-[408px]" v-if="list.length === 0 && !refreshing && finished" />
        <VirtualListScroller
          v-model="loading"
          :list="list"
          :refreshing="refreshing"
          :finished="finished"
          :finished-text="list.length === 0 ? '' : ''"
          v-model:error="error"
          error-text="请求失败，点击重新加载"
          @load="onLoad"
          class="pt-[12px]"
          @scroll="onScroll"
          :firstLoad="firstLoad"
          :distance="8"
        >
          <template #default="{ itemData }">
            <AnchorCard :item="itemData" />
          </template>
        </VirtualListScroller>
        <!-- </div> -->
      </PullRefresh>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import AnchorCard from './AnchorCard.vue';
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
    console.log('onFollowing');
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
    name: 'UserFollowing',
  };
</script>

<style lang="scss" scoped>
  .van-pull-refresh {
    height: 100%;
    min-height: calc(var(--device-height) - 88px);
  }
</style>
