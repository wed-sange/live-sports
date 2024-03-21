<template>
  <page-box :class="`flex flex-col home-page h-full`">
    <template #header>
      <AppHeader />
    </template>
    <img @click="goSearch" :class="`click-style fixed right-[24px] search-icon  z-[999] w-[40px] h-[40px] bg-contain`" :src="searchIcon" />
    <van-tabs
      v-model:active="active"
      background="white"
      title-active-color="#37373D"
      title-inactive-color="#94999F"
      line-height="0.8vw"
      line-width="6.133333vw"
      @change="onChangeTab"
      sticky
      swipeable
      shrink
      animated
    >
      <van-tab :title="item.title" v-for="item in (tabList as any)" :key="item.title">
        <KeepAlive>
          <component :is="item.component" :key="item.title" />
        </KeepAlive>
      </van-tab>
    </van-tabs>
  </page-box>
</template>

<script lang="ts" setup name="HomePage">
  import { ref, markRaw } from 'vue';
  import searchIcon from '@/assets/img/home/home-search-icon.png';
  import router from '@/router';

  const Recommend = defineAsyncComponent(() => import('./components/Recommend.vue'));
  const FootBall = defineAsyncComponent(() => import('./components/FootBall.vue'));
  const BasketBall = defineAsyncComponent(() => import('./components/BasketBall.vue'));
  const News = defineAsyncComponent(() => import('./components/News.vue'));

  const active = ref(0);
  const tabList = [
    {
      title: '推荐',
      style: 0,
      component: markRaw(Recommend),
    },
    {
      title: '足球',
      style: 1,
      component: markRaw(FootBall),
    },
    {
      title: '篮球',
      style: 2,
      component: markRaw(BasketBall),
    },
    {
      title: '新闻',
      style: 3,
      component: markRaw(News),
    },
  ];

  const onChangeTab = (tabIndex) => {
    active.value = tabIndex;
  };

  const goSearch = () => {
    setTimeout(() => {
      router.push('/search');
    }, 100);
  };
</script>
<style lang="scss" scoped>
  .home-page {
    height: 100%;
    background-size: cover;
    line-height: normal;
    font-weight: 400;
    line-height: normal;

    .search-icon {
      top: calc(20px + var(--app-header-height));
    }
    font-family: PingFang SC;
    line-height: normal;
    font-weight: 400;
    :deep().van-tab {
      margin-right: 60px;
      justify-content: baseline;
      align-items: baseline;
    }
    :deep().van-tab:active {
      opacity: 0.2;
    }
    :deep().van-tab--shrink {
      padding: 0;
    }
    --van-padding-xs: 24px;

    :deep().van-tabs__line {
      border-radius: 80px;
      background: #34a853;
    }
    :deep().van-tab__text {
      margin-top: 23px;
      font-size: 32px;
      line-height: 36px;
    }
    :deep().van-tab--active {
      font-weight: 400;
      line-height: normal;
      .van-tab__text {
        font-size: 36px;
        line-height: normal;
        margin-top: 15px;
        font-weight: 500;
        font-family: 'PingFang SC';
        line-height: 50px;
      }
    }
    :deep().van-tabs__line {
      bottom: 46px;
      margin-left: 0.5px;
    }
    :deep().van-tabs__wrap {
      height: 88px;
      line-height: 88px;
      background: transparent;
    }
    :deep(.van-tabs) {
      height: 100%;
      display: flex;
      flex-flow: column;

      .van-tabs__content {
        flex: 1 1 auto;
        .van-tab__panel {
          height: 100%;
          padding-bottom: 112px;
          overflow-y: scroll;
          min-height: calc(100vh - 248px - var(--app-header-height));
        }
      }
    }
  }
</style>
