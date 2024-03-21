<template>
  <div class="custom-popup-box">
    <van-popup
      v-model:show="show"
      :overlay-style="{ background: 'transparent' }"
      :close-on-click-overlay="false"
      :overlay="overlay"
      :duration="0"
      class="custom-popup"
      ref="rootRef"
      @opened="opened"
      @closed="afterClose"
    >
      <div class="loading-box" ref="domRef"></div>
    </van-popup>
  </div>
</template>

<script lang="ts" setup>
  import { ref, reactive, nextTick } from 'vue';
  import mitt from 'mitt';
  import { type PopupInstance } from 'vant';
  import lottie from 'lottie-web';
  import animationData from './loading.json';
  import { ApiFormEvents, LoadingOption } from './types';
  const rootRef = ref<PopupInstance | null>(null);
  const domRef = ref<Element | null>(null);
  let lottieInstance: ReturnType<typeof lottie.loadAnimation> | null = null;
  const show = ref(false);
  const overlay = ref(true);
  const formMitt = reactive(mitt());
  const afterClose = () => {
    nextTick(() => {
      lottieInstance && lottieInstance.destroy();
      formMitt.emit(ApiFormEvents.afterClose);
      if (rootRef.value) {
        rootRef.value.$el.remove && rootRef.value.$el.remove();
      }
    });
  };
  const opened = () => {
    nextTick(() => {
      formMitt.emit(ApiFormEvents.opened);
    });
  };
  const close = () => {
    show.value = false;
  };
  onMounted(() => {
    nextTick(() => {
      lottieInstance = lottie.loadAnimation({
        container: domRef.value!, // the dom element that will contain the animation
        renderer: 'svg',
        loop: true,
        autoplay: true,
        animationData: animationData, // the path to the animation json
      });
    });
  });
  const changeOption = (opt?: LoadingOption) => {
    overlay.value = opt ? (opt.overlay !== undefined ? !!opt?.overlay : true) : true;
  };
  defineExpose({
    show,
    close,
    formMitt,
    changeOption,
  });
</script>

<script lang="ts">
  export default {
    name: 'SysLoading',
  };
</script>
<style scoped lang="scss">
  .custom-popup-box {
    :deep().van-popup.custom-popup {
      width: 150px;
      height: 60px;
      background: transparent;
    }
    :deep(.loading-box) {
      width: 100%;
      height: 100%;
      background: transparent;
      svg {
        display: block;
        background: transparent;
      }
    }
  }
</style>
