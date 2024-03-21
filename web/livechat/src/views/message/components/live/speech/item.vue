<script setup lang="ts">
import type { LiveMessageItem } from "@/views/message/utils/live";
import { formatLevelName } from "@/views/message/utils/chat";
import { importAssets } from "@/utils/import";
import { IdentityType } from "@/api/message/enums";
import { MessageType } from "@/views/message/utils/types";

defineOptions({ name: "ChatCard" });

interface Props {
  message: LiveMessageItem;
}
const props = defineProps<Props>();

const isAnchor = props.message.identityType === IdentityType.Anchor;
</script>

<template>
  <section>
    <div
      v-if="message.msgType === MessageType.system"
      class="text-[12px] text-[#BAC2CB] flex justify-center items-center gap-[8px] select-none"
    >
      <span class="h-[1px] w-[20px] bg-[#BAC2CB]" />
      {{ props.message.content }}
      <span class="h-[1px] w-[20px] bg-[#BAC2CB]" />
    </div>

    <div
      v-else
      class="w-fit p-[8px] bg-[#EEF0F4] rounded-[6px]"
      :class="[isAnchor ? 'text-[#34A853]' : 'text-[#37373D]']"
    >
      <span
        v-if="message?.level || isAnchor"
        class="text-[#37373D] rounded-full inline-flex items-center gap-[2px] text-[12px] px-[8px] py-[2px] mr-[4px] align-middle select-none"
        :class="[isAnchor ? 'bg-[#34A853] text-white font-medium' : 'bg-white']"
      >
        <img
          v-if="!isAnchor"
          :src="importAssets(`message/level/v${message.level}.png`)"
          class="w-[12px]"
          alt=""
        />
        {{ isAnchor ? "主播" : formatLevelName(message.level) }}
      </span>
      <span
        class="text-[14px] align-middle"
        :class="[isAnchor ? 'text-[#34A853] font-medium' : 'text-[#6B7179]']"
      >
        {{ message.sender }}：
      </span>
      <span
        class="break-all whitespace-pre-wrap align-middle overflow-hidden text-[14px]"
        v-text="message.content"
      />
    </div>
  </section>
</template>
