<template>
  <div class="member-card flex flex-col bg-[#fff] w-[90px] h-full">
    <div
      class="member-card__header grow-0 shrink-0 basis-[90px] flex items-center justify-center"
    >
      <h2>主播</h2>
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
const { list, onLoad, loading, disabled, currentPage, finished } = useIndex({
  currentAnchor,
  currentUser
});
const handleOnLoad = async () => {
  await onLoad();
  if (currentPage.value === 1) {
    handleItemClick(list.value[0] || ({} as MessageCardData));
  }
};
const handleItemClick = (item: MessageCardData) => {
  emit("update:modelValue", item.anchorId || "");
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
    background: url("@/assets/message/anchor_bg.png") no-repeat, transparent;
    background-position: 0 0;
    background-size: 100% 100%;
    border-bottom: 1px solid #e5e6eb;

    h2 {
      display: inline-block;
      min-height: 24px;
      padding-left: 26px;
      font: 500 18px / normal "PingFang SC";
      color: #1a1a1a;
      background: url("@/assets/message/icon_anchor.png") no-repeat, transparent;
      background-position: 0 center;
      background-size: 24px 24px;
    }
  }
}
</style>
