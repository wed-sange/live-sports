<script setup lang="ts">
import type { LiveMessageItem } from "@/views/message/utils/live";
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
      {{ props.message.content }} [{{ props.message.sender }}]
      <span class="h-[1px] w-[20px] bg-[#BAC2CB]" />
    </div>

    <div v-else class="w-fit text-[14px] text-[#34A853]">
      <span
        class="inline-flex p-[2px_8px] rounded-full text-[12px] text-white align-middle select-none"
        :class="[isAnchor ? 'bg-[#34A853]' : 'bg-[#F48D2F]']"
      >
        {{ isAnchor ? "主播身份" : "用户身份" }}
      </span>
      <span
        class="mx-[4px] align-middle"
        :class="[isAnchor ? 'font-medium' : 'text-[#6B7179]']"
      >
        {{ message.sender }}:
      </span>
      <span
        class="align-middle break-all whitespace-pre-wrap overflow-hidden"
        :class="[!isAnchor && 'text-[#37373D]']"
        v-text="message.content"
      />
    </div>
  </section>
</template>
