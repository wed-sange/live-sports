<template>
  <div v-ripple class="flex pt-[23px] justify-between h-[186px] border-b-[2px] border-b-[#F2F3F7]">
    <div class="flex flex-col mr-[20px]">
      <div class="text-[#37373D] text-[28px] font-normal break-all">{{ data.title }}</div>
      <div class="text-[#94999F] text-[22px] mt-[28px] normal-text font-normal">
        {{ formateTime(data.publishTime) }} <span class="text-[#94999F] text-[22px] mt-[-2px] ml-[-3.5px]">发布</span>
      </div>
    </div>
    <DefaultImg class="w-[180px] h-[140px] bg-contain rounded-[8px] shrink-0" :src="data.pic" type="default" />
  </div>
</template>
<script setup>
  import { formateDate } from '@/utils/common';
  import DefaultImg from '@/components/common/DefaultImg.vue';

  defineProps({
    data: {
      type: Object,
    },
  });
  const formateTime = (time) => {
    return formateDate(parseFloat(time), 'YYYY-MM-DD HH:mm');
  };
</script>
<style scoped>
  .normal-text {
    font-family: PingFang SC;
  }
</style>
