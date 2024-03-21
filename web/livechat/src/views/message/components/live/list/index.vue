<script setup lang="ts">
import LiveItem from "./item.vue";
import InlineLoading from "@/views/message/components/loading/index.vue";
import IconEmpty from "@/views/message/components/live/svg/IconEmpty.vue";
import type { LiveItemData } from "@/api/message/model";
import { useLiveStore } from "@/store/modules/live";
import { useRequest } from "vue-request";
import { getLiveList } from "@/api/message";
import { onUnmounted } from "vue";

defineOptions({ name: "LiveCard" });

const liveStore = useLiveStore();

const { data: list, loading } = useRequest(getLiveList, {
  loadingKeep: 200,
  onSuccess: res => {
    liveStore.currentLive = res?.[0];
  }
});

function onLiveItemChange(item: LiveItemData) {
  liveStore.currentLive = item;
}
onUnmounted(() => {
  liveStore.currentLive = undefined;
});
</script>

<template>
  <div
    class="flex flex-col h-full w-full overflow-y-auto"
    :class="[!list?.length && 'flex h-full items-center justify-center']"
  >
    <el-scrollbar v-if="list?.length">
      <LiveItem
        v-for="item in list"
        :key="item.id"
        :class="{ active: item.anchorId === liveStore.currentLive.anchorId }"
        :item="item"
        @click="onLiveItemChange(item)"
      />
    </el-scrollbar>
    <template v-else>
      <InlineLoading v-if="loading" />
      <IconEmpty v-else />
    </template>
  </div>
</template>
