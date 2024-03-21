<script setup lang="ts">
import IconMore from "@/views/message/components/live/svg/IconMore.vue";
import IconMute from "@/views/message/components/live/svg/IconMute.vue";
import type { Member } from "@/api/message/model";
import { importAssets } from "@/utils/import";
import { computed, ref } from "vue";
import {
  fetchForbiddenUser,
  fetchKickOutUser,
  fetchUnForbiddenUser
} from "@/api/message";
import { useLiveStore } from "@/store/modules/live";
import { ElDropdown } from "element-plus";
import { useRequest } from "vue-request";
import { useLiveRoomSocket } from "@/views/message/utils/live";
import { MessageType } from "@/views/message/utils/types";

defineOptions({ name: "LiveMemberItem" });
interface Props {
  member: Member;
}
const props = defineProps<Props>();
const emits = defineEmits(["update", "remove"]);

const liveStore = useLiveStore();
const dropdownRef = ref<typeof ElDropdown>();
const visible = ref(false);

const forbiddenParams: Parameters<typeof fetchForbiddenUser>[0] = {
  userId: props.member?.userId,
  anchorId: liveStore.currentLive?.anchorId
};
const kickOutParams: Parameters<typeof fetchKickOutUser>[0] = {
  ...forbiddenParams,
  liveId: liveStore.currentLive?.id
};
const { sendMessage } = useLiveRoomSocket();

const { run: runFetchForbiddenUser } = useRequest(fetchForbiddenUser, {
  manual: true,
  onSuccess: () => {
    emits("update", true);
    sendMessage({
      msgType: MessageType.system,
      content: `主播禁言了 ${props.member?.nick}`
    });
  }
});
const { run: runFetchUnForbiddenUser } = useRequest(fetchUnForbiddenUser, {
  manual: true,
  onSuccess: () => {
    emits("update", false);
    sendMessage({
      msgType: MessageType.system,
      content: `主播解除了 ${props.member?.nick} 的禁言`
    });
  }
});
const { run: runFetchKickOutUser } = useRequest(fetchKickOutUser, {
  manual: true,
  onSuccess: () => {
    emits("remove");
    sendMessage({
      msgType: MessageType.system,
      content: `主播将 ${props.member?.nick} 移出直播间`
    });
  }
});

const actions = computed(() => {
  return [
    {
      label: props.member?.mute ? "取消禁言" : "禁言",
      handler: () => {
        dropdownRef.value?.handleClose();
        props.member?.mute
          ? runFetchUnForbiddenUser(forbiddenParams)
          : runFetchForbiddenUser(forbiddenParams);
      }
    },
    {
      label: "移出直播间",
      class: "text-[#EB502E]",
      handler: () => {
        dropdownRef.value?.handleClose();
        runFetchKickOutUser(kickOutParams);
      }
    }
  ];
});
</script>

<template>
  <div class="flex justify-between items-center w-full flex-shrink-0">
    <span class="inline-flex items-center gap-[4px]">
      <img
        :src="importAssets(`message/level/v${member?.level}.png`)"
        class="w-[12px] select-none"
        alt=""
      />
      <span class="text-[#37373D] text-[#14px]">{{ member?.nick }}</span>
      <el-icon v-if="member?.mute">
        <IconMute />
      </el-icon>
    </span>
    <el-dropdown ref="dropdownRef" @visible-change="v => (visible = v)">
      <el-icon
        size="20"
        :class="[visible && 'bg-[#EEF0F4]']"
        class="hover:bg-[#EEF0F4] rounded-[2px] outline-none"
      >
        <IconMore />
      </el-icon>
      <template #dropdown>
        <button
          v-for="action in actions"
          :key="action.label"
          :class="[action.class]"
          @click="action.handler"
          class="w-[84px] px-[12px] py-[6px] flex items-center justify-center text-[12px] text-[#94999F] hover:bg-[#EEF0F4]"
        >
          {{ action.label }}
        </button>
      </template>
    </el-dropdown>
  </div>
</template>
