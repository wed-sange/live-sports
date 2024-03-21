<template>
  <div class="flex flex-col text-[#F5F5F5] w-[702px] text-[24px] pb-[52px] mt-[24px] ml-[24px] foot-data">
    <div class="flex flex-col">
      <div class="p-[30px] rounded-[20px] items-center flex flex-col w-full bg-[#1F1F20]">
        <div class="flex items-center w-full">
          <CircleItem
            :item-data="{
              title: '进攻',
              leftNum: getTypeData(23, 'home') || 0,
              rateNum: getTypeDataRate(23, 'home', 'away') || 50,
              rightNum: getTypeData(23, 'away') || 0,
            }"
          />
          <div class="ml-[60px]">
            <CircleItem
              :item-data="{
                title: '危险进攻',
                leftNum: getTypeData(24, 'home') || 0,
                rateNum: getTypeDataRate(24, 'home', 'away') || 50,
                rightNum: getTypeData(24, 'away') || 0,
              }"
            />
          </div>
          <div class="ml-[60px]">
            <CircleItem
              :item-data="{
                title: '控球率',
                leftNum: getTypeData(25, 'home') || 0,
                rateNum: getTypeDataRate(25, 'home', 'away') || 50,
                rightNum: getTypeData(25, 'away') || 0,
              }"
            />
          </div>
        </div>
        <span class="text-[#D7D7E7] text-[24px] self-center mt-[32px]">射正球门</span>
        <div class="flex items-center mt-[22px] text-[#f5f5f5] text-[20px]">
          <img :src="cornerIcon" class="w-[40px] h-[40px] flex-shrink-0" />
          <img :src="yellowIcon" class="w-[40px] h-[40px] flex-shrink-0 mx-[4px]" />
          <img :src="redIcon" class="w-[40px] h-[40px] flex-shrink-0" />
          <span class="w-[40px] flex-shrink-0 text-center ml-[4px] number-text">{{ getTypeData(21, 'home') }}</span>
          <div class="w-[278px] flex-shrink-0 h-[16px] bg-[#323235] rounded-[200px] mx-[10px] relative">
            <span
              class="rounded-[200px] absolute left-0 h-[16px] bg-[#34A853]"
              :style="{ width: getTypeDataRate(21, 'home', 'away', true) + '%' }"
            ></span>
          </div>
          <span class="w-[40px] flex-shrink-0 text-center ml-[4px] number-text">{{ getTypeData(21, 'away') }}</span>
          <img :src="redIcon" class="w-[40px] h-[40px]" />
          <img :src="yellowIcon" class="w-[40px] h-[40px] mx-[4px]" />
          <img :src="cornerIcon" class="w-[40px] h-[40px]" />
        </div>
        <span class="text-[#D7D7E7] text-[24px] self-center mt-[30px]">射偏球门</span>
        <div class="flex items-center text-[#94999F] text-[22px] mt-[20px] number-text">
          <span class="w-[40px] text-center mt-[0.5px]">{{ getTypeData(2, 'home') }}</span>
          <span class="w-[40px] text-center mt-[0.5px]">{{ getTypeData(3, 'home') }}</span>
          <span class="w-[40px] text-center mx-[4px] mt-[0.5px]">{{ getTypeData(4, 'home') }}</span>
          <span class="w-[40px] text-center mt-[0.5px]">{{ getTypeData(22, 'home') }}</span>
          <div class="w-[278px] flex-shrink-0 h-[16px] bg-[#323235] rounded-[200px] mx-[10px] relative">
            <span
              class="rounded-[200px] absolute left-[0.5px] h-[16px] bg-[#34A853]"
              :style="{ width: getTypeDataRate(22, 'home', 'away', true) + '%' }"
            ></span>
          </div>
          <span class="w-[40px] text-center mt-[0.5px]">{{ getTypeData(22, 'away') }}</span>
          <span class="w-[40px] mx-[4px] text-center mt-[0.5px]">{{ getTypeData(4, 'away') }}</span>
          <span class="w-[40px] text-center mt-[0.5px]">{{ getTypeData(3, 'away') }}</span>
          <span class="w-[40px] ml-[4px] text-center mt-[0.5px]">{{ getTypeData(2, 'away') }}</span>
        </div>
      </div>
      <div
        class="flex w-[400px] mt-[39px] mb-[30px] h-[84px] self-center items-center p-[12px] justify-center bg-[#1F1F20] rounded-[52px] text-[26px]"
      >
        <span
          @click="onChangeTab(true)"
          :class="`w-[180px] h-[60px] click-style flex items-center justify-center ${
            active ? 'bg-[#323235] rounded-[48px] text-white font-medium' : ' text-[#94999F]'
          } `"
          >文字直播</span
        >
        <span
          @click="onChangeTab(false)"
          :class="`w-[180px] click-style h-[60px] ml-[16px] flex items-center justify-center  ${
            !active ? 'bg-[#323235] rounded-[48px]  text-white font-medium' : ' text-[#94999F]'
          } rounded-[16px]`"
          >重要事件</span
        >
      </div>
      <div>
        <div v-if="active && textLives.length > 0" :class="`px-[30px] py-[30px] flex flex-col  mb-[40px] bg-[#1F1F20] rounded-[20px]`">
          <div class="flex mb-[40px] items-center" v-for="(item, index) in textLives" :key="index">
            <div class="w-[40px] h-[40px] flex-shrink-0">
              <img v-if="getResIcon(item.type)" :class="`w-[40px] mt-[0.5px] h-[40px] bg-contain`" :src="getResIcon(item.type)" />
            </div>
            <span
              :class="`${
                index != 0 ? '' : ' '
              } ml-[20px] flex-1 break-words mt-[1px]  flex items-center min-[40px] whitespace-pre-wrap text-[24px] leading-[38px] text-[#f5f5f5]`"
              >{{ item.data }}</span
            >
            <img
              v-if="item.position"
              class="max-w-[40px] max-h-[40px] flex-shrink-0"
              :src="(item.position === 1 ? matchName.homeLogo : matchName.awayLogo) || defaultTeamLogo"
            />
          </div>
        </div>

        <img v-if="active" :src="eventBk" class="h-[310px] w-[702px] bg-contain mt-[40px]" />

        <div
          v-else
          :class="`w-[702px] pt-[42px] pb-[36px] ${
            incidents.length > 0 ? 'bg-[#1F1F20]' : ''
          }  flex flex-col rounded-[20px] mb-[46px] text-[#f5f5f5] text-[20px]`"
        >
          <div class="flex items-center justify-center relative" v-for="(item, index) in incidents" :key="index">
            <event-card-left
              v-if="item.position <= 1 && item.type != 12"
              :type="item.type"
              :icon="resIcons[item.type]"
              :data="item"
              :reverse="true"
            />
            <div class="flex flex-col items-center">
              <img v-if="item.type == 10" class="w-[40px] h-[40px] bg-contain" :src="startIcon" />
              <span
                v-else-if="item.type == 12"
                class="event-level bg-[#242427] rounded-[40px] text-white text-[24px] py-[4px] px-[16px] font-normal flex items-center justify-center min-h-[32px]"
              >
                结束<span class="number-text">{{
                  ((incidents[index + 1] && incidents[index + 1].homeScore) || 0) +
                  '-' +
                  ((incidents[index + 1] && incidents[index + 1].awayScore) || 0)
                }}</span></span
              >
              <span
                v-else-if="item.type != 11"
                class="w-[72px] text-[#94999F] number-text text-[24px] font-medium flex items-center justify-center h-[72px] rounded-full event-time"
                >{{ item.time }}’</span
              >
              <span
                v-else
                class="event-level bg-[#242427] number-text rounded-[40px] text-white text-[24px] py-[4px] px-[16px] font-normal flex items-center justify-center min-h-[32px]"
              >
                中场<span class="number-text">{{ (item.homeScore || 0) + '-' + (item.awayScore || 0) }}</span></span
              >
              <span
                v-show="item.type != 10"
                :class="`w-[1px]  line-item ${
                  item.type == 12 || (incidents[index + 1] && incidents[index + 1].type == 10) ? 'h-[50px]' : 'h-[80px]'
                }`"
              ></span>
            </div>
            <event-card-right v-if="item.position === 2 && item.type != 12" :type="item.type" :data="item" :icon="resIcons[item.type]" />
          </div>

          <no-data
            v-if="(incidents.length == 0 && !active) || (active && textLives.length == 0)"
            class="mt-[64px]"
            icon="data_black"
            :grayText="true"
            text="暂无数据"
          /> </div
      ></div>
    </div>
  </div>
</template>

<script setup>
  import apis from '@/api/room';
  import { useMessageStore } from '@/store/modules/message';
  import { MessageCommand } from '@/views/message/utils/types';
  import { useAppStore } from '@/store/modules/app';

  import defaultTeamLogo from '@/assets/img/common/default-footer-icon.png';
  import eventBk from '@/assets/img/room/events-intro.png';
  import startIcon from '@/assets/img/room/event/0.png';
  import goal from '@/assets/img/room/event/1.png';
  import goalRight from '@/assets/img/room/event/2.png';
  import goalLose from '@/assets/img/room/event/3.png';
  import goalPk from '@/assets/img/room/event/4.png';
  import goalFail from '@/assets/img/room/event/5.png';
  import wulongIcon from '@/assets/img/room/event/6.png';
  import assistIcon from '@/assets/img/room/event/7.png';
  import redIcon from '@/assets/img/room/event/8.png';
  import cornerIcon from '@/assets/img/room/event/9.png';
  import yellowIcon from '@/assets/img/room/event/10.png';
  import changeIcon from '@/assets/img/room/event/11.png';
  import vRIcon from '@/assets/img/room/event/12.png';
  import redYellowIcon from '@/assets/img/room/event/13.png';
  import defaultIcon from '@/assets/img/room/event/default-icon.png';

  const appStore = useAppStore();
  const resIcons = {
    1: goal,
    2: cornerIcon,
    8: goalPk,
    15: redYellowIcon,
    18: assistIcon,
    9: changeIcon,
    16: goalFail,
    17: wulongIcon,
    21: goalRight,
    22: goalLose,
    3: yellowIcon,
    4: redIcon,
    10: startIcon,
    28: vRIcon,
    12: startIcon,
  };
  const EventCardRight = defineAsyncComponent(() => import('./EventCardRight.vue'));
  const EventCardLeft = defineAsyncComponent(() => import('./EventCardLeft.vue'));
  const CircleItem = defineAsyncComponent(() => import('./CircleItem.vue'));
  const noData = defineAsyncComponent(() => import('@/components/common/EmptyData.vue'));
  // 21-射正 22-射偏 23-进攻 24-危险进攻 25-控球率 1-进球 2-角球 3-黄牌 4-红牌
  const statics = ref({});
  const active = ref(true);
  const textLives = ref([]);
  const incidents = ref([]);
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
  onMounted(() => {
    onLoadFirst();
    onWsListen();
  });
  onDeactivated(() => {
    if (appStore.clearRoom) {
      onDestroyMsg();
    }
  });
  onUnmounted(() => {
    onDestroyMsg();
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
    apis.getMatchFootStats(props.matchId).then((res) => {
      formatStaticData(res);
    });
    getLiveIncidents(props.matchId);
  };
  const onChangeTab = (value) => {
    active.value = value;
  };
  const getResIcon = (type) => {
    return resIcons[type] || defaultIcon;
  };
  const getLiveIncidents = (matchId) => {
    apis.getMatchFootText(matchId).then((res) => {
      textLives.value = res;
    });
    apis.getMatchFootIncidents(matchId).then((res) => {
      if (res && res.length > 0) {
        const firstItem = { time: 0, type: 10 };
        const dataList = res && res.filter((item) => [1, 3, 4, 9, 10, 11, 12, 15].find((itemData) => itemData == item.type));
        const findStart = dataList && dataList.find((item) => item.type == 10);
        if (!findStart) {
          incidents.value = dataList.push(firstItem);
        }
        incidents.value = [...dataList];
      }
    });
  };
  const getTypeDataRate = (type, teamName1, teamName2, isLimit) => {
    const awayRate = getTypeData(type, teamName2);
    const homeRate = getTypeData(type, teamName1);
    if (awayRate + homeRate == 0) {
      return isLimit ? 50 : 0;
    }
    const rateValue = (homeRate * 100) / (awayRate + homeRate);
    if (isLimit) {
      return rateValue > 80 ? 80 : rateValue < 20 ? 20 : rateValue;
    }
    return (homeRate * 100) / (awayRate + homeRate);
  };

  const getTypeData = (type, name) => {
    return statics.value[type] && statics.value[type][name] ? statics.value[type][name] : 0;
  };
  const formatStaticData = (res) => {
    res &&
      res.forEach((item) => {
        const { away, home, type } = item;
        statics.value[type] = { away, home };
      });
  };
</script>
<style scoped lang="scss">
  .foot-data {
    font-family: PingFang SC;

    .event-time {
      background: #242427;
      filter: drop-shadow(0px 5px 8.8px rgba(16, 16, 16, 0.55));
    }
    .event-level {
      /* 黑底投影 */
      box-shadow: 0px 5px 8.8px 0px rgba(16, 16, 16, 0.55);
    }
    .number-text {
      font-family: MiSans;
    }
    .van-tab__panel {
      background: #07061d;
    }

    .line-item {
      background: #27272a;
    }
  }
</style>
