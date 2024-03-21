<!-- eslint-disable vue/no-v-html -->
<template>
  <div
    class="chat-card-box w-full px-[30px] py-[8px]"
    :class="{ me: item.isMe, 'has-tip': item.tip }"
  >
    <div
      class="chat-card__tip pb-[16px] text-center truncate"
      v-show="item.tip"
    >
      {{ item.tip }}
    </div>
    <div class="chat-card w-full flex items-stretch">
      <div
        class="chat-card__cover grow-0 shrink-0 basis-[40px] h-[40px] rounded-[40px]"
      >
        <img
          :key="item.id + 'avatar'"
          class="inline-block w-full h-full rounded-[inherit] overflow-hidden"
          :src="item.accountAvatar"
          alt="logo"
        />
      </div>
      <div
        class="chat-card__message grow-0 shrink-0 basis-[auto] max-w-[calc(100%-80px-24px-128px)]"
        :class="{ image: item.type === MessageType.image }"
        ref="itemHookRef"
        @contextmenu="handleContextMenu($event)"
      >
        <template v-if="item.type === MessageType.image">
          <ChatImage
            class="chat-card__message--image"
            @click.stop.prevent="handleImgClick(item)"
            :src="item.message"
            :key="item.id"
            @on-load="onLoad"
          />
        </template>
        <template v-else>
          <div class="chat-card__message--text" v-text="item.message" />
        </template>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { type ChatCardData } from "./useCommon";
import { MessageType } from "@/views/message/utils/types";
import { onLongPress } from "@vueuse/core";
import { ref, unref } from "vue";
import ChatImage from "./chatImage.vue";

defineOptions({
  name: "ChatCard"
});
const props = withDefaults(
  defineProps<{
    item: ChatCardData;
  }>(),
  {
    item: () => {
      return {} as ChatCardData;
    }
  }
);
const emit = defineEmits<{
  (event: "on-img-load", imgEvent: Event): void;
  (event: "status-click", item: ChatCardData): void;
  (event: "img-click", item: ChatCardData): void;
  (event: "long-click", item: ChatCardData, e: HTMLElement | null): void;
}>();
const onLoad = (imgEvent: Event) => {
  emit("on-img-load", imgEvent);
};
// const handleStatus = (item: ChatCardData) => {
//   emit("status-click", item);
// };
const handleImgClick = (item: ChatCardData) => {
  emit("img-click", item);
};
const handleContextMenu = (event: Event) => {
  event.preventDefault();
};
const itemHookRef = ref<HTMLElement | null>(null);
const handleLongPressCallbackHook = () => {
  emit("long-click", props.item, unref(itemHookRef.value));
};
onLongPress(itemHookRef, handleLongPressCallbackHook, {
  modifiers: { prevent: true }
});
</script>
<style scoped lang="scss">
.chat-card-box {
  &:not(.me) + .chat-card-box:not(.me, .has-tip),
  &.me + .chat-card-box.me:not(.has-tip) {
    .chat-card__cover > img {
      display: none;
    }

    .chat-card__message {
      margin-top: 0;
    }
  }

  .chat-card__tip {
    font: 400 12px / normal "PingFang SC";
    color: #a6a6a6;
  }

  .chat-card__message {
    padding: 12px 20px;
    margin-top: 14px;
    margin-left: 9px;
    overflow: hidden;
    font: 400 14px / 22px "PingFang SC";
    color: #434343;
    letter-spacing: -0.816px;
    word-break: break-all;
    white-space: pre-wrap;
    background: #f6f5f8;
    border-radius: 0 46px 46px 20px;

    &.image {
      padding: 0;
      overflow: hidden;
      border-radius: 8px;
    }
  }

  .chat-card__message--image {
    &.limit-height {
      height: 362px;
    }

    & > :deep(img) {
      max-width: 362px;
      max-height: 362px;
    }
  }

  &.me {
    .chat-card {
      flex-flow: row-reverse;

      .chat-card__message {
        margin-right: 9px;
        margin-left: 0;
        color: #fff;
        background: #34a853;
        border-radius: 46px 0 20px 46px;

        &.image {
          padding: 0;
          overflow: hidden;
          border-radius: 8px;
        }
      }
    }
  }
}
</style>
