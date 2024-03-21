<template>
  <van-popup v-model:show="showDialog" close-on-popstate round position="center" :close-on-click-overlay="false">
    <div class="flex flex-col justify-center items-center w-[540px] h-[273px]">
      <div
        class="text-black w-full text-center leading-[48px] break-words whitespace-pre h-[184px] flex justify-center items-center flex-col text-[30px] px-[32px]"
        >{{ reasons[reasonType] }}</div
      >
      <div
        @click="$emit('on-back')"
        class="h-[88px] w-full border-t-[1px] border-t-[#E8EAEF] flex items-center justify-center text-[30px] text-[#34A853]"
      >
        确定
      </div>
    </div>
  </van-popup>
</template>
<script setup>
  import apis from '@/api/room';
  import { useUserStore } from '@/store/modules/user';
  const userStore = useUserStore();
  const props = defineProps({
    anchorId: {
      type: String,
    },
  });
  const emit = defineEmits(['is-muted', 'on-back']);
  const showDialog = ref(false);
  const reasons = ['你已被主播移出直播间', '暂时无法观看该直播间内容\n请稍后重试'];
  const reasonType = ref(1);
  onMounted(() => {
    judgeIsMuted();
  });
  const judgeIsMuted = () => {
    const { anchorId } = props;
    if (!anchorId || !userStore.token) {
      return;
    }
    apis.getAnchorControlUserInfo(anchorId).then((res) => {
      const { tickOut, mute, tickOutLeftTime } = res;
      showDialog.value = tickOut;
      reasonType.value = 1;
      if (tickOut && tickOutLeftTime > 0) {
        let countTime = 0;
        judgeCountTime(tickOutLeftTime, countTime);
      }
      emit('is-muted', mute);
    });
  };
  //倒计时结束重新刷一下接口获取最新状态  停留在当前页面的倒计时
  const judgeCountTime = (tickOutLeftTime, countTime) => {
    setTimeout(() => {
      countTime++;
      if (countTime >= tickOutLeftTime) {
        judgeIsMuted();
        return;
      }
      judgeCountTime(tickOutLeftTime, countTime);
    }, 1000);
  };
  const onShowTickOut = () => {
    showDialog.value = true;
    reasonType.value = 0;
  };
  defineExpose({
    onShowTickOut,
  });
</script>
