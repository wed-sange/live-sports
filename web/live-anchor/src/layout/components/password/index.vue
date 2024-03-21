<template>
  <div class="custom-modal">
    <el-dialog :title="title" v-model="show" top="12vh" :show-close="false">
      <h2 class="custom-modal__title">账号：{{ model.account }}</h2>
      <el-form :model="model" :rules="rules" ref="formRef" label-position="top">
        <el-form-item label="旧密码" prop="oldPass">
          <el-input
            v-model="model.oldPass"
            placeholder="请输入当前密码"
            type="password"
            show-password
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPass">
          <el-input
            v-model="model.newPass"
            placeholder="请输入新密码"
            type="password"
            show-password
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPass">
          <el-input
            v-model="model.confirmPass"
            placeholder="请输入确认密码"
            type="password"
            show-password
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="close">取 消</el-button>
          <el-button type="primary" @click="sure">确 定</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { updatePassword } from "@/api/user";
import type { UpdatePasswordParams } from "@/api/user/model";
import { ref, reactive } from "vue";
import {
  ElDialog,
  ElMessage,
  ElButton,
  ElForm,
  ElFormItem,
  ElInput,
  type FormRules,
  type FormInstance
} from "element-plus";
import { useUserStore } from "@/store/modules/user";

const userStore = useUserStore();
const emit = defineEmits(["onSuccess"]);
const show = ref(false);
const ajaxLoader = ref(false);
const formRef = ref<FormInstance | null>(null);
const model = reactive({
  account: "",
  // 旧密码
  oldPass: "",
  // 新密码
  newPass: "",
  // 确认密码
  confirmPass: ""
});
const passRegx = /^[0-9A-Za-z]{6,18}$/;
const validateNewPass = (rule: any, value: any, callback: any) => {
  if (value === "") {
    callback(new Error("请输入新密码"));
  } else {
    if (!passRegx.test(value)) {
      callback(new Error("密码只能由数字、字母至少6位组成"));
    } else {
      callback();
    }
  }
};
const validateConfirmPass = (rule: any, value: any, callback: any) => {
  if (value === "") {
    callback(new Error("请输入确认密码"));
  } else {
    if (value === model.newPass) {
      callback();
    } else {
      callback(new Error("确认密码和新密码不同"));
    }
  }
};
const rules = reactive<FormRules>({
  oldPass: [
    { required: true, message: "请输入当前密码", trigger: ["blur", "change"] }
  ],
  newPass: [{ validator: validateNewPass, trigger: ["blur", "change"] }],
  confirmPass: [{ validator: validateConfirmPass, trigger: ["blur", "change"] }]
});
const title = ref("修改密码");
const close = () => {
  show.value = false;
};
const open = () => {
  show.value = true;
  initModel();
};
const initModel = () => {
  formRef.value && formRef.value.resetFields();
  model.account = userStore.userInfo.account || "";
};
const sure = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(valid => {
    if (valid) {
      //暂无校验
      submit();
    }
  });
};
const submit = async () => {
  const params: UpdatePasswordParams = {
    oldPasswd: model.oldPass || "", // 旧密码
    newPasswd: model.newPass || "", //	新密码
    rePasswd: model.confirmPass || "" // 确认密码
  };
  try {
    ajaxLoader.value = true;
    await updatePassword(params);
    ElMessage.success("密码已修改");
    ajaxLoader.value = false;
    close();
    emit("onSuccess");
    userStore.logout();
  } catch (error) {
    // ElMessage.error(error.message || '接口访问异常')
    ajaxLoader.value = false;
  }
};
defineExpose({
  show,
  open,
  initModel,
  close
});
</script>
<style scoped lang="scss">
.custom-modal {
  margin: 0;

  :deep() {
    .el-dialog__body {
      padding-top: 16px;
      padding-bottom: 0;
    }

    .el-dialog {
      --el-dialog-width: 480px;
      --el-dialog-border-radius: 16px;
      --el-dialog-padding-primary: 24px;
    }

    .el-dialog__header {
      padding-bottom: 0;
      font: 600 22px / 24px "PingFang SC";
      color: #1a1a1a;
    }

    .custom-modal__title {
      padding-bottom: 32px;
      font: 500 16px / 24px "PingFang SC";
      color: #727272;
    }

    .el-form-item {
      margin-bottom: 24px;

      .el-input {
        --el-input-bg-color: #f7f7f7;
        --el-input-border: none;
        --el-input-height: 36px;
        --el-input-placeholder-color: #a6a6a6;

        .el-input__wrapper {
          padding: 1px 16px;
        }
      }

      &:not(.is-error) {
        .el-input__wrapper {
          box-shadow: none;
        }
      }

      &.custom-upload {
        .el-form-item__label {
          line-height: 100px;
        }
      }
    }

    .el-form-item__label {
      padding-right: 8px;
      font-size: 14px;
      font-weight: 400;
      color: #1a1a1a;

      &::before {
        display: none;
      }
    }
  }

  .dialog-footer {
    .el-button + .el-button {
      margin-left: 16px;
    }

    .el-button {
      --el-button-bg-color: #fff;
      --el-button-border-color: #d0d5dd;
      --el-button-text-color: #1a1a1a;
      --el-border-radius-base: 4px;

      width: 76px;
      height: 34px;
      font: 500 14px / 1 "PingFang SC";

      &:focus,
      &:hover {
        --el-button-hover-text-color: #1a1a1a;
        --el-button-hover-border-color: #4f4f4f;
        --el-button-hover-bg-color: #fff;
      }

      &.el-button--primary {
        --el-button-bg-color: #34a853;
        --el-button-border-color: #34a853;
        --el-button-text-color: #fff;

        &:focus,
        &:hover {
          --el-button-hover-text-color: #fff;
          --el-button-hover-border-color: #128831;
          --el-button-hover-bg-color: #128831;
        }
      }
    }
  }

  .custom-upload__preview {
    position: relative;
    width: 100px;
    height: 100px;
    padding: 5px;
    cursor: pointer;
    border: 1px dashed #6c5ec6;
    border-radius: 100%;

    & > img {
      width: 100%;
      height: 100%;
      overflow: hidden;
      border-radius: inherit;
    }

    .custom-upload__preview--edit {
      position: absolute;
      top: 50%;
      left: 50%;
      z-index: 1;
      display: flex;
      align-items: center;
      justify-content: center;
      width: 40px;
      height: 24px;
      margin-top: -12px;
      margin-left: -20px;
      font: 400 12px / 22px "PingFang SC";
      color: #fff;
      background: rgb(0 0 0 / 50%);
      border-radius: 26px;
      opacity: 0;
      transition-duration: 300ms;
      transition-property: opacity;
    }

    &:hover {
      .custom-upload__preview--edit {
        opacity: 1;
      }
    }

    .custom-upload__file {
      position: absolute;
      top: 0;
      left: 0;
      z-index: 1;
      width: 0;
      height: 0;
      opacity: 0;
    }
  }
}
</style>
