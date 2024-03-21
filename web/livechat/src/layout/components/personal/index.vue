<template>
  <div class="custom-modal">
    <el-dialog
      :title="modelIsFirst ? '完善信息' : title"
      v-model="show"
      top="33vh"
      @closed="onDialogClosed"
      :show-close="false"
    >
      <el-form :model="model" :rules="rules" ref="formRef" label-width="36px">
        <el-form-item label="昵称" prop="name">
          <el-input v-model="model.name" placeholder="请输入昵称" />
        </el-form-item>
        <el-form-item class="custom-upload" label="头像" prop="avatar">
          <div class="custom-upload__preview" @click="openPhoto">
            <img
              class="rounded-[50px] object-cover"
              :src="model.avatar || defaultLogo"
            />
            <div class="custom-upload__preview--edit">修改</div>
            <input
              ref="photoInputRef"
              class="custom-upload__file"
              type="file"
              accept="image/*"
              @change="handleChooseFile"
            />
          </div>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="close" v-show="!modelIsFirst">取 消</el-button>
          <el-button type="primary" @click="sure">
            {{ modelIsFirst ? "下一步" : "确 定" }}
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import { uploadAvatar, updateUserInfo } from "@/api/user";
import type { UpdateUserInfoParams } from "@/api/user/model";
import { ref, reactive, computed, watch } from "vue";
import {
  ElDialog,
  ElMessage,
  ElButton,
  ElForm,
  ElFormItem,
  ElInput,
  type FormRules,
  type FormInstance,
  ElLoading
} from "element-plus";
import { useUserStore } from "@/store/modules/user";
import defaultLogo from "@/assets/common/default-avatar.png";
import { useRouter } from "vue-router";

const userStore = useUserStore();
const emit = defineEmits(["onSuccess"]);
const modelIsFirst = ref(false);
const show = ref(false);
const ajaxLoader = ref(false);
const formRef = ref<FormInstance | null>(null);
const model = reactive({
  userId: "",
  // 昵称
  name: "",
  // 头像
  avatar: ""
});
const rules = reactive<FormRules>({
  name: [{ required: true, message: "请输入用户昵称", trigger: "blur" }]
  // avatar: [{ required: true, message: "请选择头像", trigger: "change" }]
});
const title = ref("个人信息");
const onDialogClosed = () => {
  modelIsFirst.value = false;
  userStore.clearFirstLogin();
};
const close = () => {
  show.value = false;
};
const initModel = () => {
  formRef.value && formRef.value.resetFields();
  model.userId = userStore.userInfo.id || "";
  model.name = userStore.userInfo.nickname || "";
  model.avatar = userStore.userInfo.avatar || "";
};
const open = (_modelIsFirst?: boolean) => {
  show.value = true;
  modelIsFirst.value = _modelIsFirst !== undefined ? !!_modelIsFirst : false;
  initModel();
};
const isFirstLogin = computed(
  () => userStore.userInfo.identityType === 1 && userStore.userInfo.isFirstLogin
  // () => false
);
watch(
  isFirstLogin,
  () => {
    if (isFirstLogin.value) {
      open(true);
    }
  },
  { immediate: true }
);
const sure = async () => {
  if (!formRef.value) return;
  await formRef.value.validate(valid => {
    if (valid) {
      //暂无校验
      submit();
    }
  });
};
const router = useRouter();
const submit = async () => {
  const params: UpdateUserInfoParams = {
    name: model.name || "",
    head: model.avatar || ""
  };
  try {
    ajaxLoader.value = true;
    await updateUserInfo(params);
    ElMessage.success("个人信息已更新");
    ajaxLoader.value = false;
    userStore.clearFirstLogin();
    userStore.getUserInfo();
    if (modelIsFirst.value) {
      router.push("/broadcast/list");
    }
    close();
    emit("onSuccess");
  } catch (error) {
    // ElMessage.error(error.message || '接口访问异常')
    ajaxLoader.value = false;
  }
};
const photoInputRef = ref<HTMLInputElement | null>(null);
const openPhoto = () => {
  photoInputRef.value && photoInputRef.value.click();
};
const handleChooseFile = async (event: Event) => {
  const files = (event.target as HTMLInputElement).files;
  if (files && files.length >= 1) {
    const file = files[0];
    photoInputRef.value!.value = "";
    if (file.size > 1024 * 1000) {
      ElMessage.error("请上传小于1M的图片");
      return;
    }
    const loadingInstance = ElLoading.service({ fullscreen: true });
    try {
      const response = await uploadAvatar({
        file: file
      });
      if (response) {
        model.avatar = response;
      } else {
        ElMessage.error("图片上传失败");
      }
    } finally {
      loadingInstance.close();
    }
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
      padding-top: 24px;
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

    .el-form-item {
      margin-bottom: 24px;

      &.is-error {
        .el-form-item__error {
          padding-top: 4px;
          padding-bottom: 4px;
          padding-left: 18px;
          background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAACgAAAAoCAYAAACM/rhtAAAACXBIWXMAABYlAAAWJQFJUiTwAAAAAXNSR0IArs4c6QAAAARnQU1BAACxjwv8YQUAAAKbSURBVHgB7ZjBcdtADEU/6MxIR6WCbCqIXYHlm8aXsAMnHaSD2B0kHTgdMBcPb1YqiFKBWYKO1EHaAGtKI5O7EnbJGfugd7CpHRIAsQAWIHDixOtCSMTm+QR1nW+ASyI65yUDayfPUmnJfytr7cICf86AOZVlhQSiDbSz2ZT/3ViifGeQ5jngPgPuYg1VG+g8tlp9Z698Qw/Y2z8wGt1RUSxV92tuYq8Z9sAjZBuHoWLFVxpvZsdusNfX57ydfzGccYIRmSL72I0HPeg8J8YpY03iDG4X6bPqGU4msvbikCeDBkrM2bpWe46z+eu7srx3z3IiNSGhoaLx+CIUk+Et5oTQGiee2BrnfpblvCk1GkyjC2oD3db2zNYYRFdTvjqEPBh8o4CGiStD259yHVEjBQ6RG996x8CmpHxBLHU98V4r4cTK918SIQPXwBT9MYiFPb7mo7O93DGQz81LpGHQE/Lo7m7x88HfF4MEyKM7G0r4eu+5Tbo3TXuha2Bk9g2KR/fRs1gLb8++cIOByDyatCdAG7MTAXxACh7dPg9WSJHdFFqpo0gvVVV7oZvF1v5DChw/m9nsqWnNEkXYRXst8yzMkY7pk2Qyv6BrT4vxuOgRh7048zinY6D0Zezq34iE3/4X93XvORY/yjUikWbX17h6G9bIhtORleVOlmt2V6unmO2WF/MZ6K2D0nBysP+EWvrLkNBObFtEV6jtDxbqbDS6hbbkSD+413C6UqP3XtXo8jLo0MSeLDh+l6Qd6vsMTTsjZey09nHwM1qMI7qih4fFwdug4E0P7oIIktEwKnECuISQMVP5jSbl45Hhfu+WAkOOXwtJbS0yqZUykkaQ/vmtaQrY2CnH0icEPr9lcnzx6RRbek6ceCv8BwpNH6lO2a7gAAAAAElFTkSuQmCC")
              no-repeat,
            #fff;
          background-position: 0 2px, center;
          background-size: 16px 16px, 100% 100%;
        }
      }

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

    .el-button + .el-button {
      margin-left: 16px;
    }
  }

  .custom-upload__preview {
    position: relative;
    width: 100px;
    height: 100px;
    padding: 5px;
    cursor: pointer;
    border: 1px dashed #34a853;
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
