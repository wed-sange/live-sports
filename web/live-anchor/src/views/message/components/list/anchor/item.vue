<!-- eslint-disable vue/html-self-closing -->
<template>
  <div
    class="anchor-card-box w-full px-[12px] py-[8px] relative cursor-pointer"
    :title="item.name"
    @click="handleClick(item)"
  >
    <div class="anchor-card w-full">
      <div
        class="anchor-card__cover grow-0 shrink-0 basis-[56px] w-[56px] h-[56px] rounded-[100%] mx-[5px] p-[7px]"
      >
        <img
          class="inline-block w-full h-full rounded-[inherit] overflow-hidden"
          :src="item.cover"
          alt="logo"
        />

        <div
          class="anchor-card__info--dot grow-0 shrink-0 basis-[auto]"
          :class="{ large: item.readDot > 9 }"
          v-show="item.readDot > 0"
        >
          <i>{{ item.readDot >= 100 ? "99+" : item.readDot }}</i>
        </div>
      </div>

      <div class="w-full flex items-center py-[2px]">
        <div class="anchor-card__info--name text-center flex-auto truncate">
          {{ item.name }}
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { MessageCardData } from "@/views/message/utils/types";
defineOptions({
  name: "AnchorItem"
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
.anchor-card-box {
  &:hover,
  &.active {
    .anchor-card {
      .anchor-card__cover {
        background: rgb(98 60 234 / 20%);
        border-color: #34a853;
        box-shadow: inset 0 0 1px #34a853;
      }

      .anchor-card__info--name {
        font: 500 12px / normal "PingFang SC";
        color: #1a1a1a;
      }
    }
  }
}

.anchor-card {
  .anchor-card__cover {
    position: relative;
    background: transparent;
    border: 1px solid #dadada;
    transition-timing-function: linear;
    transition-duration: 300ms;
    transition-property: background, border, box-shadow;
  }

  .anchor-card__info--name {
    font: 400 12px / normal "PingFang SC";
    color: #434343;
    transition-timing-function: linear;
    transition-duration: 300ms;
    transition-property: font, color;
  }

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
    top: 0;
    left: 44px;
    z-index: 1;
    display: block;
    width: 18px;
    height: 18px;
    overflow: hidden;
    font: 500 12px / 16.8px "PingFang SC";
    color: #fff;
    text-align: center;
    background: #fe6470;
    border-radius: 150px;

    &.large {
      left: 42px;
      width: auto;
      padding: 0 4px;
    }

    & > i {
      display: inline-block;
      font-style: normal;
      // transform: scale(0.8);
    }
  }
}
</style>
