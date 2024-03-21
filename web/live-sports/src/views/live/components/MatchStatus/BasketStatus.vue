<template>
  <div class="flex flex-col text-[#F5F5F5] w-[702px] text-[24px] pb-[52px] mt-[24px] ml-[24px]">
    <div class="bg-[#181819] w-[702px] rounded-[20px] flex">
      <div class="flex-col flex w-[248px] text-[24px] text-white">
        <span class="flex-shrink-0 pt-[1px] pl-[30px] bg-[#1F1F20] rounded-tl-[20px] w-full h-[74px] flex text-[#94999F] items-center"
          >球队</span
        >
        <span class="w-[248px] flex-shrink-0 pl-[28px] flex items-center h-[88px] border-l-[#27272A] border-l-[1px]">
          <span class="w-[196px] truncate pt-[3px]">
            {{ matchName.awayName }}
          </span>
        </span>
        <span
          class="w-[248px] pl-[28px] flex items-center h-[76px] border-b-[#27272A] border-b-[1px] rounded-bl-[20px] border-l-[1px]] border-l-[#27272A] border-l-[1px]"
        >
          <span class="w-[196px] truncate mt-[-14px]">
            {{ matchName.homeName }}
          </span>
        </span>
      </div>
      <div class="flex flex-col flex-nowrap flex-grow text-[24px] text-[#D7D7E7] font-medium">
        <div class="flex h-[74px] items-center pr-[29px] bg-[#1F1F20] text-[#94999F] text-[24px] font-normal">
          <div class="flex-grow flex justify-center" v-for="item in timeList" :key="item">
            <span class="flex items-center justify-center pt-[1px]">{{ item }}</span>
          </div>
        </div>
        <div class="h-[84px] flex-grow flex items-center justify-around pr-[29px]">
          <div
            :class="`text-[#f5f5f5] flex justify-center number-text  h-[84px]  `"
            v-for="(item, index) in bsScore.awayScoreList"
            :key="index"
          >
            <span class="w-[50px] pt-[3px] flex items-center justify-center"> {{ showScoreData(item, index, bsScore.status) }}</span>
          </div>
        </div>
        <div class="h-[80px] flex items-center flex-grow border-b-[#27272A] border-b-[1px] pr-[29px]">
          <div
            :class="`mt-[-12px] flex-grow  number-text  flex justify-center text-[#f5f5f5]   h-[84px]   `"
            v-for="(item, index) in bsScore.homeScoreList"
            :key="index"
          >
            <span class="w-[50px] pt-[3px] flex items-center justify-center">{{ showScoreData(item, index, bsScore.status) }}</span>
          </div>
        </div>
      </div>
      <div class="flex flex-col flex-shrink-0 rounded-[20px] w-[80px]">
        <span
          class="h-[74px] pt-[1px] pr-[28px] rounded-tr-[20px] bg-[#1F1F20] flex items-center justify-center text-[#94999F] text-[24px] font-normal"
          >总分</span
        >
        <div class="h-[86px] pr-[22px] number-text flex items-center justify-center border-r-[#27272A] border-r-[1px]">
          {{ getTotal(1) }}
        </div>
        <span
          class="h-[78px] pr-[22px] pb-[14px] w-[80px] number-text flex items-center justify-center border-b-[#27272A] border-b-[1px] border-r-[#27272A] rounded-br-[20px] border-r-[1px]"
          >{{ getTotal(0) }}</span
        >
      </div>
    </div>
    <div class="bg-[#1F1F20] rounded-[20px] mt-[40px] p-[30px]">
      <div class="flex flex-col w-full">
        <span class="text-[#D7D7E7] text-[24px] mt-[2px] self-center">2分球</span>
        <div class="flex items-center w-full">
          <span class="w-[60px] flex justify-center mt-[6px] text-[28px] font-medium text-white number-text">{{
            getTypeData('hit2', 'away', 2) || 0
          }}</span>
          <div class="w-[472px] flex-shrink-0 h-[16px] mt-[2px] bg-[#323235] rounded-[200px] relative mx-[25px]">
            <span
              class="rounded-[200px] absolute left-0 h-[16px] bg-[#34A853]"
              :style="{ width: (getTypeDataRate('shot2', 'away', 'home') || 50) + '%' }"
            ></span>
          </div>
          <span class="w-[38px] text-right text-[28px] font-medium text-white ml-[7px] mt-[7px] number-text">{{
            getTypeData('hit2', 'home', 2) || 0
          }}</span>
        </div>
        <span class="text-[#D7D7E7] text-[24px] self-center mt-[26px]">3分球</span>
        <div class="flex items-center w-full mt-[8px]">
          <span class="w-[60px] flex justify-center text-[28px] font-medium text-white number-text">{{
            getTypeData('hit3', 'away', 3) || 0
          }}</span>
          <div class="w-[472px] flex-shrink-0 h-[16px] mt-[-2px] bg-[#323235] rounded-[200px] relative mx-[25px]">
            <span
              class="rounded-[200px] absolute left-0 h-[16px] bg-[#34A853]"
              :style="{ width: (getTypeDataRate('shot3', 'away', 'home') || 50) + '%' }"
            ></span>
          </div>
          <span class="w-[38px] text-right text-[28px] font-medium text-white ml-[7px] number-text">{{
            getTypeData('hit3', 'home', 3) || 0
          }}</span>
        </div>

        <span class="text-[#D7D7E7] text-[24px] self-center mt-[27px]">罚球</span>
        <div class="flex items-center w-full mt-[7px]">
          <span class="w-[60px] flex justify-center text-[28px] font-medium text-white number-text">{{
            getTypeData('penaltyHit', 'away') || 0
          }}</span>
          <div class="w-[472px] mt-[-1px] flex-shrink-0 h-[16px] bg-[#323235] rounded-[200px] relative mx-[25px]">
            <span
              class="rounded-[200px] absolute left-0 h-[16px] bg-[#34A853]"
              :style="{ width: (getTypeDataRate('penalty', 'away', 'home') || 50) + '%' }"
            ></span>
          </div>
          <span class="w-[38px] text-right text-[28px] font-medium text-white ml-[7px] number-text">{{
            getTypeData('penaltyHit', 'home') || 0
          }}</span>
        </div>

        <div class="flex items-center w-full mt-[37px] text-[24px] text-[#D7D7E7]">
          <CircleItem
            :item-data="{
              title: '2分%',
              leftNum: getNewTypeData('hit2', 'shot2', 'away') || 0,
              rateNum: getNewTypeDataBsRate('hit2', 'shot2', 'away', 'home'),
              rightNum: getNewTypeData('hit2', 'shot2', 'home') || 0,
            }"
          />
          <div class="ml-[60px]">
            <CircleItem
              :item-data="{
                title: '3分%',
                leftNum: getNewTypeData('hit3', 'shot3', 'away') || 0,
                rateNum: getNewTypeDataBsRate('hit3', 'shot3', 'away', 'home'),
                rightNum: getNewTypeData('hit3', 'shot3', 'home') || 0,
              }"
            />
          </div>
          <div class="ml-[60px]">
            <CircleItem
              :item-data="{
                title: '罚球%',
                leftNum: getNewTypeData('penaltyHit', 'penalty', 'away') || 0,
                rateNum: getNewTypeDataBsRate('penaltyHit', 'penalty', 'away', 'home'),
                rightNum: getNewTypeData('penaltyHit', 'penalty', 'home') || 0,
              }"
            />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
  import apis from '@/api/room';
  import { useMessageStore } from '@/store/modules/message';
  import { MessageCommand } from '@/views/message/utils/types';
  import { useAppStore } from '@/store/modules/app';

  const appStore = useAppStore();
  const CircleItem = defineAsyncComponent(() => import('./CircleItem.vue'));
  const messageStore = useMessageStore();
  const props = defineProps({
    matchId: {
      type: String,
      required: true,
      default: () => '',
    },
    matchName: {
      type: Object,
      required: true,
      default: () => ({}),
    },
  });
  const statics = ref({});
  const bsScore = ref({});
  const timeList = ref(['一', '二', '三', '四']);
  onMounted(() => {
    onLoadFirst();
    onWsListen();
  });
  onUnmounted(() => {
    onDestroyMsg();
  });
  onDeactivated(() => {
    if (appStore.clearRoom) {
      onDestroyMsg();
    }
  });
  const onDestroyMsg = () => {
    const messageServer = messageStore.getMessageServer();
    if (messageServer) {
      messageServer.chatEmitter.off('messageAll', onRefreshData);
    }
  };
  const onWsListen = () => {
    const onCheck = () => {
      const messageServer = messageStore.getMessageServer();
      if (!messageServer) {
        setTimeout(() => {
          onCheck();
        }, 1000);
        return;
      }
      messageServer.chatEmitter.on('messageAll', onRefreshData);
    };
    onCheck();
  };
  const onRefreshData = (res) => {
    const { data } = res;
    if (res.command === MessageCommand.matchChange && data && data.length > 0) {
      const findItem = data.find((item) => item.matchId == props.matchId);
      if (findItem) {
        onLoadFirst();
      }
    }
  };
  const onLoadFirst = () => {
    const { matchId } = props;
    getBsStats(matchId);
    getMatchBsScore(matchId);
  };
  const getBsStats = (matchId) => {
    apis.getMatchBasketStats(matchId).then((res) => {
      statics.value = res || {};
    });
  };
  const getMatchBsScore = (matchId) => {
    apis.getMatchBasketballScore(matchId).then((res) => {
      let { awayScoreList, homeScoreList, awayOverTimeScoresList, homeOverTimeScoresList, status } = res;
      if (status == 9) {
        //加时赛
        if (!awayOverTimeScoresList) {
          awayOverTimeScoresList = [awayScoreList[4]];
        }
        if (!homeOverTimeScoresList) {
          homeOverTimeScoresList = [homeScoreList[4]];
        }
      } else {
        if (awayScoreList[4] && !awayOverTimeScoresList) {
          awayOverTimeScoresList = [awayScoreList[4]];
        }
        if (homeScoreList[4] && !homeOverTimeScoresList) {
          homeOverTimeScoresList = [homeScoreList[4]];
        }
      }
      if (homeScoreList && homeScoreList.length > 4) {
        homeScoreList = homeScoreList.slice(0, 4);
      }
      if (awayScoreList && awayScoreList.length > 4) {
        awayScoreList = awayScoreList.slice(0, 4);
      }
      timeList.value = ['一', '二', '三', '四'];
      awayOverTimeScoresList &&
        awayOverTimeScoresList.forEach((item, index) => {
          timeList.value.push('+' + (index + 1));
          awayScoreList.push(item);
        });
      homeOverTimeScoresList &&
        homeOverTimeScoresList.forEach((item) => {
          homeScoreList.push(item);
        });
      bsScore.value = { ...res, homeScoreList, awayScoreList };
    });
  };
  const getNewTypeData = (type, total, name) => {
    if (!statics.value) {
      return '';
    }
    return statics.value[name] && statics.value[name][type]
      ? parseInt(((statics.value[name][type] * 100) / statics.value[name][total]).toFixed(0))
      : 0;
  };
  const getTypeData = (type, name, scoreType = 1) => {
    if (!statics.value) {
      return '';
    }
    return statics.value[name] && statics.value[name][type] ? parseFloat(statics.value[name][type]) * scoreType : 0;
  };
  const getNewTypeDataBsRate = (type, total, teamName1, teamName2) => {
    const homeHit = getNewTypeData(type, total, teamName1);
    const awayHit = getNewTypeData(type, total, teamName2);
    const rate = (homeHit * 100) / (homeHit + awayHit);
    return rate == 0 ? 0 : rate || 50;
  };
  const getTypeDataRate = (type, teamName1, teamName2, isLimit) => {
    const awayRate = getNewTypeData(type, teamName2);
    const homeRate = getNewTypeData(type, teamName1);
    if (awayRate + homeRate == 0) {
      return isLimit ? 50 : 0;
    }
    const rateValue = (homeRate * 100) / (awayRate + homeRate);
    if (isLimit) {
      return rateValue > 80 ? 80 : rateValue < 20 ? 20 : rateValue;
    }
    return (homeRate * 100) / (awayRate + homeRate);
  };
  const showScoreData = (score, index, status) => {
    if (!score) {
      //第一节
      if (status == 2 || status == 3) {
        if (index > 0) {
          return '  -';
        }
      } else if (status == 4 || status == 5) {
        if (index > 1) {
          return '-';
        }
      } else if (status == 6 || status == 7) {
        if (index > 2) {
          return '-';
        }
      } else if (status == 8) {
        if (index > 3) {
          return '-';
        }
      }
    }
    return score;
  };
  const getTotal = (type) => {
    let total = 0;
    let { awayScoreList, homeScoreList } = bsScore.value;
    if (!type) {
      if (!homeScoreList) {
        return 0;
      }
      homeScoreList.forEach((item) => {
        total = total + item;
      });
      return total;
    }
    if (!awayScoreList) {
      return 0;
    }
    awayScoreList.forEach((item) => {
      total = total + item;
    });

    return total;
  };
</script>
<style scoped>
  .number-text {
    font-family: MiSans;
  }
</style>
