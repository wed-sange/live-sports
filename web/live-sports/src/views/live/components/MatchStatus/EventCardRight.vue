<template>
  <div :class="`absolute top-0 flex w-[316px] pl-[30px] right-0 min-h-[72px] max-h-[78px]  text-[#D7D7E7] text-[24px]    `">
    <div v-if="type === 1" class="flex flex-col">
      <div class="flex items-center w-full">
        <img class="w-[40px] h-[40px] bg-contain" :src="icon" />
        <span class="mx-[10px] text-[#D7D7E7] font-semibold number-text">{{ (data.homeScore || 0) + ':' + (data.awayScore || 0) }}</span>
      </div>
      <div class="flex truncate mt-[4px]">
        <span class="truncate max-w-[210px]">{{ data.playerName }}</span>
        进球
      </div>
    </div>
    <div v-else-if="type === 9" class="flex flex-col truncate">
      <div class="flex items-center">
        <img :class="`w-[12px] h-[24px] `" :src="upPlayer" />
        <span class="mx-[10px] truncate">{{ data.inPlayerName }}</span>
      </div>
      <div class="flex items-center mt-[4px]">
        <img :class="`w-[12px] h-[24px] `" :src="downPlayer" />
        <span class="mr-[10px] ml-[16px] truncate">{{ data.outPlayerName }}</span>
      </div>
    </div>
    <div v-else class="flex items-center truncate h-full">
      <img class="max-w-[40px] max-h-[40px] bg-contain mr-[4px]" :src="icon" />
      <span class="truncate">{{ data.playerName }}</span>
    </div>
  </div>
</template>

<script setup>
  import upPlayer from '@/assets/img/room/event/up-player.png';
  import downPlayer from '@/assets/img/room/event/down-player.png';

  defineProps({
    type: {
      type: Number,
      required: true,
    },
    data: {
      type: Object,
      required: true,
      default: () => ({}),
    },
    icon: {
      type: String,
      required: true,
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
