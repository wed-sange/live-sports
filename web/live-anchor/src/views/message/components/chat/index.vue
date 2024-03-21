<template>
  <div class="chat-box flex flex-col bg-[#fff] w-full h-full">
    <div
      v-show="receiveId"
      class="chat-box__header grow-0 shrink-0 basis-[90px] flex items-center"
    >
      <ChatHeader :account="receiveAccount" />
    </div>
    <div
      class="chat-box__body flex-1 overflow-auto flex flex-col"
      v-show="receiveId"
    >
      <div class="chat-box__section flex-1 overflow-auto">
        <ChatScroll
          ref="chatScrollRef"
          v-model="loading"
          :finished="finished"
          :list="list"
          @load="onScrollLoad"
        >
          <template v-for="item in list" :key="item.id">
            <ChatCard :class="item.id" :item="item" @on-img-load="onImgLoad" />
          </template>
        </ChatScroll>
      </div>
      <div class="chat-box__send grow-0 shrink-0 basis-auto">
        <ChatSend />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, nextTick, provide, toRef, watch } from "vue";
import ChatHeader from "./chatHeader.vue";
import ChatScroll from "./chatScroll.vue";
import ChatCard from "./chatCard.vue";
import ChatSend from "./chatSend.vue";
import { useIndex } from "./useIndex";
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
const {
  list,
  onLoad,
  loading,
  finished,
  sendMessage,
  sendImageMessage,
  receiveAccount,
  onRefresh
} = useIndex({
  receiveId,
  anchorId,
  onReceiveMessage: () => {
    scrollToBottom();
  }
});
const scrollToBottom = async () => {
  await nextTick();
  chatScrollRef.value.scrollToBottom();
};
const scrollToItem = async index => {
  await nextTick();
  const scrollEl = chatScrollRef.value.$el;
  const itemDom: HTMLElement = scrollEl.querySelector(
    ".chat-card-box:nth-child(" + index + ")"
  );
  if (itemDom) {
    chatScrollRef.value.setScrollTop(itemDom.offsetTop);
  }
};

provide("scrollToBottom", scrollToBottom);

provide("sendMessage", sendMessage);
provide("sendImageMessage", sendImageMessage);
const onScrollLoad = async () => {
  try {
    const dataLength = list.value.length;
    if (dataLength === 0) {
      loading.value = false;
      return;
    }
    await onLoad();
    nextTick(() => {
      scrollToItem(list.value.length - dataLength + 1);
      scrollInitHeight.value = chatScrollRef.value.getScrollHeight();
    });
  } catch {
    // pageLoading.value = false;
  }
};
const scrollInitHeight = ref(0);
const handleRefresh = async () => {
  await onRefresh();
  scrollToBottom();
  nextTick(() => {
    scrollInitHeight.value = chatScrollRef.value.getScrollHeight();
  });
};
let timer = null;
const onImgLoad = async () => {
  if (timer) {
    clearTimeout(timer);
  }
  timer = setTimeout(() => {
    const currentScrollHeight = chatScrollRef.value.getScrollHeight();
    const currentScrollTop = chatScrollRef.value.currentScrollTop;
    const diff = currentScrollHeight - scrollInitHeight.value;
    // scrollInitHeight.value = currentScrollHeight;
    if (currentScrollTop + diff >= currentScrollHeight - 4) {
      // chatScrollRef.value && chatScrollRef.value.setScrollOffset(diff);
      scrollToBottom();
    }
    timer = null;
  }, 50);
};
watch(
  receiveId,
  (newValue, oldValue) => {
    const sendId = receiveId.value.split("-")[1];
    if (sendId && newValue !== oldValue) {
      handleRefresh();
    }
  },
  {
    immediate: true
  }
);
</script>

<style scoped lang="scss">
.chat-box {
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
