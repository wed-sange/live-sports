<template>
  <div v-if="state.lineUpResult" class="pb-[72px] mt-[24px] ml-[24px]">
    <div v-if="!state.lineUpResult.notHasLineUp" class="flex flex-col pr-[24px] justify-center">
      <div class="relative h-[1260px] line-up-bk mb-[40px]">
        <LineCoach :data="state.lineUpResult.awayTeam" />
        <LineCoach :data="state.lineUpResult.homeTeam" :isHome="true" />
        <PlayerItem v-for="item in state.lineUpResult.homeFirstList" :key="item.number" :player="item" :isHome="true" />
        <PlayerItem v-for="item in state.lineUpResult.awayFirstList" :key="item.number" :player="item" />
        <div class="flex justify-center w-[702px] items-center self-center">
          <span class="truncate text-center max-w-[148px] mt-[617px] ml-[1px] text-[20px] center-text">
            {{ state.lineUpResult.refereeName || '暂无裁判信息' }}
          </span>
        </div>
      </div>
    </div>
    <div v-else class="flex w-full mb-[40px]">
      <LineUpTable name="首发阵容" :item="state.first" :isFirst="true" :matchName="matchName" />
    </div>
    <div class="flex w-full" v-if="state.lineUpResult.replace && state.lineUpResult.replace.length > 0">
      <LineUpTable name="替补阵容" :item="state.replace" :matchName="matchName" />
    </div>
  </div>
  <div v-else class="h-screen">
    <!-- <DefaultLoading /> -->
  </div>
</template>

<script setup>
  import { reactive, onMounted, computed } from 'vue';
  import apis from '@/api/room';
  import { createLoading, closeLoading } from '@/components/SysLoading';

  const LineCoach = defineAsyncComponent(() => import('./LineCoach.vue'));
  const PlayerItem = defineAsyncComponent(() => import('./PlayerItem.vue'));
  const LineUpTable = defineAsyncComponent(() => import('./LineUpTable.vue'));

  const props = defineProps({
    matchId: {
      type: String,
    },
    matchType: {
      type: String,
    },
    matchName: {
      type: Object,
    },
  });

  const state = reactive({
    lineUpResult: null,
    first: computed(() => {
      const homeAwayData = getHomeAway();
      const dataList = getDataList(1);
      return { ...dataList, ...homeAwayData };
    }),
    replace: computed(() => {
      const homeAwayData = getHomeAway();
      const dataList = getDataList(0);
      return { ...dataList, ...homeAwayData };
    }),
    showDetail: false,
  });
  //{ F: '前锋', M: '中场', D: '后卫', G: '守门员' }
  // const positionY = { F: 13, M: 9.2, D: 5.6, G: 2 };
  const getDataList = (first) => {
    if (!state.lineUpResult) {
      return {};
    }
    const { homeTeam, awayTeam } = state.lineUpResult;
    const homeList = homeTeam.list.filter((item) => item.first == first);
    const awayList = awayTeam.list.filter((item) => item.first == first);
    return { homeList, awayList };
  };
  const getHomeAway = () => {
    if (!state.lineUpResult) {
      return {};
    }
    const { homeTeam, awayTeam } = state.lineUpResult;
    return { homeTeam, awayTeam };
  };
  onMounted(async () => {
    createLoading({ overlay: false });
    getLineupData();
  });
  // const calX = (item, isHome) => {
  //   let x = parseFloat(item.x) / 4.1;
  //   if (isHome) {
  //     x = x - 2;
  //   } else {
  //     x = 10 - x + 12.2;
  //   }
  //   return x * 4 + 'vw';
  // };

  // const calY = (item, isHome) => {
  //   let y = positionY[item.position];
  //   if (!isHome) {
  //     y = 10 - y + 29.5;
  //   }
  //   // y = parseInt(item.y);
  //   // if (isHome) {
  //   //   y = parseInt(item.y) / 6;
  //   //   y = y;
  //   // } else {
  //   //   y = parseInt(item.y) / 6;
  //   //   y = 10 - y + 29;
  //   // }
  //   // y = parseInt(y);
  //   return y * 4 + 'vw';
  // };
  const calX = (player, isHome) => {
    let x = parseFloat(player.x) / 4.6;
    if (!isHome) {
      x = 10 - x + 10.4;
    } else {
      x = x - 1.75;
    }
    return x * 16 + 'px';
  };
  const calY = (player, isHome) => {
    let y = 0;
    y = parseFloat(player.y);
    if (!isHome) {
      y = parseFloat(player.y) / 6.2;
      y = 13 - y + 23.5;
    } else {
      y = parseFloat(player.y) / 6.2;
      y = y - 0.06;
    }
    return y * 16 + 'px';
  };
  const getLineupData = () => {
    const { matchId, matchType } = props;
    if (matchType == 1) {
      apis
        .getFootballLineup(matchId)
        .then((res) => {
          let lineUpResult = res;
          const {
            home,
            away,
            homeFormation,
            awayFormation,
            homeMarketValue,
            homeMarketValueCurrency,
            awayMarketValue,
            awayMarketValueCurrency,
            awayLogo,
            homeLogo,
          } = res;
          const fisrtList = [...home, ...away];
          lineUpResult.notHasLineUp = fisrtList && fisrtList.every((item) => item.x == '0' && item.y == '0');
          let homeTeam = {
            logo: homeLogo,
            list: home,
            formation: homeFormation,
            marketValue: homeMarketValue,
            marketValueCurrency: homeMarketValueCurrency,
          };
          let awayTeam = {
            logo: awayLogo,
            list: away,
            formation: awayFormation,
            marketValue: awayMarketValue,
            marketValueCurrency: awayMarketValueCurrency,
          };
          lineUpResult.homeTeam = homeTeam;
          lineUpResult.awayTeam = awayTeam;
          let replace = fisrtList.filter((item) => !item.first);
          lineUpResult.replace = replace;
          if (!lineUpResult.notHasLineUp) {
            let homeFirstList = [];
            lineUpResult.home.map((data) => {
              if (data.first) {
                homeFirstList.push({ ...data, locationY: calY(data, true), locationX: calX(data, true) });
              }
            });
            let awayFirstList = [];
            lineUpResult.away.map((data) => {
              if (data.first) {
                awayFirstList.push({ ...data, locationY: calY(data), locationX: calX(data) });
              }
            });
            lineUpResult.homeFirstList = homeFirstList;
            lineUpResult.awayFirstList = awayFirstList;
          }
          state.lineUpResult = Object.assign(lineUpResult);
          closeLoading();
        })
        .catch(() => {
          closeLoading();
        });
    }
  };
  const updateMsg = () => {};

  defineExpose({ instanceName: 'lineup', updateMsg });
</script>
<style scoped>
  .center-text {
    color: rgba(255, 255, 255, 0.2);
  }

  .line-up-bk {
    background: no-repeat url('@/assets/img/room/lineup/lineup-bk.png');
    background-size: 100% 100%;
  }

  .judge-item {
    border-radius: 0.81rem 0.81rem 0.81rem 0.81rem;
  }
</style>
