<template>
  <page-box class="personal-info-page">
    <template #header>
      <AppHeader /><NavBar title="编辑资料" left-arrow @click-left="onLeftClick">
        <template #right>
          <van-button class="personal-save" :disabled="model.disabled" @click="save">保存</van-button>
        </template>
      </NavBar>
    </template>
    <div class="w-full h-full">
      <div class="personal-info__item px-[24px] cursor-pointer" @click="updateAvatar">
        <div class="flex justify-between items-center w-full pt-[30px] pb-[40px] cursor-pointer text-[28px] van-hairline--bottom">
          <div class="personal-info__label flex-auto w-full"> 头像 </div>
          <div class="personal-info__info shrink-0 grow-0 basis-auto flex items-center">
            <div class="h-[100px] w-[100px] relative">
              <img class="inline-block w-full h-full rounded-[100%] overflow-hidden" :src="model.avatar || defaultImgSrc" alt="logo" />
              <input ref="avatarInputRef" class="personal-info__info--file" type="file" accept="image/*" @change="handleChooseFile" />
            </div>
          </div>
        </div>
      </div>
      <div class="px-[24px] cursor-pointer">
        <div class="flex justify-between items-center w-full py-[40px] cursor-pointer text-[28px] van-hairline--bottom">
          <div class="personal-info__label flex-auto w-full"> 昵称 </div>
          <div class="personal-info__info shrink-0 grow-0 basis-auto flex items-center">
            <div>
              <van-field
                class="personal-info__input"
                maxlength="8"
                v-model="model.name"
                @input="handleInputChange"
                placeholder="请输入昵称"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import { uploadAvatar, updateUserInfo } from '@/api/user';
  import { useUserStore } from '@/store/modules/user';
  import defaultImgSrc from '@/assets/img/user/default_user.png';
  // import { showToast, showLoadingToast, allowMultipleToast } from 'vant';
  import { showToast, allowMultipleToast } from 'vant';
  import { createLoading, closeLoading } from '@/components/SysLoading';

  const userStore = useUserStore();

  const model = reactive({
    name: userStore.userInfo.nickname,
    avatar: userStore.userInfo.avatar,
    disabled: true,
  });
  const router = useRouter();
  const onLeftClick = () => {
    router.go(-1);
  };

  const save = async () => {
    if (model.disabled) return;
    allowMultipleToast();
    // const toast = showLoadingToast({
    //   duration: 0,
    //   forbidClick: true,
    //   message: '更新中',
    // });
    try {
      createLoading();
      await updateUserInfo({
        name: model.name,
        head: model.avatar,
      });
      await userStore.getUserInfo();

      showToast({
        className: 'custom-toast',
        message: '保存成功',
      });
      onLeftClick();
    } finally {
      closeLoading();
    }
  };
  const handleInputChange = () => {
    if (model.disabled) {
      model.disabled = false;
    }
  };
  const avatarInputRef = ref<HTMLInputElement | null>(null);
  const updateAvatar = () => {
    if (avatarInputRef.value) {
      avatarInputRef.value.value = '';
    }
    avatarInputRef.value && avatarInputRef.value.click();
  };
  const handleChooseFile = async (event: Event) => {
    const files = (event.target as HTMLInputElement).files;
    if (files && files.length >= 1) {
      const file = files[0];
      // const toast = showLoadingToast({
      //   duration: 0,
      //   forbidClick: true,
      //   message: '上传中',
      // });
      createLoading();
      try {
        const response = await uploadAvatar({
          file: file,
        });
        if (response) {
          model.avatar = response;
          model.disabled = false;
        } else {
          showToast({
            className: 'custom-toast',
            message: '头像上传失败',
          });
        }
      } finally {
        // toast.close();
        closeLoading();
      }
    }

    if (avatarInputRef.value) {
      avatarInputRef.value.value = '';
    }
  };
</script>
<script lang="ts">
  export default {
    name: 'UserPersonalInfo',
  };
</script>

<style scoped lang="scss">
  .personal-info-page {
    background: #fff;
  }
  .van-hairline--bottom {
    &::after {
      --van-border-color: #f2f3f7;
    }
  }
  .personal-save.van-button {
    --van-button-default-background: #34a853;
    --van-button-default-color: #fff;
    --van-button-radius: 56px;
    --van-button-normal-padding: 0;
    --van-button-default-height: 50px;
    border: none;
    width: 100px;
    font: 400 26px / var(--van-button-default-height) 'PingFang SC';
    &.van-button--disabled {
      --van-button-default-background: #f2f3f7;
      --van-button-default-color: #94999f;
      --van-button-disabled-opacity: 1;
    }
  }
  .personal-info__label {
    font: 400 28px / normal 'PingFang SC';
    color: #37373d;
    padding-left: 2px;
  }
  .personal-info__info {
    font: 500 28px / normal 'PingFang SC';
    color: #37373d;

    .personal-info__info--file {
      position: absolute;
      cursor: pointer;
      top: 0;
      left: 0;
      opacity: 0;
      z-index: 1;
      width: 0;
      height: 0;
      overflow: hidden;
    }

    .personal-info__input.van-field {
      --van-cell-background: transparent;
      --van-field-input-text-color: #37373d;
      --van-field-placeholder-text-color: #8a8c90;
      --van-cell-vertical-padding: 0;
      --van-cell-horizontal-padding: 0;
      font: 500 28px / normal 'PingFang SC';
      width: 620px;
      :deep(.van-field__control) {
        text-align: right;
      }
    }
  }
  .personal-info__item {
    @include use-active-style();
  }
</style>
