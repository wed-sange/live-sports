<template>
  <div class="spf h-full overflow-y-scroll ml-[24px] mr-[24px]">
    <div class="w-full text-center table-auto mb-[32px]">
      <div
        class="h-[74px] bg-[#1F1F20] normal-text mt-[0px] rounded-tl-[20px] rounded-tr-[20px] flex items-center text-[#94999F] text-[24px]"
      >
        <span class="w-[234px] flex-shrink-0 text-left pl-[30px]">公司</span>
        <span class="w-[72px] flex-shrink-0 text-center">主胜</span>
        <span class="w-[72px] mx-[50px] flex-shrink-0 text-center">平</span>
        <span class="w-[72px] flex-shrink-0 text-center">客胜</span>
        <span class="w-[72px] ml-[50px] flex-shrink-0 text-center">赔付率</span>
      </div>
      <div
        v-for="(v, i) in data"
        :key="i"
        :class="`flex items-center border-l h-[188px] border-b text-[24px] border-r 
        ${i == data.length - 1 ? 'rounded-bl-[20px] rounded-br-[20px]' : ''}
         ${i % 2 == 0 ? 'bg-[#181819]' : 'bg-[#1D1D1D]'}`"
      >
        <div class="w-[132px] truncate h-[188px] flex items-center text-left pl-[30px] text-white border-r">{{
          cnameFormat(v.companyName)
        }}</div>
        <div class="flex flex-col flex-grow">
          <div class="flex items-center border-b number-text h-[94px] pt-[8px]">
            <span class="w-[84px] text-center text-[#94999F] text-[24px] h-[94px] border-r flex justify-center items-center">初</span>
            <span class="w-[108px] text-center">{{ v.firstHomeWin || 0 }}</span>
            <span class="w-[126px] text-center">{{ v.firstDraw || 0 }}</span>
            <span class="w-[112px] text-center">{{ v.firstAwayWin || 0 }}</span>
            <span class="w-[136px] text-center">{{ v.firstLossRatio ? v.firstLossRatio * 100 + '%' : 0 }}</span>
          </div>
          <div class="flex items-center number-text h-[94px] pt-[8px]">
            <span class="w-[84px] text-center border-r h-[94px] text-[24px] text-[#94999F] flex items-center justify-center">即</span>
            <span :class="`w-[108px] text-center  color-${v.close ? '3' : compareRate('HomeWin', v)}`">{{
              v.close ? '封' : v.currentHomeWin || 0
            }}</span>
            <span :class="`w-[132px] text-center color-${v.close ? '3' : compareRate('Draw', v)}`">{{
              v.close ? '封' : v.currentDraw || 0
            }}</span>
            <span :class="`w-[112px] text-center color-${v.close ? '3' : compareRate('AwayWin', v)}`">{{
              v.close ? '封' : v.currentAwayWin || 0
            }}</span>
            <span :class="`w-[128px] text-center color-3`">{{
              v.close ? '封' : v.currentLossRatio ? v.currentLossRatio * 100 + '%' : 0
            }}</span>
          </div>
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
      default: '即时',
    },
  });

  const cnameFormat = (name) => {
    if (name.length == 0 || !name) return '';
    if (name.length <= 2) return name.slice(0, 1).padEnd(name.length, '*');
    if (name.length >= 3) {
      name = name.slice(0, 2).padEnd(name.length, '*');
      if (name.length > 7) {
        name = name.slice(0, 7);
      }
      return name;
    }
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
  .spf {
    font-family: PingFang SC;
    .number-text {
      font-family: MiSans;
    }
    .normal-text {
      font-family: PingFang SC;
    }
    .color-1 {
      color: #d63823;
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
