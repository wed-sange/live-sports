<template>
  <div class="relative live-player h-[380px] max-h-[380px] overflow-hidden">
    <div class="w-full text-[28px] relative font-medium">
      <div v-show="showLiveControl" class="top-mask z-10 absolute top-0 left-0 w-full h-[152px]"></div>
      <div v-show="showLiveControl" class="bottom-mask z-10 absolute bottom-0 left-0 w-full h-[152px]"></div>
      <img
        v-show="showLiveControl"
        @click.stop="$emit('on-share')"
        class="w-[40px] h-[40px] click-style bg-contain absolute top-[20px] z-20 right-[24px]"
        :src="shareIcon"
      />

      <div v-show="showLiveControl" class="truncate max-w-[600px] break-all absolute top-[20px] z-10 left-[64px]">
        {{ title }}
      </div>
      <div class="w-full h-full" :style="{ display: showPlayer == 1 ? 'block' : 'none' }">
        <LivePlayer
          alt=" "
          :timeout="15"
          @message="onErrorMsg"
          @error="onError"
          @timeupdate="onTimeupdate"
          @play="onPlay"
          @pause="onPause"
          :hide-big-play-button="true"
          ref="videoPlayer"
          :controls="false"
          :videoUrl="videoUrl"
          fluent
          autoplay
          live
        />
      </div>
      <div class="w-full relative h-[380px] z-10" v-if="showPlayer == 0">
        <img :src="poster" class="w-full h-full object-cover" />
        <div class="w-full h-[380px] filter-bg absolute left-0 top-0 z-20"></div>
        <div class="w-screen h-[380px] absolute left-0 top-0 z-20 flex justify-center items-center">
          <img :src="pauseCenterIcon" class="w-[104px] h-[104px] object-contain z-30" @click="onUnmutePlay" />
        </div>
      </div>
      <img @click="onClick" v-if="!isPlay" class="w-full h-[380px] bg-contain absolute top-0 left-0 z-[1]" :src="backgroundBk" />

      <div v-show="showLiveControl" class="flex w-full h-[106px] mb-[60px] absolute items-end bottom-0 justify-between z-10">
        <img
          @click.stop="onPauseOrPlay"
          class="w-[30px] click-style h-[32px] mb-[36px] ml-[24px] bg-cover"
          :src="isPlay ? pauseIcon : playIcon"
        />
        <span
          @click.stop="$emit('on-action')"
          class="live-switch click-style w-[104px] mb-[42px] h-[42px] justify-center mr-[24px] flex pt-[4px] text-white text-[24px] font-medium"
          >信号源</span
        >
      </div>
    </div>
    <div v-if="showPlayer == -1" class="animation absolute top-[130px] left-[316px] w-[120px] h-[120px] z-10"></div>
    <div class="w-full h-full absolute top-0 left-0 opacity-0" @click="onClick"></div>
  </div>
</template>

<script setup>
  import LivePlayer from '@liveqing/liveplayer-v3';
  import pauseIcon from '@/assets/img/player/pause.png';
  import playIcon from '@/assets/img/player/play.png';
  import shareIcon from '@/assets/img/room/share-icon.png';
  import lottie from 'lottie-web';
  import animationData from '@/components/VideoLoading/loading.json';
  import pauseCenterIcon from '@/assets/img/player/pause-center-icon.png';

  const videoPlayer = ref();
  const showLiveControl = ref(false);
  const timer = ref(null);
  const isPlay = ref(false);
  const animationRef = ref();
  const emits = defineEmits(['on-error', 'on-action', 'on-share']);
  const showPlayer = ref(-1);
  defineProps({
    msg: {
      type: String,
    },
    videoUrl: {
      type: String,
    },
    backgroundBk: {
      type: String,
    },
    title: {
      type: String,
    },
    poster: {
      type: String,
    },
  });
  onUnmounted(() => {
    if (videoPlayer.value) {
      videoPlayer.value.destory();
    }
    animationRef.value && animationRef.value.destroy();
  });
  onMounted(() => {
    onCreatLoading();
    onShowVideoLoading();
    setTimeout(() => {
      if (showPlayer.value == -1) {
        onCloseLoading();
        showPlayer.value = 0;
      }
    }, 6000);
  });
  const onCreatLoading = () => {
    animationRef.value = lottie.loadAnimation({
      container: document.querySelector('.animation'), // 需要渲染动画的dom元素
      animationData, // 动画文件
      renderer: 'svg', // 渲染方式
      loop: true, // 是否循环播放
      autoplay: true, // 是否自动播放
      name: 'VideoLoading', // 动画参考名称
    });
  };
  const onShowVideoLoading = () => {
    animationRef.value && animationRef.value.play();
  };
  const onTimeupdate = () => {
    isPlay.value = true;
    onCloseLoading();
  };
  const onCloseLoading = () => {
    animationRef.value.stop();
  };
  const onErrorMsg = () => {
    onError();
  };
  const onError = () => {
    emits('on-error');
    onCloseLoading();
  };

  const onPauseOrPlay = () => {
    if (isPlay.value) {
      videoPlayer.value.pause();
    } else {
      videoPlayer.value.play();
    }
    // videoPlayer.value.requestFullscreen();
    // isPlay.value = !isPlay.value;
  };
  const onUnmutePlay = () => {
    const player = videoPlayer.value;
    if (player && player.getMuted()) {
      player.setMuted(false);
    }
    videoPlayer.value.play();
    showPlayer.value = 1;
  };
  const onPause = () => {
    isPlay.value = false;
  };
  const onPlay = () => {
    isPlay.value = true;
    if (showPlayer.value == -1) {
      showPlayer.value = 0;
      onPause();
    }
    onCloseLoading();
  };
  const onClick = () => {
    onUnmutePlay();
    showLiveControl.value = !showLiveControl.value;
    if (showLiveControl.value) {
      if (timer.value) {
        clearTimeout(timer.value);
      }
      timer.value = setTimeout(() => {
        showLiveControl.value = false;
      }, 3000);
    }
  };
  const onShowLoading = () => {
    onShowVideoLoading();
  };
  defineExpose({
    onShowLoading,
  });
  // const onSnap = () => {
  //   videoPlayer.value.snap();
  // };
  // const onPlay = () => {
  //   console.log('onClick');
  // };
  // export default {
  //   name: 'LivePlayerDemo',
  //   components: {
  //     LivePlayer,
  //   },
  //   props: {
  //     msg: String,
  //     videoUrl: String,
  //   },
  //   methods: {
  //     pause: function () {
  //       this.$refs.player2.pause();
  //     },
  //     play: function () {
  //       this.$refs.player2.play();
  //     },
  //     snap: function () {
  //       this.$refs.player2.snap();
  //     },
  //     snapOutside: function (snapData) {
  //       alert(snapData);
  //     },
  //   },
  // };
</script>

<style scoped lang="scss">
  .live-player {
    .filter-bg {
      background: rgba(229, 229, 229, 0.4);
      backdrop-filter: blur(10px);
    }
    .bottom-mask {
      background: linear-gradient(0deg, rgba(0, 0, 0, 0.47) 0%, rgba(25, 24, 42, 0) 100%);
    }
    .top-mask {
      background: linear-gradient(180deg, rgba(0, 0, 0, 0.63) 0%, rgba(25, 24, 42, 0) 100%);
    }
    .live-switch {
      border-radius: 40px;
      background: rgba(255, 255, 255, 0.25);
    }
    .live-player-stretch-btn {
      display: none !important;
    }

    .vjs-snapshot-control {
      display: none !important;
    }
    .vjs-control-bar {
      display: none !important;
    }
  }
</style>
