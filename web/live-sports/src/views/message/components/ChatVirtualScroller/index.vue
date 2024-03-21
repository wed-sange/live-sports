<template>
  <div class="w-full h-full" ref="boxRef">
    <DynamicScroller
      ref="scrollerRef"
      class="w-full h-full"
      :items="list"
      :min-item-size="100"
      :pageMode="false"
      :buffer="5000"
      @scroll="onScroll"
    >
      <template #before>
        <div class="flex justify-center" v-if="modelValue"><van-loading class="loading-size">加载中...</van-loading></div>
      </template>
      <template #default="{ item, index, active }">
        <DynamicScrollerItem :item="item" :active="active" :data-index="index" :data-active="active" class="message">
          <slot :item="item" :index="index"></slot>
        </DynamicScrollerItem>
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
      finished: boolean;
    }>(),
    {
      modelValue: false,
      finished: false,
    },
  );

  const emit = defineEmits(['update:modelValue', 'load']);
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
  const onScrollStart = async () => {
    if (props.finished) {
      return;
    }
    if (props.modelValue) return;
    emit('update:modelValue', true);
    emit('load');
  };
  onScrollStart();
  const onScroll = (event: Event) => {
    const target = event.target as HTMLElement;
    const scrollTop = target.scrollTop;
    currentScrollTop.value = scrollTop;
    if (scrollTop === 0) {
      onScrollStart();
    }
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
    name: 'ChatVirtualScroller',
  };
</script>

<style scoped lang="scss">
  .van-loading.loading-size {
    --van-loading-spinner-size: 36px;
  }
</style>
