<script setup lang="ts">
import SpeechItem from "./item.vue";
import TextLivingRoom from "@/views/message/components/live/svg/TextLivingRoom.vue";
import IconEmpty from "@/views/message/components/live/svg/IconEmpty.vue";
import InlineLoading from "@/views/message/components/loading/index.vue";
import ScrollContainer from "@/views/message/components/live/ScrollContainer.vue";
import { computed, nextTick, shallowRef, watch } from "vue";
import { useRequest } from "vue-request";
import { useLiveStore } from "@/store/modules/live";
import { getMessageHistory } from "@/api/message";
import {
  LiveMessageItem,
  transformMessage,
  useLiveRoomSocket
} from "@/views/message/utils/live";

defineOptions({ name: "LiveSpeech" });

const liveStore = useLiveStore();

const params: Parameters<typeof getMessageHistory>[0] = {
  size: 40,
  groupId: "", //	群聊Id
  type: "0", // 消息类型，0-历史消息，1-离线消息
  chatType: "1", // 群聊还是私聊 1-群聊，2-私聊
  searchType: "2" // 查询类型（判断是用户还是直播端，普通用户查看不了被删除的消息）， live-2, APP-3
};
const messages = shallowRef<LiveMessageItem[]>();
const { run, loading } = useRequest(getMessageHistory, {
  manual: true,
  loadingKeep: 200,
  onSuccess: async res => {
    const data = res.map(transformMessage);
    const last = messages.value?.[0];
    messages.value = [...data, ...messages.value];
    await nextTick(() => {
      const dom = document.querySelector(`[data-speech="${last?.id}"]`);
      dom?.scrollIntoView({ block: "start" });
    });
  }
});
watch(
  () => liveStore.currentLive?.id,
  roomId => {
    params.offset = undefined;
    messages.value = [];
    if (!roomId) return;
    params.groupId = roomId;
    run(params);
  }
);

const offsetId = computed(() => messages.value?.[0]?.id || "");
const onReachTop = () => {
  params.offset = String(offsetId.value);
  run(params);
};

useLiveRoomSocket({
  onMessage: res => {
    const isExist = messages.value?.findIndex(item => item.id === res.id);
    if (isExist !== -1) return;
    messages.value = [...messages.value, res];
  }
});
</script>

<template>
  <div class="flex flex-col bg-white w-full h-full p-[14px]">
    <div
      class="grow-0 shrink-0 flex items-center justify-between p-[4px] pb-[14px]"
    >
      <TextLivingRoom />
      <InlineLoading v-if="messages?.length && loading" />
    </div>

    <div
      class="flex-grow overflow-y-scroll"
      :class="[!messages?.length && 'flex h-full items-center justify-center']"
    >
      <ScrollContainer v-if="messages?.length" @reach-top="onReachTop">
        <div class="flex flex-col gap-[2px]">
          <SpeechItem
            v-for="message in messages"
            :key="message.id"
            :message="message"
            :data-speech="message.id"
          />
        </div>
      </ScrollContainer>
      <template v-else>
        <InlineLoading v-if="loading" />
        <IconEmpty v-else />
      </template>
    </div>
  </div>
</template>
