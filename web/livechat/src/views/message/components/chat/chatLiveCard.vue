<!-- eslint-disable vue/no-v-html -->
<template>
  <div
    class="chat-card-box w-full px-[30px] py-[8px]"
    :class="{ me: item.isMe }"
  >
    <div
      class="chat-card__tip pb-[16px] text-center truncate"
      v-show="item.tip"
    >
      {{ item.tip }}
    </div>
    <div class="inline-block max-w-full">
      <div class="chat-card w-full px-[20px] py-[12px]">
        <span class="chat-card__role" :class="['level-' + item.level]">
          {{ item.isMe ? "主播" : formatLevelName(item.level) }}
        </span>
        <span class="chat-card__nickname"> {{ item.accountName }}： </span>
        <template v-if="item.type === MessageType.image">
          <img
            class="inline-block rounded-[8px] overflow-hidden align-text-top max-w-[362px] max-h-[362px]"
            :src="item.message"
            :key="item.id"
            alt="image"
            loading="lazy"
            @load="onLoad($event)"
          />
        </template>
        <template v-else>
          <span class="chat-card__message" v-text="item.message" />
        </template>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { type ChatCardData } from "./useCommon";
import { MessageType } from "@/views/message/utils/types";
import { formatLevelName } from "@/views/message/utils/chat";

defineOptions({
  name: "ChatCard"
});
withDefaults(
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
}>();
const onLoad = (imgEvent: Event) => {
  emit("on-img-load", imgEvent);
};
</script>
<style scoped lang="scss">
.chat-card-box {
  .chat-card__tip {
    font: 400 12px / normal "PingFang SC";
    color: #a6a6a6;
  }

  .chat-card {
    background: #f6f5f8;
    border-radius: 16px;
  }

  .chat-card__nickname {
    font: 400 14px / 16.8px "PingFang SC";
    color: #4c95d8;
    vertical-align: middle;
  }

  .chat-card__message {
    overflow: hidden;
    font: 400 14px / 26px "PingFang SC";
    color: #1a1a1a;
    word-break: break-all;
    white-space: pre-wrap;
    vertical-align: middle;
  }

  .chat-card__role {
    @for $i from 1 through 6 {
      &.level-#{$i} {
        background: url("@/assets/message/level/v#{$i}.png") no-repeat, #2f2f30;
        background-position: 8px 3px;
        background-size: auto 13px;
      }
    }

    display: inline-block;
    width: 54px;
    height: 18px;
    padding-right: 8px;
    margin-right: 4px;
    font: 400 12px / 18px "PingFang SC";
    color: #fff;
    text-align: right;
    vertical-align: middle;
    border-radius: 20px;
  }

  &.me {
    .chat-card__role {
      padding: 0;
      text-align: center;
      // background: url("@/assets/message/icon_message_anchor.png") no-repeat,
      //   transparent;
      // background-position: center;
      // background-size: 100% 100%;
      background: #2f2f30;
    }
  }
}
</style>
