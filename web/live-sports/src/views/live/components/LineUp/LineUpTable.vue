<template>
  <div class="flex flex-col items-center w-full mr-[24px] bg-[#181819] text-white text-[28px] lineup-table">
    <div
      v-if="item.homeTeam"
      class="flex w-full bg-[#1F1F20] rounded-t-[20px] h-[114px] flex-shrink-0 text-[28px] font-medium text-[#f5f5f5]"
    >
      <div class="flex w-full h-[60px] mt-[30px] items-center justify-center">
        <div class="flex items-center w-[60px] h-[60px] ml-[30px] justify-center">
          <img class="max-w-[60px] max-h-[60px] bg-contain" :src="item.homeTeam.logo || teamLogo" />
        </div>
        <span class="flex-grow font-medium h-full flex items-center justify-center ml-[-3px] mt-[-2px]"> {{ name }}</span>
        <div class="flex items-center justify-center w-[60px] h-[60px] mr-[30px]">
          <img class="max-w-[60px] max-h-[60px] bg-contain" :src="item.awayTeam.logo || teamLogo" />
        </div>
      </div>
    </div>

    <div class="flex w-full">
      <ul v-if="item.homeList && item.homeList.length > 0" class="flex w-[350px] flex-col h-auto rounded-b-[20px]">
        <SubItem
          v-for="(itemData, index) in item.homeList"
          :key="itemData.playerId"
          :end="index == item.homeList.length - 1"
          :player="itemData"
          :isHome="true"
          :isInjury="isInjury"
          :isFirst="isFirst"
        />
      </ul>
      <ul
        v-if="item.awayList && item.awayList.length > 0"
        class="flex w-[352px] flex-col h-auto rounded-b-[20px]"
        style="height: min-content"
      >
        <SubItem
          isShow="true"
          v-for="(itemData, index) in item.awayList"
          :key="itemData.playerId"
          :end="index == item.awayList.length - 1"
          :player="itemData"
          :isInjury="isInjury"
          :isFirst="isFirst"
          :right="true"
        />
      </ul>
    </div>
  </div>
</template>

<script setup>
  import SubItem from './SubItem.vue';
  defineProps({
    item: {
      type: Object,
      required: true,
    },
    name: {
      type: String,
      default: '',
    },
    isInjury: {
      type: Boolean,
      default: false,
    },
    isFirst: {
      type: Boolean,
      default: false,
    },
    matchName: {
      type: Object,
    },
  });
</script>
<style scoped lang="scss">
  .lineup-table {
  }
</style>
