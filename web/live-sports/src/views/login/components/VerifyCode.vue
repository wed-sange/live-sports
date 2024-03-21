<template>
  <van-button type="primary" class="verify-code-btn" :class="{ primary: !verify.timer }" :disabled="disabled" @click="$emit('on-verify')">{{
    verify.text
  }}</van-button>
</template>
<script setup lang="ts">
  import { reactive } from 'vue';
  import dayjs from 'dayjs';
  const props = withDefaults(
    defineProps<{
      disabled?: boolean;
      count?: number;
      beforeValidate?: (() => boolean) | (() => Promise<boolean>) | null;
      action?: Function | null;
    }>(),
    {
      count: 60,
      beforeValidate: null,
      disabled: false,
    },
  );
  const verify = reactive<{
    text: string;
    begin: number | null;
    timer: number | null;
  }>({
    text: '获取验证码',
    begin: null,
    timer: null,
  });
  const createTimer = () => {
    if (!verify.begin) {
      const now = dayjs().set('millisecond', 0);
      verify.begin = now.valueOf();
    }
    const diff = props.count - dayjs().set('millisecond', 0).diff(verify.begin, 'second');
    if (diff <= 0) {
      clearTimer();
      verify.text = '重新发送';
      return;
    }
    verify.text = `${diff}S`;
    verify.timer = setTimeout(() => {
      verify.timer && clearTimeout(verify.timer);
      verify.timer = null;
      createTimer();
    }, 1000) as unknown as number;
  };
  const clearTimer = () => {
    verify.timer && clearTimeout(verify.timer);
    verify.timer = null;
    verify.begin = null;
  };
  const send = async () => {
    if (verify.timer) return;
    let validate = true;
    if (props.beforeValidate) {
      validate = await props.beforeValidate();
    }
    if (!validate) {
      return;
    }
    clearTimer();
    createTimer();
    if (props.action) {
      props.action();
    }
  };
  defineExpose({
    send,
  });
</script>
<script lang="ts">
  export default {
    name: 'VerifyCode',
  };
</script>

<style scoped lang="scss">
  .van-button {
    width: 180px;
  }
</style>
