<template>
  <div
    v-ripple
    @click="$router.push({ path: '/roomDetail', query: { id: data.matchId, liveId: data.id, type: data.matchType, userId: data.userId } })"
    class="flex flex-col rounded-[16px] w-[344px] h-[286px]"
    :key="data.userId"
  >
    <div class="relative">
      <UseImg class="w-full h-[184px] rounded-tl-[16px] rounded-tr-[16px]" :src="data.titlePage" type="match" />
      <span class="w-full absolute bottom-0 h-[72px] filter-item-living z-1"></span>
      <div class="absolute bottom-[10px] w-full justify-between text-[22px] text-[#fff] flex items-center z-2">
        <span class="ml-[16px]">{{ data.competitionName }}</span>
        <div class="flex items-center mr-[16px]">
          <img class="w-[16px] h-[18px] bg-contain" :src="hotIcon" />
          <div class="ml-[4px] number-text">{{ onFormat(data.hotValue) }}</div>
        </div>
      </div>
    </div>
    <div :class="`px-[16px] flex text-[24px] rounded-bl-[16px] rounded-br-[16px] bg-[${bg}]`">
      <DefaultImg
        class="w-[64px] h-[65px] mr-[10px] rounded-[50%] mt-[19px] bg-cover"
        type="default"
        :src="data.userLogo || defaultAvatar"
      />
      <div class="flex flex-col mt-[18px] mb-[16px]">
        <span :class="`text-[${textColor}] max-w-[240px] truncate`"> {{ data.homeTeamName }}VS{{ data.awayTeamName }} </span>
        <span class="text-[22px] text-[#94999F] mt-[5px]">
          {{ data.nickName }}
          <!-- 王叔叔 -->
        </span>
      </div>
    </div>
  </div>
</template>
<script setup>
  import { formateNum } from '@/utils/common';
  import DefaultImg from '@/components/common/DefaultImg.vue';
  import hotIcon from '@/assets/img/home/hot-white.png';
  import defaultAvatar from '@/assets/img/common/default-anchor-icon.png';
  import UseImg from '@/components/common/UseImg.vue';
  defineProps({
    data: {
      type: Object,
    },
    bg: {
      type: String,
      default: '#fff',
    },
    textColor: {
      type: String,
      default: '#37373D',
    },
  });
  const onFormat = (num) => {
    return formateNum(num);
  };
</script>
<style scoped>
  .filter-item-living {
    background: linear-gradient(180deg, rgba(0, 0, 0, 0) 13.89%, rgba(0, 0, 0, 0.6) 173.61%);
  }

  .number-text {
    font-family: MiSans;
  }
</style>
