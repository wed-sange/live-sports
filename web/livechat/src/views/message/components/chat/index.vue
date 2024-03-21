<template>
  <div class="bg-[#fff] w-full h-full px-[20px]">
    <div
      class="flex items-center flex-col justify-center w-full h-full"
      v-if="!receiveId || !anchorId"
    >
      <IconNoData />
      <p class="text-center text-[13px] text-[#B8BDC9]">暂无内容</p>
    </div>
    <div v-else class="chat-box w-full h-full flex flex-col">
      <div
        class="chat-box__header grow-0 shrink-0 basis-[90px] flex items-center"
      >
        <ChatHeader :account="receiveAccount" />
      </div>
      <div class="chat-box__body flex-1 overflow-auto flex flex-col">
        <div class="chat-box__section flex-1 overflow-auto chat-container">
          <ChatScroll
            ref="chatScrollRef"
            v-model="chatLoading"
            :list="list"
            @load="onLazyLoad"
          >
            <template v-for="item in list" :key="item.id">
              <!-- @on-img-load="onImgLoad" -->
              <ChatCard :class="item.id" :item="item" />
            </template>
          </ChatScroll>
          <section
            v-if="unreadCount"
            @click="handleBackToLatest"
            class="chat-back-to-latest"
          >
            <img :src="vectorIcon" alt="" />
            <span>{{ unreadCount }}条新消息</span>
          </section>
        </div>
        <div class="chat-box__send grow-0 shrink-0 basis-auto">
          <ChatSend />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, nextTick, provide, toRef, watch, computed } from "vue";
import ChatHeader from "./chatHeader.vue";
import ChatScroll from "./chatScroll.vue";
import ChatCard from "./chatCard.vue";
import ChatSend from "./chatSend.vue";
import { useIndex } from "./useIndex";
import { IconNoData } from "../icon/index";
import vectorIcon from "@/assets/message/vector.png";
import { useMessageUnReadCountStore } from "@/store/modules/message/unReadCount";
defineOptions({
  name: "ChatBox"
});
const props = withDefaults(
  defineProps<{
    receiveId: string;
    anchorId: string;
  }>(),
  {
    receiveId: "",
    anchorId: ""
  }
);
const receiveId = toRef(props, "receiveId");
const anchorId = toRef(props, "anchorId");
const chatScrollRef = ref<InstanceType<typeof ChatScroll>>();
const messageUnReadCountStore = useMessageUnReadCountStore();
const unreadCount = computed(
  () => messageUnReadCountStore.unReadRecord?.[receiveId.value] || 0
);

const {
  list,
  onLazyLoad,
  chatLoading,
  sendMessage,
  sendImageMessage,
  sendVideoMessage,
  receiveAccount,
  onRefresh,
  handleBackToLatest
} = useIndex({
  receiveId,
  anchorId,
  onReceiveMessage: (reallyBottom = true) => {
    scrollToBottom(reallyBottom);
  }
});
const scrollToBottom = async (reallyBottom = true) => {
  await nextTick();
  chatScrollRef.value.scrollToBottom(reallyBottom);
};

watch(
  receiveId,
  async (newValue, oldValue) => {
    const userId = receiveId.value.split("-")[1];
    if (userId && newValue !== oldValue) {
      await onRefresh({
        userId,
        anchorId: anchorId.value
      });
    }
  },
  {
    immediate: true
  }
);

provide("scrollToBottom", scrollToBottom);
provide("sendMessage", sendMessage);
provide("sendImageMessage", sendImageMessage);
provide("sendVideoMessage", sendVideoMessage);
</script>

<style scoped lang="scss">
.chat-box {
  .chat-container {
    position: relative;
  }

  .chat-back-to-latest {
    position: absolute;
    right: 0;
    bottom: 27px;
    display: flex;
    gap: 8px;
    align-items: center;
    padding: 12px 18px;
    font-family: "PingFang SC";
    font-size: 16px;
    font-style: normal;
    font-weight: 400;
    line-height: normal;
    color: #34a853;
    letter-spacing: 0.16px;
    cursor: pointer;
    background: #fff;
    border-radius: 22px 0 0 22px;
    box-shadow: 0 3px 8.3px 2px rgb(32 34 39 / 10%);
  }

  .chat-box__header {
    background: #fff;
    border-bottom: 1px solid #e5e6eb;

    h2 {
      display: inline-block;
      min-height: 24px;
      padding-left: 26px;
      margin-left: 22px;
      font: 500 18px / normal "PingFang SC";
      color: #1a1a1a;
      background: url("@/assets/message/icon_user.png") no-repeat, transparent;
      background-position: 0 center;
      background-size: 24px 24px;
    }
  }
}
</style>
