<template>
  <div class="chat-send-box w-full px-[32px] overflow-hidden pb-[26px]">
    <div
      class="chat-send w-full max-w-full flex items-center px-[24px] py-[8px]"
    >
      <!-- <div class="grow-0 shrink-0 basis-[48px] h-[48px]">
        <IconFace />
      </div> -->
      <div class="flex-auto">
        <el-input
          v-model="message"
          @keydown="handleKeydown"
          resize="none"
          class="chat-send__input"
          rows="1"
          :maxlength="500"
          type="textarea"
          placeholder="说点什么吧........"
        />
      </div>
      <div
        class="chat-send__message grow-0 shrink-0 flex ml-[8px]"
        :class="{
          large: type === 'chat'
        }"
      >
        <div
          class="grow-0 shrink-0 basis-[36px] h-[36px] cursor-pointer relative"
          @click="openPhoto()"
          v-if="type === 'chat'"
        >
          <IconCamera />
          <input
            ref="photoInputRef"
            class="chat-send-tool__item--file"
            type="file"
            accept="image/*"
            @change="handleChooseFile"
          />
        </div>
        <div
          class="grow-0 shrink-0 basis-[36px] h-[36px] cursor-pointer ml-[16px]"
          @click="handleSend"
        >
          <component :is="message ? IconSend : IconSendGray" />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import {
  type SendMessageFunction,
  type SendImageMessageFunction
} from "./useIndex";
import {
  type SendMessageFunction as LiveSendMessageFunction,
  type SendImageMessageFunction as LiveSendImageMessageFunction
} from "./useLiveIndex";
import { IconSend, IconCamera, IconSendGray } from "../icon";
import { inject, ref } from "vue";
import { ElMessage } from "element-plus";

withDefaults(
  defineProps<{
    type?: "chat" | "live";
  }>(),
  {
    type: "chat"
  }
);
const emit = defineEmits(["send"]);
const scrollToBottom = inject("scrollToBottom") as () => void;
const sendMessage = inject("sendMessage") as
  | SendMessageFunction
  | LiveSendMessageFunction;
const sendImageMessage = inject("sendImageMessage") as
  | SendImageMessageFunction
  | LiveSendImageMessageFunction;
const message = ref("");
const handleSend = async () => {
  // toggleTool(false);
  const messageText = message.value.trim();
  if (!messageText) {
    ElMessage.error("请输入内容");
    return;
  }
  emit("send", message.value);
  sendMessage(message.value);
  scrollToBottom();
  message.value = "";
};
const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === "Enter" && !event.shiftKey) {
    event.preventDefault(); // 阻止默认的换行行为
    handleSend();
  }
};

const photoInputRef = ref<HTMLInputElement | null>(null);
const openPhoto = () => {
  photoInputRef.value && photoInputRef.value.click();
};
const handleChooseFile = async (event: Event) => {
  const files = (event.target as HTMLInputElement).files;
  if (files && files.length >= 1) {
    const file = files[0];
    photoInputRef.value!.value = "";
    if (file.size > 1024 * 5000) {
      ElMessage.error("请上传小于5M的图片");
      return;
    }

    sendImageMessage(file);
    scrollToBottom();
    // const loadingInstance = ElLoading.service({ fullscreen: true });
    // try {
    //   const response = await uploadChatImg({
    //     file: file
    //   });
    //   if (response) {
    //     sendImageMessage(response);
    //     scrollToBottom();
    //   } else {
    //     ElMessage.error("图片上传失败");
    //   }
    // } finally {
    //   loadingInstance.close();
    // }
  }
};
</script>
<script lang="ts">
export default {
  name: "ChatSend"
};
</script>
<style scoped lang="scss">
.chat-send-box {
  .chat-send {
    background: #f6f6fa;
    border-radius: 8px;
  }

  .chat-send__message {
    width: 52px;

    &.large {
      width: 96px;
    }
  }

  .chat-send__input.el-textarea {
    --el-input-border: none;
    --el-input-bg-color: transparent;
    --el-input-placeholder-color: #a6a6a6;
    --el-input-text-color: #1a1a1a;

    font: 400 14px / 22px "PingFang SC";
    border: none;

    :deep() .el-textarea__inner {
      padding-left: 0;
      box-shadow: none;
    }
  }

  .chat-send-tool__item--file {
    position: absolute;
    top: 0;
    left: 0;
    z-index: 1;
    width: 0;
    height: 0;
    opacity: 0;
  }
}
</style>
