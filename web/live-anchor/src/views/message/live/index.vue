<template>
  <div class="message-box w-full h-[calc(100vh-78px)] mx-0 px-[18px] pb-[24px]">
    <div class="message-inner-box w-full h-full flex">
      <div class="sub flex">
        <LiveCard ref="liveCardRef" v-model="currentLive" />
      </div>
      <div class="flex-1">
        <ChatLiveBox :anchorId="currentLive" />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import ChatLiveBox from "../components/chat/live.vue";
import LiveCard from "../components/list/live/index.vue";
import { useIndex } from "./useIndex";
defineOptions({
  name: "MessageLive"
});
const { liveCardRef, currentLive } = useIndex();
</script>

<style scoped lang="scss">
.message-box.main-content {
  margin: 0;

  .message-inner-box {
    overflow: hidden;
    border-radius: 16px;
  }
}
</style>
