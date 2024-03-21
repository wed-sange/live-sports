<script setup lang="ts">
import LiveChatItem from "./item.vue";
import LiveChatSender from "./sender.vue";
import IconEmpty from "@/views/message/components/live/svg/IconEmpty.vue";
import InlineLoading from "@/views/message/components/loading/index.vue";
import ScrollContainer from "@/views/message/components/live/ScrollContainer.vue";
import { computed, nextTick, shallowRef, watch } from "vue";
import { storeToRefs } from "pinia";
import { useRequest } from "vue-request";
import { useLiveStore } from "@/store/modules/live";
import { getMessageHistory } from "@/api/message";
import {
  LiveMessageItem,
  transformMessage,
  useLiveRoomSocket
} from "@/views/message/utils/live";
import TextSent from "@/views/message/components/live/svg/TextSent.vue";
import { IdentityType } from "@/api/message/enums";

defineOptions({ name: "LiveSpeech" });

const liveStore = useLiveStore();
const { currentLive } = storeToRefs(liveStore);

const params: Parameters<typeof getMessageHistory>[0] = {
  size: 20,
  groupId: "", //	群聊Id
  type: "0", // 消息类型，0-历史消息，1-离线消息
  chatType: "1", // 群聊还是私聊 1-群聊，2-私聊
  searchType: "2", // 查询类型 live-2, APP-3
  anchorSendOnly: true //只查询主播组发送的消息
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
      const dom = document.querySelector(`[data-chat="${last?.id}"]`);
      dom?.scrollIntoView({ block: "start" });
    });
  }
});
watch(
  () => currentLive.value?.id,
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
    if (
      ![IdentityType.Anchor, IdentityType.Operator].includes(res.identityType)
    )
      return;
    const isExist = messages.value?.findIndex(item => item.id === res.id);
    if (isExist !== -1) return;
    messages.value = [...messages.value, res];
  }
});
</script>

<template>
  <div class="flex flex-col bg-white w-full h-full p-[14px]">
    <div class="grow-0 shrink-0 flex items-center justify-between p-[4px] pb-0">
      <TextSent />
      <InlineLoading v-if="messages?.length && loading" />
    </div>
    <div
      class="flex-grow overflow-y-auto py-[14px]"
      :class="[!messages?.length && 'flex h-full items-center justify-center']"
    >
      <ScrollContainer v-if="messages?.length" @reach-top="onReachTop">
        <div class="flex flex-col gap-[16px]">
          <LiveChatItem
            v-for="message in messages"
            :key="message.id"
            :message="message"
            :data-chat="message.id"
          />
        </div>
      </ScrollContainer>
      <template v-else>
        <InlineLoading v-if="loading" />
        <IconEmpty v-else />
      </template>
    </div>
    <LiveChatSender />
  </div>
</template>
