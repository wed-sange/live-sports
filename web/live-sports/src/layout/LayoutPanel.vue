<template>
  <div class="flex flex-col h-full screen">
    <div class="relative z-10 flex-shrink-0">
      <slot name="top"></slot>
    </div>
    <div class="relative flex flex-col h-full contanier overflow-y-scroll ios-scrolling">
      <slot></slot>
    </div>

    <div class="relative z-10 flex-shrink-0">
      <slot name="bottom"></slot>
    </div>
  </div>
</template>

<script setup>
  /*
  布局-容器面板

  1，中间部分自动占用剩余高度；
  2，top，bottom部分的高度会占用中间部分的高度；
  3，整个组件的高度取决与父元素的高度；
*/
</script>
<script></script>
<style scoped></style>
