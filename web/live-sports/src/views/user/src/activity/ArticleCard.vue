<template>
  <div class="w-full px-[24px] bg-[#fff] flex flex-col">
    <div class="article-card__item w-full cursor-pointer border-b-[1px] border-b-[#F2F3F7] pt-[24px] pb-[21px]" @click="handleClick">
      <div class="flex items-stretch h-[140px]">
        <div class="flex-auto flex flex-col justify-between">
          <div class="article-card__title line-clamp-2">{{ item.name }}</div>
          <div class="article-card__time">{{ item.publishByTime }}<span class="ml-[13px]">发布</span></div>
        </div>
        <div class="grow-0 shrink-0 basis-[180x] w-[180px] h-[140px] rounded-[8px]">
          <UseImg class="w-full h-full rounded-[inherit]" :src="item.cover" alt="logo" />
          <!-- <img class="inline-block w-full h-full rounded-[inherit] overflow-hidden object-cover" :src="item.cover" alt="logo" /> -->
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { type ItemData } from './useIndex';
  import UseImg from '@/components/common/UseImg.vue';
  const props = withDefaults(
    defineProps<{
      item: ItemData;
    }>(),
    {
      item: () => {
        return {} as ItemData;
      },
    },
  );
  const router = useRouter();
  const handleClick = () => {
    router.push({
      name: 'UserActivityDetail',
      query: {
        id: props.item.id,
      },
    });
  };
</script>
<script lang="ts">
  export default {
    name: 'ArticleCard',
  };
</script>

<style scoped lang="scss">
  .van-hairline--bottom {
    &::after {
      --van-border-color: #f2f3f7;
    }
  }
  .article-card__title {
    font: 400 28px / normal 'PingFang SC';
    flex: 0 0 80px;
    height: 80px;
    color: #37373d;
  }
  .article-card__time {
    font: 400 22px / normal 'PingFang SC';
    color: #94999f;
    padding-bottom: 2px;
  }
  .article-card__item {
    @include use-active-style();
  }
</style>
