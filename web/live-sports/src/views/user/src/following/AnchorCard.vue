<template>
  <div class="anchor-card__item w-full px-[24px] py-[12px]">
    <div class="w-full p-[30px] bg-[#fff] rounded-[20px] cursor-pointer" @click="handleClick(item)">
      <div class="flex items-stretch">
        <div class="grow-0 shrink-0 basis-[100px] w-[100px] h-[100px] rounded-[100%]">
          <UseImg class="w-full h-full rounded-[inherit]" :src="item.head" alt="logo" not-fill-icon />
          <!-- <img class="inline-block w-full h-full rounded-[inherit] object-cover overflow-hidden" :src="item.head" alt="logo" /> -->
        </div>
        <div class="flex-auto w-[calc(100%-100px-130px)] flex flex-col justify-between pl-[20px]">
          <div class="anchor-card__title flex items-end pt-[7px]">
            <span class="truncate"> {{ item.name }} </span>
            <TipBar class="ml-[18px]" v-if="item.liveId">正在直播</TipBar>
          </div>
          <div class="anchor-card__sub">粉丝数:{{ item.fans }}</div>
        </div>
        <div class="grow-0 shrink-0 basis-auto self-center">
          <van-button class="anchor-card__btn" :class="{ active: item.isFocused }" @click.stop="handleFollowClick(item)">
            {{ item.isFocused ? '已关注' : '关注' }}
          </van-button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { addAnchorFollowing, removeAnchorFollowing } from '@/api/user';
  import TipBar from '@/components/SportsCard/TipBar.vue';

  import UseImg from '@/components/common/UseImg.vue';

  import { showLoadingToast } from 'vant';
  import { type ItemData } from './useIndex';
  withDefaults(
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
  const handleClick = async (item: ItemData) => {
    if (!item.liveId) return;
    router.push({ path: '/roomDetail', query: { id: item.matchId, liveId: item.liveId, type: item.matchType, userId: item.id } });
  };
  const handleFollowClick = async (item: ItemData) => {
    const toast = showLoadingToast({
      duration: 0,
      forbidClick: true,
      message: '加载中...',
    });
    const submitInfo: {
      request: typeof addAnchorFollowing | typeof removeAnchorFollowing | null;
    } = {
      request: null,
    };
    if (item.isFocused) {
      submitInfo.request = removeAnchorFollowing;
    } else {
      submitInfo.request = addAnchorFollowing;
    }
    try {
      await submitInfo.request(item.id);
      item.isFocused = !item.isFocused;
    } finally {
      toast.close();
    }
  };
</script>
<script lang="ts">
  export default {
    name: 'AnchorCard',
  };
</script>

<style scoped lang="scss">
  .anchor-card__item {
    @include use-active-style();
  }
  .anchor-card__title {
    font: 500 30px / normal 'PingFang SC';
    color: #37373d;
    white-space: nowrap;
    .anchor-card__state {
      border-radius: 8px;
      background: #ff5151;
      padding: 6px 16px;
      display: inline-block;
      height: 50px;
      font: 400 25px / 1.6 'PingFang SC';
      color: #fff;
      margin-left: 16px;
      transform-origin: 0 50%;
      transform: scale(0.8);
      &.close {
        background: #8a91a0;
      }
    }
  }
  .anchor-card__sub {
    font: 400 24px / normal 'PingFang SC';
    color: #94999f;
    padding-bottom: 7px;
  }
  .anchor-card__btn {
    --van-button-default-background: #34a853;
    --van-button-default-color: #fff;
    --van-button-default-height: 54px;
    --van-button-normal-padding: 0;
    border: none;
    font: 400 24px / var(--van-button-default-height) 'PingFang SC';
    border-radius: 40px;
    width: 120px;
    padding-top: 2px;
    &.active {
      --van-button-default-background: #f2f3f7;
      --van-button-default-color: #94999f;
    }
    .van-button__text {
      font: inherit;
      color: inherit;
    }
  }
</style>
