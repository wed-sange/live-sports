<template>
  <page-box class="relative">
    <template #header>
      <NavBar transparent left-arrow @click-left="onLeftClick">
        <template #title>
          <div class="max-w-full h-full inline-flex items-center justify-center">
            <div class="grow-0 shrink-0 basis-[40px] h-[40px] rounded-[100%]">
              <img class="inline-block w-full h-full rounded-[inherit] overflow-hidden" :src="anchorAccount.cover" alt="logo" />
            </div>
            <div class="grow-0 shrink-0 basis-auto max-w-[calc(100%-40px)] ml-[16px] mt-[14px]">
              <div class="max-w-full truncate">{{ anchorAccount.name || '聊天消息' }}</div>
            </div>
          </div>
        </template>
      </NavBar>
    </template>
    <!-- <div class="w-full h-full keyboard-abr-scroll"> -->
    <div class="w-full h-full">
      <ChatVirtualScroller v-model="loading" ref="scrollerRef" :finished="finished" @load="onScrollLoad">
        <template v-for="item in list" :key="item.id">
          <ChatCard :item="item" @status-click="handleStatus" @on-img-load="onImgLoad" />
        </template>
      </ChatVirtualScroller>
    </div>
    <template #footer>
      <!-- <ChatSend @send="onSend" /> -->
      <ChatSend />
    </template>
    <div class="chat-init-loading" v-if="pageLoading">
      <van-loading class="chat-init-loading-size" vertical>加载中...</van-loading>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import { provide } from 'vue';
  import ChatVirtualScroller from '@/views/message/components/ChatVirtualScroller/scroll.vue';
  import ChatCard from './ChatCard.vue';
  import ChatSend from './ChatSend.vue';
  import { useIndex } from './useIndex';
  const pageLoading = ref(true);
  const scrollerRef = ref<typeof ChatVirtualScroller | null>(null);
  const router = useRouter();
  const onLeftClick = () => {
    router.go(-1);
  };
  const { list, loading, onLoad, finished, sendMessage, sendImageMessage, anchorAccount, handleStatus } = useIndex({
    onReceiveMessage: () => {
      scrollToBottom();
    },
  });
  const scrollToItem = async (index: number) => {
    await nextTick();
    const scrollEl = scrollerRef.value!.$el;
    const itemDom: HTMLElement = scrollEl.querySelector('.chat-card-box:nth-child(' + index + ')');
    if (itemDom) {
      scrollerRef.value && scrollerRef.value.setScrollTop(itemDom.offsetTop);
    }
  };
  const scrollToBottom = () => {
    scrollerRef.value && scrollerRef.value.scrollToBottom();
  };
  const setScrollOffset = (value: number) => {
    scrollerRef.value && scrollerRef.value.setScrollOffset(value);
  };
  provide('scrollToBottom', scrollToBottom);

  provide('sendMessage', sendMessage);
  provide('sendImageMessage', sendImageMessage);
  const onScrollLoad = async () => {
    try {
      const dataLength = list.value.length;
      await onLoad();
      nextTick(() => {
        if (dataLength === 0) {
          setTimeout(() => {
            scrollToBottom();
            setTimeout(() => {
              pageLoading.value = false;
            }, 100);
          }, 200);
        } else {
          scrollToItem(list.value.length - dataLength - 1);
        }
      });
    } catch {
      pageLoading.value = false;
    }
  };
  let offsetTop = 0;
  let timer: number | null = null;
  const onImgLoad = async (imgEvent: Event) => {
    const clientHeight = (imgEvent.target as HTMLElement).clientHeight;
    if (clientHeight > 40) {
      offsetTop += clientHeight;
      if (timer) {
        clearTimeout(timer);
      }
      timer = setTimeout(() => {
        setScrollOffset(offsetTop);
        offsetTop = 0;
        timer = null;
      }, 100) as unknown as number;
    }
  };
  // const onSend = (value, type) => {
  //   if (type === 'text') {
  //     sendMessage(value);
  //   }
  //   if (type === 'image') {
  //     sendImageMessage(value);
  //   }
  //   scrollToBottom();
  // };
</script>
<script lang="ts">
  export default {
    name: 'MessageChat',
  };
</script>

<style scoped lang="scss">
  .chat-init-loading {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
    background: #07061d;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  :deep() .van-nav-bar .van-nav-bar__title {
    height: 100%;
    width: 60%;
  }
</style>
