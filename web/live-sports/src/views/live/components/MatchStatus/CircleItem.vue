<template>
  <div class="flex flex-col">
    <span class="text-[#D7D7E7] text-[24px] mt-[-0.5px] self-center">{{ itemData.title }}</span>
    <div class="flex mt-[22px] text-[#94999F] text-[22px]">
      <span class="number-text mr-[7px] w-[28px] flex items-center justify-center mt-[2px]">{{ itemData.leftNum }}</span>
      <van-circle
        stroke-width="190"
        :current-rate="itemData.rateNum"
        color="#34A853"
        layer-color="#323235"
        :rate="100"
        start-position="top"
        :clockwise="false"
        size="52px"
      />
      <span class="number-text ml-[7px] w-[28px] flex items-center justify-center mt-[2px]">{{ itemData.rightNum }}</span>
    </div>
  </div>
</template>

<script setup>
  defineProps({
    itemData: {
      type: Object,
      required: true,
      default: () => ({}),
    },
  });
</script>
<style scoped>
  .right-text {
    unicode-bidi: bidi-override;
    direction: rtl;
  }

  .number-text {
    font-family: MiSans;
  }
</style>
