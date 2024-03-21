<template>
  <div class="w-full h-full" ref="boxRef">
    <DynamicScroller
      ref="scrollerRef"
      class="w-full h-full"
      v-bind="$attrs"
      :items="list"
      :min-item-size="100"
      :pageMode="false"
      :buffer="5000"
      @scroll="onScroll"
      :key-field="keyField"
    >
      <template #default="{ item, index, active }">
        <DynamicScrollerItem :item="item" :active="active" :data-index="index" :data-active="active" class="message">
          <slot :item="item" :index="index"></slot>
        </DynamicScrollerItem>
      </template>
      <template #after>
        <div :style="{ height: (finished && noBottom ? 0 : distance) + 'px' }">
          <div class="scroll-after__text flex justify-center" v-if="modelValue && !refreshing && list && list.length > 0">
            <van-loading class="loading-size">加载中...</van-loading>
          </div>
          <div class="scroll-after__text flex justify-center cursor-pointer" v-else-if="error" @click="handleError">{{ errorText }}</div>
          <div class="scroll-after__text flex justify-center" v-else-if="finished">{{ finishedText }}</div>
        </div>
      </template>
    </DynamicScroller>
  </div>
</template>
<script setup lang="ts">
  import { type DynamicScroller } from 'vue-virtual-scroller';
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
      refreshing?: boolean;
    }>(),
    {
      modelValue: false,
      finished: false,
      distance: 60,
      errorText: '请求失败，点击重新加载',
      finishedText: '加载完成',
      keyField: 'id',
      noBottom: false,
      refreshing: false,
    },
  );

  const emit = defineEmits(['update:modelValue', 'update:error', 'load', 'scroll']);
  const scrollerRef = ref<typeof DynamicScroller | null>(null);
  const boxRef = ref<HTMLElement | null>(null);
  const currentScrollTop = ref(0);
  let i = 0;
  let t: number | null = null;
  const _scrollToBottom = () => {
    t && clearTimeout(t);
    scrollerRef.value && scrollerRef.value.scrollToBottom();
    if (i > 4) {
      i = 0;
      return;
    }
    i++;
    // t = setTimeout(() => {
    //   _scrollToBottom();
    // }, 20) as unknown as number;
  };
  const scrollToBottom = () => {
    t && clearTimeout(t);
    i = 0;
    _scrollToBottom();
  };
  const scrollToItem = (index: number) => {
    scrollerRef.value && scrollerRef.value.scrollToItem(index);
  };
  const setScrollOffset = (value: number) => {
    if (scrollerRef.value) {
      scrollerRef.value.$el.scrollTop = currentScrollTop.value + value;
    }
  };
  const getScrollHeight = () => {
    if (scrollerRef.value) {
      let scrollHeight = scrollerRef.value.$el.scrollHeight - boxRef.value!.clientHeight;
      scrollHeight = scrollHeight < 0 ? 0 : scrollHeight;
      return scrollHeight;
    } else {
      return 0;
    }
  };
  const onScrollBottom = async () => {
    if (props.finished || props.modelValue || props.error) {
      return;
    }
    emit('update:modelValue', true);
    console.log('loadFirstData');
    emit('load');
  };
  onScrollBottom();
  const onScroll = (event: Event) => {
    const target = event.target as HTMLElement;
    const scrollTop = target.scrollTop;
    currentScrollTop.value = scrollTop;
    if (scrollTop >= getScrollHeight() - props.distance) {
      onScrollBottom();
    }
    emit('scroll', scrollTop);
  };
  const handleError = () => {
    emit('update:error', false);
    onScrollBottom();
  };
  defineExpose({
    scrollToBottom,
    scrollToItem,
    setScrollOffset,
    currentScrollTop,
    getScrollHeight,
  });
</script>
<script lang="ts">
  export default {
    name: 'PageVirtualScroller',
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
