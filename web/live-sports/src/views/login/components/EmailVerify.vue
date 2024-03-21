<template>
  <div class="w-full px-[60px]">
    <div class="form-group__title">邮箱</div>
    <div class="form-group">
      <!-- <div class="form-label form-label__divide grow-0 shrink-0 basis-auto w-[108px] text-right">
        <van-button class="form-btn" type="primary"><van-icon name="free-postage" /></van-button>
      </div> -->
      <div class="flex-auto">
        <van-field class="form-input keyboard-abr" v-model="model.email" placeholder="请输入邮箱号" />
      </div>
    </div>
    <div class="form-group__title">验证码</div>
    <div class="form-group flex-row-reverse">
      <div class="grow-0 shrink-0 basis-auto pr-[28px]">
        <VerifyCode
          ref="verifyRef"
          @on-verify="$emit('on-verify')"
          :before-validate="verificationBefore"
          :disabled="isEmptyInput"
          :action="sendVerification"
          class="form-btn"
        />
        <!-- <van-button class="form-btn" type="primary" @click="sendVerification">获取验证码</van-button> -->
      </div>
      <div class="flex-auto">
        <van-field
          @keyup.enter="$emit('on-sure')"
          enterkeyhint="done"
          class="form-input keyboard-abr"
          v-model="model.verification"
          placeholder="请输入验证码"
        />
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  import VerifyCode from './VerifyCode.vue';
  import { emailSend } from '@/api/user';
  import { useValidate, type ValidateRule } from '../useValidate';
  import { useUserStore } from '@/store/modules/user';
  import { showToast } from 'vant';

  const verifyRef = ref();
  const model = reactive({
    email: '',
    verification: '',
    token: '', //图形验证码token
  });
  const isEmptyInput = computed(() => {
    return model.email === '';
  });
  const isEmptyForm = computed(() => {
    return model.email === '' || model.verification === '';
  });
  const sendVerifyCode = async (token) => {
    model.token = token;
    await emailSend({ email: model.email, lang: 1 });
    verifyRef.value?.send();
  };
  const rules = reactive<ValidateRule<typeof model>>({
    email: [
      {
        validate: (value) => {
          if (!value) {
            return {
              result: false,
              msg: '请输入邮箱号',
            };
          }
          const regEmail = /^([a-zA-Z\d][\w-]{0,})@(\w{2,})\.([a-z]{2,})(\.[a-z]{2,})?$/;
          if (!regEmail.test(value)) {
            return {
              result: false,
              msg: '邮箱格式错误',
            };
          }
          return {
            result: true,
            msg: '',
          };
        },
      },
    ],
    verification: [
      {
        validate: (value) => {
          if (!value) {
            return {
              result: false,
              msg: '请输入验证码',
            };
          }
          return {
            result: true,
            msg: '',
          };
        },
      },
    ],
  });
  const getRule = (type?: string) => {
    let rule = {};
    Object.entries(rules).forEach(([key, value]) => {
      if (type === 'verification') {
        if (key === 'email') {
          rule[key] = value;
        }
      } else {
        rule[key] = value;
      }
    });
    return rule;
  };
  const { validate } = useValidate();
  const validateRule = (type?: string) => {
    return new Promise<boolean>((resolve) => {
      const result = validate(model, getRule(type));
      if (!result.result) {
        showToast({
          className: 'custom-toast',
          message: result.msg,
        });
      }
      resolve(result.result);
    });
  };

  const userStore = useUserStore();
  const login = (): Promise<boolean> => {
    return new Promise<boolean>((resolve) => {
      validateRule().then(async (validate) => {
        if (validate) {
          await userStore.loginEmail({
            email: model.email,
            code: model.verification,
            tokenStr: model.token,
          });
          resolve(true);
        } else {
          resolve(false);
        }
      });
    });
  };
  const verificationBefore = () => {
    return validateRule('verification');
  };
  const sendVerification = () => {
    verifyRef.value.send();
    console.log('yes-sendVerification');
  };
  defineExpose({
    login,
    isEmptyForm,
    sendVerifyCode,
  });
</script>
<script lang="ts">
  export default {
    name: 'EmailVerify',
  };
</script>
