<template>
  <div class="chat-scroll-box w-full h-full overflow-auto">
    <section
      class="scroll-loading"
      v-show="modelValue"
      :class="{ mode: modeRef }"
    >
      <InlineLoading />
    </section>
    <el-scrollbar
      ref="scrollRef"
      class="messgaeScroll"
      id="messgaeScroll"
      @scroll="onScroll"
    >
      <div ref="innerRef">
        <slot />
      </div>
    </el-scrollbar>
  </div>
</template>
<script setup lang="ts">
import { ref, nextTick } from "vue";
import { ElScrollbar } from "element-plus";
import InlineLoading from "../loading/index.vue";
import { isScrollBottom } from "./scroll";
defineOptions({
  name: "ChatScroll"
});
const props = withDefaults(
  defineProps<{
    modelValue: boolean;
  }>(),
  {
    modelValue: false
  }
);
const emit = defineEmits(["update:modelValue", "load"]);
const innerRef = ref<HTMLDivElement>();
const modeRef = ref("up");
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
// reallyBottom是真实置底，还是伪置底，伪置底目的是为了下滚动继续加载内容
const scrollToBottom = async (reallyBottom = true) => {
  await nextTick();
  setScrollTop(reallyBottom ? getScrollHeight() : getScrollHeight() - 10);
  i++;
  if (i > 10) {
    i = 0;
    return;
  }
};
const onScrollStart = async (mode: "init" | "up" | "down") => {
  if (props.modelValue) return;
  modeRef.value = mode;
  emit("load", mode);
};
onScrollStart("up");
const onScroll = ({ scrollTop }: { scrollLeft: number; scrollTop: number }) => {
  currentScrollTop.value = scrollTop;
  if (scrollTop === 0) {
    onScrollStart("up");
  } else if (isScrollBottom()) {
    onScrollStart("down");
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
<style scoped>
.scroll-loading {
  position: absolute;
  left: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 30px;

  &.up {
    top: 0;
  }

  &.down {
    bottom: 0;
  }
}

.messgaeScroll {
  padding-right: 20px;
  padding-bottom: 10px;
}
</style>
