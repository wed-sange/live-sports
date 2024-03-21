<template>
  <layout-panel :class="`chat-page pr-[24px]  text-[#F5F5F] ${changeHeight ? 'current-page' : 'other-page'}`">
    <template #top v-if="anchorInfo && anchorInfo.notice">
      <div
        ref="noticeItemRef"
        class="flex ml-[24px] flex-col fixed top-[24px] w-[702px] rounded-[16px] notice-item mb-[24px] pt-[20px] bg-[#1F1F20] pl-[20px]"
      >
        <div class="text-[28px] flex justify-between">
          <div class="flex items-center h-[40px] font-medium">
            <img class="w-[36px] h-[28px] bg-contain mr-[10px]" :src="noticeIcon" />
            主播公告
          </div>
          <div class="flex h-[40px] click-style" v-if="showMore" @click="onShowMore">
            <span class="text-[#D7D7E7] text-[26px] mr-[8px] mt-[0.5px]">{{ showNoticeSimple ? '展开' : '收起' }}</span>
            <img class="w-[18px] h-[11px] mt-[13.5px] mr-[20px] bg-cover" :src="showNoticeSimple ? downIcon : upIcon" />
          </div>
        </div>
        <div class="pb-[20px] notice-content pt-[16px] mr-[12px] flex items-center leading-[46px] overflow-y-scroll text-[#94999F]">
          <div
            v-if="anchorInfo"
            ref="noticeRef"
            v-dompurify-html="anchorInfo.notice"
            :class="`text-[26px] ${
              showNoticeSimple ? 'h-[84px] line-clamp-2 ' : ' max-h-[280px] '
            } break-all whitespace-pre-wrap  break-words overflow-y-scroll`"
          ></div>
        </div>
      </div>
    </template>
    <div class="flex flex-col pl-[24px] overflow-y-scroll flex-grow" ref="chartRef">
      <ChatList ref="chatRef" v-if="messageList && messageList.length > 0" :list="messageList" />
      <div v-else-if="loaded" class="mt-[116px] ml-[1px]">
        <no-data text="暂无聊天内容" :grayText="true" icon="chat" />
      </div>
    </div>
    <template #bottom>
      <div :class="`chat-btm w-screen ${anchorInfo ? 'h-[113.5px]' : 'h-[105px]'} flex-shrink-0`" ref="bottomRef" v-if="loaded">
        <div
          v-if="!isMuted"
          :class="`w-[702px] send-msg-item h-[70px] ml-[24px] ${
            anchorInfo ? 'mt-[18.5px]' : 'mt-[18px]'
          }  mb-[15px] bg-[#1F1F20] rounded-[70px] flex items-center`"
        >
          <span
            @click="onJudgeSign"
            v-if="isNotSignIn()"
            class="h-full ml-[30px] flex items-center flex-grow text-[#94999F] bg-[#1F1F20] text-[28px]"
            >说点什么吧</span
          >
          <input
            v-else
            type="search"
            @focusin="onFocusin"
            @blur="onFocusout"
            @keydown.enter="onSendMsg"
            id="myInput"
            ref="inputRef"
            v-model="inputStr"
            placeholder="说点什么吧"
            enterkeyhint="send"
            :disabled="isMuted"
            @touchstart="onJudgeMute"
            class="h-full ml-[30px] flex-grow text-[#fff] bg-[#1F1F20] text-[28px]"
          />
          <div class="w-[2px] h-[40px] bg-[#3E3E40]"></div>
          <div class="w-[40px] h-[40px] click-style flex-shrink-0 ml-[28px] mr-[30px] cursor-pointer z-10" @touchend="onSendMsg">
            <img class="w-[40px] h-[40px] bg-contain" :src="inputStr ? sendMsgIcon : graySendMsgIcon" />
          </div>
        </div>
        <div
          v-else
          @click="onShowMuteToast"
          :class="`w-[702px] flex send-msg-item h-[70px] items-center justify-center rounded-[70px] ml-[24px] ${
            anchorInfo ? 'mt-[18.5px]' : 'mt-[18px]'
          }  mb-[15px] bg-[#1F1F20]  `"
        >
          <span class="text-[#94999F] text-[28px]">当前您已被禁言</span>
        </div>
      </div>
    </template>
  </layout-panel>
</template>
<script setup>
  import NoData from '@/components/common/EmptyData.vue';
  import ChatList from './ChatList.vue';
  import noticeIcon from '@/assets/img/room/notice-icon.png';
  import LayoutPanel from '@/layout/LayoutPanel.vue';
  import sendMsgIcon from '@/assets/img/room/send-msg-icon.png';
  import graySendMsgIcon from '@/assets/img/room/send-icon-disable.png';
  import { useUserStore } from '@/store/modules/user';
  import { showToast } from 'vant';
  import { useRoute, useRouter } from 'vue-router';
  import { useMessageStore } from '@/store/modules/message';
  import apis from '@/api/room';
  import { MessageCommand } from '@/views/message/utils/types';
  import downIcon from '@/assets/img/room/arrow-down-icon1.png';
  import upIcon from '@/assets/img/room/arrow-top-icon.png';
  import { isIos } from '@/utils/common';
  import { useAppStore } from '@/store/modules/app';

  const appStore = useAppStore();
  const iosDevice = isIos();
  const bottomRef = ref();
  const msgList = ref([]);
  const chatRef = ref();
  const loaded = ref(false);
  const router = useRouter();
  const route = useRoute();
  const messageStore = useMessageStore();
  const chartRef = ref();
  const { query } = route;
  const userStore = useUserStore();
  const messageList = ref([]);
  const inputStr = ref('');
  const inputRef = ref();
  const showNoticeSimple = ref(false);
  const showMore = ref(false);
  const noticeRef = ref('');
  const noticeItemRef = ref();
  const changeHeight = ref(true);
  const isMuted = ref(false);
  const props = defineProps({
    anchorList: {
      type: Array,
    },
    matchId: {
      type: String,
    },
    matchType: {
      type: String,
    },
    anchorInfo: {
      type: Object,
      default: null,
    },
  });
  const emits = defineEmits(['closeLive']);
  const groupId = query.liveId ? query.liveId : `${props.matchType}_${props.matchId}`;
  // 处理ws 加入群聊逻辑
  const handleWsJoinGroup = () => {
    const messageServer = messageStore.getMessageServer();
    if (!messageServer) {
      return;
    }
    // 用户是否登录
    // 用户已登录
    if (userStore.token) {
      // 判断ws是否已登录，已登录则直接加入群聊，未登录则监听onWebsocketUserLogin加入群聊
      if (messageServer.isLogin()) {
        messageServer.joinGroup(groupId);
      } else {
        messageServer.chatEmitter.on('onWebsocketUserLogin', (isLogin) => {
          if (!isLogin) return;
          messageServer.joinGroup(groupId);
        });
      }
    } else {
      // 用户未登录
      // 判断ws是否已连接，已连接则直接加入群聊，未连接则监听onWebsocketConnected加入群聊
      if (messageServer.isConnect()) {
        messageServer.joinGroup(groupId);
      } else {
        messageServer.chatEmitter.on('onWebsocketConnected', (isConnect) => {
          if (!isConnect) return;
          messageServer.joinGroup(groupId);
        });
      }
    }
  };

  onMounted(() => {
    const onCheck = () => {
      const messageServer = messageStore.getMessageServer();
      if (!messageServer) {
        setTimeout(() => {
          onCheck();
        }, 1000);
        return;
      }
      handleWsJoinGroup();
      messageServer.chatEmitter.on('message', (res) => {
        if (!res.data || res.data.chatType !== 1) {
          return;
        }
        addMessage(res.data);
      });
      messageServer.chatEmitter.on('liveBanned', (res) => {
        if (!res.data) {
          return;
        }
        isMuted.value = true;
        const hash = window.location.hash;
        if (hash && hash.indexOf('#/roomDetail') != -1) {
          onShowMuteToast();
        }
      });
      messageServer.chatEmitter.on('liveUnBanned', (res) => {
        if (!res.data) {
          return;
        }
        isMuted.value = false;
      });
      messageServer.chatEmitter.on('messageAll', onAddMsgData);
    };
    onCheck();
  });
  const onShowMuteToast = () => {
    showToast({
      className: 'custom-toast-live',
      message: '你已被禁言',
    });
  };
  onUnmounted(() => {
    onDestroyMsg();
  });
  onDeactivated(() => {
    if (appStore.clearRoom) {
      onDestroyMsg();
    }
  });
  const onDestroyMsg = () => {
    const messageServer = messageStore.getMessageServer();
    messageServer?.chatEmitter.off('messageAll', onAddMsgData);
    messageServer?.leaveGroup(groupId);
  };
  const onJudgeMute = () => {
    if (isMuted.value) {
      showToast({
        className: 'custom-toast-white',
        message: '您已被禁言',
      });
    }
  };
  const onAddMsgData = (res) => {
    const { command, code, data } = res;
    if (code === 10114) {
      isMuted.value = true;
      showToast('你已被禁言');
      return;
    }
    const { anchorInfo } = props;
    if (data && anchorInfo && data.anchorId + '' == anchorInfo.id) {
      if (command === MessageCommand.closeLive) {
        emits('closeLive');
      }
    }
  };
  const isNotSignIn = () => {
    const userStore = useUserStore();
    const token = userStore.token;
    return !token;
  };
  const onFocusin = () => {
    const userId = `${userStore.userInfo.token}`;
    if (!userId) {
      router.push(`/login?redirect=${route.fullPath}`);
      return;
    }
    if (isMuted.value) {
      showToast({
        className: 'custom-toast-white',
        message: '您已被禁言',
      });
      return;
    }
    document.body.addEventListener('touchmove', onStop, {
      passive: false,
    });
    if (iosDevice && appStore.showBar) {
      bottomRef.value.style.marginBottom = '80px';
    }
  };
  const onFocusout = () => {
    bottomRef.value.style.marginBottom = '0px';
    document.body.removeEventListener('touchmove', onStop, {
      passive: false,
    });
  };
  const onStop = (e) => {
    e.preventDefault(); // 阻止默认的处理方式(阻止下拉滑动的效果)
    return false;
  };
  const onShowMore = () => {
    showNoticeSimple.value = !showNoticeSimple.value;
  };
  const onJudgeSign = () => {
    if (isNotSignIn()) {
      router.push(`/login?redirect=${route.fullPath}`);
      return;
    }
  };

  const onSendMsg = async () => {
    if (isNotSignIn()) {
      onFocusout();
      router.push(`/login?redirect=${route.fullPath}`);
      return;
    }
    if (userStore.userInfo.isBanned || isMuted.value) {
      showToast({
        className: 'custom-toast-white',
        message: '您已被禁言',
      });
      return;
    }
    if (!inputStr.value || !inputStr.value.trim()) {
      showToast({
        className: 'custom-toast-white',
        message: '请输入内容',
      });
      return;
    }
    const messageServer = messageStore.getMessageServer();
    const sendMsg = formatSendData(inputStr.value);
    msgList.value.push(sendMsg);
    await messageServer.sendMessage(sendMsg);
    inputStr.value = '';
    inputRef.value.blur();
  };
  const formatSendData = (content) => {
    const { userInfo } = userStore;
    const { id, avatar, levelName, nickname, level } = userInfo;
    const userId = `${id}`;
    const sendData = {
      groupId,
      sendId: new Date().getTime(),
      content,
      cmd: 11,
      userId,
      chatType: '1',
      msgType: 0,
      from: userId,
      createTime: new Date().getTime(),
      to: '',
      toAvatar: '',
      toNickName: '',
      fromAvatar: avatar,
      fromNickName: nickname || levelName,
      identityType: 0,
      level,
      anchorId: '',
    };
    return sendData;
  };

  const addMessage = (message) => {
    if ((message.identityType && message.identityType != 3) || message.anchorId) {
      message.levelName = '主播';
      message.fromNickName = props.anchorInfo?.nickName || message.fromNickName;
    } else {
      message.levelName = userStore.convertLevelName(message.level);
    }
    const { content } = message;
    if (
      content &&
      (content.indexOf('http://') != -1 || content.indexOf('https://') != -1) &&
      (content.lastIndexOf('.jpg') != -1 || content.lastIndexOf('.png') != -1 || content.lastIndexOf('.gif') != -1)
    ) {
      message.receiveContentType = 'img';
    }
    if (!message.id) {
      message.id = new Date().getTime() + '';
    }
    messageList.value.push(message);
    nextTick(() => {
      setTimeout(() => {
        if (chatRef.value) {
          chatRef.value.onScrollBottom();
        }
      }, 200);
    });
  };
  const initFirst = (message) => {
    apis.getMsgHistory({ groupId, offset: null, size: 10000, chatType: 1, type: 0, searchType: 3 }).then((res) => {
      res &&
        res.map((item) => {
          addMessage(item);
        });
      if (message && message.content) {
        // console.log('messageContent', message.content);
        // const findReplaces = message.content.match(/<([\s\S]*?)>/);
        // const anchorStr = `<span style='color: #34A853;font-weight:500;margin-right:5px;flex-shrink:0;'>${
        //   message.fromNickName + ':'
        // }</span>`;
        // if (findReplaces && findReplaces[1]) {
        //   var replaceStr = findReplaces[1];
        //   message.content = message.content.replace(`<${replaceStr}>`, `<${replaceStr}>${anchorStr}`);
        // } else {
        //   message.content = `<div>${anchorStr}${message.content}</div>`;
        // }
        // const parser = new DOMParser();
        // const doc = parser.parseFromString(message.content, 'text/html');
        // //text/hml 表示生成html节点元素
        // const divNode = doc.body.firstChild;
        // const video = divNode.childNodes[0];
        // console.log('video', video, message.content);
        // const placeholder = document.createElement('p');
        // // placeholder.appendChild(video);
        // video.parentNode.replaceChild(placeholder, video);
        // message.content = "<div><span style='color: #34A853;font-weight:500;margin-right:5px;flex-shrink:0;'>Car:</span>欢迎光临xx</div>";
        addMessage({ ...message, htmlText: true });
      }
      loaded.value = true;
    });
  };
  const initNotice = () => {
    nextTick(() => {
      if (noticeRef.value.offsetHeight > 58) {
        showMore.value = true;
        showNoticeSimple.value = true;
      } else {
        showMore.value = false;
      }
      if (noticeItemRef.value) {
        setTimeout(() => {
          chartRef.value.style.marginTop = noticeItemRef.value.offsetHeight + 12 + 'px';
        }, 0);
      }
    });
  };
  const onChangeHeight = (isCurrent) => {
    if (isCurrent) {
      changeHeight.value = true;
    } else {
      changeHeight.value = !changeHeight.value;
    }
  };
  const initMuted = (value) => {
    isMuted.value = value;
  };
  defineExpose({
    addMessage,
    initFirst,
    initNotice,
    onChangeHeight,
    initMuted,
  });
</script>
<style scoped lang="scss">
  .other-page {
    height: calc(var(--device-height) - 660px);
  }
  .chat-btm {
    border-top: 1px solid #1f1f20;
    box-shadow: 0px -1px 0px 0px rgba(0, 0, 0, 0.3);
    backdrop-filter: blur(20px);
  }
  .send-msg-item {
    backdrop-filter: blur(20px);
  }
  .current-page {
    height: 100%;
    // min-height: calc(var(--device-height) - 680px);
  }
  .chat-page {
    font-family: PingFang SC;

    input {
      caret-color: #fff;
      caret-shape: auto; /* 修改光标形状 */
      caret-width: 3px;
    }

    input[type='search']::-webkit-search-decoration,
    input[type='search']::-webkit-search-cancel-button {
      display: none;
    }
    input {
      -webkit-appearance: none;
    }

    ::-webkit-input-placeholder {
      /* WebKit browsers */
      color: #94999f;
    }
    :-moz-placeholder {
      /* Mozilla Firefox 4 to 18 */
      color: #94999f;
    }
    ::-moz-placeholder {
      /* Mozilla Firefox 19+ */
      color: #94999f;
    }
    :-ms-input-placeholder {
      /* Internet Explorer 10+ */
      color: #94999f;
    }

    .empty-data__text {
      color: #5b5b5b !important;
    }
    .level-item {
      border-radius: 40px;
      background: rgba(255, 255, 255, 0.1);
      font-family: 'PingFang SC';
      font-style: normal;
      font-weight: 400;
    }
    .level-anchor {
      border-radius: 40px;
      background: rgba(52, 168, 83, 0.2);
      font-family: 'PingFang SC';
      font-style: normal;
      font-weight: 500;
    }
    .level {
      display: inline-block;
      width: 24px;
      height: 26.38px;
      background: url('@/assets/img/user/level/v1.png') no-repeat, transparent;
      background-size: auto 100%;
      background-position: center;
      margin-bottom: 2px;
      @for $i from 1 through 6 {
        &.level-#{$i} {
          background: url('@/assets/img/user/level/v#{$i}.png') no-repeat, transparent;
          background-size: auto 100%;
          background-position: center;
        }
      }
    }

    .notice-item {
      box-shadow: 0 8px 20px 0 rgba(18, 18, 19, 0.4);
    }
  }
</style>
