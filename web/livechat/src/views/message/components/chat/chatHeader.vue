<!-- eslint-disable vue/html-self-closing -->
<template>
  <div class="message-card-box w-full py-[20px] relative">
    <div class="message-card w-full flex gap-2 items-stretch">
      <div
        class="grow-0 shrink-0 basis-[50px] w-[50px] h-[50px] rounded-[100%]"
      >
        <img
          class="inline-block w-full h-full rounded-[inherit] overflow-hidden"
          :src="account.cover"
          alt="logo"
        />
      </div>
      <div
        class="grow-0 shrink-0 basis-[calc(100%-46px-52px)] w-[calc(100%-46px-52px)] pl-[4px] pt-[4px]"
      >
        <div class="w-full flex items-center">
          <div class="message-card__info--name flex-auto truncate">
            {{ account.name }}
          </div>
        </div>
        <div class="w-full flex items-center pt-[4px]">
          <div class="message-card__info--message flex-auto truncate">
            当前会话
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { type ChatAccountModel } from "./useCommon";
defineOptions({
  name: "ChatHeader"
});
withDefaults(
  defineProps<{
    account: ChatAccountModel;
  }>(),
  {
    account: () => {
      return {} as ChatAccountModel;
    }
  }
);
</script>
<style scoped lang="scss">
.message-card {
  .message-card__info--name {
    font: 500 14px / 16.8px "PingFang SC";
    color: #434343;
  }

  .message-card__info--message {
    font: 400 12px / 16.8px "PingFang SC";
    color: #9a9a9a;
  }

  .message-card__info--dot {
    display: inline-block;
    width: 18px;
    height: 18px;
    margin: 0 auto;
    overflow: hidden;
    font: 500 12px / 16.8px "PingFang SC";
    color: #fff;
    text-align: center;
    background: #ff5151;
    border-radius: 150px;

    &.large {
      width: auto;
      padding: 0 6px;
    }

    & > i {
      display: inline-block;
      font-style: normal;
      // transform: scale(0.8);
    }
  }
}
</style>
