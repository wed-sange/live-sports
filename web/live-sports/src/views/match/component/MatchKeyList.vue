<template>
  <PullRefresh :disabled="true" v-model="matchStore.refreshing" @refresh="matchStore.onPullMatchListRefresh">
    <VirtualListScroller
      v-show="listData.length > 0"
      v-model="matchStore.loadingKey[getListKey()]"
      :finished="matchStore.finishedKey[getListKey()]"
      :finished-text="LOAD_MORE_TEXT"
      @load="onLoadMore"
      :list="listData"
      keyField="matchId"
      :distance="getDistance()"
      ref="virtualRef"
    >
      <template #default="{ itemData, index }">
        <sports-card :item="itemData" :class="!index ? 'pt-[24px]' : ''">
          <template #bottom v-if="itemData && itemData.anchorList && itemData.anchorList.length > 0">
            <anchors :anchorList="itemData.anchorList" />
          </template>
        </sports-card>
      </template>
    </VirtualListScroller>
  </PullRefresh>
</template>
<script setup>
  import { LOAD_MORE_TEXT } from '../../../../build/constant';
  import { useMatchStore } from '@/store/modules/match';
  import { VirtualListScroller } from '@/components/common/VirtualListScroller';
  import { useAppStore } from '@/store/modules/app';
  import { isIos } from '@/utils/common';

  const appStore = useAppStore();
  const SportsCard = defineAsyncComponent(() => import('@/components/SportsCard/index.vue'));
  const Anchors = defineAsyncComponent(() => import('@/components/SportsCard/Anchors.vue'));
  const matchStore = useMatchStore();
  const virtualRef = ref();
  const props = defineProps({
    item: {
      type: Object,
    },
    tabs: {
      type: Array,
    },
  });
  const listData = computed(() => {
    return matchStore.getKeyList(getListKey());
  });
  const getDistance = () => {
    return appStore.showBar ? (isIos() ? 112 : 72) : 20;
  };

  const getListKey = () => {
    const { style, matchType, competitionId } = props.item;
    const listKey = style + 'x-' + (competitionId || '') + '-x' + (matchType || '');
    return listKey;
  };
  const onLoadMore = () => {
    new Promise(() => {
      if (matchStore.loadingKey[getListKey()]) {
        matchStore.loadingKey[getListKey()] = true;
      }
      matchStore.onMatchListLoad(getListKey());
    });
  };
  const getScrollOffset = () => {
    return virtualRef.value && virtualRef.value.getScrollOffset();
  };
  const setScrollOffset = (offset) => {
    virtualRef.value && virtualRef.value.setScrollOffset(offset);
  };
  defineExpose({ getScrollOffset, setScrollOffset });
</script>
<style scoped lang="scss">
  :deep(.vue-recycle-scroller) {
    padding-bottom: 100px;
  }
  .van-pull-refresh {
    :deep() {
      .van-pull-refresh__track {
        overflow-y: scroll;
        height: calc(100vh - 250px - var(--app-header-height));
        // height: calc(var(--device-height) - 198px - var(--app-header-height));
      }
    }
  }
</style>
