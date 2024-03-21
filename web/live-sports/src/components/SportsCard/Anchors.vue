<template>
  <div class="anchor-card-box px-[30px] pt-[20px] w-full">
    <div class="anchor-card__name py-[20px]"><TipBar>主播解说</TipBar></div>
    <div class="anchor-card__list flex items-center">
      <template v-for="item in anchorList.slice(0, 5)" :key="item.id">
        <div class="anchor-card__item" @click.stop="onDetail(item)">
          <UseImg class="mx-[auto] w-[64px] h-[64px] rounded-[100%]" :src="item.cover" alt="logo" not-fill-icon />
          <!-- <img class="mx-[auto] w-[64px] h-[64px] rounded-[100%] object-cover" :src="item.cover" /> -->
          <div class="anchor-card__item--text line-clamp-1 text-center mt-[10px]">{{ item.name }}</div>
        </div>
      </template>
    </div>
  </div>
</template>
<script setup>
  import TipBar from '@/components/SportsCard/TipBar.vue';
  import { useRouter } from 'vue-router';
  import UseImg from '@/components/common/UseImg.vue';
  const router = useRouter();

  defineProps({
    anchorList: {
      type: Array,
    },
  });
  const onDetail = (data) => {
    router.push({ path: '/roomDetail', query: { id: data.matchId, liveId: data.liveId, type: data.matchType, userId: data.id } });
  };
</script>

<style scoped lang="scss">
  .anchor-card-box {
    .anchor-card__name {
      font: 400 24px / normal 'PingFang SC';
      color: #121212;
      text-align: center;
      vertical-align: middle;
      position: relative;
      &::before {
        content: '';
        display: block;
        width: 100%;
        height: 2px;
        background: #f2f3f7;
        position: absolute;
        top: 0;
        left: 0;
        z-index: 1;
        transform: translate(0, -100%);
      }
      :deep(.sports-tip-bar-box) {
        justify-content: center;
        .sports-tip-bar__text {
          font: 400 24px / normal 'PingFang SC';
          color: #121212;
          padding-right: 8px;
        }
      }
    }
    .anchor-card__list {
      display: flex;
      align-items: center;
      .anchor-card__item {
        flex: 0 0 110px;
        width: 110px;
        & ~ .anchor-card__item {
          margin-left: 23px;
        }
      }
      .anchor-card__item--text {
        font: 400 20px / normal 'PingFang SC';
        color: #575762;
      }
    }
  }
</style>
