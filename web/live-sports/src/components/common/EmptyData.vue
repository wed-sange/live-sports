<template>
  <!-- <div :class="`w-full h-full flex items-center justify-center mt-[${top}]`"> -->
  <div class="w-full h-full flex items-center justify-center">
    <div class="grow-0 shrink-0 basis-auto max-w-full">
      <div class="empty-data__icon w-[340px] h-[260px] mx-auto" :class="[icon]"></div>
      <div :class="`text-center pt-[10px] ${grayText ? 'empty-data-gray__text' : 'empty-data__text'}`">
        <slot>{{ text || '暂无数据' }}</slot>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  // withDefaults(defineProps<{ text?: string; grayText?: boolean; top?: string; icon?: 'anchor' | 'data' | 'message' | 'search' }>(), {
  //   icon: 'data',
  //   top: '320px',
  // });
  withDefaults(defineProps<{ text?: string; grayText?: boolean; icon?: 'anchor' | 'data' | 'message' | 'search' }>(), {
    icon: 'data',
  });
</script>
<script lang="ts">
  export default {
    name: 'EmptyData',
  };
</script>
<style scoped lang="scss">
  .empty-data__icon {
    $icon-list: 'data', 'anchor', 'message', 'lives', 'chat', 'data_black', 'search';
    @for $i from 1 through length($icon-list) {
      $cur: nth($icon-list, $i);
      &.#{$cur} {
        background: url('@/assets/img/common/empty_#{$cur}.png') no-repeat, transparent;
        background-size: auto 100%;
        background-position: center;
      }
    }
  }
  .empty-data__text {
    font: 400 26px / normal 'PingFang SC';
    color: #b8bdc9;
    white-space: normal;
    word-break: break-all;
  }
  .empty-data-gray__text {
    font: 400 26px / normal 'PingFang SC';
    color: #5b5b5b;
    white-space: normal;
    word-break: break-all;
  }
</style>
