<!-- eslint-disable vue/html-self-closing -->
<template>
  <div
    class="anchor-card-box w-full relative cursor-pointer"
    :title="item.name"
    @click="handleClick(item)"
  >
    <div
      class="anchor-card flex p-[14px] justify-between gap-2 items-center w-full"
    >
      <div
        class="anchor-card__cover grow-0 shrink-0 basis-[52px] w-[52px] h-[52px] rounded-[100%] border-2 border-[#F3F4F7]"
      >
        <img
          class="inline-block w-full h-full rounded-[inherit] overflow-hidden"
          :src="item.cover"
          alt="logo"
        />
      </div>

      <div
        class="anchor-card__info--name w-full flex gap-2 items-center flex-1 py-[2px]"
      >
        <div class="truncate max-w-[56px]">
          {{ item.name }}
        </div>
        <IconOnline v-show="item.liveStatus === LiveStatus.Living" />
        <IconTopping v-show="item.setTop" />
      </div>

      <ChatTop
        :top="item.setTop"
        @set-top="handleSetTop(item)"
        @un-set-top="handleUnSetTop(item)"
      />

      <div>
        <div
          class="anchor-card__info--dot"
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
import { MessageCardData } from "@/views/message/utils/types";
import { IconOnline, IconTopping } from "@/views/message/components/icon";
import { LiveStatus } from "@/api/message/enums";
import ChatTop from "@/views/message/components/chat/chatTop.vue";
import { updateSetTop, updateUnTop } from "@/api/user";
defineOptions({
  name: "AnchorItem"
});
const props = withDefaults(
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
  (event: "on-setTop", item: MessageCardData): void;
  (event: "on-unSetTop", item: MessageCardData): void;
}>();

const handleClick = (item: MessageCardData) => {
  emit("on-click", item);
};

const handleSetTop = async item => {
  console.log("置顶聊天", item);

  await updateSetTop({
    anchorId: props.item.anchorId
    // userId: item.id
  });

  emit("on-setTop", item);
};
const handleUnSetTop = async item => {
  console.log("取消置顶", item);
  await updateUnTop({
    anchorId: item.anchorId
    // userId: item.userId
  });

  emit("on-unSetTop", item);
};
</script>
<style scoped lang="scss">
.anchor-card__info--name {
  font: 400 12px / normal "PingFang SC";
  color: #434343;
  transition-timing-function: linear;
  transition-duration: 300ms;
  transition-property: font, color;
}

.anchor-card-box {
  .anchor-card {
    background: transparent;
    transition-timing-function: linear;
    transition-duration: 300ms;
    transition-property: background;

    // .anchor-card__cover {
    //   position: relative;
    //   background: transparent;
    //   border: 1px solid #dadada;
    //   transition-timing-function: linear;
    //   transition-duration: 300ms;
    //   transition-property: background, border, box-shadow;
    // }

    .anchor-card__info--time {
      font: 400 24px / normal "PingFang SC";
      color: #8a91a0;
    }

    .anchor-card__info--message {
      font: 400 24px / normal "PingFang SC";
      color: #8a91a0;
    }

    .anchor-card__info--dot {
      position: absolute;
      top: 12px;
      left: 48px;
      z-index: 1;
      display: block;
      // width: 18px;
      // height: 18px;
      padding: 0 6px;
      overflow: hidden;
      font: 500 12px / 16.8px "PingFang SC";
      color: #fff;
      text-align: center;
      background: #fe6470;
      border: 1px solid #fff;
      border-radius: 150px;

      // &.large {
      //   left: 42px;
      //   width: auto;
      //   padding: 0 4px;
      // }

      & > i {
        display: inline-block;
        font-style: normal;
        // transform: scale(0.8);
      }
    }
  }

  .anchor-card::after {
    position: absolute;
    top: 0;
    bottom: 0;
    left: 0;
    z-index: 1;
    display: block;
    width: 4px;
    height: 60%;
    margin: auto;
    content: "";
    background: #34a853;
    border-radius: 0 37px 37px 0;
    transition-timing-function: linear;
    transition-duration: 300ms;
    transition-property: transform;
    transform: scaleX(0);
    transform-origin: 0 50%;
  }

  &:hover,
  &.active {
    .anchor-card {
      position: relative;
      background: #ecf6ef;
      // .anchor-card__cover {
      //   background: rgb(98 60 234 / 20%);
      //   border-color: #34a853;
      //   box-shadow: inset 0 0 1px #34a853;
      // }

      .anchor-card__info--name {
        font: 500 12px / normal "PingFang SC";
        color: #1a1a1a;
      }

      &::after {
        transform: scaleX(1);
      }
    }
  }
}
</style>
