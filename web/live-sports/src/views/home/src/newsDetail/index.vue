<template>
  <page-box>
    <template #header>
      <AppHeader />
      <NavBar left-arrow title="新闻详情" @click-left="back" />
    </template>
    <div class="p-[24px] text-[#F5F5F5] bg-white news-detail">
      <div class="text-[36px] text-[#37373D] font-medium">{{ data.title }}</div>
      <div class="flex w-[702px] justify-between h-[47px] items-center mt-[18px] mb-[28px]">
        <div class="flex items-center">
          <img class="w-[44px] h-[44px] bg-contain rounded-full" :src="icon" />
          <span class="text-[#575762] text-[24px] ml-[10px]">体育直播</span>
        </div>
        <div class="text-[#94999F] flex items-center mt-[-1px] text-[22px] font-normal detail-time"
          >{{ data.time }}<span class="ml-[7.8px]">发布</span>
        </div>
      </div>
      <div class="content-html" v-dompurify-html="data.content"></div>
    </div>
  </page-box>
</template>
<script setup>
  import { useRouter, useRoute } from 'vue-router';
  import apis from '@/api/news/index';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  import { formateDate } from '@/utils/common';
  import icon from '@/assets/img/home/publisher-icon.png';

  const NavBar = defineAsyncComponent(() => import('@/components/common/NavBar.vue'));
  const router = useRouter();
  const route = useRoute();
  const { query } = route;
  const back = () => {
    router.go(-1);
  };
  const data = ref({ title: '', publishTime: '', content: '', pic: '' });
  onMounted(() => {
    createLoading({ overlay: false });
    apis.getNewsDetail(query.id).then((res) => {
      closeLoading();
      let { title, publishTime, content, pic, tournament } = res;
      const time = formateDate(publishTime, 'YYYY-MM-DD HH:mm');
      content = content.replace(/\<img/gi, '<img style="margin-bottom:15px;margin-top:15px; border-radius:4px";object-fit: cover');
      data.value = { title, time, content, pic, tournament };
    });
  });
</script>
<style scoped lang="scss">
  .news-detail {
    font-family: PingFang SC;
    font-style: normal;
    line-height: normal;
    .content-html {
      width: 702px;
      display: flex;
      flex-wrap: wrap;
      color: #37373d;
      font-family: PingFang SC;
      font-size: 28px;
      font-style: normal;
      font-weight: 400;
      line-height: 42px; /* 21px */
      white-space: pre-line;
      word-break: break-all;
    }

    .detail-time {
      font-family: PingFang SC;
      font-style: normal;
      font-weight: 400;
      line-height: 48px;
    }
  }
</style>
