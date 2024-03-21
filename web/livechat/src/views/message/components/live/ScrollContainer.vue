<script setup lang="ts">
import { onUnmounted, ref } from "vue";
import { useIntersectionObserver } from "@vueuse/core";

defineOptions({ name: "ScrollContainer" });

const emits = defineEmits(["reachTop"]);
const ContentRef = ref<HTMLElement>();
const TopTriggerRef = ref<HTMLElement>();

const { stop, pause, resume, isActive } = useIntersectionObserver(
  TopTriggerRef,
  ([{ isIntersecting }]) => {
    if (!isIntersecting) return;
    emits("reachTop");
  },
  { root: ContentRef }
);
pause();
onUnmounted(() => stop());

const onScroll = () => {
  if (isActive.value) return;
  resume();
};
</script>

<template>
  <el-scrollbar ref="ContentRef" @scroll="onScroll">
    <div ref="TopTriggerRef" />
    <slot />
  </el-scrollbar>
</template>
