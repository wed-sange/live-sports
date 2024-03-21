<template>
  <div :class="`text-[22px] mt-[2px] ml-[20px] text-[#F69521] num-status`"
    >{{ state.showTime }}<span v-if="state.showTime" class="num-dot">'</span>
  </div>
</template>

<script setup>
  const basketTimes = {
    2: '第一节',
    3: '第一节',
    4: '第二节',
    5: '第二节',
    6: '第三节',
    7: '第三节',
    8: '第四节',
    9: '加时',
    10: '',
  };
  const props = defineProps({
    matchData: {
      type: Object,
      required: true,
      default: null,
    },
  });
  const state = reactive({
    matchData: props.matchData,
    showTime: computed(() => {
      const { matchType, status, openTime, matchTime, runTime } = props.matchData;
      if (status == 1) {
        return '';
      }
      if (matchType == 1) {
        if (status == 8) {
          return '';
        }
        const startTime = parseFloat((new Date().getTime() - parseFloat(openTime || matchTime)) / 60000);
        return +(runTime || timeProcess(startTime, status));
        //足球
      } else {
        return basketTimes[status] || '';
      }
    }),
    // color: computed(() => {
    //   const { status, matchType } = props.matchData;
    //   if (status == 1) {
    //     return statusColor.notStart;
    //   } else if (matchType == 1) {
    //     if (status == 8) {
    //       return statusColor.end;
    //     } else if (status >= 2 && status < 8) {
    //       return statusColor.gaming;
    //     }
    //     return statusColor.notStart;
    //   } else {
    //     if (status == 10) {
    //       return statusColor.end;
    //     } else if (status >= 2 && status < 10) {
    //       return statusColor.gaming;
    //     }
    //     return statusColor.notStart;
    //   }
    // }),
  });
  const timeProcess = (time, statusId) => {
    if (!time) return time;
    time = parseInt(time);
    if (statusId == 2) return time >= 45 ? 45 : time;
    if (statusId == 4) return time >= 90 ? 90 : time;
    return time;
  };
</script>
<style scoped>
  .num-status {
    font-family: 'MiSans';
    line-height: normal;
  }

  .num-dot {
    animation: sportsStateOpacity 1.5s linear infinite;
  }
</style>
