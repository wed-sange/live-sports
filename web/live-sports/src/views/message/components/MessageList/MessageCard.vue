<template>
  <van-swipe-cell>
    <div
      class="message-card-box w-full px-[24px] py-[30px] relative cursor-pointer"
      v-ripple="{ color: '#3a3a3a' }"
      @click="handleClick(item)"
    >
      <div class="message-card w-full flex items-stretch">
        <div class="grow-0 shrink-0 basis-[100px] w-[100px] h-[100px] rounded-[100%]">
          <UseImg class="w-full h-full rounded-[inherit]" :src="item.cover" alt="logo" not-fill-icon>
            <template #error>
              <img class="w-full h-full" :src="defaultLogo" alt="error" />
            </template>
          </UseImg>
          <!-- <img class="inline-block w-full h-full rounded-[inherit] overflow-hidden object-cover" :src="item.cover" alt="logo" /> -->
        </div>
        <div class="flex-[calc(100%-100px)] w-[calc(100%-100px)] pl-[20px] pt-[6px]">
          <div class="w-full flex items-start">
            <div class="message-card__info--name flex-auto truncate">{{ item.name }}</div>
            <div class="message-card__info--time grow-0 shrink-0 basis-[auto] pt-[2px] pr-[1px]">{{ item.createTimeText }}</div>
          </div>
          <div class="w-full flex items-center pt-[10px]">
            <div
              class="message-card__info--message flex-auto truncate"
              v-text="item.msgType === MessageType.image ? '[图片]' : item.msgType === MessageType.video ? '[视频]' : item.message"
            ></div>
            <div
              class="message-card__info--dot grow-0 shrink-0 basis-[auto]"
              :class="{ large: item.readDot > 9 }"
              v-show="item.readDot > 0"
            >
              <i>{{ item.readDot }}</i>
            </div>
          </div>
        </div>
      </div>
    </div>
    <template #right>
      <div class="h-full py-[32px] pl-[16px]">
        <van-button v-ripple class="message-card__btn--remove" square @click="handleRemove(item)">
          <i class="message-card__btn--remove-icon"></i>
        </van-button>
      </div>
    </template>
  </van-swipe-cell>
</template>
<script setup lang="ts">
  import { MessageCardData, MessageCardType, MessageType } from '@/views/message/utils/types';
  import { useMessageStore } from '@/store/modules/message';
  import defaultLogo from '@/assets/img/user/default_anchor.png';
  import UseImg from '@/components/common/UseImg.vue';
  withDefaults(
    defineProps<{
      item: MessageCardData;
    }>(),
    {
      item: () => {
        return {} as MessageCardData;
      },
    },
  );
  const emit = defineEmits<{
    (event: 'on-remove', item: MessageCardData): void;
  }>();
  const messageStore = useMessageStore();
  const messageServer = messageStore.getMessageServer();
  const handleRemove = (item: MessageCardData) => {
    emit('on-remove', item);
  };
  const router = useRouter();
  const handleClick = (item: MessageCardData) => {
    if (item.type === MessageCardType.SYSTEM) {
      item.readDot = 0;
      router.push({ name: 'MessageNotify', query: { id: item.id } });
    } else {
      // item.readDot = 0;
      messageServer?.clearDot(item.anchorId).finally(() => {
        router.push({ name: 'MessageChat', query: { anchorId: item.anchorId } });
      });
    }
  };
</script>
<script lang="ts">
  export default {
    name: 'MessageCard',
  };
</script>
<style scoped lang="scss">
  .van-swipe-cell {
    // @include use-active-style();
  }
  .message-card {
    .message-card__info--name {
      font: 500 30px / normal 'PingFang SC';
      color: #37373d;
    }
    .message-card__info--time {
      font: 400 24px / normal 'PingFang SC';
      color: #94999f;
    }
    .message-card__info--message {
      font: 400 26px / normal 'PingFang SC';
      color: #666b72;
    }
    .message-card__info--dot {
      display: block;
      width: 32px;
      height: 32px;
      border-radius: 16px;
      overflow: hidden;
      text-align: center;
      font: 600 20px / 32px 'MiSans';
      color: #fff;
      background: #ff5e2a;
      &.large {
        width: auto;
        padding: 0 10px;
      }
      & > i {
        display: inline-block;
        font-style: normal;
        // transform: scale(0.8);
      }
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
