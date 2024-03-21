<template>
  <div class="flex w-full h-[62px] flex-shrink-0 mb-[22px] items-center mt-[24px] anchor-info">
    <img :src="anchorInfo?.head || defaultAvatar" class="w-[60px] h-[60px] rounded-full bg-contain ml-[24px] mr-[10px]" />
    <div class="flex flex-col flex-grow mt-[2px]">
      <span class="text-[24px] text-white">{{ anchorInfo?.nickName || '未知用户' }} </span>
      <span class="text-[#94999F] text-[20px] flex items-center leading-[28px] mt-[-0.5px]">
        <img :src="grayHotIcon" class="mb-[4px] w-[16px] h-[18px] bg-cover" />
        <span class="ml-[8px]"> {{ hotValue || 0 }}热度值</span></span
      >
    </div>
    <div class="flex mr-[24px] font-medium text-[24px]">
      <span
        @click="onChart(anchorInfo?.id)"
        class="w-[120px] h-[46px] chat-item pt-[2.5px] rounded-[40px] text-white flex items-center justify-center"
      >
        私信
      </span>
      <span
        @click="onAttention(anchorInfo)"
        :class="`w-[120px]  h-[46px] rounded-[40px] text-[24px] flex items-center justify-center ${
          anchorInfo.focus ? ' focus-active' : 'border-[1px] focus-unactive'
        } ml-[20px] pt-[2.5px]`"
      >
        {{ anchorInfo.focus ? '已关注' : '关注' }}
      </span>
    </div>
  </div>
</template>
<script setup>
  import grayHotIcon from '@/assets/img/room/gray-hot.png';
  import defaultAvatar from '@/assets/img/common/default-anchor-icon.png';
  import router from '@/router';

  defineProps({
    anchorInfo: {
      type: Object,
    },
    hotValue: {
      type: Number,
    },
  });
  const emit = defineEmits(['on-attention']);

  const onAttention = (item) => {
    emit('on-attention', item.id, item.focus);
  };
  const onChart = (anchorId) => {
    const path = `/message/chat?anchorId=${anchorId}`;
    router.push(path);
  };
</script>
<style scoped lang="scss">
  .anchor-info {
    font-family: PingFang SC;
    .chat-item {
      background: #34a853;
    }
    .chat-item:active {
      background: #00733c;
    }
    .focus-active {
      background: #1f1f20;
      color: #94999f;
    }
    .focus-active:active {
      background: #3f3f42;
      color: #94999f;
      font-weight: 400;
      font-size: 24px;
    }
    .focus-unactive {
      border: #34a853;
      color: #34a853;
      border: 0.5px solid #34a853;
    }
    .focus-unactive:active {
      border: #34a853;
      color: #34a853;
      font-weight: 400;
      font-size: 24px;
      background: #3f3f42;
      border: 0.5px solid #34a853;
    }
  }
</style>
