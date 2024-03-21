<template>
  <div class="flex flex-col match-recommend" ref="tabRef">
    <div class="flex items-center relative match-tabs" v-if="tabs && tabs.length > 0">
      <div class="overflow-x-hidden relative">
        <van-tabs
          background="transparent"
          title-active-color="#34A853"
          title-inactive-color="#94999F"
          v-model:active="active"
          :swipeable="!appStore.isMatchScroll"
          shrink
          animated
          sticky
          offset-top="16px"
          @change="onChangeTab"
        >
          <van-tab :key="item.competitionName" :value="item.competitionId" :title="item.competitionName" v-for="(item, index) in tabs">
            <KeepAlive v-show="judgeShowComponent(index)">
              <div
                :id="itemData.style + 'x-' + item.competitionId + '-x' + item.matchType"
                class="match-list-page match-position w-full overflow-auto bg-[#F2F3F7]"
                ref="scrollRef"
              >
                <MatchKeyList ref="matchScrollRef" :tabs="tabs" :item="{ ...item, style: itemData.style }" />
                <empty-data
                  v-show="!matchStore.loadingKey[getListKey()] && matchStore.getKeyList(getListKey()).length == 0"
                  class="mt-[250px]"
                  text="暂无数据"
                />
              </div>
            </KeepAlive>
          </van-tab>
        </van-tabs>
        <div @touchstart.stop class="absolute right-0 top-0 h-[64px] w-[136px] flex items-center">
          <div class="w-[136px] h-[40px] absolute right-0 right-filter-item"></div>
          <img
            v-show="tabs && tabs.length > 0"
            class="w-[44px] click-style mt-[-8px] h-[44px] bg-contain z-10 ml-[72px]"
            @click="onShowCalendar"
            :src="calendarIcon"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
  import dayjs from 'dayjs';
  import { useMatchStore } from '@/store/modules/match';
  import { useAppStore } from '@/store/modules/app';

  import calendarIcon from '@/assets/img/match/calendar-icon.png';

  const EmptyData = defineAsyncComponent(() => import('@/components/common/EmptyData.vue'));
  const MatchKeyList = defineAsyncComponent(() => import('./MatchKeyList.vue'));

  const appStore = useAppStore();
  const matchStore = useMatchStore();
  const active = ref(0);
  const scrollRef = ref();
  const queryParams = ref({});
  const tabRef = ref();
  const matchScrollRef = ref();

  const props = defineProps({
    itemData: {
      type: Object,
    },
  });
  const judgeShowComponent = (index) => {
    const currentIndex = active.value;
    if (index == currentIndex || index == currentIndex + 1 || index == currentIndex - 1) {
      return true;
    }
    return false;
  };
  const tabs = computed(() => {
    return matchStore.getSecondSort(props.itemData.style);
  });

  onMounted(async () => {
    //滚动拦截推荐足球篮球tab的分类列表拦截滚动，以防止切换页面
    const findParentNode = (el) => {
      if (el == tabRef.value) {
        return null;
      }
      if (!el.classList.contains('van-tabs__wrap')) {
        return findParentNode(el.parentNode);
      } else {
        return el;
      }
    };
    tabRef.value.addEventListener('touchstart', (e) => {
      const elData = findParentNode(e.target);
      if (elData) {
        e.stopPropagation();
      }
    });
  });

  const getListKey = () => {
    const currentItem = tabs.value[active.value];
    if (!currentItem) {
      return '';
    }
    const listKey = props.itemData.style + 'x-' + (currentItem.competitionId || '') + '-x' + (currentItem.matchType || '');
    return listKey;
  };
  const onChangeTab = () => {
    const index = active.value;
    const currentItem = tabs.value[index];
    if (!currentItem) {
      return;
    }
    //切换tab规则推荐篮球足球tab全部默认取当天数据其下的别的tab 取当天到后两天的数据
    //赛果tab篮球足球全部tab取当天赛果，其下别的tab取前两天到今天数据
    matchStore.setCurrentActive(index);
    queryParams.value.competitionId = currentItem.competitionId || '';
    queryParams.value.matchType = currentItem.matchType || '';
    let startTime = dayjs(new Date()).format('YYYY-MM-DD');
    let endTime = dayjs(new Date()).format('YYYY-MM-DD');
    if (matchStore.oneTabStyle == 3) {
      startTime = queryParams.value.competitionId ? dayjs().subtract(2, 'day').format('YYYY-MM-DD') : startTime;
    } else {
      endTime = queryParams.value.competitionId ? dayjs().add(2, 'day').format('YYYY-MM-DD') : startTime;
    }
    matchStore.onResetSort({ ...queryParams.value, startTime, endTime }, true);
  };
  const onShowCalendar = () => {
    matchStore.onShowOrHideCalendar(true);
  };
  const getKeyId = (item) => {
    return props.itemData.style + 'x-' + item.competitionId + '-x' + item.matchType;
  };
  //保留滚动前tab高度,滚动到新tab,新tab滚动到之前的保留的高度
  watch(
    () => active.value,
    (newValue, oldValue) => {
      if (!tabs.value || !tabs.value[oldValue]) {
        return;
      }
      const id = getKeyId(tabs.value[oldValue]);
      const currentItem = document.getElementById(id);
      if (!currentItem && matchScrollRef.value[oldValue]) {
        return;
      }
      const scrollTop = matchScrollRef.value[oldValue].getScrollOffset();
      matchStore.setMatchPageScroll(scrollTop, id);
      setTimeout(() => {
        const currentScrollTab = matchScrollRef.value[newValue];
        const newId = getKeyId(tabs.value[newValue]);
        const newScrollTop = matchStore.getMatchPageScroll(newId);
        if (!newScrollTop) {
          return;
        }
        currentScrollTab && currentScrollTab.setScrollOffset(newScrollTop);
      }, 0);
    },
    { immediate: true },
  );
</script>
<style lang="scss" scoped>
  .match-recommend {
    .match-tabs {
      background: white;
      --van-padding-xs: 24px;
      .right-filter-item {
        background: linear-gradient(271deg, #fff 42.69%, rgba(255, 255, 255, 0) 175.8%);
      }
      .match-list-page {
        height: 100%;
        overflow-y: scroll;
        // height: calc(var(--device-height) - 198px - var(--app-header-height));
      }

      :deep().van-tabs__wrap {
        height: 70px;
        line-height: 70px;
        background: transparent;
        padding-right: 80px;
      }

      :deep().van-tabs__line {
        display: none;
      }
      :deep().van-tabs__nav--complete {
        justify-content: flex-start !important;
      }
      :deep().van-tab {
        padding: 0 50px 0 0;
        margin: 8px 0 0 0;
      }
      :deep().van-tab__text {
        font-family: PingFang SC;
        font-size: 28px;
        font-style: normal;
        font-weight: 400;
        line-height: 40px;
        margin-top: 0;
      }
      :deep().van-tab:active {
        opacity: 0.2;
      }
      :deep().van-tab--active {
        .van-tab__text {
          font-family: PingFang SC;
          font-size: 30px;
          font-style: normal;
          font-weight: 500;
          // line-height: normal;
          line-height: 40px;
        }
      }

      :deep(.van-tabs) {
        height: 100%;
        display: flex;
        flex-flow: column;

        .van-tabs__content {
          flex: 1 1 auto;
          .van-tab__panel {
            // height: 100%;
            // padding-bottom: 180px;
            overflow-y: scroll;
            height: calc(100vh - 198px - var(--app-header-height));
          }
        }
      }
      :deep().van-swipe-item {
        width: 100vw;
      }
    }
  }
</style>
