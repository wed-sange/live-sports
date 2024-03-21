<template>
  <div
    ref="iconRef"
    class="icon-custom cursor-pointer"
    :style="{
      width,
      height
    }"
  />
</template>
<script setup lang="ts">
defineOptions({
  name: "IconOnline"
});
import lottie from "lottie-web";
import animate from "@/assets/liveIcon.json";
import { VNodeRef, onMounted, onUnmounted, ref } from "vue";
const iconRef: VNodeRef = ref();
const lottieRef = ref(null);
withDefaults(
  defineProps<{
    width?: string;
    height?: string;
  }>(),
  {
    width: "14px",
    height: "14px"
  }
);

onMounted(() => {
  if (!iconRef.value) return;
  lottieRef.value = lottie.loadAnimation({
    container: iconRef.value, // 绑定dom节点
    renderer: "svg", // 渲染方式:svg、canvas
    loop: true, // 是否循环播放，默认：false
    autoplay: true, // 是否自动播放, 默认true
    animationData: animate // AE动画使用bodymovie导出为json数据
  });
});

onUnmounted(() => {
  if (lottieRef.value) {
    lottieRef.value.destroy();
  }
});
</script>
