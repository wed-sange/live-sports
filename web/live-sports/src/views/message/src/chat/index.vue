<template>
  <page-box class="relative">
    <ChatVideo v-if="showChatVideo" @on-close="closeVideo" :video-url="videoInfo.videoUrl" :poster="videoInfo.poster" />
    <template #header>
      <AppHeader />
      <NavBar left-arrow @click-left="onLeftClick">
        <template #title>
          <div class="max-w-full h-full inline-flex items-center justify-center">
            <div class="max-w-full truncate">{{ anchorAccount.name || '聊天消息' }}</div>
          </div>
        </template>
      </NavBar>
    </template>
    <!-- <div class="w-full h-full keyboard-abr-scroll"> -->
    <div class="w-full h-full relative">
      <div class="chat-init-loading" v-if="pageLoading">
        <!-- <van-loading class="chat-init-loading-size" vertical>加载中...</van-loading> -->
      </div>
      <div class="chat-empty-box" v-if="list.length <= 0">
        <EmptyData class="pt-[320px] !h-auto" icon="message" text="暂无聊天记录" />
      </div>
      <ChatVirtualScroller v-model="loading" ref="scrollerRef" :finished="finished" :list="list" @load="onScrollLoad">
        <template #default="{ item }">
          <ChatCard
            :item="(item as ChatCardData)"
            @status-click="handleStatus"
            @img-click="handleImg"
            @long-click="handleLongClick"
            @on-img-load="onImgLoad"
            @video-click="handleVideo"
          />
        </template>
      </ChatVirtualScroller>
    </div>
    <template #footer>
      <!-- <ChatSend @send="onSend" /> -->
      <ChatSend />
    </template>
    <ChatPopover ref="chatPopoverRef" />
  </page-box>
</template>
<script setup lang="ts">
  import { provide } from 'vue';
  import { showImagePreview } from 'vant';
  import ChatVirtualScroller from '@/views/message/components/ChatVirtualScroller/index.vue';
  import ChatCard from './ChatCard.vue';
  import ChatSend from './ChatSend.vue';
  import ChatPopover from './ChatPopover.vue';
  import ChatVideo from './ChatVideo.vue';
  import { type ChatCardData } from './useCommon';

  import { useIndex } from './useIndex';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  const pageLoading = ref(true);
  const videoInfo = ref({ videoUrl: '', poster: '' });
  const showChatVideo = ref(false);
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
  const scrollToItem = (index: number) => {
    scrollerRef.value && scrollerRef.value.scrollToItem(index);
  };
  const scrollToBottom = () => {
    scrollerRef.value && scrollerRef.value.scrollToBottom();
  };
  // const setScrollOffset = (value: number) => {
  //   scrollerRef.value && scrollerRef.value.setScrollOffset(value);
  // };
  provide('scrollToBottom', scrollToBottom);
  provide('sendMessage', sendMessage);
  provide('sendImageMessage', sendImageMessage);
  createLoading();
  const onScrollLoad = async () => {
    try {
      const dataLength = list.value.length;
      await onLoad();
      nextTick(() => {
        if (dataLength === 0) {
          setTimeout(() => {
            scrollToBottom();
            scrollInitHeight.value = scrollerRef.value!.getScrollHeight();
            setTimeout(() => {
              pageLoading.value = false;
              closeLoading();
            }, 100);
          }, 200);
        } else {
          scrollToItem(list.value.length - dataLength - 1);
          scrollInitHeight.value = scrollerRef.value!.getScrollHeight();
        }
      });
    } catch {
      pageLoading.value = false;
    }
  };
  const scrollInitHeight = ref(0);
  let timer: number | null = null;
  const onImgLoad = async () => {
    if (timer) {
      clearTimeout(timer);
    }
    timer = setTimeout(() => {
      const currentScrollHeight = scrollerRef.value!.getScrollHeight();
      const currentScrollTop = scrollerRef.value!.currentScrollTop;
      const diff = currentScrollHeight - scrollInitHeight.value;
      scrollInitHeight.value = currentScrollHeight;
      if (currentScrollTop + diff >= currentScrollHeight - 4) {
        scrollToBottom();
      }
      timer = null;
    }, 50) as unknown as number;
  };
  const handleImg = (item: ChatCardData) => {
    showImagePreview({
      images: [item.message],
      showIndex: false,
    });
  };
  const handleVideo = (list) => {
    videoInfo.value.poster = list[0];
    videoInfo.value.videoUrl = list[1];
    showChatVideo.value = true;
  };
  const closeVideo = () => {
    showChatVideo.value = false;
  };

  const chatPopoverRef = ref<typeof ChatPopover | null>(null);
  const handleLongClick = (item: ChatCardData, e: HTMLElement | null) => {
    if (chatPopoverRef.value) {
      chatPopoverRef.value.openPopover(item, e);
    }
  };
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
    z-index: 3;
    background: #f2f3f7;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .chat-empty-box {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
    background: #f2f3f7;
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: flex-start;
  }
  :deep() .van-nav-bar .van-nav-bar__title {
    height: 100%;
    width: 60%;
  }
</style>
