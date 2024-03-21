<script setup lang="ts">
import LiveMemberItem from "./item.vue";
import TextMember from "@/views/message/components/live/svg/TextMember.vue";
import IconSearch from "@/views/message/components/live/svg/IconSearch.vue";
import IconEmpty from "@/views/message/components/live/svg/IconEmpty.vue";
import InlineLoading from "@/views/message/components/loading/index.vue";
import { computed, ref, watch } from "vue";
import { useRequest } from "vue-request";
import { fetchLiveMembers } from "@/api/message";
import { useLiveStore } from "@/store/modules/live";

defineOptions({ name: "LiveMembers" });

const liveStore = useLiveStore();
const showSearchBar = ref(false);
const keyword = ref("");

const params: Parameters<typeof fetchLiveMembers>[0] = {
  current: 1,
  size: 25,
  groupId: ""
};
const members = ref<AsyncReturnType<typeof fetchLiveMembers>["records"]>();
const isFinished = computed(() => members.value?.length >= data.value?.total);
const { run, loading, data, refresh } = useRequest(fetchLiveMembers, {
  manual: true,
  loadingKeep: 200,
  onSuccess: res => {
    members.value?.push(...res.records);
  }
});
watch(
  () => liveStore.currentLive?.id,
  roomId => {
    reset();
    keyword.value = undefined;
    showSearchBar.value = false;
    if (!roomId) return;
    params.groupId = roomId;
    run(params);
  }
);

const reset = () => {
  params.current = 1;
  params.keywords = undefined;
  members.value = [];
};
const onSearch = () => {
  reset();
  params.keywords = keyword.value;
  run(params);
};
const onCancelSearch = () => {
  showSearchBar.value = false;
  if (!keyword.value) return;
  reset();
  keyword.value = undefined;
  refresh();
};

const onReachBottom = () => {
  if (isFinished.value) return;
  params.current++;
  run(params);
};

const onUpdateState = (id: string, state: boolean) => {
  const index = members.value?.findIndex(item => item.id === id);
  if (index !== -1) {
    members.value[index].mute = state;
  }
};

const onRemoveMember = (id: string) => {
  const index = members.value?.findIndex(item => item.id === id);
  if (index !== -1) {
    members.value.splice(index, 1);
  }
};
</script>

<template>
  <div class="flex flex-col w-full h-full bg-white members">
    <div
      class="flex-shrink-0 flex justify-between items-center p-[14px] pt-[18px] pb-0 text-[#A8A8A9]"
    >
      <div class="flex-shrink-0 flex items-center">
        <TextMember />
      </div>
      <InlineLoading v-if="members?.length && loading" />
      <button
        v-else
        class="inline-flex items-center gap-[2px] text-[12px]"
        @click="showSearchBar = !showSearchBar"
      >
        <el-icon>
          <IconSearch />
        </el-icon>
        搜索
      </button>
    </div>
    <div
      v-if="showSearchBar"
      class="flex justify-between gap-[10px] px-[14px] pt-[14px] animate-scale-in"
    >
      <el-input v-model="keyword" @change="onSearch">
        <template #prefix>
          <el-icon> <IconSearch /></el-icon>
        </template>
      </el-input>
      <button
        class="text-[#94999F] text-[12px] shrink-0"
        @click="onCancelSearch"
      >
        取消
      </button>
    </div>
    <div
      class="flex-grow text-[12px] text-[#37373D] overflow-y-auto py-[14px]"
      :class="[!members?.length && 'flex h-full items-center justify-center']"
    >
      <el-scrollbar v-if="members?.length">
        <div
          class="flex flex-col gap-[14px] px-[14px]"
          v-infinite-scroll="onReachBottom"
        >
          <LiveMemberItem
            v-for="member in members"
            :key="member.id"
            :member="member"
            :data-member="member.id"
            @update="state => onUpdateState(member.id, state)"
            @remove="onRemoveMember(member.id)"
          />
        </div>
      </el-scrollbar>
      <template v-else>
        <InlineLoading v-if="loading" />
        <el-icon v-else size="80">
          <IconEmpty />
        </el-icon>
      </template>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.members :deep() {
  .el-input {
    --el-input-bg-color: #f7f7f7;
    --el-input-border: none;
    --el-input-height: 16px;
    --el-input-placeholder-color: #a6a6a6;

    .el-input__wrapper {
      padding: 6px 4px;
    }
  }

  &:not(.is-error) {
    .el-input__wrapper {
      box-shadow: none;
    }
  }
}
</style>
