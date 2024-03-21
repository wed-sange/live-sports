<template>
  <div class="member-card flex flex-col bg-[#FAFAFE] w-[187px] h-full">
    <div
      class="member-card__header grow-0 shrink-0 basis-[90px] flex items-center"
    >
      <h2>直播间</h2>
    </div>
    <div class="member-card__body flex-1 overflow-auto">
      <el-scrollbar>
        <div class="member-card__list">
          <template v-for="item in list" :key="item.id">
            <LiveItem
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
          暂无直播间
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>
<script setup lang="ts">
import { toRef } from "vue";
import { MessageCardData } from "@/views/message/utils/types";
import LiveItem from "./item.vue";
import { useIndex } from "./useIndex";
import InlineLoading from "../../loading/index.vue";
import { useMessageStore } from "@/store/modules/message";
defineOptions({
  name: "LiveCard"
});
const props = withDefaults(
  defineProps<{
    modelValue: string;
  }>(),
  {
    modelValue: ""
  }
);
const emit = defineEmits(["update:modelValue"]);
const currentAnchor = toRef(props, "modelValue");
const { list, loading, onLoad, getItem, finished } = useIndex({
  currentAnchor
});
const handleOnLoad = async () => {
  await onLoad();
  handleItemClick(list.value[0] || ({} as MessageCardData));
};
handleOnLoad();
const messageStore = useMessageStore();
const messageServer = messageStore.getMessageServer();
const handleItemClick = (item: MessageCardData) => {
  emit("update:modelValue", item.anchorId || "");
  item.readDot > 0 &&
    messageServer?.clearDot({
      id: item.sendUserId,
      readDot: item.readDot,
      pId: currentAnchor.value
    });
};
defineExpose({
  getItem
});
</script>

<style scoped lang="scss">
.member-card {
  .member-card__header {
    background: url("@/assets/message/live_bg.png") no-repeat, transparent;
    background-position: 0 0;
    background-size: 100% 100%;
    border-bottom: 1px solid #e5e6eb;

    h2 {
      display: inline-block;
      min-height: 24px;
      padding-left: 26px;
      margin-left: 20px;
      font: 500 18px / normal "PingFang SC";
      color: #1a1a1a;
      background: url("@/assets/message/icon_live.png") no-repeat, transparent;
      background-position: 0 center;
      background-size: 24px 24px;
    }
  }
}
</style>
