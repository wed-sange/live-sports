<!-- eslint-disable vue/no-v-html -->
<template>
  <div class="chat-card-box w-full px-[24px] py-[25px]">
    <div class="chat-card__tip pt-[5px] pb-[30px] text-center truncate" v-show="item.tip">{{ item.tip }}</div>
    <div class="chat-card w-full flex items-stretch relative pl-[2px]" :class="{ me: item.isMe }">
      <div class="grow-0 shrink-0 basis-[100px] w-[100px] h-[100px] rounded-[100%]">
        <UseImg class="w-full h-full rounded-[inherit]" :src="item.accountAvatar" alt="logo" not-fill-icon>
          <template #error>
            <img class="w-full h-full" :src="item.isMe ? defaultUserLogo : defaultAnchorLogo" alt="error" />
          </template>
        </UseImg>
        <!-- <img
          :key="item.id + 'avatar'"
          class="inline-block w-full h-full rounded-[inherit] overflow-hidden object-cover"
          :src="item.accountAvatar"
          alt="logo"
        /> -->
      </div>
      <div v-if="item.type !== MessageType.video" class="chat-card__message--triangle" :class="{ img: item.type === MessageType.image }">
        <svg xmlns="http://www.w3.org/2000/svg" width="6" height="10" viewBox="0 0 6 10" fill="none">
          <path
            class="chat-card__message--triangle-p"
            d="M5.32881 6.23381L0 9.5L-1.07324e-07 0.5L5.32881 3.76619C6.22373 4.31295 6.22373 5.68705 5.32881 6.24101L5.32881 6.23381Z"
          />
        </svg>
      </div>
      <div
        class="chat-card__message p-[28px] grow-0 shrink-0 basis-[auto] max-w-[calc(100%-100px-24px-120px)] min-h-[100px]"
        ref="itemHookRef"
        :class="{ img: item.type === MessageType.image, 'chat-video': item.type === MessageType.video }"
        @contextmenu="handleContextMenu($event)"
      >
        <template v-if="item.type === MessageType.image">
          <!-- <div class="chat-card__message--image w-[340px] h-[340px]">
            <img :src="item.message" :key="item.id" class="w-full h-full object-contain" alt="image" @load="onLoad($event)" />
          </div> -->
          <UseImg
            class="chat-card__message--image w-[284px] h-[200px]"
            @click.stop.prevent="handleImgClick(item)"
            :src="item.message"
            :key="item.id"
            @on-load="onLoad"
          />
        </template>
        <template v-else-if="item.type === MessageType.video">
          <div class="w-full h-full relative" @click.stop.prevent="handleVideoClick(item)">
            <UseImg
              class="chat-card__message--image w-[288px] h-[204px] rounded-[12px]"
              :src="getVideoImg(item.message)"
              :key="item.id"
              @on-load="onLoad"
            />
            <img :src="videoPlayIcon" class="w-[72px] h-[72px] object-contain absolute left-[108px] top-[66px]" />
          </div>
        </template>
        <template v-else>
          <div class="chat-card__message--text" v-text="item.message"> </div>
        </template>
      </div>
      <div class="chat-card__status self-center px-[16px]" v-if="item.status">
        <ChatLoading class="w-full h-full" color="#6d48fe" v-show="item.status === 'sending'" />
        <div class="chat-card__status--error" @click="handleStatus(item)" v-show="item.status === 'failed'"></div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { type ChatCardData } from './useCommon';
  import { MessageType } from '@/views/message/utils/types';
  import { onLongPress } from '@vueuse/core';
  import ChatLoading from './ChatLoading.vue';
  import defaultUserLogo from '@/assets/img/user/default_user.png';
  import defaultAnchorLogo from '@/assets/img/user/default_anchor.png';
  import UseImg from '@/components/common/UseImg.vue';

  import videoPlayIcon from '@/assets/img/message/video-play.png';

  const props = withDefaults(
    defineProps<{
      item: ChatCardData;
    }>(),
    {
      item: () => {
        return {} as ChatCardData;
      },
    },
  );
  const emit = defineEmits<{
    (event: 'on-img-load', imgEvent: Event): void;
    (event: 'status-click', item: ChatCardData): void;
    (event: 'img-click', item: ChatCardData): void;
    (event: 'video-click', item: ChatCardData): void;
    (event: 'long-click', item: ChatCardData, e: HTMLElement | null): void;
  }>();
  const onLoad = (imgEvent: Event) => {
    emit('on-img-load', imgEvent);
  };
  const handleStatus = (item: ChatCardData) => {
    emit('status-click', item);
  };
  const handleImgClick = (item: ChatCardData) => {
    emit('img-click', item);
  };
  const handleVideoClick = (item: ChatCardData) => {
    const list = getVideoRes(item.message);
    if (list && list.length == 2) {
      emit('video-click', list);
    }
  };
  const handleContextMenu = (event: Event) => {
    event.preventDefault();
  };
  const itemHookRef = ref<HTMLElement | null>(null);
  const handleLongPressCallbackHook = () => {
    emit('long-click', props.item, unref(itemHookRef.value));
  };
  const getVideoRes = (message) => {
    if (!message) {
      return '';
    }
    const list = message.split('***');
    if (!list || list.length != 2) {
      return '';
    }
    return list;
  };
  const getVideoImg = (message) => {
    const list = getVideoRes(message);
    return list ? list[0] : '';
  };
  onLongPress(itemHookRef, handleLongPressCallbackHook, { modifiers: { prevent: false } });
</script>
<script lang="ts">
  export default {
    name: 'ChatCard',
  };
</script>
<style scoped lang="scss">
  .chat-card-box {
    -webkit-text-size-adjust: 100%;
    :deep(img) {
      -webkit-touch-callout: none;
      // -webkit-user-drag: none;
    }
    .chat-card__tip {
      font: 400 22px / normal 'PingFang SC';
      color: #94999f;
    }
    .chat-card__message--triangle {
      color: #fff;
      position: absolute;
      top: 0;
      left: 0;
      z-index: 2;
      width: 20px;
      height: 20px;
      top: 40px;
      left: 124px;
      z-index: 2;
      transform: translate(-30%, 0) scaleX(-1);
      &.img {
        display: none;
      }
      svg {
        width: 100%;
        height: 100%;
      }
      .chat-card__message--triangle-p {
        fill: currentColor;
        // stroke: currentColor;
        // stroke-width: 20;
      }
    }
    .chat-card__message {
      background: #fff;
      border-radius: 20px;
      overflow: hidden;
      word-break: break-all;
      margin-left: 32px;
      font: 400 30px / 44px 'PingFang SC';
      white-space: pre-wrap;
      color: #37373d;
      cursor: pointer;
      user-select: none;
      position: relative;

      :deep(.van-popover__wrapper) {
        position: absolute;
        top: 0;
        left: 50%;
      }
      &.img {
        margin-left: 20px;
        padding: 0;
        border-radius: 12px;
        border: 2px solid #dedede;
      }
      &.chat-video {
        background: white !important;
        padding: 0;
      }
    }
    .chat-card {
      &.me {
        flex-flow: row-reverse;

        .chat-card__message--triangle {
          color: #34a853;
          left: auto;
          right: 124px;
          transform: translate(39%, 0) scaleX(1);
        }
        .chat-card__message {
          margin-left: 0;
          margin-right: 32px;
          background: #34a853;
          color: #fff;

          &.img {
            margin-right: 20px;
          }
        }
      }
    }
    .chat-card__message--image {
      // border-radius: inherit;
      :deep() {
        .use-img__image,
        .chat-image__img {
          object-fit: cover;
          width: 100%;
          height: 100%;
          border-radius: inherit;
          overflow: hidden;
        }
      }
    }
    .chat-card__status {
      box-sizing: content-box;
      width: 36px;
      height: 36px;
      .chat-card__status--error {
        width: 100%;
        height: 100%;
        cursor: pointer;
        background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACQAAAAkCAYAAADhAJiYAAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAM2SURBVHgBxZjPTxNBFMe/s12rQKI9WGKNifVoPKDxx1HLScWDNWJCFGM9efCg/gVg4h04eBYTRSKgXEy8ueg/gAfjkcWDGEoMB1oo7O7zzbYp23YrOzs1fi6dzk5mvpn35s17I6DIytBQVjiJPAnq4785QSJFoJT8JiDWPZBtCNggsUDmvvnM9KStMD3PEVXIrdsFXuQeQDkoQMCiEJjIzExNRhm/p6CVweEchPeCZ85CB7lrwNO9hLUVtJQvpLrMnRE2x2N0EDbr+JHZ10/afw9hZaiQJWf7PX88jX+B3K1Esj/Mv0SYGLjbn6KaqPvaFfQMXAWVSyh9+IhN6zMi0UaU0TJQQUzy1EkcLNxFovcwzOxxHHr4wO+LBK8hrSBdo62gX4N3xlSc98D5syF95xAV6RLSTxEm6OfN4XwnHNgrlZTGyzX9k9wsSBjeGBRximtoFVSGMjKsBAVVg556nPGKxZC+NSjDa/sasLtDI4hB6A6V1Uy2K0reAizIP+YxozBttJrHjbND1dly8p40hFPJIyZuiMnc1SLiQo7IGwTjOjQICggTqIIQoo8FIQUNKOAzOrtTI8epC2ndVzv2j3rbK21CB0FIGdCEAoHQ0zSZtJa2oODRV43SYRgy7YQGFIjM2k4NrBucG2sJCoqIH4Pqkmx5yhahgWMv19vuqp4gLhCWTYNogSBiB0d5mW7MzPm/uiZjB7BMRwgrQdBi4+07dAJh0rxxbGaKTSYs/HeElZmets1qm15yEMghBjJl7cpdhOju8U0X9Ck1PawBgSSf85El1Vs/0ZtG+vl4Q9/v0WfY/vYdSnDCz/XaCdncDYxk3IciPQOXW/pUcur60h7V67S6oMzsK4sIE1AgVrraLIbXPDr3Zr5FkKTiJkdrJW8kNq0vDbFHtmVtFhleq+KWRxu7mpBZG1wjcm2WSKex/0K1HNpigZHvM79Q9PrlyWrsDkFVlCr+i4jp3WgWIwm97eXArZ3kGVWfiiSG56w45f4wMZK9n2Oq5clIR55j+CTLw/P3YRGRwojEI/UMk28BDnode7BqEVZ70uNFLnkkskGBMreqpTMWP/V9NQRZvf7VFJ0/SIZ6MHYmmeoAAAAASUVORK5CYII=')
            no-repeat,
          transparent;
        background-size: auto 100%;
        background-position: 0 0;
      }
    }
  }
</style>
