<template>
  <div class="chat-list h-full">
    <PageVirtualScroller
      @scroll="handleScroll"
      ref="chatPageRef"
      v-model="loading"
      finished-text=""
      :finished="true"
      @load="onLoad"
      :list="list"
      :no-bottom="true"
    >
      <template #default="{ item, index }">
        <div :class="`flex flex-shrink-0 pb-[24px] ${!index ? 'pt-[24px]' : ''}`">
          <div v-if="item.msgType == 6" class="text-[#6B6E73] text-[28px]">
            {{ item.content }}
          </div>
          <div v-else class="flex flex-shrink-0">
            <div
              :class="`${
                item.identityType == 0 || item.identityType == 3 ? 'level-item ' : 'level-anchor'
              } text-[24px] flex  pt-[2px]  items-center justify-center h-[42px] px-[16px]  flex-shrink-0`"
            >
              <span v-if="item.identityType == 0 || item.identityType == 3" :class="`level mr-[6px] level-${item.level}`"></span>
              {{ getLevelName(item) }}
            </div>
            <div v-if="item.receiveContentType == 'img'" class="w-[574px] mx-[10px] text-[28px] leading-[42px] break-words break-all flex">
              <span :class="`${!item.level ? 'text-[#34A853] font-medium' : 'text-[#8A91a0]'}`">{{ item.fromNickName || item.nick }}:</span>
              <img class="max-w-[240px max-h-[240px] bg-contain rounded-[8px]" :src="item.content" />
            </div>
            <div v-else :class="`w-[574px] mx-[10px] text-[28px] leading-[42px] break-words break-all `">
              <span v-show="!item.htmlText" :class="`${!item.level ? 'text-[#34A853] font-medium' : 'text-[#8A91a0]'} flex-shrink-0`"
                >{{ item.fromNickName || item.nick }}:</span
              >
              <span v-show="!item.htmlText" class="text-[#f5f5f5] whitespace-pre-wrap break-words ml-[10px]">{{ item.content }} </span>
              <span v-show="item.htmlText" v-dompurify-html="item.content" class="text-[#f5f5f5] whitespace-pre-wrap break-words block">
              </span>
            </div>
          </div>
        </div>
      </template>
    </PageVirtualScroller>
  </div>
</template>

<script setup>
  import { PageVirtualScroller } from '@/components/common/PageVirtualScroller';
  import { useAppStore } from '@/store/modules/app';

  const appStore = useAppStore();
  let checkScrollTimer;
  const loading = ref(false);
  const chatPageRef = ref();
  const props = defineProps({
    list: {
      type: Array,
    },
  });
  const getLevelName = (item) => {
    if (item.identityType == 0 || item.identityType == 3) {
      return item.levelName || item.nick;
    }
    return '主播';
  };
  const onLoad = () => {
    console.log('xxxx', props.list);
  };
  const onScrollBottom = () => {
    chatPageRef.value.scrollToBottom();
  };
  const handleScroll = () => {
    appStore.isLiveScroll = true;
    if (checkScrollTimer) {
      clearTimeout(checkScrollTimer);
    }
    checkScrollTimer = setTimeout(() => {
      checkIsScroll();
    }, 500);
  };
  const checkIsScroll = () => {
    appStore.isLiveScroll = false;
  };

  defineExpose({
    onScrollBottom,
  });
</script>
<style scoped lang="scss">
  .chat-list {
    font-family: 'PingFang SC';
    font-style: normal;
    font-weight: 400;
    .level-item {
      border-radius: 40px;
      background: rgba(255, 255, 255, 0.1);
      font-family: 'PingFang SC';
      font-style: normal;
      font-weight: 400;
    }
    .level-anchor {
      border-radius: 40px;
      background: rgba(52, 168, 83, 0.2);
      font-family: 'PingFang SC';
      font-style: normal;
      font-weight: 500;
    }
    .level {
      display: inline-block;
      width: 24px;
      height: 26.38px;
      background: url('@/assets/img/user/level/v1.png') no-repeat, transparent;
      background-size: auto 100%;
      background-position: center;
      margin-bottom: 2px;
      @for $i from 1 through 6 {
        &.level-#{$i} {
          background: url('@/assets/img/user/level/v#{$i}.png') no-repeat, transparent;
          background-size: auto 100%;
          background-position: center;
        }
      }
    }
  }
</style>
