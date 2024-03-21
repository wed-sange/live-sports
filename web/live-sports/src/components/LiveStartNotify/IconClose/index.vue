<template>
  <div class="live-start__icon--close" ref="domRef"></div>
</template>

<script lang="ts" setup>
  import lottie from 'lottie-web';
  import animationData from './close.json';
  const emit = defineEmits<{
    (event: 'an-complete'): void;
  }>();
  const domRef = ref<Element | null>(null);
  let lottieInstance: ReturnType<typeof lottie.loadAnimation> | null = null;
  let loading = ref(false);
  const createAnimation = () => {
    lottieInstance?.destroy();
    loading.value = true;
    lottieInstance = lottie.loadAnimation({
      container: domRef.value!, // the dom element that will contain the animation
      renderer: 'svg',
      loop: false,
      autoplay: true,
      animationData: animationData, // the path to the animation json
    });
    lottieInstance.addEventListener('complete', () => {
      emit('an-complete');
    });
  };
  const destroyAnimation = () => {
    loading.value = false;
    lottieInstance?.destroy();
    lottieInstance = null;
  };
  onMounted(() => {
    nextTick(() => {
      createAnimation();
    });
  });
  onUnmounted(() => {
    destroyAnimation();
  });
</script>

<script lang="ts">
  export default {
    name: 'FollowStar',
  };
</script>
<style scoped lang="scss">
  .live-start__icon--close {
    width: 36px;
    height: 36px;
  }
</style>
