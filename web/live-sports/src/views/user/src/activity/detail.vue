<!-- eslint-disable vue/no-v-html -->
<template>
  <page-box class="activity-page">
    <template #header> <AppHeader /><NavBar title="活动详情" left-arrow @click-left="onLeftClick" /></template>
    <div class="w-full h-full overflow-scroll px-[24px]">
      <div class="article-detail__title pt-[24px] pb-[20px]">{{ item.name }}</div>
      <div class="article-detail__desc">
        <div class="article-detail__author"><span class="article-detail__author--icon"></span>体育直播</div>
        <div class="article-detail__time pb-[2px]">{{ item.publishByTime }}<span class="ml-[7px]">发布</span></div>
      </div>
      <div class="article-detail__info mt-[30px] pb-[60px]" v-html="item.content"> </div>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import { useDetail } from './useIndex';
  const route = useRoute();
  const router = useRouter();
  const onLeftClick = () => {
    router.go(-1);
  };
  const id = ref((route.query.id || '') as string);
  const { item, initData } = useDetail();
  initData(id.value);
</script>
<script lang="ts">
  export default {
    name: 'UserActivityDetail',
  };
</script>

<style scoped lang="scss">
  .activity-page.page-box {
    background: #fff;
  }
  .article-detail__title {
    font: 500 36px / normal 'PingFang SC';
    color: #37373d;
    word-break: break-all;
  }
  .article-detail__desc {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .article-detail__author {
    flex: 0 0 auto;
    width: auto;
    height: 44px;
    font: 400 24px / normal 'PingFang SC';
    color: #575762;
    display: flex;
    align-items: center;
    .article-detail__author--icon {
      display: inline-block;
      width: 44px;
      height: 44px;
      border-radius: 100%;
      overflow: hidden;
      background: url('@/assets/img/user/icon_sports.png') no-repeat, transparent;
      background-size: auto 100%;
      background-position: 0 center;
      margin-right: 10px;
    }
  }
  .article-detail__time {
    flex: 0 0 auto;
    width: auto;
    font: 400 22px / normal 'PingFang SC';
    color: #94999f;
  }
  .article-detail__info {
    font: 400 28px / normal 'PingFang SC';
    color: #37373d;
    word-break: break-all;
    :deep() img {
      width: 100%;
      max-width: none;
      height: auto;
      object-fit: contain;
      overflow: hidden;
      border-radius: 8px;
      margin: 28px 0;
    }
  }
</style>
