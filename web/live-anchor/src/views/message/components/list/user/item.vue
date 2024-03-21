<!-- eslint-disable vue/html-self-closing -->
<template>
  <div
    class="message-card-box w-full pl-[22px] pr-[18px] my-[12px] py-[2px] relative cursor-pointer"
    :title="item.name"
    @click="handleClick(item)"
  >
    <div class="message-card w-full flex items-stretch">
      <div
        class="grow-0 shrink-0 basis-[46px] w-[46px] h-[46px] rounded-[100%] p-[4px]"
      >
        <img
          class="inline-block w-full h-full rounded-[inherit] overflow-hidden"
          :src="item.cover"
          alt="logo"
        />
      </div>
      <div
        class="grow-0 shrink-0 basis-[calc(100%-46px-52px)] w-[calc(100%-46px-52px)] pl-[4px] pt-[4px]"
      >
        <div class="w-full flex items-center">
          <div class="message-card__info--name flex-auto truncate">
            {{ item.name }}
          </div>
        </div>
        <div class="w-full flex items-center pt-[4px]">
          <div
            class="message-card__info--message flex-auto truncate"
            v-text="
              item.msgType === MessageType.image ? '[图片]' : item.message
            "
          ></div>
        </div>
      </div>
      <div class="flex-1 pl-[24px] text-center self-center h-[18px]">
        <div
          class="message-card__info--dot grow-0 shrink-0 basis-[auto]"
          :class="{ large: item.readDot > 9 }"
          v-show="item.readDot > 0"
        >
          <i>{{ item.readDot >= 100 ? "99+" : item.readDot }}</i>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { MessageCardData, MessageType } from "@/views/message/utils/types";
defineOptions({
  name: "UserItem"
});
withDefaults(
  defineProps<{
    item: MessageCardData;
  }>(),
  {
    item: () => {
      return {} as MessageCardData;
    }
  }
);
const emit = defineEmits<{
  (event: "on-click", item: MessageCardData): void;
}>();
const handleClick = (item: MessageCardData) => {
  emit("on-click", item);
};
</script>
<style scoped lang="scss">
.message-card-box {
  position: relative;
  transition-timing-function: linear;
  transition-duration: 300ms;
  transition-property: background;

  &::after {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
    display: block;
    width: 3px;
    height: 100%;
    content: "";
    background: #34a853;
    border-radius: 29px;
    transition-timing-function: linear;
    transition-duration: 300ms;
    transition-property: transform;
    transform: scaleX(0);
    transform-origin: 0 50%;
  }

  &:hover,
  &.active {
    background: rgb(52 168 83 / 10%);

    &::after {
      transform: scaleX(1);
    }
  }
}

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
