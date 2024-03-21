<template>
  <div class="login-container">
    <div
      class="login-logo inline-flex items-center"
      v-motion="{
        initial: { opacity: 0, x: 50 },
        enter: {
          opacity: 1,
          x: 0
        }
      }"
    >
      <img src="@/assets/login/logo.png" />
      <span class="text">体育直播</span>
    </div>
    <!-- eslint-disable-next-line vue/html-self-closing -->
    <div class="login-cover"></div>
    <div class="login-box">
      <div class="login-form">
        <Motion>
          <h2 class="login-title large">账密登录</h2>
        </Motion>
        <Motion :delay="100">
          <h2 class="login-title">欢迎回来！请登录您的账号</h2>
        </Motion>

        <el-form
          ref="formRef"
          :model="model"
          :rules="rules"
          label-position="top"
        >
          <Motion :delay="150">
            <el-form-item label="账号" prop="username">
              <el-input
                clearable
                v-model="model.username"
                :placeholder="t('login.username')"
              />
            </el-form-item>
          </Motion>

          <Motion :delay="200">
            <el-form-item label="密码" prop="password">
              <el-input
                clearable
                show-password
                v-model="model.password"
                :placeholder="t('login.password')"
              />
            </el-form-item>
          </Motion>

          <Motion :delay="250">
            <el-form-item>
              <el-button
                class="w-full mt-[32px] sure-btn"
                size="default"
                type="primary"
                :loading="loading"
                @click="onLogin()"
              >
                {{ t("login.login") }}
              </el-button>
            </el-form-item>
          </Motion>
        </el-form>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, reactive, onMounted, onBeforeUnmount } from "vue";
import { useI18n } from "vue-i18n";
import Motion from "./utils/motion";
import { useRouter } from "vue-router";
import { message } from "@/utils/message";
import type { FormInstance } from "element-plus";
import { useLayout } from "@/layout/hooks/useLayout";
import { useUserStoreHook } from "@/store/modules/user";
import { initRouter, getTopMenu } from "@/router/utils";
defineOptions({
  name: "Login"
});

const router = useRouter();
const { t } = useI18n();
const { initStorage } = useLayout();
initStorage();

const loading = ref(false);
const formRef = ref<FormInstance>(null);
const model = reactive({
  username: "",
  password: ""
  //  username: "live",
  // password: "sp123456"
});
const rules = reactive({
  username: [{ required: true, message: "请输入账号名", trigger: "blur" }],
  password: [{ required: true, message: "请输入密码", trigger: "blur" }]
});
const onLogin = async () => {
  loading.value = true;
  if (!formRef.value) return;
  await formRef.value.validate((valid, fields) => {
    if (valid) {
      useUserStoreHook()
        .loginByUsername({ account: model.username, pass: model.password })
        .then(() => {
          // 获取后端路由
          initRouter().then(() => {
            router.push(getTopMenu(true).path);
            message("登录成功", { type: "success" });
          });
        })
        .finally(() => {
          loading.value = false;
        });
    } else {
      loading.value = false;
      return fields;
    }
  });
};

/** 使用公共函数，避免`removeEventListener`失效 */
function onkeypress({ code }: KeyboardEvent) {
  if (code === "Enter") {
    onLogin();
  }
}

onMounted(() => {
  window.document.addEventListener("keypress", onkeypress);
});

onBeforeUnmount(() => {
  window.document.removeEventListener("keypress", onkeypress);
});
</script>

<style lang="scss" scoped>
.login-container {
  display: grid;
  grid-template-columns: 1fr 560px;
  width: 100vw;
  height: 100vh;
}

.login-cover {
  width: 100%;
  height: 100%;
  background: url("@/assets/login/log-bg.png") no-repeat, transparent;
  background-position: right bottom;
  background-size: cover;
}

.login-box {
  display: flex;
  align-items: center;
  text-align: center;
}

.login-form {
  width: 100%;
  padding: 0 80px;
}

.login-title {
  padding: 4px 0;
  font: 400 16px / 20px "PingFang SC";
  color: #1a1a1a;
  text-align: left;

  &.large {
    font: 600 36px / normal "PingFang SC";
    letter-spacing: 0.36px;
  }
}

:deep() .el-form {
  margin-top: 36px;

  .el-form-item {
    margin-bottom: 24px;
  }

  .el-form-item .el-form-item__label {
    font: 500 14px / 20px "PingFang SC";
    color: #434343;

    &::before {
      display: none;
      content: none;
    }
  }

  .el-input {
    --el-input-text-color: #262626;
    --el-input-placeholder-color: #a6a6a6;
    --el-input-border-radius: 8px;
    --el-input-border-color: #d0d5dd;
    --el-input-height: 22px;
    --el-input-focus-border-color: #34a853;

    font: 400 16px / 24px "PingFang SC";

    .el-input__wrapper {
      padding: 14px;
    }
  }

  .sure-btn.el-button {
    --el-button-bg-color: #34a853;
    --el-button-hover-bg-color: var(--el-button-bg-color);
    --el-button-active-bg-color: var(--el-button-bg-color);
    --el-border-radius-base: 8px;

    height: 52px;
    font: 500 16px / 20px "PingFang SC";
    border: none;
  }
}

.login-logo {
  position: absolute;
  top: 40px;
  right: 79px;

  & > img {
    display: block;
    width: 28px;
    height: 28px;
  }

  .text {
    padding-left: 10px;
    font: 500 16px / normal "PingFang SC";
    color: #000;
    letter-spacing: 0.16px;
  }
}
</style>
