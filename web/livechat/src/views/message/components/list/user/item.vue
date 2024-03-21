<!-- eslint-disable vue/html-self-closing -->
<template>
  <div
    class="message-card-box w-full p-[14px] relative cursor-pointer"
    :title="item.name"
    @click="handleClick(item)"
  >
    <div class="message-card w-full flex items-stretch">
      <div
        class="grow-0 shrink-0 basis-[52px] w-[52px] h-[52px] rounded-[100%] border-2 border-[#F3F4F7]"
      >
        <img
          class="inline-block w-full h-full rounded-[inherit] overflow-hidden"
          :src="item.cover"
          alt="logo"
        />

        <div
          class="message-card__info--dot grow-0 shrink-0 basis-[auto]"
          :class="{ large: item.readDot > 9 }"
          v-show="item.readDot > 0"
        >
          <i>{{ item.readDot >= 100 ? "99+" : item.readDot }}</i>
        </div>
      </div>
      <div class="pl-[4px] pt-[4px] flex-1">
        <div class="w-full flex items-center justify-between">
          <div class="w-full flex gap-2 items-center">
            <div class="message-card__info--name max-w-[90px] truncate">
              {{ item.name }}
            </div>
            <IconTopping v-show="item.setTop" />
          </div>

          <ChatTop
            :top="item.setTop"
            @set-top="handleSetTop(item)"
            @un-set-top="handleUnSetTop(item)"
          />
        </div>

        <div class="w-full flex items-center pt-[4px] max-w-[140px] truncate">
          <div
            class="message-card__info--message flex-auto truncate"
            v-text="messageLabelEnums[item.msgType] ?? item.message"
          ></div>
        </div>
      </div>
      <!-- <div class="flex-1 pl-[24px] text-center self-center h-[18px]">123</div> -->
    </div>
  </div>
</template>
<script setup lang="ts">
import { MessageCardData, MessageType } from "@/views/message/utils/types";
import { IconTopping } from "@/views/message/components/icon";
import ChatTop from "@/views/message/components/chat/chatTop.vue";
import { updateSetTop, updateUnTop } from "@/api/user";
import { computed } from "vue";
defineOptions({
  name: "UserItem"
});
enum messageLabelEnums {
  // "文字" = MessageType.text,
  "[图片]" = MessageType.image,
  "[语音]" = MessageType.voice,
  "[视频]" = MessageType.video
}
const props = withDefaults(
  defineProps<{
    item: MessageCardData;
    currentAnchor?: string;
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

const userId = computed(() => {
  return props.item.sendUserId
    .split("-")
    .filter(v => v !== props.currentAnchor)[0];
});

const handleSetTop = async item => {
  console.log("置顶聊天", item);

  await updateSetTop({
    anchorId: props.currentAnchor,
    userId: userId.value
  });
  emit("on-setTop", item);
};
const handleUnSetTop = async item => {
  console.log("取消置顶", item);

  await updateUnTop({
    anchorId: item.anchorId,
    userId: userId.value
  });
  emit("on-unSetTop", item);
};
</script>
<style scoped lang="scss">
.message-card-box {
  position: relative;
  transition-timing-function: linear;
  transition-duration: 300ms;
  transition-property: background;

  // &::after {
  //   position: absolute;
  //   top: 0;
  //   left: 0;
  //   z-index: 1;
  //   display: block;
  //   width: 3px;
  //   height: 100%;
  //   content: "";
  //   background: #34a853;
  //   border-radius: 29px;
  //   transition-timing-function: linear;
  //   transition-duration: 300ms;
  //   transition-property: transform;
  //   transform: scaleX(0);
  //   transform-origin: 0 50%;
  // }

  &:hover,
  &.active {
    background: #e4edf4;

    // &::after {
    //   transform: scaleX(1);
    // }
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
    position: absolute;
    top: 12px;
    left: 48px;
    // width: 18px;
    // height: 18px;
    padding: 0 6px;
    margin: 0 auto;
    overflow: hidden;
    font: 500 12px / 16.8px "PingFang SC";
    color: #fff;
    text-align: center;
    background: #ff5151;
    border: 1px solid #fff;
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
