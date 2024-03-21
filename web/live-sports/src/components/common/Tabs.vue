<template>
  <div class="flex self-tabs relative w-full bg-white">
    <div class="absolute left-0">
      <slot class="flex-shrink-0" @click="onLeftClick" name="left"></slot>
    </div>
    <van-tabs
      v-model:active="active"
      background="white"
      title-active-color="#37373D"
      title-inactive-color="#94999F"
      shrink
      animated
      line-height="3px"
      line-width="23px"
      @change="onChange"
    >
      <van-tab class="click-style" :title="item.title" v-for="item in (tabList as any)" :key="item.style" />
    </van-tabs>
    <div class="absolute right-[24px] top-[20px]">
      <slot class="flex-shrink-0" name="right"></slot>
    </div>
  </div>
</template>
<script setup lang="ts">
  const emits = defineEmits(['clickLeft', 'on-change']);
  defineProps({
    tabList: {
      type: Array,
    },
  });
  const active = ref(0);

  const onLeftClick = (event: MouseEvent) => {
    emits('clickLeft', event);
  };
  const onChange = (index) => {
    emits('on-change', index);
  };
</script>
<script lang="ts">
  export default {
    name: 'Tabs',
  };
</script>
<style scoped lang="scss">
  .self-tabs {
    font-family: PingFang SC;
    line-height: normal;
    font-weight: 400;
    :deep().van-tab {
      margin-right: 60px;
      justify-content: baseline;
      align-items: baseline;
    }
    :deep().van-tab:active {
      opacity: 0.2;
    }
    :deep().van-tab--shrink {
      padding: 0;
    }
    --van-padding-xs: 24px;

    :deep().van-tabs__line {
      border-radius: 80px;
      background: #34a853;
    }
    :deep().van-tab__text {
      margin-top: 23px;
      font-size: 32px;
      line-height: 36px;
    }
    :deep().van-tab--active {
      font-weight: 400;
      line-height: normal;
      .van-tab__text {
        font-size: 36px;
        line-height: normal;
        margin-top: 15px;
        font-weight: 500;
        font-family: 'PingFang SC';
      }
    }
    // :deep().van-tabs__nav--line {
    //   padding-bottom: 4px;
    // }
    :deep().van-tabs__line {
      bottom: 46px;
      margin-left: 0.5px;
    }
    :deep().van-tabs__wrap {
      height: 88px;
      line-height: 88px;
      background: transparent;
    }
  }
</style>
