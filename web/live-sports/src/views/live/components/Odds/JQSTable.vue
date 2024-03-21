<template>
  <div class="rqt jqs h-full overflow-y-scroll ml-[24px] mr-[24px]">
    <div class="w-full text-center mb-[32px]">
      <div
        class="h-[74px] bg-[#1F1F20] w-[702px] rounded-tl-[20px] rounded-tr-[20px] text-[#94999F] text-[24px] pt-[2px] flex items-center"
      >
        <span class="w-[164px] text-left ml-[30px]">公司</span>
        <span class="w-[250px] text-center">初盘</span>
        <span class="w-[254px] text-center">{{ thTitle }}</span>
      </div>
      <div v-for="(v, i) in data" :key="i" class="flex text-[#D7D7E7] items-center h-[94px] text-[24px]">
        <span
          :class="`w-[194px] h-[94px] flex-shrink-0 flex items-center text-left border-b pl-[30px] border-l border-r ${
            i == data.length - 1 ? 'rounded-bl-[20px]' : ''
          }`"
          >{{ cnameFormat(v.companyName) }}</span
        >
        <div class="w-[254px] number-text flex-shrink-0 justify-around text-center h-[94px] border-b border-r items-center flex px-[20px]">
          <span class="w-[66px] text-center">{{ v.firstHomeWin || 0 }}</span>
          <span class="w-[76px] text-center">{{ v.firstDraw || 0 }}</span>
          <span class="w-[72px] text-center">{{ v.firstAwayWin || 0 }}</span>
        </div>
        <div
          :class="`w-[254px] text-center flex h-[94px] border-b border-r items-center justify-center px-[20px] number-text ${
            i == data.length - 1 ? 'rounded-br-[20px]' : ''
          }`"
        >
          <span :class="`w-[66px] text-center color-${v.close ? '3' : compareRate('HomeWin', v)}`">{{
            v.close ? '封' : v.currentHomeWin || 0
          }}</span>
          <span :class="`w-[76px]  text-center color-${v.close ? '3' : compareRate('Draw', v)}`">{{
            v.close ? '封' : v.currentDraw || 0
          }}</span>
          <span :class="`w-[72px] color-${v.close ? '3' : compareRate('AwayWin', v)}`">{{ v.close ? '封' : v.currentAwayWin || 0 }}</span>
        </div>
      </div>
    </div>
    <no-data v-if="data.length == 0" class="mt-[120px]" icon="data_black" />
  </div>
</template>

<script setup>
  import noData from '@/components/common/EmptyData.vue';
  defineProps({
    data: {
      type: Array,
      required: true,
      default: () => [],
    },
    thTitle: {
      type: String,
      default: '即盘',
    },
  });

  const cnameFormat = (name) => {
    if (name.length == 0 || !name) return '';
    if (name.length <= 2) return name.slice(0, 1).padEnd(name.length, '*');
    if (name.length >= 3) return name.slice(0, 2).padEnd(name.length, '*');
  };
  const compareRate = (key, item) => {
    const currentValue = item['current' + key];
    const firstValue = item['first' + key];
    if (parseFloat(currentValue) > parseFloat(firstValue)) {
      return '1';
    } else if (parseFloat(currentValue) == parseFloat(firstValue)) {
      return '3';
    }
    return '2';
  };
</script>
<style scoped lang="scss">
  .jqs {
    font-family: PingFang SC;
    .color-1 {
      color: #d63823;
    }
    .number-text {
      font-family: MiSans;
    }
    .color-2 {
      color: #34a853;
    }
    .color-3 {
      color: #d7d7e7;
    }
    .border-b {
      border-bottom: 1px solid #27272a;
    }
    .border-r {
      border-right: 1px solid #27272a;
    }
    .border-l {
      border-left: 1px solid #27272a;
    }
  }
</style>
