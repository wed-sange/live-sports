<template>
  <div class="w-full h-full overflow-auto" ref="scrollRef">
    <PullRefresh v-model="refreshing" @refresh="onRefresh">
      <EmptyData class="pt-[336px]" icon="message" text="暂无聊天记录" v-if="list.length === 0 && (refreshing || finished)" />
      <van-list
        v-model:loading="loading"
        :finished="finished"
        finished-text=""
        v-model:error="error"
        error-text="请求失败，点击重新加载"
        @load="onLoad"
        class="pt-[16px]"
      >
        <template v-for="item in list" :key="item.anchorId">
          <MessageCard :item="item" @on-remove="handleRemove" />
        </template>
      </van-list>
    </PullRefresh>
  </div>
</template>
<script setup lang="ts">
  import MessageCard from './MessageCard.vue';
  import { useRestoreScroll } from '@/plugins/restoreScroll';
  import { useIndex } from './useIndex';
  const { list, error, loading, finished, refreshing, onLoad, onRefresh, handleRemove } = useIndex();
  const scrollRef = ref<HTMLElement>();
  useRestoreScroll({
    el: scrollRef,
  });
  onActivated(() => {
    onRefresh();
  });
</script>
<script lang="ts">
  export default {
    name: 'MessageList',
  };
</script>
<style lang="scss" scoped>
  .van-pull-refresh {
    :deep() {
      .van-pull-refresh__track {
        min-height: calc(var(--device-height) - 114px - 72px);
      }
    }
  }
</style>
