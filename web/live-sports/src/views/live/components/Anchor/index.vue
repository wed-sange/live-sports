<template>
  <div class="pt-[24px] pb-[52px] ml-[24px]">
    <div class="w-[702px] text-[#94999F] bg-[#1F1F20] flex flex-col rounded-[20px] mr-[24px] p-[30px] h-auto">
      <div class="flex w-full border-b-[1px] border-b-[#27272A] pb-[27px]">
        <img :src="anchorInfo?.head || defaultAvatar" class="w-[88px] h-[88px] rounded-full bg-contain mr-[16px]" />
        <div class="flex flex-col flex-grow">
          <span class="text-[30px] text-white font-medium">{{ anchorInfo?.nickName || '未知用户' }}</span>
          <span class="text-[#94999F] mt-[8px] text-[24px]">{{ anchorInfo?.fansCount || 0 }}粉丝</span>
        </div>
      </div>
      <div class="flex flex-col pt-[30px]">
        <div class="font-medium text-[28px] text-white flex items-center h-[40px]">
          <img class="w-[36px] h-[28px] bg-contain mr-[10px]" :src="noticeIcon" />
          主播公告</div
        >
        <div
          v-if="anchorInfo && anchorInfo.notice"
          v-dompurify-html="anchorInfo.notice"
          class="text-[26px] mt-[14.8px] text-[#94999F] leading-[46px] whitespace-pre-wrap break-words"
        ></div>
      </div>
    </div>
  </div>
</template>
<script setup>
  import noticeIcon from '@/assets/img/room/notice-icon.png';
  import defaultAvatar from '@/assets/img/common/default-anchor-icon.png';

  defineProps({
    anchorInfo: {
      type: Object,
    },
  });
</script>
<style scoped lang="scss"></style>
