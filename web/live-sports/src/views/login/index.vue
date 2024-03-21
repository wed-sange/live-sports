<template>
  <page-box class="login-page">
    <template #header>
      <AppHeader />
      <NavBar left-arrow title="登录注册" @click-left="onLeftClick" />
    </template>
    <div class="w-full h-full overflow-auto">
      <div class="pt-[64px] pr-[2px] pb-[17px] w-full">
        <div class="w-[416px] h-[48px] rounded-none mx-auto overflow-hidden">
          <img class="inline-block w-full h-full rounded-none overflow-hidden" src="@/assets/img/common/logo.png" alt="logo" />
        </div>
        <!-- <div class="w-full text-center text-[36px] whitespace-nowrap font-bold py-[40px]">足球直播</div> -->
      </div>
      <van-tabs v-model:active="model.loginType" shrink animated>
        <van-tab name="phoneVerify">
          <template #title>
            <div class="custom-btn">手机号登录</div>
          </template>
          <PhoneVerify @on-verify="onShowVerify" @on-sure="sure" ref="phoneVerifyRef" />
        </van-tab>
        <van-tab name="emailVerify">
          <template #title>
            <div class="custom-btn">邮箱登录</div>
          </template>
          <EmailVerify @on-verify="onShowVerify" @on-sure="sure" ref="emailVerifyRef" />
        </van-tab>
      </van-tabs>

      <div class="w-full px-[60px]">
        <van-button class="submit-btn" type="primary" :disabled="isEmptyForm" @click="sure">注册/登录</van-button>
      </div>
    </div>
    <VerifyItem
      @on-success="onVerifySuccess"
      mode="pop"
      class=""
      :captchaType="captchaType"
      :imgSize="{ width: '300px', height: '200px' }"
      ref="verifyRef"
    />

    <!-- <template #footer><div class="grow-0 shrink-0 basis-auto h-[80px]"> woshi底部 </div></template> -->
  </page-box>
</template>

<script lang="ts" setup>
  import PhoneVerify from './components/PhoneVerify.vue';
  import EmailVerify from './components/EmailVerify.vue';
  import VerifyItem from '@/components/Verifition/Verify.vue';

  const captchaType = ref('clickWord'); // clickWord点击文字确认  blockPuzzle滑块
  const verifyRef = ref();
  const router = useRouter();
  const hashs = window.location.hash.split('redirect=');
  const url = (hashs ? hashs[1] : '') as string;
  const onLeftClick = () => {
    if (url) {
      router.replace(url);
    } else {
      //todo 测试下来back可以实现效果，不知router.replace为什么无法做到从那里进入返回那个界面
      window.history.back();
      // router.replace('/');
    }
  };
  const onVerifySuccess = (token) => {
    if (model.loginType === 'phoneVerify') {
      phoneVerifyRef.value && phoneVerifyRef.value.sendVerifyCode(token);
    } else {
      emailVerifyRef.value && emailVerifyRef.value.sendVerifyCode(token);
    }
  };
  const onShowVerify = () => {
    verifyRef.value.show();
  };
  const model = reactive<{
    loginType: 'phoneVerify' | 'emailVerify';
  }>({
    loginType: 'phoneVerify',
  });
  const phoneVerifyRef = ref<typeof PhoneVerify | null>(null);
  const emailVerifyRef = ref<typeof EmailVerify | null>(null);
  const isEmptyForm = computed(() => {
    if (model.loginType === 'phoneVerify') {
      return phoneVerifyRef.value ? phoneVerifyRef.value.isEmptyForm : true;
    } else if (model.loginType === 'emailVerify') {
      return emailVerifyRef.value ? emailVerifyRef.value.isEmptyForm : true;
    } else {
      return true;
    }
  });
  const sure = async () => {
    let form: typeof PhoneVerify | typeof EmailVerify | null = null;
    if (model.loginType === 'phoneVerify') {
      form = phoneVerifyRef.value;
    } else {
      form = emailVerifyRef.value;
    }
    if (!form) {
      console.log('error');
      return;
    }
    const response = await form.login();
    response && onLeftClick();
  };
</script>

<script lang="ts">
  export default {
    name: 'Login',
  };
</script>
<style scoped lang="scss">
  .login-page.page-box {
    background: #fff;
  }
  :deep(.van-tabs) {
    margin-top: 48px;
    .van-tabs__nav {
      justify-content: center;
      padding-left: 2px;
      padding-right: 0;
    }
    .van-tabs__wrap {
      --van-tabs-line-height: 74px;
      .van-tab.van-tab--line {
        width: 160px;
        border: none;
        border-radius: 0;
        color: #000;
        font: 400 32px / normal 'PingFang SC';
        padding: 0;
        & + .van-tab--line {
          margin-left: 168px;
        }
        .van-tab__text {
          display: block;
          width: 100%;
          height: 100%;
          border-radius: inherit;
          .custom-btn {
            display: flex;
            width: 100%;
            height: 100%;
            align-items: center;
            justify-content: center;
            border-radius: inherit;
            white-space: nowrap;
          }
        }
      }
      .van-tabs__line {
        --van-tabs-bottom-bar-width: 76px;
        --van-tabs-bottom-bar-height: 6px;
        --van-tabs-bottom-bar-color: #34a853;
        border-radius: 80px;
        bottom: 31px;
      }
    }
    .van-tabs__content {
      padding-top: 59px;
    }
  }
  .page-box {
    :deep(.form-group__title) {
      font: 500 28px / normal 'PingFang SC';
      color: #37373d;
      padding: 0 0 24px 16px;
    }
    :deep(.form-group) {
      background: #fff;
      height: 102px;
      @apply w-full flex items-stretch mb-[40px] rounded-[86px] overflow-hidden;
      border: 2px solid #dddde9;
      .form-label {
        height: 100%;
      }
      .form-label.form-label__divide {
        position: relative;
        &::after {
          content: '';
          display: block;
          height: 52px;
          width: 2px;
          background: #dddde9;
          position: absolute;
          top: 50%;
          right: 0;
          z-index: 1;
          transform: translate(100%, -52%);
        }
        .form-btn.van-button {
          padding: 2px 19px 0 28px;
          width: 100%;
          .van-button__content {
            justify-content: flex-end;
          }
          .van-icon {
            font-size: 31px;
          }
        }
      }
      .form-btn.van-button {
        --van-button-primary-background: transparent;
        border: none;
        padding: 0;
        height: 100%;
        width: 150px;
        font: 400 30px / normal 'PingFang SC';
        white-space: nowrap;
        color: #a6a6b2;
        &.primary:not(.van-button--disabled) {
          color: #34a853;
        }
        &.van-button--disabled {
          opacity: 1;
          color: #a6a6b2;
        }
        &.verify-code-btn {
          .van-button__content {
            justify-content: flex-end;
          }
        }
      }
      .form-input.van-field {
        --van-cell-background: transparent;
        --van-field-placeholder-text-color: #a6a6b2;
        --van-field-input-text-color: #37373d;
        --van-cell-vertical-padding: 28px;
        --van-cell-horizontal-padding: 24px;
        padding-left: 28px;
        font: 500 30px / normal 'PingFang SC';

        .van-field__control::-webkit-input-placeholder {
          color: var(--van-field-placeholder-text-color);
          font-weight: 400;
        }
        .van-field__control::placeholder {
          color: var(--van-field-placeholder-text-color);
          font-weight: 400;
        }
        &.form-input__phone {
          padding-left: 24px;
        }
      }
    }
  }
  .submit-btn {
    @apply bg-[#34A853];
    border: none;
    border-radius: 86px;
    width: 100%;
    color: #fff;
    font: 500 32px / normal 'PingFang SC';
    padding: 2px 0 0 2px;
    height: 102px;
    margin-top: 20px;
    margin-bottom: 40px;
    &.van-button--disabled {
      @apply bg-[#F2F3F7];
      color: #94999f;
      opacity: 1;
    }
  }
</style>
