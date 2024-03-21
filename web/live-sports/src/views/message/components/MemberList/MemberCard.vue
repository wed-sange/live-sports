<template>
  <van-swipe-cell>
    <div
      class="message-card-box w-full px-[24px] py-[30px] relative cursor-pointer"
      v-ripple="{ color: '#3a3a3a' }"
      @click="handleClick(item)"
    >
      <div class="message-card w-full flex items-center">
        <div class="grow-0 shrink-0 basis-[80px] w-[80px] h-[80px] rounded-[100%]">
          <UseImg class="w-full h-full rounded-[inherit]" :src="item.cover" alt="logo" not-fill-icon>
            <template #error>
              <img class="w-full h-full" :src="defaultLogo" alt="error" />
            </template>
          </UseImg>
          <!-- <img class="inline-block w-full h-full rounded-[inherit] overflow-hidden object-cover" :src="item.cover" alt="logo" /> -->
        </div>
        <div class="flex-[calc(100%-80px)] w-[calc(100%-80px)] pl-[20px]">
          <div class="w-full flex items-center">
            <div class="message-card__info--name flex-auto truncate">{{ item.name }}</div>
          </div>
        </div>
      </div>
    </div>
    <template #right>
      <div class="h-full py-[32px] pl-[16px]" v-if="remove">
        <van-button v-ripple class="message-card__btn--remove" square @click="handleRemove(item)">
          <i class="message-card__btn--remove-icon"></i>
        </van-button>
      </div>
    </template>
  </van-swipe-cell>
</template>
<script setup lang="ts">
  import { type MemberCardData } from './useIndex';
  import { useMessageStore } from '@/store/modules/message';
  import UseImg from '@/components/common/UseImg.vue';
  import defaultLogo from '@/assets/img/user/default_anchor.png';
  withDefaults(
    defineProps<{
      item: MemberCardData;
      remove?: boolean;
    }>(),
    {
      item: () => {
        return {} as MemberCardData;
      },
      remove: true,
    },
  );
  const messageStore = useMessageStore();
  const messageServer = messageStore.getMessageServer();
  const emit = defineEmits<{
    (event: 'on-remove', item: MemberCardData): void;
  }>();
  const handleRemove = (item: MemberCardData) => {
    emit('on-remove', item);
  };
  const router = useRouter();
  const handleClick = (item: MemberCardData) => {
    messageServer?.clearDot(item.anchorId).finally(() => {
      router.push({ name: 'MessageChat', query: { anchorId: item.anchorId } });
    });
  };
</script>
<script lang="ts">
  export default {
    name: 'MemberCard',
  };
</script>
<style scoped lang="scss">
  .van-swipe-cell {
    position: relative;
    // @include use-active-style();
    &::after {
      content: '';
      display: block;
      width: calc(100% - 48px);
      height: 2px;
      background: #f2f3f7;
      position: absolute;
      left: 24px;
      bottom: 0;
      z-index: 1;
      pointer-events: none;
    }
  }
  .message-card {
    .message-card__info--name {
      font: 500 30px / normal 'PingFang SC';
      color: #37373d;
    }
  }
  .message-card__btn--remove {
    --van-button-default-color: #fff;
    --van-button-default-background: #ff5e2a;
    width: 120px;
    height: 96px;
    border: none;
    border-radius: 20px 0px 0px 20px;
    .message-card__btn--remove-icon {
      display: inline-block;
      width: 40px;
      margin-top: 8px;
      height: 36px;

      background: url('@/assets/img/message/icon-remove.png') no-repeat, transparent;
      background-size: auto 100%;
      background-position: center;
    }
  }
</style>
