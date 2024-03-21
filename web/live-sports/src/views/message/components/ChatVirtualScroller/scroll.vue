<template>
  <div class="chat-scroll-box w-full h-full overflow-auto" ref="scrollRef" @scroll="onScroll">
    <div ref="innerRef">
      <div class="chat-scroll-box__header">
        <div class="flex justify-center" v-if="modelValue"><van-loading class="loading-size">加载中...</van-loading></div>
      </div>
      <slot></slot>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { ref, nextTick } from 'vue';
  const props = withDefaults(
    defineProps<{
      modelValue: boolean;
      finished: boolean;
    }>(),
    {
      modelValue: false,
      finished: false,
    },
  );
  const emit = defineEmits(['update:modelValue', 'load']);
  const innerRef = ref<HTMLElement | null>(null);
  const scrollRef = ref<HTMLElement | null>(null);
  const currentScrollTop = ref(0);
  const setScrollTop = (value: number) => {
    if (scrollRef.value) {
      scrollRef.value.scrollTop = value;
    }
  };
  const setScrollOffset = (value: number) => {
    setScrollTop(currentScrollTop.value + value);
  };
  let i = 0;
  const scrollToBottom = async () => {
    await nextTick();
    let scrollHeight = innerRef.value!.clientHeight - scrollRef.value!.clientHeight;
    scrollHeight = scrollHeight < 0 ? 0 : scrollHeight;
    setScrollTop(scrollHeight);
    i++;
    if (i > 10) {
      i = 0;
      return;
    }
    // setTimeout(async () => {
    //   await scrollToBottom();
    // }, 0);
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
    setScrollTop,
    setScrollOffset,
  });
</script>

<script lang="ts">
  export default {
    name: 'ChatScroller',
  };
</script>
