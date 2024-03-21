<template>
  <div class="member-card flex flex-col bg-[#FAFAFE] w-[225px] h-full">
    <div
      class="member-card__header grow-0 shrink-0 basis-[90px] flex items-center"
    >
      <h2>用户</h2>
    </div>
    <div class="member-card__body flex-1 overflow-auto" v-show="currentAnchor">
      <el-scrollbar>
        <div
          class="member-card__list"
          :infinite-scroll-disabled="disabled"
          v-infinite-scroll="handleOnLoad"
        >
          <template v-for="item in list" :key="item.id">
            <UserItem
              :class="{ active: item.sendUserId === modelValue }"
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
          暂无用户
        </div>
      </el-scrollbar>
    </div>
  </div>
</template>
<script setup lang="ts">
import { toRef, watch } from "vue";
import { MessageCardData } from "@/views/message/utils/types";
import UserItem from "./item.vue";
import { useIndex } from "./useIndex";
import InlineLoading from "../../loading/index.vue";
import { useMessageStore } from "@/store/modules/message";
defineOptions({
  name: "UserCard"
});
const props = withDefaults(
  defineProps<{
    currentAnchor: string;
    modelValue: string;
  }>(),
  {
    currentAnchor: "",
    modelValue: ""
  }
);
const emit = defineEmits(["update:modelValue"]);
const currentAnchor = toRef(props, "currentAnchor");
const currentUser = toRef(props, "modelValue");
const { list, onLoad, loading, disabled, currentPage, onRefresh, finished } =
  useIndex({
    currentAnchor,
    currentUser
  });
const handleOnLoad = async () => {
  await onLoad();
  if (currentPage.value === 1) {
    handleItemClick(list.value[0] || ({} as MessageCardData));
  }
};
const handleRefresh = async () => {
  await onRefresh();
  if (currentPage.value === 1) {
    handleItemClick(list.value[0] || ({} as MessageCardData));
  }
};
watch(currentAnchor, (newValue, oldValue) => {
  if (newValue !== oldValue) {
    handleRefresh();
  }
});
const messageStore = useMessageStore();
const messageServer = messageStore.getMessageServer();
const handleItemClick = (item: MessageCardData) => {
  emit("update:modelValue", item.sendUserId || "");

  item.readDot > 0 &&
    messageServer?.clearDot({
      id: item.sendUserId,
      readDot: item.readDot,
      pId: currentAnchor.value
    });
};
const getItem = (sendUserId: string) => {
  return list.value.find(cur => cur.sendUserId === sendUserId);
};
defineExpose({
  getItem
});
</script>

<style scoped lang="scss">
.member-card {
  .member-card__header {
    background: url("@/assets/message/user_bg.png") no-repeat, transparent;
    background-position: 0 0;
    background-size: 100% 100%;
    border-bottom: 1px solid #e5e6eb;

    h2 {
      display: inline-block;
      min-height: 24px;
      padding-left: 26px;
      margin-left: 22px;
      font: 500 18px / normal "PingFang SC";
      color: #1a1a1a;
      background: url("@/assets/message/icon_user.png") no-repeat, transparent;
      background-position: 0 center;
      background-size: 24px 24px;
    }
  }
}
</style>
