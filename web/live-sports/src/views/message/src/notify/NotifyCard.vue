<template>
  <div class="notify-card-box w-full px-[24px] pb-[30px]">
    <div class="notify-card__tip pb-[30px] text-center truncate" v-show="item.tip">{{ item.tip }}</div>
    <div class="notify-card w-full p-[30px]">
      <div class="notify-card__title pb-[24px]">
        <div class="notify-card__title--text">系统通知</div>
      </div>
      <div class="notify-card__text">
        {{ item.content }}
      </div>
      <div class="notify-card__text" v-show="item.result"> {{ item.type === '1' ? '反馈结果' : '原因' }}：{{ item.result }} </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import { type NotifyCardData } from './useIndex';
  withDefaults(
    defineProps<{
      item: NotifyCardData;
    }>(),
    {
      item: () => {
        return {} as NotifyCardData;
      },
    },
  );
</script>
<script lang="ts">
  export default {
    name: 'NotifyCard',
  };
</script>
<style scoped lang="scss">
  .notify-card-box {
    .notify-card {
      background: #fff;
      border-radius: 20px;
    }
    .notify-card__tip {
      font: 400 22px / normal 'PingFang SC';
      color: #94999f;
    }
    .notify-card__title {
      position: relative;
      &::before {
        content: '';
        display: block;
        width: 100%;
        height: 2px;
        background: #f2f3f7;
        position: absolute;
        bottom: 0;
        left: 0;
        z-index: 2;
      }
      .notify-card__title--text {
        font: 500 30px / 52px 'PingFang SC';
        color: #37373d;
        padding-left: 68px;
        background: url('@/assets/img/message/notify-small.png') no-repeat, transparent;
        background-size: 52px 52px;
        background-position: 0 0;
      }
    }
    .notify-card__text {
      padding-top: 24px;
      font: 400 30px / normal 'PingFang SC';
      color: #37373d;
    }
  }
</style>
