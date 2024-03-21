<template>
  <div class="chat-send-box w-full px-[32px] overflow-hidden pb-[20px]">
    <div class="chat-send w-full max-w-full flex items-center pl-[24px]">
      <!-- <div class="grow-0 shrink-0 basis-[48px] h-[48px]">
        <IconFace />
      </div> -->
      <div class="flex-auto">
        <el-input
          ref="inputRef"
          v-model="message"
          @keydown="handleKeydown"
          resize="none"
          class="chat-send__input flex-1"
          rows="1"
          :maxlength="500"
          type="textarea"
          placeholder="说点什么吧........"
        />
      </div>
      <div
        class="chat-send__message flex items-center gap-4"
        :class="{
          large: type === 'chat'
        }"
      >
        <template v-if="type === 'chat'">
          <ChatEmoji @emoji-click="chooseEmoji">
            <el-icon class="cursor-pointer" size="28">
              <IconEmoji class="text-emerald-100" />
            </el-icon>
          </ChatEmoji>
        </template>
        <div
          class="grow-0 shrink-0 cursor-pointer relative"
          @click="openMedia()"
          v-if="type === 'chat'"
        >
          <IconCamera />
        </div>

        <div class="grow-0 shrink-0 cursor-pointer" @click="handleSend">
          <IconSend
            :class="{
              'opacity-60': !message
            }"
          />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import {
  type SendMessageFunction,
  type SendFileMessageFunction
} from "./useIndex";
import {
  type SendMessageFunction as LiveSendMessageFunction,
  type SendFileMessageFunction as LiveSendImageMessageFunction
} from "./useLiveIndex";
import { IconSend, IconCamera, IconEmoji } from "../icon";
import { inject, ref } from "vue";
import { ElMessage } from "element-plus";
import { useFileDialog } from "@vueuse/core";
import ChatEmoji from "./chatEmoji.vue";
import { nextTick } from "vue";

withDefaults(
  defineProps<{
    type?: "chat" | "live";
  }>(),
  {
    type: "chat"
  }
);
const inputRef = ref(null);
const emit = defineEmits(["send"]);
const scrollToBottom = inject("scrollToBottom") as () => void;
const sendMessage = inject("sendMessage") as
  | SendMessageFunction
  | LiveSendMessageFunction;
const sendImageMessage = inject("sendImageMessage") as
  | SendFileMessageFunction
  | LiveSendImageMessageFunction;
const sendVideoMessage = inject("sendVideoMessage") as
  | SendFileMessageFunction
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
  await sendMessage(message.value);
  scrollToBottom();
  message.value = "";
};
const handleKeydown = (event: KeyboardEvent) => {
  if (event.key === "Enter" && !event.shiftKey) {
    event.preventDefault(); // 阻止默认的换行行为
    handleSend();
  }
};
const { open, reset, onChange } = useFileDialog({
  accept: "video/*, image/*" // Set to accept only image files
});
const openMedia = () => {
  open();
};

const chooseEmoji = (emoji: string) => {
  message.value += emoji;
  nextTick(() => {
    inputRef.value.focus();
    scrollToBottom();
  });
};

const handleChooseVideo = async (files: FileList) => {
  console.log("handleChooseVideo", files);

  if (files && files.length >= 1) {
    const file = files[0];
    reset();
    if (file.size > 1024 * 1000 * 100) {
      ElMessage.error("请上传小于100M的视频");
      return;
    }

    await sendVideoMessage(file);
    await nextTick();
    scrollToBottom();
  }
};

const handleChooseFile = async (files: FileList) => {
  if (files[0].type.includes("video")) {
    handleChooseVideo(files);
    return;
  }
  if (files && files.length >= 1) {
    const file = files[0];
    reset();
    if (file.size > 1024 * 5000) {
      ElMessage.error("请上传小于5M的图片");
      return;
    }

    await sendImageMessage(file);
    await nextTick();
    scrollToBottom();
  }
};

onChange(handleChooseFile);
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

  // .chat-send__message {
  // width: 52px;

  // &.large {
  //   width: 96px;
  // }
  // }

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
