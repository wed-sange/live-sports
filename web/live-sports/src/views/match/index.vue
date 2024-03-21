<template>
  <page-box :class="`flex flex-col text-black match-page style-1`">
    <template #header>
      <AppHeader />
    </template>
    <van-tabs
      v-model:active="active"
      background="white"
      title-active-color="#37373D"
      title-inactive-color="#94999F"
      line-height="0.8vw"
      line-width="6.133333vw"
      @change="onChangeTab"
      sticky
      shrink
      animated
      :swipeable="!appStore.isMatchScroll"
    >
      <!-- swipeable -->
      <van-tab :title="item.title" v-for="(item,index) in (tabList as any)" :key="item.style">
        <keep-alive>
          <component v-show="judgeShowComponent(index)" :itemData="item" :is="item.component" />
        </keep-alive>
      </van-tab>
    </van-tabs>
    <van-action-sheet @click-overlay="onClickOverlay" v-model:show="showCalendar" safe-area-inset-bottom>
      <div class="flex flex-col h-[772px] z-50 px-[24px]" v-pressed-slide="handlePressedSlide">
        <van-date-picker
          :show-toolbar="false"
          @close="onCloseCalendar"
          @confirm="onConfirm"
          @change="onChangeDate"
          :formatter="onFormatter"
          v-model="callendarTime"
          :min-date="minDate"
          :max-date="maxDate"
          :visible-option-num="5"
          :z-index="99999"
        >
          <template #columns-top>
            <div class="flex flex-col pb-[30px]">
              <div class="action-sheet-bar mb-[30px]"></div>
              <div class="flex justify-center w-full text-[#37373D] text-[30px] font-medium">
                <span>选择日期</span>
                <img
                  @click="onCloseCalendar"
                  :src="calendarIcon"
                  class="w-[40px] click-style h-[40px] bg-contain absolute right-0 top-[60px]"
                />
              </div>
              <div class="flex items-center mt-[25px] py-[16px] text-[28px] border-t-[0.5px] border-b-[0.5px] border-[#E8EAEF]">
                <span class="flex-grow flex items-center justify-center ml-[-15px]">年份</span>
                <span class="flex-grow flex items-center justify-center ml-[-12px]">月份</span>
                <span class="flex-grow flex items-center justify-center ml-[1px]">天</span>
              </div>
            </div>
          </template>
          <template #columns-bottom>
            <div class="flex items-center justify-center text-[#FFF] mt-[16px]">
              <div
                @click="onResetTime()"
                v-ripple
                class="w-[351px] h-[80px] bg-[#37373D] rounded-tl-[48px] rounded-bl-[48px] text-[30px] font-medium flex items-center justify-center"
              >
                重置
              </div>
              <div
                @click="onConfirm()"
                v-ripple
                class="w-[351px] h-[80px] bg-[#34A853] rounded-tr-[48px] rounded-br-[48px] text-[30px] font-medium flex items-center justify-center"
              >
                确定
              </div>
            </div>
          </template>
        </van-date-picker>
      </div>
    </van-action-sheet>
  </page-box>
</template>

<script lang="ts" setup name="Match">
  import { markRaw } from 'vue';
  import { useMatchStore } from '@/store/modules/match';
  import { useMessageStore } from '@/store/modules/message';
  import { useUserStore } from '@/store/modules/user';
  import { MessageCommand } from '@/views/message/utils/types';
  import dayjs from 'dayjs';
  import calendarIcon from '@/assets/img/match/calendar-close.png';
  import { pressedSlideDirective as vPressedSlide } from '@/plugins/pressedSlide';
  import { useAppStore } from '@/store/modules/app';

  const appStore = useAppStore();
  const Recommend = defineAsyncComponent(() => import('./component/Recommend.vue'));
  const Basketball = defineAsyncComponent(() => import('./component/Recommend.vue'));
  const Football = defineAsyncComponent(() => import('./component/Recommend.vue'));
  const Result = defineAsyncComponent(() => import('./component/Recommend.vue'));

  const minDate = new Date(1979, 1, 1);
  const maxDate = new Date(2099, 12, 31);
  const messageStore = useMessageStore();
  // const tabRef = ref();
  const userStore = useUserStore();
  const matchStore = useMatchStore();
  const showCalendar = computed(() => matchStore.showCalendar);
  const active = ref(0);
  let timer;

  const callendarTime = ref([dayjs().format('YYYY'), dayjs().format('MM'), dayjs().format('DD')]);
  const tabList = ref([
    {
      title: '推荐',
      style: 0,
      req: { matchType: '', competitionId: '' },
      component: markRaw(Recommend),
      defaultTabs: [{ matchType: '', competitionName: '全部', competitionId: '' }],
    },
    {
      title: '足球',
      style: 1,
      component: markRaw(Football),
      req: {
        matchType: 1,
        competitionId: '',
      },
      defaultTabs: [{ matchType: 1, competitionName: '全部', competitionId: '' }],
    },
    {
      title: '篮球',
      style: 2,
      component: markRaw(Basketball),
      defaultTabs: [{ matchType: 2, competitionName: '全部', competitionId: '' }],
      req: {
        matchType: 2,
        competitionId: '',
      },
    },
    {
      title: '赛果',
      style: 3,
      component: markRaw(Result),
      defaultTabs: [
        { matchType: 1, competitionName: '足球比分', competitionId: '' },
        { matchType: 2, competitionName: '篮球比分', competitionId: '' },
      ],
      req: {
        status: 99,
        matchType: 1,
        sortType: 1,
      },
    },
  ]);
  onMounted(async () => {
    setTimeout(() => {
      onChangeTab(0);
      onWsListen();
    }, 0);
  });
  const judgeShowComponent = (index) => {
    const currentIndex = active.value;
    if (index == currentIndex || index == currentIndex + 1 || index == currentIndex - 1) {
      return true;
    }
    return false;
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
      messageServer.chatEmitter.on('messageAll', (res) => {
        const onWsDataChange = (command, data) => {
          if (command === MessageCommand.matchChange) {
            matchStore.onDataChange(data);
          } else if (command === MessageCommand.openLive) {
            matchStore.onAddAnchor(data);
          } else if (command === MessageCommand.closeLive) {
            matchStore.onRemoveAnchor(res.data);
          }
        };
        onWsDataChange(res.command, res.data);
      });
    };
    onCheck();
  };
  watch(
    () => matchStore.selectDate,
    () => {
      const formatDate = matchStore.selectDate || dayjs().format('YYYY-MM-DD');
      callendarTime.value = [dayjs(formatDate).format('YYYY'), dayjs(formatDate).format('MM'), dayjs(formatDate).format('DD')];
    },
  );
  watch(
    () => userStore.token,
    () => {
      //大刷新所有数据
      matchStore.onAllRefreshData();
    },
    {
      deep: true,
    },
  );
  const onClickOverlay = () => {
    matchStore.onShowOrHideCalendar(false);
  };
  const handlePressedSlide = (start, move) => {
    if (move.y - start.y >= 10) {
      matchStore.showCalendar = false;
    }
  };
  const onChangeDate = (item) => {
    const selectTime = item.selectedValues[0] + '/' + item.selectedValues[1] + '/' + item.selectedValues[2];
    const isResult = active.value == 3;
    const currentTime = dayjs().format('YYYYMMDD');
    const selectDate = dayjs(selectTime).valueOf();
    const beforeMonth = dayjs(currentTime).subtract(30, 'day').valueOf();
    const afterMonth = dayjs(currentTime).add(30, 'day').valueOf();
    const onSetCurrentDate = () => {
      callendarTime.value = [dayjs().format('YYYY'), dayjs().format('MM'), dayjs().format('DD')];
    };
    if (isResult) {
      //赛果规则
      if (selectDate < beforeMonth) {
        setTimeout(() => {
          callendarTime.value = [
            dayjs().subtract(30, 'day').format('YYYY'),
            dayjs().subtract(30, 'day').format('MM'),
            dayjs().subtract(30, 'day').format('DD'),
          ];
        }, 400);
      } else if (selectDate > dayjs(currentTime).valueOf()) {
        setTimeout(onSetCurrentDate, 400);
      }
    } else {
      //赛程规则
      if (selectDate > afterMonth) {
        //大于最大时间 往后推30天取最大时间
        setTimeout(() => {
          callendarTime.value = [
            dayjs().add(31, 'day').format('YYYY'),
            dayjs().add(31, 'day').format('MM'),
            dayjs().add(31, 'day').format('DD'),
          ];
        }, 400);
      } else if (selectDate < dayjs(currentTime).valueOf()) {
        //小于今天取今天时间
        setTimeout(onSetCurrentDate, 400);
      }
    }
  };
  const onFormatter = (type, options) => {
    if (type === 'month') {
      options.text += '月';
    } else if (type === 'day') {
      options.text += '日';
    }
    return options;
  };
  const onResetTime = () => {
    callendarTime.value = [dayjs().format('YYYY'), dayjs().format('MM'), dayjs().format('DD')];
  };
  const onCloseCalendar = () => {
    matchStore.onShowOrHideCalendar(false);
  };
  const onGetTabs = () => {
    matchStore.clearTabAndList();
    const tabItem = tabList.value[active.value];
    const { req, defaultTabs, style } = tabItem;
    let startTime = dayjs(new Date()).format('YYYY-MM-DD');
    let endTime = dayjs(new Date()).format('YYYY-MM-DD');
    let competitionId = '';
    if (req.matchType == matchStore.twoTabReq.matchType && style == 3 && matchStore.twoTabReq.status == 9) {
      competitionId = matchStore.getCompetitionId() || '';
    }
    if (style == 3) {
      startTime = competitionId ? dayjs().subtract(2, 'day').format('YYYY-MM-DD') : startTime;
    } else {
      endTime = competitionId ? dayjs().add(2, 'day').format('YYYY-MM-DD') : startTime;
    }
    setTimeout(() => {
      matchStore.getTabList(req, defaultTabs, { startTime, endTime }, style);
    }, 0);
  };
  //大tab切换
  const onChangeTab = (index) => {
    active.value = index;
    const scrollTop = document.querySelector('.match-position')?.scrollTop;
    matchStore.setMatchPageScroll(scrollTop || 0, '');
    matchStore.clearData();
    if (timer) {
      clearTimeout(timer);
      timer = null;
    }
    timer = setTimeout(() => {
      onGetTabs();
    }, 50);
  };
  const onConfirm = () => {
    const end = callendarTime.value[0] + '-' + callendarTime.value[1] + '-' + callendarTime.value[2];
    let startTime = end;
    let endTime = end;
    const competitionId = matchStore.getCompetitionId();

    if (active.value == 3) {
      startTime = competitionId ? dayjs(end).subtract(2, 'day').format('YYYY-MM-DD') : endTime;
    } else {
      endTime = competitionId ? dayjs(end).add(2, 'day').format('YYYY-MM-DD') : startTime;
    }
    matchStore.onShowOrHideCalendar(false);
    matchStore.setReqTime({ startTime, endTime });
  };
</script>
<style lang="scss" scoped>
  .match-page {
    height: 100%;
    background-size: cover;
    --van-calendar-popup-height: 70%;

    :deep().van-ellipsis {
      color: #37373d;
      text-align: center;
      font-family: PingFang SC;
      font-size: 28px;
      font-style: normal;
      font-weight: 400;
      line-height: normal;
    }
    :deep().van-picker {
      border-top-right-radius: 32px;
      border-top-left-radius: 32px;
      z-index: 50;
    }
    // :deep().van-picker__columns {
    //   height: 500px !important;
    // }
    :deep().van-picker-column {
      z-index: 11;
    }
    :deep().van-picker-column__item--selected {
      height: 82px !important;
      .van-ellipsis {
        color: #34a853;
        text-align: center;
        font-family: PingFang SC;
        font-size: 30px;
        font-style: normal;
        font-weight: 500;
        line-height: normal;
        z-index: 11;
      }
    }
    :deep().van-hairline-unset--top-bottom:after {
      border-width: 0 !important;
      border-top: 1px solid #f4f4f5 !important;
      border-bottom: 1px solid #f4f4f5 !important;
      border-radius: 10px !important;
      background: #f4f4f5;
      opacity: 0.99;
    }
    :deep().van-list {
      padding-bottom: 86px;
    }

    :deep() .van-pull-refresh {
      // overflow: scroll;
    }
    .action-sheet-bar {
      padding: 28px 0 0;
      position: relative;
      &::before {
        content: '';
        display: block;
        width: 72px;
        height: 8px;
        background: #eff1f5;
        border-radius: 8px;
        position: absolute;
        top: 20px;
        left: 50%;
        z-index: 2;
        transform: translate(-50%, 0);
      }
    }
    font-family: PingFang SC;
    line-height: normal;
    font-weight: 400;
    :deep().van-tab {
      margin-right: 60px;
      justify-content: baseline;
      align-items: baseline;
    }
    :deep().van-tab:active {
      opacity: 0.2;
    }
    :deep().van-tab--shrink {
      padding: 0;
    }
    --van-padding-xs: 24px;

    :deep().van-tabs__line {
      border-radius: 80px;
      background: #34a853;
    }
    :deep().van-tab__text {
      margin-top: 23px;
      font-size: 32px;
      line-height: 36px;
    }
    :deep().van-tab--active {
      font-weight: 400;
      line-height: normal;
      .van-tab__text {
        font-size: 36px;
        margin-top: 15px;
        font-weight: 500;
        line-height: 50px;
        font-family: 'PingFang SC';
      }
    }
    // :deep().van-tabs__nav--line {
    //   padding-bottom: 4px;
    // }
    :deep().van-tabs__line {
      bottom: 46px;
      margin-left: 0.5px;
    }
    :deep().van-tabs__wrap {
      height: 88px;
      line-height: 88px;
      background: transparent;
    }
    :deep(.van-tabs) {
      height: 100%;
      display: flex;
      flex-flow: column;

      .van-tabs__content {
        flex: 1 1 auto;
        .van-tab__panel {
          height: 100%;
          background: #f2f3f7;
        }
      }
    }
  }
</style>
