<template>
  <div
    :class="`${
      isHome ? 'top-[590px]' : 'bottom-[590px]'
    } absolute px-[20px] flex justify-between text-[20px] text-white w-full items-center`"
  >
    <div class="flex">
      <div class="flex flex-col justify-between">
        <span>阵型 {{ props.data.formation || '-' }}</span>
      </div>
    </div>
    <div class="flex items-center">
      <span class="mx-[8px]">球队身价 </span>
      <span class="text-right">{{ formatMarketValue() }}</span>
    </div>
  </div>
</template>

<script setup>
  import { currencyNum } from '@/utils/common';
  const props = defineProps({
    data: {
      type: Object,
      required: true,
    },
    isHome: {
      type: Boolean,
    },
  });
  const formatMarketValue = () => {
    const { marketValue } = props.data;
    if (!marketValue) {
      return '-';
    }
    return currencyNum(marketValue) + '欧元';
  };
</script>
<style></style>
