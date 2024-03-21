<template>
  <page-box class="message-page pb-[114px]" :class="[currentTab]">
    <template #header>
      <AppHeader />
    </template>

    <van-tabs v-model:active="currentTab" animated>
      <template #nav-right>
        <van-button
          :disabled="readDotAll <= 0"
          :class="['message-clear-btn', { 'blue-btn': readDotAll > 0 }]"
          v-show="currentTab === 'message'"
          @click="handleClear"
          type="primary"
        >
          全部已读
        </van-button>
      </template>
      <van-tab title="消息" name="message"><MessageList /></van-tab>
      <van-tab title="好友" name="member"><MemberList /></van-tab>
    </van-tabs>
  </page-box>
</template>

<script lang="ts" setup>
  import MemberList from './components/MemberList/index.vue';
  import MessageList from './components/MessageList/index.vue';
  import { showConfirmDialog } from 'vant';
  import { useMessageStore } from '@/store/modules/message';

  const messageStore = useMessageStore();
  // messageStore.createMessageServer();
  const messageServer = messageStore.getMessageServer();
  const readDotAll = computed(() => {
    return messageStore.readDotAll;
  });

  // if (messageServer) {
  //   onActivated(() => {
  //     messageServer.createSocket();
  //   });
  //   onDeactivated(() => {
  //     messageServer.destroy();
  //   });
  // }
  const currentTab = ref('message');
  const handleClear = () => {
    if (readDotAll.value <= 0) {
      return;
    }
    showConfirmDialog({
      message: '确定清除全部的未读信息吗？',
      className: 'custom-dialog-confirm',
      overlayStyle: { background: 'rgba(34, 35, 36, 0.60)' },
    })
      .then(async () => {
        messageServer?.clearDot();
      })
      .catch(() => {
        // on cancel
      });
  };
</script>

<script lang="ts">
  export default {
    name: 'Message',
  };
</script>

<style lang="scss" scoped>
  .message-page {
    background: #fff;
    &.member {
      :deep(.van-tabs) {
        .van-tabs__line {
          left: -1.4px;
        }
      }
    }
  }
  :deep(.van-tabs) {
    --van-tabs-nav-background: transparent;
    --van-tabs-line-height: 72px;
    height: 100%;
    display: flex;
    flex-flow: column;
    .van-tabs__nav--line {
      padding-bottom: 16px;
    }
    .van-tabs__wrap {
      flex: 0 0 var(--van-tabs-line-height);
      height: var(--van-tabs-line-height);
      overflow: visible;
    }
    .van-tabs__content {
      flex: 1 1 auto;
      .van-tab__panel {
        height: 100%;
      }
    }
    .van-tabs__nav {
      justify-content: flex-start;
    }
    .van-tab {
      --van-padding-base: 24px;
      --van-tab-text-color: #94999f;
      --van-tab-active-text-color: #37373d;
      font: 400 32px / normal 'PingFang SC';
      flex: 0 0 auto;
      padding-top: 8px;
      @include use-active-style();
      & ~ .van-tab {
        margin-left: 12px;
      }
      .van-tab__text {
        font: inherit;
        color: inherit;
      }
    }
    .van-tab--active {
      --van-font-bold: 500;
      font-size: 36px;
      font-weight: 500;
    }
    .van-tabs__line {
      --van-tabs-bottom-bar-width: 46px;
      --van-tabs-bottom-bar-height: 6px;
      --van-tabs-bottom-bar-color: #34a853;
      border-radius: 80px;
      bottom: 16px;
      left: 1.4px;
    }
  }
  .message-clear-btn {
    --van-button-primary-color: rgba(207, 210, 212, 1);
    --van-button-primary-background: #f2f3f7;
    --van-button-normal-padding: 9px 16px 12px 16px;
    --van-button-radius: 50px;
    --van-button-default-height: 50px;
    font: 400 22px / normal 'PingFang SC';
    border: none;
    position: absolute;
    top: 12px;
    right: 24px;
    .van-button__text {
      font: inherit;
      color: inherit;
    }
  }
  .blue-btn {
    background: rgba(52, 168, 83, 1);
    color: white;
  }
  .clear-icon {
    width: 40px;
    height: 40px;
    z-index: 1;
  }
</style>
