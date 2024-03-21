<script setup lang="ts">
import { useLiveStore } from "@/store/modules/live";
import { storeToRefs } from "pinia";
import { watch, ref, nextTick, onUnmounted } from "vue";
import { useElementSize, useFullscreen } from "@vueuse/core";
import InlineLoading from "@/views/message/components/loading/index.vue";
import IconEmpty from "@/views/message/components/live/svg/IconEmpty.vue";

defineOptions({ name: "LiveStream" });

const liveStore = useLiveStore();
const { currentLive } = storeToRefs(liveStore);

const loading = ref(false);
const notLive = ref(false);
const url = ref("");

const boxRef = ref(null);
const containerRef = ref(null);

// 动态计算视频流显示比例
const { height: boxH = 280 } = useElementSize(boxRef);
const streamSize = ref({ width: 812, height: 480 });

// player容器
let player: NodePlayer = null;

// 初始化Player
const initPlayer = () => {
  player = new NodePlayer();
  NodePlayer.debug(false);
  NodePlayer.workletAudioEngine();
  player.setTimeout(6);
  player.setVolume(0);
  player.setView("video");

  player.on("start", () => {});
  player.on("stop", () => {});
  player.on("error", () => {
    loading.value = true;
  });
  player.on("videoInfo", (width, height) => {
    streamSize.value.width = width;
    streamSize.value.height = height;
    loading.value = false;
  });
  player.on("stats", () => {});
  player.on("timeout", () => {
    loading.value = true;
  });

  nextTick(() => {
    player.start(url.value);
  });
};

watch(
  () => currentLive.value,
  row => {
    if (row) {
      if (row.liveStatus === 2) {
        loading.value = true;
        notLive.value = false;

        // TODO test
        const urls = [
          "https://live.nodemedia.cn:8443/live/ls_2.flv",
          "https://live.nodemedia.cn:8443/live/b480_264.flv",
          "https://live.nodemedia.cn:8443/live/ls_3.flv"
        ];
        if (row.playUrl) {
          url.value = row.playUrl;
        } else {
          url.value = urls[Math.floor(Math.random() * 3)];
        }

        if (player) {
          player.stop();
          player.clearView();
          player.start(url.value);
        } else {
          initPlayer();
        }
      } else {
        if (!player) return;
        player.stop();
        player.clearView();
        url.value = "";
        notLive.value = true;
      }
    }
  },
  { immediate: true }
);

// 音量
const volume = ref(0);
watch(
  () => volume.value,
  () => {
    player.setVolume(volume.value / 100);
  }
);

// 开启/关闭声音
const setVolume = (status: boolean) => {
  if (status) {
    player.audioResume();
    volume.value = 100;
  } else {
    volume.value = 0;
  }
};

// 全屏
const {
  isFullscreen,
  enter: enterFullScreen,
  exit: exitFullScreen
} = useFullscreen(containerRef);

onUnmounted(() => {
  if (!player) return;
  player.stop();
  player.clearView();
  player.release(false);
});
</script>

<template>
  <div
    ref="containerRef"
    class="container w-full h-full relative overflow-hidden"
  >
    <div
      ref="boxRef"
      class="w-full h-full flex justify-center items-center relative"
    >
      <div
        :style="{
          paddingRight: `${streamSize.width * (boxH / streamSize.height)}px`,
          paddingBottom: `${boxH}px`
        }"
        class="relative"
      >
        <canvas id="video" class="w-full h-full absolute top-0 left-0" />
      </div>
    </div>

    <!--  controls-->
    <div
      v-show="!notLive"
      class="controls w-full h-[44px] absolute z-[2] bottom-0 flex justify-end items-center gap-[20px] translate-y-[44px] transition-transform bg-gradient-to-t from-[#000000] via-[rgba(0,0,0,0.7)] to-[transparent]"
    >
      <div class="flex items-center">
        <el-icon :size="22" color="#ffffff" class="cursor-pointer mr-[12px]">
          <svg
            v-if="volume"
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            viewBox="0 0 24 24"
            @click="setVolume(false)"
          >
            <g
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <path d="M15 8a5 5 0 0 1 0 8" />
              <path
                d="M6 15H4a1 1 0 0 1-1-1v-4a1 1 0 0 1 1-1h2l3.5-4.5A.8.8 0 0 1 11 5v14a.8.8 0 0 1-1.5.5L6 15"
              />
            </g>
          </svg>
          <svg
            v-else
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            viewBox="0 0 24 24"
            @click="setVolume(true)"
          >
            <g
              fill="none"
              stroke="currentColor"
              stroke-width="2"
              stroke-linecap="round"
              stroke-linejoin="round"
            >
              <path
                d="M6 15H4a1 1 0 0 1-1-1v-4a1 1 0 0 1 1-1h2l3.5-4.5A.8.8 0 0 1 11 5v14a.8.8 0 0 1-1.5.5L6 15"
              />
              <path d="M16 10l4 4m0-4l-4 4" />
            </g>
          </svg>
        </el-icon>

        <el-slider
          v-model="volume"
          size="small"
          input-size="small"
          class="!w-[80px] !h-[8px]"
          :format-tooltip="tip => `${tip}%`"
        />
      </div>

      <div class="h-[20px] mr-[10px]">
        <el-icon
          :size="20"
          color="#ffffff"
          class="cursor-pointer hover:scale-125 transition-transform"
        >
          <svg
            v-if="isFullscreen"
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            viewBox="0 0 24 24"
            @click="exitFullScreen"
          >
            <g fill="none">
              <path
                d="M9 4a1 1 0 0 0-2 0v2.5a.5.5 0 0 1-.5.5H4a1 1 0 0 0 0 2h2.5A2.5 2.5 0 0 0 9 6.5V4zm0 16a1 1 0 1 1-2 0v-2.5a.5.5 0 0 0-.5-.5H4a1 1 0 1 1 0-2h2.5A2.5 2.5 0 0 1 9 17.5V20zm7-17a1 1 0 0 0-1 1v2.5A2.5 2.5 0 0 0 17.5 9H20a1 1 0 1 0 0-2h-2.5a.5.5 0 0 1-.5-.5V4a1 1 0 0 0-1-1zm-1 17a1 1 0 1 0 2 0v-2.5a.5.5 0 0 1 .5-.5H20a1 1 0 1 0 0-2h-2.5a2.5 2.5 0 0 0-2.5 2.5V20z"
                fill="currentColor"
              />
            </g>
          </svg>
          <svg
            v-else
            xmlns="http://www.w3.org/2000/svg"
            xmlns:xlink="http://www.w3.org/1999/xlink"
            viewBox="0 0 16 16"
            @click="enterFullScreen"
          >
            <g fill="none">
              <path
                d="M4 3.5a.5.5 0 0 0-.5.5v1.614a.75.75 0 0 1-1.5 0V4a2 2 0 0 1 2-2h1.614a.75.75 0 0 1 0 1.5H4zm5.636-.75a.75.75 0 0 1 .75-.75H12a2 2 0 0 1 2 2v1.614a.75.75 0 0 1-1.5 0V4a.5.5 0 0 0-.5-.5h-1.614a.75.75 0 0 1-.75-.75zM2.75 9.636a.75.75 0 0 1 .75.75V12a.5.5 0 0 0 .5.5h1.614a.75.75 0 0 1 0 1.5H4a2 2 0 0 1-2-2v-1.614a.75.75 0 0 1 .75-.75zm10.5 0a.75.75 0 0 1 .75.75V12a2 2 0 0 1-2 2h-1.614a.75.75 0 1 1 0-1.5H12a.5.5 0 0 0 .5-.5v-1.614a.75.75 0 0 1 .75-.75z"
                fill="currentColor"
              />
            </g>
          </svg>
        </el-icon>
      </div>
    </div>

    <!--  loading-->
    <div
      v-show="loading && !notLive"
      class="w-full h-full flex items-center justify-center absolute z-[1] left-0 top-0"
    >
      <InlineLoading class="p-2 bg-[rgba(255,255,255,0.4)] rounded-[8px]" />
    </div>

    <!--  empty-->
    <div
      v-show="notLive"
      class="w-full h-full flex items-center justify-center absolute left-0 top-0 bg-[rgba(255,255,255,0.1)]"
    >
      <el-icon size="80">
        <IconEmpty />
      </el-icon>
    </div>
  </div>
</template>

<style scoped lang="scss">
.container {
  &:hover {
    .controls {
      transform: translateY(0);
    }
  }
}
</style>
