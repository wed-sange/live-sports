<template>
  <van-nav-bar
    v-touchstart-prevent="{ target: '.van-nav-bar__title' }"
    :class="{ transparent: transparent }"
    v-bind="props"
    @click-left="onLeftClick"
    @click-right="onRightClick"
  >
    <template v-if="$slots.left" #left>
      <slot name="left"></slot>
    </template>
    <template v-if="$slots.right" #right>
      <slot name="right"></slot>
    </template>
    <template v-if="$slots.title" #title>
      <slot name="title"></slot>
    </template>
  </van-nav-bar>
</template>
<script setup lang="ts">
  import type { NavBarProps } from 'vant';
  import { useAppStore } from '@/store/modules/app';
  import { touchstartPreventDirective as vTouchstartPrevent } from '@/plugins/touchstartPrevent';
  interface CustomNavBarProps extends NavBarProps {
    transparent?: boolean;
  }
  const appStore = useAppStore();
  const props = withDefaults(defineProps<CustomNavBarProps>(), {
    fixed: false,
    border: true,
    leftArrow: false,
    placeholder: false,
    safeAreaInsetTop: false,
    clickable: true,
    transparent: false,
  });
  const emits = defineEmits(['click-left', 'click-right']);
  const onLeftClick = (event: MouseEvent) => {
    setTimeout(() => {
      appStore.backAnimation = true;
      emits('click-left', event);
    }, 200);
  };
  const onRightClick = (event: MouseEvent) => {
    emits('click-right', event);
  };
</script>
<script lang="ts">
  export default {
    name: 'NavBar',
  };
</script>
<style scoped lang="scss">
  .van-nav-bar {
    --van-nav-bar-icon-color: #37373d;
    --van-nav-bar-title-text-color: #37373d;
    --van-nav-bar-background: #fff;
    --van-nav-bar-height: 88px;
    &.van-hairline--bottom::after {
      border-bottom: none;
    }
    :deep().van-nav-bar__title {
      font: 500 32px / 90px 'PingFang SC';
    }
    &.transparent {
      --van-nav-bar-background: transparent;
      --van-nav-bar-icon-color: #fff;
      --van-nav-bar-title-text-color: #fff;
      :deep().van-nav-bar__left {
        .van-icon.van-icon-arrow-left {
          background: url('@/assets/img/common/icon_nav_bar_left_w.png') no-repeat, transparent;
          background-size: auto 100%;
          background-position: 0 0;
        }
      }
    }
    :deep().van-nav-bar__left {
      .van-icon.van-icon-arrow-left {
        display: inline-block;
        width: 20px;
        height: 34px;
        background: url('@/assets/img/common/icon_nav_bar_left.png') no-repeat, transparent;
        background-size: auto 100%;
        background-position: 0 0;
        margin-top: 2px;
        &::before {
          content: '';
        }
        transition: opacity 0.5s ease;
        opacity: 1;
      }
      .van-icon.van-icon-arrow-left:active {
        opacity: 0.3;
      }
    }
    :deep().van-nav-bar__left,
    :deep().van-nav-bar__right {
      @include use-active-style();
      --van-padding-md: 24px;
      --van-nav-bar-arrow-size: 46px;
      --van-nav-bar-text-color: #37373d;
      font: 400 32px / 88px 'PingFang SC';
    }
  }
</style>
