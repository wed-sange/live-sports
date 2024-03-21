<template>
  <div class="chat-send-box w-full px-[24px] pt-[16px] overflow-hidden relative">
    <div class="chat-send w-full flex items-end pb-[24px]">
      <!-- <div class="grow-0 shrink-0 basis-[48px] h-[48px]">
        <IconFace />
      </div> -->
      <div class="chat-send__input-box flex-auto px-[30px] py-[16px]">
        <form
          class="w-full block"
          @submit.prevent.stop="
            () => {
              return false;
            }
          "
        >
          <van-field
            id="inputMsg"
            v-model="message"
            class="chat-send__input keyboard-abr"
            @focus="handleMessageFocus"
            @blur="handleMessageBlur"
            @keydown="handleKeydown"
            ref="inputRef"
            :rows="1"
            :autosize="{
              minHeight: 20,
              maxHeight: 60,
            }"
            enterkeyhint="send"
            :maxlength="200"
            type="textarea"
            placeholder="说点什么吧"
          />
        </form>
      </div>
      <div class="chat-send__message grow-0 shrink-0 basis-[90px] flex justify-end">
        <div
          class="grow-0 shrink-0 basis-[60px] w-[60px] mx-[20px] h-[60px] mb-[6px] rounded-[50%] cursor-pointer chat-send_message--emoji"
          v-ripple
          @touchstart="toggleEmoji()"
        ></div>
        <div
          class="grow-0 shrink-0 basis-[60px] w-[60px] h-[60px] mb-[6px] rounded-[50%] cursor-pointer chat-send__message--add"
          v-ripple
          @click="toggleTool()"
        ></div>
        <!-- <div class="grow-0 shrink-0 basis-[72px] h-[72px] cursor-pointer ml-[32px] rounded-[50%]" v-ripple @click="handleSend">
          <IconSend />
        </div> -->
      </div>
    </div>
    <Transition name="chat-send-tool-toggle-transition" @after-leave="onAfterLeave" @after-enter="onAfterEnter">
      <div class="chat-send-tool-outbox overflow-hidden" v-show="toolShow">
        <div class="chat-send-tool flex items-center pt-[16px] pb-[2px]">
          <div
            class="chat-send-tool__item grow-0 shrink-0 basis-[120px] w-[120px] rounded-[16px] overflow-hidden cursor-pointer"
            @click="openPhoto"
          >
            <div class="chat-send-tool__item--icon photo"></div>
            <div class="chat-send-tool__item--text">相册</div>
            <input ref="photoInputRef" class="chat-send-tool__photo--file" type="file" accept="image/*" @change="handleChooseFile" />
          </div>
          <!-- <div class="chat-send-tool__item grow-0 shrink-0 basis-[96px] h-[96px] rounded-[16px] overflow-hidden">
            <div class="chat-send-tool__item--icon"><IconCamera /></div>
            <div class="chat-send-tool__item--text">拍照</div>
          </div> -->
        </div>
      </div>
    </Transition>
    <Transition name="emoji-tool">
      <div class="h-[320px] overflow-y-scroll pb-[16px]" v-show="emojiShow">
        <Emoji @on-select="onSelect" />
      </div>
    </Transition>
  </div>
</template>
<script setup lang="ts">
  import { showToast } from 'vant';
  import { type SendMessageFunction, type SendImageMessageFunction } from './useIndex';
  // import { IconSend, IconPlus, IconClose, IconPhoto } from '../../components/icon';
  import { useUserStore } from '@/store/modules/user';
  import Emoji from '@/components/common/Emoji.vue';
  // const emit = defineEmits<{
  //   (event: 'send', value: string | File, type: 'text' | 'image'): void;
  // }>();
  const userStore = useUserStore();
  const isBanned = computed(() => userStore.userInfo.isBanned);
  const scrollToBottom = inject('scrollToBottom') as () => void;
  const sendMessage = inject('sendMessage') as SendMessageFunction;
  const sendImageMessage = inject('sendImageMessage') as SendImageMessageFunction;
  const inputRef = ref<HTMLInputElement>();
  const message = ref('');
  const emojiShow = ref(false);
  const insetNum = ref(0);
  const handleSend = async () => {
    toggleTool(false);
    if (isBanned.value) {
      showToast({
        className: 'custom-toast',
        message: '您已被禁言',
      });
      return;
    }
    const messageText = message.value.trim();
    if (!messageText) {
      showToast({
        className: 'custom-toast',
        message: '请输入内容',
      });
      return;
    }
    inputRef.value?.blur();
    // emit('send', message.value, 'text');
    sendMessage(message.value);
    scrollToBottom();
    message.value = '';
  };
  const handleKeydown = (event) => {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault(); // 阻止默认的换行行为
      handleSend();
    }
  };
  const onSelect = (emoji) => {
    const onInsert = (insert) => {
      message.value = message.value.substr(0, insert) + emoji + message.value.substr(insert);
      insetNum.value = insetNum.value + emoji.length;
    };
    // 拼接字符串的形式来得到需要的内容
    if (!insetNum.value) {
      message.value = message.value + emoji;
    } else {
      onInsert(insetNum.value);
    }
    if (!inputRef.value) {
      return;
    }
    handleInput();
  };
  const handleInput = () => {
    // 使滚动条保持在最底部
    nextTick(() => {
      const inputMsg = document.getElementById('inputMsg');
      if (inputMsg) {
        inputMsg.scrollTop = inputMsg.scrollHeight;
      }
    });
  };
  const handleMessageFocus = () => {
    toggleTool(false);
    if (emojiShow.value) {
      emojiShow.value = false;
    }
  };
  const handleMessageBlur = () => {
    // alert('blur');
    // toggleTool(false);
  };
  const toolShow = ref(false);
  const toggleTool = (show?: boolean) => {
    emojiShow.value = false;
    if (show === null || show === undefined) {
      toolShow.value = !toolShow.value;
    } else {
      toolShow.value = !!show;
    }

    // scrollToBottom();
  };
  const toggleEmoji = () => {
    toolShow.value = false;
    if (!emojiShow.value) {
      let textInput = document.getElementById('inputMsg') as HTMLInputElement;
      let insert = textInput.selectionStart as number;
      insetNum.value = insert;
      inputRef.value?.blur();
    }
    emojiShow.value = !emojiShow.value;
  };
  const onAfterLeave = () => {
    // scrollToBottom();
  };
  const onAfterEnter = () => {
    // scrollToBottom();
  };

  const photoInputRef = ref<HTMLInputElement | null>(null);
  const openPhoto = () => {
    if (isBanned.value) {
      showToast({
        className: 'custom-toast',
        message: '您已被禁言',
      });
      return;
    }
    photoInputRef.value!.value = '';
    photoInputRef.value && photoInputRef.value.click();
  };
  const handleChooseFile = async (event: Event) => {
    // showToast({
    //   className: 'custom-toast',
    //   message: 'choose',
    // });
    const files = (event.target as HTMLInputElement).files;
    if (isBanned.value) {
      photoInputRef.value!.value = '';
      showToast({
        className: 'custom-toast',
        message: '您已被禁言',
      });
      return;
    }
    if (files && files.length >= 1) {
      const file = files[0];
      if (file.size > 1024 * 5000) {
        showToast({
          className: 'custom-toast',
          message: '请上传小于5M的图片',
        });
        return;
      }
      sendImageMessage(file);
      scrollToBottom();
      toggleTool(false);
    }
    photoInputRef.value!.value = '';
  };
</script>
<script lang="ts">
  export default {
    name: 'ChatSend',
  };
</script>
<style scoped lang="scss">
  .chat-send-box {
    background: #fff;
    .chat-send__input-box {
      border-radius: 70px;
      background: #f2f3f7;
    }
    .chat-send {
      // background: #18152a;
      // border-radius: 16px;
    }
    .chat-send__input {
      --van-cell-background: transparent;
      --van-field-input-text-color: #37373d;
      --van-field-placeholder-text-color: #94999f;
      --van-cell-vertical-padding: 0;
      --van-cell-horizontal-padding: 0;
      font: 400 28px / 40px 'PingFang SC';
    }
    .chat-send__message--add {
      background: url('@/assets/img/message/icon_add.png') no-repeat, transparent;
      background-size: auto 100%;
      background-position: 0 0;
    }
    .chat-send_message--emoji {
      background: url('@/assets/img/message/emoji-icon.png') no-repeat, transparent;
      background-size: auto 100%;
      background-position: 0 0;
    }
    .chat-send-tool__item {
      position: relative;
      & + .chat-send-tool__item {
        margin-left: 52px;
      }
      .chat-send-tool__item--icon {
        margin: 0 auto 16px;
        width: 120px;
        height: 120px;
        &.photo {
          background: url('@/assets/img/message/icon_photo.png') no-repeat, transparent;
          background-size: auto 100%;
          background-position: 0 0;
        }
      }
      .chat-send-tool__item--text {
        font: 400 24px / normal 'PingFang SC';
        color: #575762;
        text-align: center;
      }
    }
  }
  .chat-send-tool__photo--file {
    position: absolute;
    top: 0;
    left: 0;
    opacity: 0;
    z-index: 1;
    width: 0;
    height: 0;
    overflow: hidden;
    pointer-events: none;
  }
  .chat-send-tool-outbox {
    height: 200px;
  }
  .chat-send-tool-toggle-transition-enter-active,
  .chat-send-tool-toggle-transition-leave-active {
    transition: height 100ms linear;
  }

  .chat-send-tool-toggle-transition-enter-from,
  .chat-send-tool-toggle-transition-leave-to {
    height: 0;
  }
  .emoji-tool-enter-active,
  .emoji-tool-leave-active {
    transition: height 100ms linear;
  }
  .emoji-tool-enter-from,
  .emoji-tool-leave-to {
    height: 0;
  }
</style>
