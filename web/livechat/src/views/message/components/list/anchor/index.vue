<template>
  <div class="member-card flex flex-col bg-[#fff] w-[240px] h-full">
    <div class="pl-[14px] grow-0 shrink-0 basis-[62px]">
      <div class="member-card__header h-full flex items-center">
        <IconAnchor1 />
      </div>
    </div>
    <div class="member-card__body flex-1 overflow-auto">
      <el-scrollbar>
        <div
          class="member-card__list"
          :infinite-scroll-disabled="disabled"
          v-infinite-scroll="handleOnLoad"
        >
          <template v-for="item in list" :key="item.id">
            <AnchorItem
              :class="{ active: item.anchorId === modelValue }"
              :item="item"
              @on-click="handleItemClick"
              @on-set-top="setLocalToping"
              @on-un-set-top="setLocalUnToping"
            />
          </template>
        </div>
        <InlineLoading v-if="loading" />
        <div
          class="text-center py-[10px] text-[12px] text-[#565656]"
          v-if="finished && list.length === 0"
        >
          暂无主播
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>
<script setup lang="ts">
import { toRef } from "vue";
import { MessageCardData } from "@/views/message/utils/types";
import AnchorItem from "./item.vue";
import { useIndex } from "./useIndex";
import InlineLoading from "../../loading/index.vue";
import { IconAnchor1 } from "../../icon/index";
import { useStorage } from "@vueuse/core";

defineOptions({
  name: "AnchorCard"
});
const props = withDefaults(
  defineProps<{
    currentUser: string;
    modelValue: string;
  }>(),
  {
    currentUser: "",
    modelValue: ""
  }
);
const emit = defineEmits(["update:modelValue"]);
const currentAnchor = toRef(props, "modelValue");
const currentUser = toRef(props, "currentUser");
const {
  list,
  onLoad,
  loading,
  disabled,
  currentPage,
  finished,
  setLocalToping,
  setLocalUnToping
} = useIndex({
  currentAnchor,
  currentUser
});

const anchorId = useStorage("anchorId", "", sessionStorage);

const handleOnLoad = async () => {
  await onLoad();
  if (currentPage.value === 1) {
    // TODO 暂时注释 UI图上没有默认选择第一个
    const preAnchor = list.value.find(cur => cur.anchorId === anchorId.value);
    handleItemClick(preAnchor || ({} as MessageCardData));
  }
};
const handleItemClick = (item: MessageCardData) => {
  emit("update:modelValue", item.anchorId || "");
  anchorId.value = item.anchorId || "";
};
const getItem = (anchorId: string) => {
  return list.value.find(cur => cur.anchorId === anchorId);
};
defineExpose({
  getItem
});
</script>

<style scoped lang="scss">
.member-card {
  .member-card__header {
    // background: url("@/assets/message/anchor_bg.png") no-repeat, transparent;
    background-position: 0 0;
    background-size: 100% 100%;
    border-bottom: 1px solid #e5e6eb;
  }
}
</style>
