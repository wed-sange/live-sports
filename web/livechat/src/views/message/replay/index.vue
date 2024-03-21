<template>
  <div class="message-box w-full h-[calc(100vh-78px)] mx-0 px-[24px] pb-[24px]">
    <div class="message-inner-box w-full h-full flex gap-6">
      <div class="sub flex rounded-2xl overflow-hidden">
        <AnchorCard
          ref="anchorCardRef"
          :currentUser="currentUser"
          v-model="currentAnchor"
          v-if="userInfo.identityType !== 1"
        />
        <UserCard
          v-show="currentAnchor"
          ref="userCardRef"
          :currentAnchor="currentAnchor"
          v-model="currentUser"
        />
      </div>
      <div class="flex-1 rounded-2xl overflow-hidden">
        <ChatBox :receiveId="currentUser" :anchorId="currentAnchor" />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import ChatBox from "../components/chat/index.vue";
import AnchorCard from "../components/list/anchor/index.vue";
import UserCard from "../components/list/user/index.vue";
import { useIndex } from "./useIndex";
defineOptions({
  name: "MessageReplay"
});
const { anchorCardRef, userCardRef, currentAnchor, currentUser, userInfo } =
  useIndex();
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
