<script setup lang="ts">
import { ref, reactive, computed } from "vue";
import {
  queryBroadcastDetail,
  addBroadcast,
  updateBroadcast
} from "@/api/broadcast";
import { uploadFile } from "@/api/common";
import { ElMessage } from "element-plus";
import type { FormInstance, FormRules } from "element-plus";
import { Base } from "@/views/editor/components";
import { useRouter, useRoute } from "vue-router";
import { useUserStore } from "@/store/modules/user";
import { extractRichText } from "@/utils/tool.kit";
const userStore = useUserStore();
const neverCreateOpenInfo = computed(() => {
  return userStore.userInfo.neverCreateOpenInfo;
});
const router = useRouter();
const route = useRoute();
const firstMessageRef = ref();
const noticeRef = ref();
interface RuleForm {
  id: string;
  remark: string;
  notice: string;
  firstMessage: string;
  titlePage: string;
}
const ruleFormRef = ref<FormInstance>();
const ruleForm = reactive<RuleForm>({
  id: "",
  remark: "",
  notice: "",
  firstMessage: "",
  titlePage: ""
});
ruleForm.id = (route.query.id || "") + "";
const rules = reactive<FormRules<RuleForm>>({
  remark: [{ required: true, message: "备注信息必填哦", trigger: "blur" }],
  titlePage: [
    { required: true, message: "直播间封面必填哦", trigger: ["blur", "change"] }
  ],
  notice: [
    { required: true, message: "请输入主播公告", trigger: ["blur", "change"] }
  ],
  firstMessage: [
    {
      required: true,
      message: "请输入开播首条聊天",
      trigger: ["blur", "change"]
    }
  ]
});
const imgRef = ref();

defineOptions({
  name: "Broadcast"
});

const loading = ref<boolean>(false);

/** 获取开播信息 */
async function getList() {
  if (!ruleForm.id) return;
  loading.value = true;
  try {
    const response = await queryBroadcastDetail(ruleForm.id);
    ruleForm.remark = response.remark || "";
    ruleForm.titlePage = response.titlePage || "";
    ruleForm.notice = response.notice || "";
    ruleForm.firstMessage = response.firstMessage || "";
    firstMessageRef.value.setContent(ruleForm.firstMessage);
    noticeRef.value.setContent(ruleForm.notice);
  } finally {
    loading.value = false;
  }
}
function onSaveInfo(formEl: FormInstance | undefined) {
  // const { titlePage, notice, firstMessage } = data;
  if (!formEl) {
    return;
  }
  const firstMessage = firstMessageRef.value.getContent();
  if (!firstMessage || firstMessage == "<p><br></p>") {
    ruleForm.firstMessage = "";
  } else {
    ruleForm.firstMessage = firstMessage;
  }
  const notice = noticeRef.value.getContent();
  if (!notice || notice == "<p><br></p>") {
    ruleForm.notice = "";
  } else {
    ruleForm.notice = notice;
  }
  formEl.validate(valid => {
    if (valid) {
      onSubmit();
    } else {
      if (ruleForm.titlePage) {
        onSubmit();
      }
    }
  });
}

const onSubmit = async () => {
  if (!ruleForm.firstMessage) {
    ElMessage.error("请输入开播首条聊天");
    return;
  }
  if (!ruleForm.notice) {
    ElMessage.error("请输入主播公告");
    return;
  }
  const noticeText = extractRichText(ruleForm.notice);
  if (noticeText.length > 300) {
    ElMessage.error("主播公告内容大于300字");
    return;
  }
  const params = {
    id: "",
    titlePage: ruleForm.titlePage,
    notice: ruleForm.notice,
    firstMessage: ruleForm.firstMessage,
    remark: ruleForm.remark
  };
  const submitInfo: {
    methods: typeof addBroadcast | typeof updateBroadcast | null;
    msg: string;
  } = {
    methods: null,
    msg: ""
  };
  if (ruleForm.id) {
    params.id = ruleForm.id;
    submitInfo.methods = updateBroadcast;
    submitInfo.msg = "主播开播信息已更新";
  } else {
    delete params.id;
    submitInfo.methods = addBroadcast;
    submitInfo.msg = "主播开播信息已添加";
  }
  loading.value = true;
  try {
    await submitInfo.methods(params);
    if (neverCreateOpenInfo.value) {
      await userStore.getUserInfo();
    }
    ElMessage.success(submitInfo.msg);
    cancel();
  } finally {
    loading.value = false;
  }
};
function onImgSelect(e) {
  const file = e.target.files[0];
  const param = new FormData();
  param.append("file", file);
  e.srcElement.value = "";
  console.log("result", file, { file });
  uploadFile({ file }).then((result: any) => {
    ruleForm.titlePage = result;
  });
}

function onUploadImg() {
  imgRef.value.click();
}
const cancel = () => {
  router.push({ name: "BroadcastList" });
};
getList();
</script>

<template>
  <div
    class="broadcast flex flex-col bg-white rounded-[16px] p-[26px]"
    v-loading="loading"
  >
    <el-form
      ref="ruleFormRef"
      :model="ruleForm"
      :rules="rules"
      class="demo-ruleForm"
    >
      <el-form-item label="备注信息" prop="remark" class="remark">
        <el-input
          :class="{ 'has-word': ruleForm.remark }"
          v-model="ruleForm.remark"
          placeholder="请输入备注信息"
          class="remark-input"
          maxlength="20"
          show-word-limit
          type="text"
        />
      </el-form-item>
      <el-form-item label="直播封面:" prop="titlePage" required>
        <input type="file" v-show="false" @change="onImgSelect" ref="imgRef" />
        <div class="rounded-[8px] mt-[16px] flex flex-col justify-center">
          <div
            v-if="!ruleForm.titlePage"
            class="w-[120px] h-[120px] cursor-pointer upload-icon"
            @click="onUploadImg"
          />
          <div
            v-else
            class="w-[120px] h-[120px] cursor-pointer relative rounded-[8px]"
            @click="onUploadImg"
          >
            <img
              class="w-full h-full absolute bg-contain bg-no-repeat rounded-[8px]"
              :src="ruleForm.titlePage"
            />
            <div
              class="w-[120px] h-[30px] rounded-b-[8px] bg-black bg-opacity-[0.7] flex items-center justify-center absolute bottom-0"
            >
              <span class="text-[14px] font-medium text-white">重新上传</span>
            </div>
          </div>
        </div>
      </el-form-item>

      <div class="form-group-col">
        <el-form-item
          class="mt-[24px] mb-[8px]"
          label="开播首条聊天:"
          prop="firstMessage"
          required
        >
          <div
            class="w-full border-[1px] rounded-[4px] border-[#f5f5f5] bg-[#f5f5f5]"
          >
            <Base ref="firstMessageRef" />
          </div>
          <!-- <el-input
          clearable
          style="width: 431px; background: #f7f8f7"
          maxlength="100"
          show-word-limit
          v-model="ruleForm.firstMessage"
          type="textarea"
          :row="10"
          :max="100"
          placeholder="请输入内容"
      /> -->
        </el-form-item>
        <el-form-item
          class="mt-[24px] mb-[8px]"
          label="主播公告:"
          prop="notice"
          required
        >
          <div
            class="w-full border-[1px] rounded-[4px] border-[#f5f5f5] bg-[#f5f5f5]"
          >
            <Base ref="noticeRef" />
          </div>
          <!-- <el-input
          clearable
          style="width: 431px; background: #f7f8f7"
          maxlength="100"
          v-model="ruleForm.notice"
          show-word-limit
          type="textarea"
          :row="10"
          :max="100"
          placeholder="请输入内容"
      /> -->
        </el-form-item>
      </div>
    </el-form>
    <div class="form-bottom">
      <el-button
        class="page-header-btn"
        @click="cancel"
        v-if="!userStore.userInfo.neverCreateOpenInfo"
      >
        取消
      </el-button>
      <el-button
        class="page-header-btn"
        type="primary"
        @click="onSaveInfo(ruleFormRef)"
      >
        保存
      </el-button>
    </div>
  </div>
</template>

<style lang="scss">
.broadcast {
  .form-group-col {
    display: flex;

    .el-form-item {
      flex: 0 0 calc(50% - 20px);
      width: calc(50% - 20px);
      max-width: 800px;

      & ~ .el-form-item {
        margin-left: 40px;
      }

      .el-form-item__content {
        width: 100%;
      }
    }
  }

  .el-form-item {
    flex-direction: column;
    align-items: baseline;
  }

  .upload-icon {
    background: url("@/assets/common/upload-icon.png") no-repeat center;
    background-size: contain;
  }

  .upload-icon:hover {
    background: url("@/assets/common/hover-upload-icon.png") no-repeat center;
    background-size: contain;
  }

  .el-textarea__inner {
    height: 91px;
    background: #f7f8f7 !important;
  }

  .el-input__count {
    margin-right: 6px !important;
    background: #f7f8f7 !important;
  }

  .el-form-item.remark {
    .el-form-item__content {
      width: 100%;
    }
  }

  .remark-input.el-input {
    --el-input-text-color: #1a1a1a;
    --el-input-bg-color: #f7f8f7;
    --el-input-border-radius: 8px;
    --el-input-height: 48px;
    --el-color-info: #a6a6a6;

    flex: 0 0 auto;
    width: 100%;
    max-width: 545px;
    font: 500 14px / var(--el-input-height) "PingFang SC";
    color: #333332;

    &.has-word {
      --el-color-info: #1a1a1a;
    }

    .el-input__count {
      font-size: 14px;

      .el-input__count-inner {
        --el-fill-color-blank: transparent;
      }
    }
  }

  .form-bottom {
    padding-top: 24px;
    text-align: center;

    .el-button {
      --el-button-text-color: #1a1a1a;
      --el-button-bg-color: #fff;
      --el-button-hover-text-color: #1a1a1a;
      --el-button-active-text-color: #1a1a1a;
      --el-button-hover-border-color: #4f4f4f;
      --el-button-active-border-color: #4f4f4f;
      --el-button-hover-bg-color: transparent;
      --el-button-active-bg-color: transparent;
      --el-border-radius-base: 8px;

      width: 116px;
      height: 44px;
      padding: 0;
      font: 500 18px / 44px "PingFang SC";

      &.el-button--primary {
        --el-button-text-color: #fff;
        --el-button-hover-text-color: #fff;
        --el-button-active-text-color: #fff;
        --el-button-bg-color: #34a853;
        --el-button-hover-border-color: #128831;
        --el-button-active-border-color: #128831;
        --el-button-hover-bg-color: #128831;
        --el-button-active-bg-color: #128831;

        border: none;
      }
    }
  }
}

@media only screen and (width <= 1200px) {
  .broadcast .form-group-col {
    flex-flow: column;

    .el-form-item {
      flex: 0 0 100%;
      width: 100%;

      & ~ .el-form-item {
        margin-left: 0;
      }
    }
  }
}
</style>
