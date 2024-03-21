<template>
  <div class="">
    <div
      class="flex mt-[30px] ml-[24px] font-normal items-center text-[#94999F] text-[24px] bg-[#1F1F20] rounded-tl-[20px] rounded-tr-[20px] w-[702px] h-[74px]"
    >
      <span class="w-[162px] ml-[30px] text-left flex-shrink-0">球员</span>
      <span class="w-[100px] flex-shrink-0 text-center">时间 </span>
      <span class="w-[100px] flex-shrink-0 text-center">得分 </span>
      <span class="w-[100px] flex-shrink-0 text-center">篮板 </span>
      <span class="w-[100px] flex-shrink-0 text-center">助攻 </span>
      <span class="w-[100px] flex-shrink-0 mr-[6px] text-center">投篮 </span>
    </div>
    <div
      class="flex ml-[24px] flex-col w-[702px] border-r-[1px] rounded-bl-[20px] rounded-br-[20px] border-r-[#27272A] border-b-[1px] border-b-[#27272A] border-l-[1px] border-l-[#27272A]"
    >
      <div
        v-for="(item, index) in list"
        :key="item.id"
        :class="`flex  items-center h-[110px]  number-text   text-[#D7D7E7] text-[24px] ${
          index % 2 == 0 ? 'bg-[#1D1D1D]' : 'bg-[#1F1F20]'
        } ${index == list.length - 1 ? 'rounded-bl-[16px] rounded-br-[16px]' : ''} `"
      >
        <span class="w-[188px] text-left flex items-center flex-shrink-0">
          <span
            :class="`flex mr-[16px] w-[38px] ml-[28px] flex-shrink-0 h-[50px] items-center justify-center mt-[-2px] text-[#fff] text-[22px] ${
              !item.data.first ? 'first-shirt' : 'second-shirt'
            }`"
            >{{ item.number }}</span
          >
          <span class="line-clamp-2 normal-text">{{ item.name }}</span></span
        >
        <span class="w-[100px] flex-shrink-0 text-center">{{ item.data.time ? item.data.time + "'" : '0' }} </span>
        <span class="w-[100px] flex-shrink-0 text-center">{{ item.data.score || '0' }} </span>
        <span class="w-[102px] flex-shrink-0 text-center">{{ item.data.rebound || '0' }} </span>
        <span class="w-[92px] flex-shrink-0 text-center">{{ item.data.assists || '0' }} </span>
        <span class="w-[113px] flex-shrink-0 text-center">{{ item.data.hitAndShot || '0' }} </span></div
      >
    </div>
  </div>
</template>

<script setup>
  defineProps({
    list: {
      type: Array,
    },
  });
</script>
<style scoped lang="scss">
  .item-b-line {
    border-top: 1px solid rgba(255, 255, 255, 0.08);
  }
  .number-text {
    font-family: MiSans;
  }
  .normal-text {
    font-family: PingFang SC;
  }
  .first-shirt {
    width: 38px;
    height: 50px;
    background: url('../../../../../assets/img/room/lineup/first-shirt.png') no-repeat center;
    background-size: contain;
  }
  .second-shirt {
    width: 38px;
    height: 50px;
    background: url('../../../../../assets/img/room/lineup/second-shirt.png') no-repeat center;
    background-size: contain;
  }
</style>
