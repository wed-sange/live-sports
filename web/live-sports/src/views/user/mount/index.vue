<template>
  <van-popup v-model:show="show" ref="rootRef" teleport="#app" position="bottom" @closed="afterClose">
    <div class="login">
      <h2>登录</h2>
    </div>
  </van-popup>
</template>

<script lang="ts" setup>
  import { ref, reactive, nextTick } from 'vue';
  import type { PopupInstance } from 'vant';
  import mitt from 'mitt';
  import { ApiFormEvents } from './types';
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
  defineExpose({
    show,
    formMitt,
  });
</script>

<script lang="ts">
  export default {
    name: 'LoginPopup',
  };
</script>
<style scoped lang="scss">
  .login {
    padding: 20px;
    color: #fff;
    h2 {
      text-align: center;
      letter-spacing: 10px;
    }
  }
</style>
