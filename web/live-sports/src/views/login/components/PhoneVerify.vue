<template>
  <div class="w-full px-[60px]">
    <div class="form-group__title">手机号</div>
    <div class="form-group">
      <div class="form-label form-label__divide grow-0 shrink-0 basis-auto min-w-[136px] text-right">
        <van-button @click="openCountry" class="form-btn whitespace-nowrap" type="primary">
          +{{ model.code }}<van-icon class="ml-[4px] pb-[8px]" name="arrow-down" />
        </van-button>
      </div>
      <div class="flex-auto">
        <van-field class="form-input keyboard-abr form-input__phone" maxlength="11" v-model="model.phone" placeholder="请输入手机号" />
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
        <!-- <van-button class="form-btn" type="primary" @click="sendVerification">{{ model.verificationText }}</van-button> -->
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
  import { useValidate, type ValidateRule } from '../useValidate';
  import { dialogCountry } from '../mount/country';
  import { useUserStore } from '@/store/modules/user';
  import { showToast } from 'vant';
  import { smsSend } from '@/api/user';
  const verifyRef = ref();
  const model = reactive({
    // phone: '14568897456',
    phone: '',
    code: '86',
    showCode: false,
    verification: '',
    token: '', //图形验证码token
  });
  const isEmptyInput = computed(() => {
    return model.phone === '';
  });
  const isEmptyForm = computed(() => {
    return model.phone === '' || model.verification === '';
  });
  const sendVerifyCode = async (token) => {
    model.token = token;
    await smsSend({ account: model.phone, areaCode: '+' + model.code });
    verifyRef.value?.send();
  };
  const rules = reactive<ValidateRule<typeof model>>({
    phone: [
      {
        validate: (value) => {
          if (!value) {
            return {
              result: false,
              msg: '请输入手机号',
            };
          }
          if (model.code[0] === '86' && !/^[1]{1}[0-9]{10}$/.test(value)) {
            return {
              result: false,
              msg: '手机号格式不对',
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
        if (key === 'phone') {
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
          await userStore.login({
            phone: model.phone,
            code: model.verification,
            tokenStr: model.token,
            areaCode: '+' + model.code,
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
    console.log('yes-sendVerification');
  };
  const openCountry = () => {
    dialogCountry({
      value: model.code,
      sure: (item, vm) => {
        model.code = item.code;
        vm.close();
      },
    });
  };
  defineExpose({
    login,
    isEmptyForm,
    sendVerifyCode,
  });
</script>
<script lang="ts">
  export default {
    name: 'PhoneVerify',
  };
</script>
