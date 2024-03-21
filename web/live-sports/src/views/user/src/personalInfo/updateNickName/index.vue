<template>
  <teleport to="body">
    <div class="custom-popup-box">
      <van-popup v-model:show="show" class="custom-popup" ref="rootRef" position="right" @closed="afterClose">
        <page-box>
          <template #header>
            <NavBar title="个人资料" transparent left-text="取消" @click-left="onLeftClick" @click-right="sure">
              <template #right> <div class="custom-right" v-ripple>完成</div> </template>
            </NavBar>
          </template>
          <div class="w-full">
            <div class="form-group__info w-full"> <van-field class="keyboard-abr" v-model="model.name" placeholder="请输入昵称" /></div>
            <div class="form-group__tip w-full">每个月只能修改一次昵称，长度不能超过8个字符</div>
          </div>
        </page-box>
      </van-popup>
    </div>
  </teleport>
</template>

<script lang="ts" setup>
  import { ref, reactive, nextTick } from 'vue';
  import mitt from 'mitt';
  import { useUserStore } from '@/store/modules/user';
  import { updateUserInfo } from '@/api/user';
  import { showLoadingToast, allowMultipleToast, type PopupInstance } from 'vant';
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
  const onLeftClick = () => {
    show.value = false;
  };

  const userStore = useUserStore();
  const model = reactive({
    name: userStore.userInfo.nickname,
  });
  const sure = async () => {
    allowMultipleToast();
    const toast = showLoadingToast({
      duration: 0,
      forbidClick: true,
      message: '更新中',
    });
    try {
      await updateUserInfo({
        name: model.name,
      });
      await userStore.getUserInfo();

      onLeftClick();
    } finally {
      toast.close();
    }
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
  .custom-popup-box {
    :deep().van-popup.custom-popup {
      width: 100%;
      height: 100%;
    }

    :deep().van-nav-bar__right {
      // --van-nav-bar-text-color: #5b6271;
      --van-nav-bar-text-color: #fff;
      padding: 0;
      .custom-right {
        font: inherit;
        color: inherit;
        height: 100%;
        padding: 0 32px;
        color: #fff;
        display: flex;
        align-items: center;
      }
    }
    .form-group__info {
      .van-field {
        --van-cell-background: #18152a;
        --van-field-input-text-color: #fff;
        --van-field-placeholder-text-color: #8a8c90;
        --van-cell-vertical-padding: 42px;
        color: #fff;
        font: 400 28px / 44px 'PingFang SC';
      }
    }
    .form-group__tip {
      font: 400 24px / normal 'PingFang SC';
      color: #5b6271;
      padding: 24px 32px;
    }
  }
</style>
