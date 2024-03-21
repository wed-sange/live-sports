<template>
  <div class="chat-scroll-box w-full h-full overflow-auto">
    <el-scrollbar ref="scrollRef" @scroll="onScroll">
      <div ref="innerRef">
        <InlineLoading v-if="modelValue" />
        <slot />
      </div>
    </el-scrollbar>
  </div>
</template>
<script setup lang="ts">
import { ref, nextTick } from "vue";
import { ElScrollbar } from "element-plus";
import InlineLoading from "../loading/index.vue";
defineOptions({
  name: "ChatScroll"
});
const props = withDefaults(
  defineProps<{
    modelValue: boolean;
    finished: boolean;
  }>(),
  {
    modelValue: false,
    finished: false
  }
);
const emit = defineEmits(["update:modelValue", "load"]);
const innerRef = ref<HTMLDivElement>();
const scrollRef = ref<InstanceType<typeof ElScrollbar>>();
const currentScrollTop = ref(0);
const setScrollTop = (value: number) => {
  scrollRef.value.setScrollTop(value);
};

const setScrollOffset = (value: number) => {
  scrollRef.value.setScrollTop(currentScrollTop.value + value);
};
const getScrollHeight = () => {
  if (scrollRef.value.wrapRef) {
    let scrollHeight =
      innerRef.value!.clientHeight - scrollRef.value.wrapRef.clientHeight;
    scrollHeight = scrollHeight < 0 ? 0 : scrollHeight;
    return scrollHeight;
  } else {
    return 0;
  }
};
let i = 0;
const scrollToBottom = async () => {
  await nextTick();
  setScrollTop(getScrollHeight());
  i++;
  if (i > 10) {
    i = 0;
    return;
  }
  // setTimeout(async () => {
  //   await scrollToBottom();
  // }, 0);
};
const onScrollStart = async () => {
  if (props.finished) {
    return;
  }
  if (props.modelValue) return;
  emit("update:modelValue", true);
  emit("load");
};
onScrollStart();
const onScroll = ({ scrollTop }: { scrollLeft: number; scrollTop: number }) => {
  currentScrollTop.value = scrollTop;
  if (scrollTop === 0) {
    onScrollStart();
  }
};
defineExpose({
  scrollToBottom,
  setScrollTop,
  setScrollOffset,
  currentScrollTop,
  getScrollHeight
});
</script>
