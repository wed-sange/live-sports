<template>
  <div class="w-full h-full" ref="boxRef">
    <VirtList
      ref="virtListRef"
      class="w-full h-full"
      v-bind="$attrs"
      :list="list"
      :itemKey="keyField"
      :minSize="minSize"
      :buffer="20"
      @to-top="onScrollTop"
      @scroll="handleScroll"
    >
      <template #default="{ itemData, index }">
        <slot :itemData="itemData" :index="index"></slot>
      </template>
      <template #footer>
        <div :style="{ height: (finished && noBottom ? 0 : distance) + 'px' }">
          <div class="scroll-after__text flex justify-center" v-if="modelValue && !refreshing && !firstLoad">
            <van-loading class="loading-size">加载中...</van-loading>
          </div>
          <div class="scroll-after__text flex justify-center cursor-pointer" v-else-if="error" @click="handleError">{{ errorText }}</div>
          <div class="scroll-after__text flex justify-center" v-else-if="finished">{{ finishedText }}</div>
        </div>
      </template>
    </VirtList>
  </div>
</template>
<script setup lang="ts">
  import { VirtList } from 'vue-virt-list';
  import { useAppStore } from '@/store/modules/app';

  const appStore = useAppStore();
  let checkScrollTimer;
  const props = withDefaults(
    defineProps<{
      list: any[];
      modelValue: boolean;
      error?: boolean;
      errorText?: string;
      finished: boolean;
      finishedText?: string;
      distance?: number;
      keyField?: string;
      noBottom?: boolean;
      minSize?: number;
      refreshing?: boolean;
      firstLoad?: boolean;
    }>(),
    {
      modelValue: false,
      finished: false,
      distance: 60,
      errorText: '请求失败，点击重新加载',
      finishedText: '加载完成',
      keyField: 'id',
      noBottom: false,
      minSize: 140,
      refreshing: false,
      firstLoad: false,
    },
  );
  const virtListRef = ref();

  const emit = defineEmits(['update:modelValue', 'update:error', 'load', 'scroll']);
  const boxRef = ref<HTMLElement | null>(null);

  const scrollToBottom = () => {
    // virtListRef.value.scrollToIndex(index);
    // console.log('virtListRef.value', virtListRef.value, virtListRef.value.getOffset());
    virtListRef.value && virtListRef.value.scrollToBottom();
  };
  const onScrollBottom = async () => {
    if (props.finished || props.modelValue || props.error) {
      return;
    }
    emit('update:modelValue', true);
    emit('load');
  };
  const onScrollTop = () => {
    console.log('xxxxTop');
  };
  const handleError = () => {
    emit('update:error', false);
    onScrollBottom();
  };
  const getScrollHeight = () => {
    if (virtListRef.value) {
      let scrollHeight = virtListRef.value.$el.scrollHeight - boxRef.value!.clientHeight;
      scrollHeight = scrollHeight < 0 ? 0 : scrollHeight;
      return scrollHeight;
    } else {
      return 0;
    }
  };
  const handleScroll = (e) => {
    appStore.isMatchScroll = true;
    const scrollTop = e.target.scrollTop;
    emit('scroll', scrollTop);
    if (scrollTop >= getScrollHeight() - props.distance) {
      onScrollBottom();
    }
    if (checkScrollTimer) {
      clearTimeout(checkScrollTimer);
    }
    checkScrollTimer = setTimeout(() => {
      checkIsScroll();
    }, 400);
  };
  const checkIsScroll = () => {
    appStore.isMatchScroll = false;
  };
  onActivated(() => {
    if (!virtListRef.value || !virtListRef.value.reactiveData) {
      return;
    }
    const offset = virtListRef.value.reactiveData.offset;
    // virtListRef.value.reset();
    virtListRef.value.scrollToOffset(offset, false);
    // virtListRef.value.reset();
    // virtListRef.value.scrollToBottom();
    // virtListRef.value.scrollTop = virtListRef.value.scrollTop + 1;
  });
  const getScrollOffset = () => {
    return virtListRef.value.getOffset();
  };
  const setScrollOffset = (offset) => {
    virtListRef.value.scrollToOffset(offset);
  };
  defineExpose({
    getScrollOffset,
    setScrollOffset,
    scrollToBottom,
  });
</script>
<script lang="ts">
  export default {
    name: 'VirtualListScroller',
    inheritAttrs: false,
  };
</script>

<style scoped lang="scss">
  .van-loading.loading-size {
    --van-loading-spinner-size: 36px;
  }

  .scroll-after__text {
    font: 400 28px / normal 'PingFang SC';
    color: rgb(150, 151, 153);
  }
</style>
