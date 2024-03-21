<script setup lang="ts">
import LiveList from "@/views/message/components/live/list/index.vue";
import LiveSpeech from "@/views/message/components/live/speech/index.vue";
import LiveStream from "@/views/message/components/live/stream/index.vue";
import LiveChat from "@/views/message/components/live/chat/index.vue";
import LiveNotice from "@/views/message/components/live/notice/index.vue";
import LiveMembers from "@/views/message/components/live/members/index.vue";

defineOptions({ name: "MessageLive" });
</script>

<template>
  <div class="w-full h-[calc(100vh-78px)] flex gap-[24px]">
    <div class="flex-shrink-0 w-[240px]">
      <LiveList />
    </div>
    <div class="flex-shrink-0 w-[367px] rounded-[10px] overflow-hidden">
      <LiveSpeech />
    </div>
    <div class="flex-grow min-w-[505px] flex flex-col gap-[24px]">
      <div
        class="flex-shrink-0 w-full h-[284px] rounded-[10px] overflow-hidden bg-[#000000]"
      >
        <LiveStream />
      </div>
      <div class="flex-grow rounded-[10px] overflow-hidden">
        <LiveChat />
      </div>
    </div>
    <div class="flex-shrink-0 w-[208px] flex flex-col gap-[24px]">
      <div
        class="flex-shrink-0 w-full h-[284px] rounded-[10px] overflow-hidden"
      >
        <LiveNotice />
      </div>
      <div class="flex-grow rounded-[10px] overflow-hidden">
        <LiveMembers />
      </div>
    </div>
  </div>
</template>
