<template>
  <teleport :to="popoverTarget">
    <van-popover
      class="popover-custom w-[120px] h-[58px]"
      placement="top"
      v-model:show="popoverShow"
      :actions="actions"
      @select="onSelect"
      theme="dark"
    />
  </teleport>
</template>
<script setup lang="ts">
  import { Popover as VanPopover, showToast } from 'vant';
  import { copyText } from '@/utils/copyText';
  import { ChatCardData } from './useCommon';
  const popoverTarget = ref<HTMLElement>(document.body);
  const popoverContent = ref('');
  const popoverShow = ref(false);
  const actions = [{ text: '复制' }];
  const onSelect = () => {
    copyText(popoverContent.value).then((success) => {
      showToast({
        // icon: 'custom-icon-success',
        className: 'custom-toast',
        message: success ? '复制成功' : '该浏览器不支持复制功能',
        // overlay: true,
      });
    });
  };
  const openPopover = (item: ChatCardData, e: HTMLElement | null) => {
    popoverTarget.value = e || document.body;
    popoverContent.value = item.message;
    popoverShow.value = true;
  };
  defineExpose({
    openPopover,
  });
</script>
<script lang="ts">
  export default {
    name: 'ChatPopover',
  };
</script>

<style lang="scss">
  .popover-custom.van-popover[data-popper-placement^='top'] {
    --van-popover-dark-background: #37373d;
    --van-popover-radius: 8px;
    --van-popover-arrow-size: 12px;
    --van-popover-action-width: 120px;
    --van-popover-action-height: 58px;
    .van-popover__arrow {
      bottom: 2px;
    }
    .van-popover__action {
      padding: 0 30px;
      font: 400 30px / normal 'PingFang SC';
      color: #fff;
    }
    .van-popover__action-text {
      min-width: 70px;
      margin-left: -5px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-wrap: nowrap;
      font: 400 30px / normal 'PingFang SC';
      color: #fff;
    }
  }
</style>
