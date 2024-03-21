<template>
  <div class="sports-card__star" :class="{ loading: loading }" ref="domRef"></div>
</template>

<script lang="ts" setup>
  import lottie from 'lottie-web';
  import animationData from './data.json';
  import animationCancelData from './data-cancel.json';
  const domRef = ref<Element | null>(null);
  let lottieInstance: ReturnType<typeof lottie.loadAnimation> | null = null;
  let loading = ref(false);
  const createAnimation = (type: 'ok' | 'cancel') => {
    lottieInstance?.destroy();
    loading.value = true;
    lottieInstance = lottie.loadAnimation({
      container: domRef.value!, // the dom element that will contain the animation
      renderer: 'svg',
      loop: false,
      autoplay: true,
      animationData: type === 'ok' ? animationData : animationCancelData, // the path to the animation json
    });
    lottieInstance.addEventListener('complete', () => {
      destroyAnimation();
    });
  };
  const destroyAnimation = () => {
    loading.value = false;
    lottieInstance?.destroy();
    lottieInstance = null;
  };
  const getAnimationState = () => {
    return loading.value;
  };
  defineExpose({
    createAnimation,
    getAnimationState,
    destroyAnimation,
  });
</script>

<script lang="ts">
  export default {
    name: 'FollowStar',
  };
</script>
<style scoped lang="scss">
  .sports-card__star {
    background: url('@/assets/img/sportsCard/star.jpg') no-repeat, transparent;
    background-size: auto 35%;
    background-position: center;
    &.active:not(.loading) {
      background: url('@/assets/img/sportsCard/star_active.jpg') no-repeat, transparent;
      background-size: auto 35%;
      background-position: center;
    }
  }
</style>
