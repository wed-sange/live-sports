<template>
  <div class="w-full h-full pt-[16px]">
    <div class="w-full h-full overflow-auto" ref="memberListRef">
      <PullRefresh v-model="refreshing" @refresh="onRefresh">
        <EmptyData class="pt-[336px]" icon="message" text="暂无好友" v-if="isEmpty && (refreshing || finished)" />
        <van-index-bar v-else :sticky="false" :index-list="indexList" :teleport="memberListRef">
          <template v-for="item in list" :key="item.index">
            <van-index-anchor class="h-0 opacity-0" :index="item.index" />
            <template v-for="subItem in item.data" :key="subItem.anchorId">
              <MemberCard :item="subItem" @on-remove="handleRemove" />
            </template>
          </template>
        </van-index-bar>
      </PullRefresh>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { IndexBar as VanIndexBar, IndexAnchor as VanIndexAnchor } from 'vant';
  import { useRestoreScroll } from '@/plugins/restoreScroll';
  import MemberCard from './MemberCard.vue';
  import { useIndex } from './useIndex';
  const { indexList, list, isEmpty, finished, refreshing, onRefresh, handleRemove } = useIndex();
  // const { indexList, list, isEmpty, finished, refreshing, handleRemove } = useIndex();
  const memberListRef = ref<HTMLElement | null>(null);
  useRestoreScroll({
    el: memberListRef,
  });
</script>
<script lang="ts">
  export default {
    name: 'MemberList',
  };
</script>

<style scoped lang="scss">
  :deep(.van-index-bar__index) {
    font: 400 22px / normal 'PingFang SC';
    color: #94999f;
    padding-right: 27px;
    &.van-index-bar__index--active {
      font-weight: 400;
      color: #94999f;
    }
  }

  .van-pull-refresh {
    :deep() {
      .van-pull-refresh__track {
        min-height: calc(var(--device-height) - 192px);
      }
    }
  }
</style>
