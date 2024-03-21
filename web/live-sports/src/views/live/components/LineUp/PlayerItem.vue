<template>
  <div
    class="absolute player-item flex flex-col items-center justify-center z-10"
    :style="{ left: player.locationX, top: player.locationY }"
  >
    <div class="flex flex-col items-center">
      <div
        :class="`${
          isHome ? 'home-shirt' : 'away-shirt'
        } w-[60.4px] h-[51px] text-[24px] text-[#fff] bg-contain flex items-center justify-center `"
      >
        <span class="mt-[4px] number-text">{{ player.shirtNumber }}</span>
      </div>
      <div class="w-[120px] truncate text-center text-[20px] player-name text-[#AEBAB1] mt-[10px]">
        <span class="text-[20px]">{{ player.name }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
  defineProps({
    player: {
      type: Object,
      required: true,
    },
    isHome: {
      type: Boolean,
      default: false,
    },
  });
</script>
<style scoped lang="scss">
  .player-item {
    .home-shirt {
      background: url('@/assets/img/room/lineup/home-shirt.png') center no-repeat;
      background-size: contain;
    }
    .away-shirt {
      background: url('@/assets/img/room/lineup/away-shirt.png') center no-repeat;
      background-size: contain;
    }
    .number-text {
      font-family: MiSans;
    }
  }
</style>
