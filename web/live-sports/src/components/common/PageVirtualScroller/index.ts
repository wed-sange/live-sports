import PageVirtualScroller from './index.vue';
const usePageVirtualScroller = () => {
  const isScrollTop = ref(true);
  // const isPointerPress = ref(false);
  // const unAllowUseTouch = computed(() => {
  //   // return isPointerPress.value || !isScrollTop.value;
  //   return !isScrollTop.value;
  // });
  const onScroll = (value: number) => {
    // isPointerPress.value = pointerPress;
    const _isScrollTop = value === 0;
    if (isScrollTop.value === _isScrollTop) return;
    isScrollTop.value = _isScrollTop;
  };
  return {
    onScroll,
    isScrollTop,
  };
};

export { usePageVirtualScroller, PageVirtualScroller };