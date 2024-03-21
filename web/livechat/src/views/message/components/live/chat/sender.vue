<script setup lang="ts">
import IconSwitch from "@/views/message/components/live/svg/IconSwitch.vue";
import IconSend from "@/views/message/components/live/svg/IconSend.vue";
import { computed, ref } from "vue";
import { IdentityType } from "@/api/message/enums";
import { useLiveRoomSocket } from "@/views/message/utils/live";
import { ElDropdown, ElMessage } from "element-plus";

defineOptions({ name: "LiveChatSender" });
const identityList = [
  {
    label: "主播身份",
    value: IdentityType.Anchor,
    class: "hover:text-[#34A853]"
  },
  {
    label: "用户身份",
    value: IdentityType.Operator,
    class: "hover:text-[#F48D2F]"
  }
];

const dropdown = ref<typeof ElDropdown>();
const content = ref("");
const identity = ref(IdentityType.Anchor);
const identityLabel = computed(
  () => identityList.find(item => item.value === identity.value)?.label
);
const isAnchor = computed(() => identity.value === IdentityType.Anchor);
const switchIdentity = (current: IdentityType) => {
  dropdown.value?.handleClose();
  identity.value = current;
};

const { sendMessage } = useLiveRoomSocket();

const onSend = async () => {
  if (!content.value) return;
  const res = await sendMessage({
    content: content.value,
    identityType: identity.value
  });
  content.value = undefined;
  if (res.success) return;
  ElMessage.error("发送失败");
};
</script>

<template>
  <div
    class="flex-shrink-0 flex h-[44px] bg-[#EEF0F4] rounded-[6px] overflow-hidden"
  >
    <el-dropdown ref="dropdown">
      <div
        class="flex-shrink-0 w-[100px] h-full flex items-center justify-center select-none rounded-[6px] text-white text-[14px] font-medium"
        :class="[isAnchor ? 'bg-[#34A853]' : 'bg-[#F48D2F]']"
      >
        <el-icon>
          <IconSwitch />
        </el-icon>
        {{ identityLabel }}
      </div>
      <template #dropdown>
        <div class="p-[4px] w-[100px]">
          <button
            v-for="item in identityList"
            :key="item.value"
            :class="[item.class]"
            @click="switchIdentity(item.value)"
            class="px-[18px] py-[8px] flex items-center justify-center text-[14px] rounded-[4px] text-[#94999F] hover:bg-[#EEF0F4] hover:font-medium"
          >
            {{ item.label }}
          </button>
        </div>
      </template>
    </el-dropdown>

    <div class="flex-grow" @keyup.enter="onSend">
      <input
        v-model="content"
        :placeholder="`您将以&quot;${identityLabel}&quot;发送消息!`"
        class="w-full h-full bg-transparent outline-none text-[12px] px-[10px]"
      />
    </div>
    <button
      :disabled="!content"
      class="disabled:cursor-not-allowed disabled:opacity-60 flex-shrink-0 w-[44px] h-full flex items-center justify-center rounded-[6px] text-white"
      :class="[isAnchor ? 'bg-[#34A853]' : 'bg-[#F48D2F]']"
      @click="onSend"
    >
      <el-icon size="16" color="#fff">
        <IconSend />
      </el-icon>
    </button>
  </div>
</template>
