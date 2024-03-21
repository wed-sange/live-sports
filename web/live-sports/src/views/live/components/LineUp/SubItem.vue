<template>
  <div
    :class="`flex flex-row sub-item  pt-[28px] h-[126px] bg-[#181819] text-[24px]   
    ${end ? (right ? 'right' : 'left') : ''}
    ${isHome ? 'home-item' : 'away-item'}
    `"
  >
    <div
      :class="`w-[60px] mt-[10px]  h-[50.8px]  flex items-center justify-center   ${
        right ? 'home-shirt  ml-[30px]' : 'away-shirt  ml-[28px]'
      }`"
    >
      <span class="number number-text">{{ player.shirtNumber }}</span>
    </div>
    <div :class="`flex flex-col ml-[16px]`">
      <span class="max-w-[220px] truncate text-[#D7D7E7] text-[22px]">{{ player.name }}</span>
      <div class="text-[22px] text-[#94999F] mt-[4px]">
        <span>{{ state.position() || '-' }}</span>
      </div>
    </div>
  </div>
</template>

<script setup>
  const props = defineProps({
    player: {
      type: Object,
      required: true,
    },
    right: {
      type: Boolean,
      default: false,
    },
    end: {
      type: Boolean,
      default: false,
    },
    isHome: {
      type: Boolean,
      default: false,
    },
  });
  const state = reactive({
    position: () => {
      return state.positionNames[props.player.position] || '';
    },
    positionNames: { F: '前锋', M: '中场', D: '后卫', G: '守门员' },
  });
</script>
<style scoped lang="scss">
  .sub-item {
    &.home-item {
      border-left: 1px solid #27272a;
      border-bottom: 1px solid #27272a;
    }
    &.away-item {
      border-left: 1px solid #27272a;
      border-right: 1px solid #27272a;
      border-bottom: 1px solid #27272a;
    }

    &.right {
      border-bottom-right-radius: 20px;
    }
    &.left {
      border-bottom-left-radius: 20px;
    }
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
