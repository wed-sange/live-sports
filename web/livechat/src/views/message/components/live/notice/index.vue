<script setup lang="ts">
import LiveNoticeEdit from "./edit.vue";
import TextNotice from "@/views/message/components/live/svg/TextNotice.vue";
import IconEdit from "@/views/message/components/live/svg/IconEdit.vue";
import IconEmpty from "@/views/message/components/live/svg/IconEmpty.vue";
import InlineLoading from "@/views/message/components/loading/index.vue";
import { ref, watch } from "vue";
import { useLiveStore } from "@/store/modules/live";
import { fetchLiveOpenInfo } from "@/api/message";
import { useRequest } from "vue-request";

defineOptions({ name: "LiveNotice" });

const liveStore = useLiveStore();
const showEdit = ref(false);

const notice = ref("");

const {
  data: info,
  run,
  loading
} = useRequest(fetchLiveOpenInfo, {
  manual: true,
  loadingKeep: 200,
  onSuccess: res => {
    notice.value = res?.notice;
  }
});
watch(
  () => liveStore.currentLive?.id,
  roomId => {
    info.value = undefined;
    if (!roomId) return;
    run(roomId);
  }
);
</script>

<template>
  <div class="flex flex-col w-full h-full bg-white">
    <div
      class="flex-shrink-0 flex justify-between items-center p-[14px] pt-[18px] pb-0 text-[#A8A8A9]"
    >
      <div class="flex-shrink-0 flex items-center">
        <TextNotice />
      </div>
      <button
        class="inline-flex items-center gap-[2px] text-[12px]"
        @click="showEdit = true"
      >
        <el-icon>
          <IconEdit />
        </el-icon>
        编辑
      </button>
    </div>
    <div
      class="flex-grow p-[14px] text-[12px] text-[#37373D]"
      :class="[!notice && 'flex h-full items-center justify-center']"
    >
      <div v-if="notice" v-text="notice" />
      <template v-else>
        <InlineLoading v-if="loading" />
        <el-icon v-else size="80">
          <IconEmpty />
        </el-icon>
      </template>
    </div>
  </div>
  <LiveNoticeEdit
    v-model:show="showEdit"
    v-model:notice="notice"
    :room-id="liveStore.currentLive?.id"
  />
</template>
