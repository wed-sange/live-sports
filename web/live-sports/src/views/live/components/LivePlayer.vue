<template>
  <div class="relative live-player h-[378px] max-h-[378px] overflow-hidden">
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
      <div class="w-screen h-full items-center justify-center" :style="{ display: showPlayer == 1 ? 'flex' : 'none' }">
        <canvas id="video" :width="fullWidth" :height="fullHeight"></canvas>
      </div>
      <div class="w-full relative h-[378px] z-10" v-if="showPlayer == 0">
        <img :src="poster" class="w-full h-full object-cover" />
        <div class="w-full h-[378px] filter-bg absolute left-0 top-0 z-20"></div>
        <div class="w-screen h-[378px] absolute left-0 top-0 z-20 flex justify-center items-center">
          <img :src="pauseCenterIcon" class="w-[104px] h-[104px] object-contain z-30" @click="onUnmutePlay" />
        </div>
      </div>

      <!-- <img @click="onClick" v-if="!isPlay" class="w-full h-[378px] bg-contain absolute top-0 left-0 z-[1]" :src="backgroundBk" /> -->
    </div>
    <div v-if="showPlayer == -1" class="animation absolute top-[130px] left-[316px] w-[120px] h-[120px] z-10"></div>
    <div class="w-full h-full absolute top-0 left-0 opacity-0" @click="onClick"></div>
    <div class="flex flex-col w-full absolute items-end bottom-0 justify-between z-10">
      <div v-show="showLiveControl" :class="`flex w-full ${advertItem ? 'mb-[50px]' : ''} items-end justify-between`">
        <img
          @click.stop="onPauseOrPlay"
          class="w-[30px] click-style h-[32px] mb-[24px] ml-[24px] bg-cover"
          :src="isPlay ? pauseIcon : playIcon"
        />
        <span
          @click.stop="$emit('on-action')"
          class="live-switch click-style w-[104px] mb-[16px] h-[42px] justify-center mr-[24px] flex pt-[4px] text-white text-[24px] font-medium"
          >信号源</span
        >
      </div>
      <div v-if="advertItem" class="notice-bar w-full px-[24px] h-[54px] absolute bottom-0 left-0 z-10">
        <van-notice-bar scrollable background="transparent" class="flex items-center">
          <div class="flex items-center text-[24px] h-[54px] text-white ml-[24px]" @click="$emit('on-jump', advertItem.targetUrl)">
            {{ advertItem.name }}
          </div>
        </van-notice-bar>
      </div>
    </div>
  </div>
</template>

<script setup>
  import pauseIcon from '@/assets/img/player/pause.png';
  import playIcon from '@/assets/img/player/play.png';
  import shareIcon from '@/assets/img/room/share-icon.png';
  import lottie from 'lottie-web';
  import animationData from '@/components/VideoLoading/loading.json';
  import pauseCenterIcon from '@/assets/img/player/pause-center-icon.png';

  const showLiveControl = ref(false);
  const timer = ref(null);
  const isPlay = ref(false);
  const animationRef = ref();
  const emits = defineEmits(['on-error', 'on-action', 'on-share']);
  const showPlayer = ref(-1);
  let player = null;
  const fullWidth = ref(375);
  const fullHeight = ref(375);
  const props = defineProps({
    msg: {
      type: String,
    },
    advertItem: {
      type: Object,
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
    if (player) {
      player.stop();
    }
    animationRef.value && animationRef.value.destroy();
  });
  onMounted(() => {
    fullWidth.value = window.innerWidth;
    fullHeight.value = fullWidth.value * 0.504;
    initPlayer();
    onCreateLoading();
    onShowVideoLoading();
  });

  // const onFullScreen = () => {
  //   player.fullscreen();
  // };
  const isStartPlay = () => {
    showPlayer.value = 1;
    if (isPlay.value) {
      return;
    }
    isPlay.value = true;
    onCloseLoading();
    onClick();
  };
  const initPlayer = () => {
    player = new NodePlayer();
    player.setKeepScreenOn();
    player.setTimeout(6);
    player.setView('video');
    player.setScaleMode(1);
    player.ve.style.objectFit = 'cover';
    player.ve.style.objectPosition = 'center';
    player.on('start', () => {
      // player.resizeView(fullWidth, fullHeight);
    });
    player.on('stop', () => {
      console.log('stopPlay');
    });
    player.on('error', (e) => {
      console.log('errorPlay', e);
      onError();
    });

    player.on('stats', (stats) => {
      const { buf } = stats;
      if (buf) {
        isStartPlay();
      }
    });
    player.on('timeout', (e) => {
      console.log('timeout', e);
      onError();
    });
    player.on('videoInfo', (e, e1, e2) => {
      const currentHeight = Math.min(fullHeight.value, e * 0.504);
      player.resizeView(fullWidth.value, currentHeight);
      // fullWidth.value = e;
      console.log('videoInfo', e, e1, e2, fullWidth.value, fullHeight.value, e * 0.507);
      // player.resizeView(e, e * 0.507);
    });

    console.log('player', player);
    // player.value('stats', (s) => {
    //   console.log('stats', s);
    // });
    nextTick(() => {
      player.start(props.videoUrl);
      // player.start('https://live.nodemedia.cn:8443/live/b480_264.flv');
    });
  };
  const onCreateLoading = () => {
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

  const onCloseLoading = () => {
    animationRef.value.stop();
  };

  const onError = () => {
    emits('on-error');
    onCloseLoading();
  };

  const onPauseOrPlay = () => {
    if (isPlay.value) {
      player.stop();
    } else {
      showPlayer.value = 1;
      player.start(props.videoUrl);
    }
    isPlay.value = !isPlay.value;
  };
  const onUnmutePlay = () => {
    showPlayer.value = 1;
  };
  // const onPause = () => {
  //   isPlay.value = false;
  // };
  // const onPlay = () => {
  //   isPlay.value = true;
  //   if (showPlayer.value == -1) {
  //     showPlayer.value = 0;
  //     onPause();
  //   }
  //   onCloseLoading();
  // };
  const onClick = () => {
    //loading中
    if (showPlayer.value == -1) {
      return;
    }
    // onUnmutePlay();
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
  const onPause = () => {
    if (isPlay.value) {
      player.stop();
      isPlay.value = false;
    }
  };
  const onPlay = () => {
    showPlayer.value = 1;
    if (player) {
      player.start(props.videoUrl);
      isPlay.value = true;
    } else {
      initPlayer();
    }
  };
  defineExpose({
    onShowLoading,
    onPause,
    onPlay,
  });
</script>

<style scoped lang="scss">
  .live-player {
    .notice-bar {
      background: rgba(40, 41, 43, 0.2) !important;
      backdrop-filter: blur(1.899999976158142px) !important;
    }
    :deep().van-notice-bar__content {
      font-family: PingFang SC;
      color: #f7da73;
      font-size: 24px;
    }
    :deep().van-notice-bar {
      padding: 0;
      height: 54px;
    }
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
