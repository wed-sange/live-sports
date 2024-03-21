<template>
  <teleport to="body">
    <div class="custom-popup-box">
      <van-popup
        v-model:show="show"
        class="custom-popup"
        :safe-area-inset-bottom="true"
        :overlay-style="{ background: 'transparent' }"
        ref="rootRef"
        position="bottom"
        @closed="afterClose"
      >
        <div class="floating-action-sheet-box">
          <div class="floating-action-sheet__bar" v-pressed-slide="handlePressedSlide"></div>
          <div class="floating-action-sheet__list">
            <template v-for="item in actions" :key="item.key">
              <div class="floating-action-sheet__item" :class="[item.class]" @click="handleClick(item)">
                <van-button class="click-style">{{ item.name }}</van-button>
              </div>
            </template>
          </div>
          <template v-if="cancelText">
            <div class="floating-action-sheet__footer click-style">
              <div class="floating-action-sheet__gap"></div>
              <div class="floating-action-sheet__cancel" @click="close">
                {{ cancelText }}
              </div>
            </div>
          </template>
        </div>
      </van-popup>
    </div>
  </teleport>
</template>

<script lang="ts" setup>
  import { ref, reactive, nextTick } from 'vue';
  import mitt from 'mitt';
  import { type PopupInstance } from 'vant';
  import { ApiFormEvents, ActionItem } from './types';
  const rootRef = ref<PopupInstance | null>(null);
  const show = ref(false);
  const formMitt = reactive(mitt());
  const afterClose = () => {
    nextTick(() => {
      formMitt.emit(ApiFormEvents.afterClose);
      if (rootRef.value) {
        rootRef.value.$el.remove && rootRef.value.$el.remove();
      }
    });
  };
  const cancelText = ref('');
  const closeOnClickAction = ref(false);
  const actions = ref<ActionItem[]>([]);
  const init = (_actions: ActionItem[], _cancelText?: string, _closeOnClickAction?: boolean) => {
    cancelText.value = _cancelText || '';
    closeOnClickAction.value = _closeOnClickAction === true;
    actions.value = _actions;
  };
  const handlePressedSlide = (start, move) => {
    if (move.y - start.y >= 10) {
      close();
    }
  };
  const handleClick = (item: ActionItem) => {
    setTimeout(() => {
      formMitt.emit(ApiFormEvents.select, item);
      if (closeOnClickAction.value) {
        close();
      }
    }, 100);
  };
  const close = () => {
    setTimeout(() => {
      show.value = false;
    }, 100);
  };
  defineExpose({
    show,
    formMitt,
    init,
  });
</script>

<script lang="ts">
  export default {
    name: 'FloatingActionSheet',
  };
</script>
<style scoped lang="scss">
  .custom-popup-box {
    :deep().van-popup.custom-popup {
      //   width: 100%;
      //   height: 100%;
      // background: transparent;
      border-radius: 32px 32px 0px 0px;
      overflow: hidden;
    }
    .floating-action-sheet-box {
      background: #fff;
    }
    .floating-action-sheet__bar {
      padding: 28px 0 0;
      position: relative;
      &::before {
        content: '';
        display: block;
        width: 72px;
        height: 8px;
        background: #eff1f5;
        border-radius: 8px;
        position: absolute;
        top: 20px;
        left: 50%;
        z-index: 2;
        transform: translate(-50%, 0);
      }
    }
    .floating-action-sheet__item {
      width: 100%;
      height: 102px;
      border: none;
      font: 400 30px / 102px 'PingFang SC';
      color: #37373d;
      padding: 0;
      position: relative;
      &.primary {
        color: #34a853;
      }
      &.error {
        color: #d63823;
      }
      &::after {
        content: '';
        display: block;
        width: 702px;
        height: 2px;
        background: #f2f3f7;
        position: absolute;
        bottom: 0;
        left: 24px;
        z-index: 2;
      }
      .van-button {
        display: block;
        width: 100%;
        height: 100%;
        border: none;
        padding: 0;
        text-align: center;
        background: #fff;
        font: inherit;
        color: inherit;
        :deep(.van-button__text) {
          font: inherit;
          color: inherit;
        }
      }
    }
    .floating-action-sheet__footer {
      padding: 0 0 14px;
    }
    .floating-action-sheet__gap {
      height: 60px;
      background: #fff;
    }
    .floating-action-sheet__cancel {
      width: 702px;
      height: 80px;
      border-radius: 48px;
      background: #f2f3f7;
      margin: 0 auto;
      font: 500 30px / 80px 'PingFang SC';
      padding: 0;
      color: #94999f;
      text-align: center;
    }
  }
</style>
