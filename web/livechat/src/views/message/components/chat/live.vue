<template>
  <div class="chat-box flex flex-col bg-[#fff] w-full h-full">
    <div
      v-show="anchorId"
      class="chat-box__header grow-0 shrink-0 basis-[90px] flex items-center"
    >
      <h2>聊天室</h2>
    </div>
    <div
      class="chat-box__body flex-1 overflow-auto flex flex-col"
      v-show="anchorId"
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
            <ChatLiveCard
              :class="item.id"
              :item="item"
              @on-img-load="onImgLoad"
            />
          </template>
        </ChatScroll>
      </div>
      <div class="chat-box__send grow-0 shrink-0 basis-auto">
        <ChatSend type="live" />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, nextTick, provide, toRef, watch } from "vue";
import ChatScroll from "./chatScroll.vue";
import ChatLiveCard from "./chatLiveCard.vue";
import ChatSend from "./chatSend.vue";
import { useIndex } from "./useLiveIndex";
defineOptions({
  name: "ChatBox"
});
const props = withDefaults(
  defineProps<{
    anchorId: string;
  }>(),
  {
    anchorId: ""
  }
);
const anchorId = toRef(props, "anchorId");
const chatScrollRef = ref<InstanceType<typeof ChatScroll>>();
const {
  list,
  onLoad,
  loading,
  finished,
  sendMessage,
  sendImageMessage,
  onRefresh
} = useIndex({
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
    });
  } catch {
    // pageLoading.value = false;
  }
};
const handleRefresh = async () => {
  await onRefresh();
  scrollToBottom();
};
let offsetTop = 0;
let timer = null;
const onImgLoad = async (imgEvent: Event) => {
  const clientHeight = (imgEvent.target as HTMLElement).clientHeight;
  if (clientHeight > 40) {
    offsetTop += clientHeight;
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      chatScrollRef.value.setScrollOffset(offsetTop);
      offsetTop = 0;
      timer = null;
    }, 200);
  }
};
watch(
  anchorId,
  (newValue, oldValue) => {
    if (newValue && newValue !== oldValue) {
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
      padding-left: 18px;
      margin-left: 31px;
      font: 500 14px / normal "PingFang SC";
      color: #1a1a1a;
      background: url("@/assets/message/icon_live_room.png") no-repeat,
        transparent;
      background-position: 0 center;
      background-size: auto 16px;
    }
  }
}
</style>
