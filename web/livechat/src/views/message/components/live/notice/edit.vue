<script setup lang="ts">
import { useRequest } from "vue-request";
import { fetchUpdateLiveOpenInfo } from "@/api/message";
import { ElMessage } from "element-plus";
import { useLiveRoomSocket } from "@/views/message/utils/live";
import { MessageType } from "@/views/message/utils/types";
import { ref } from "vue";

defineOptions({ name: "LiveNoticeEdit" });

interface Props {
  roomId?: string;
}
const show = defineModel("show", { default: false });
const notice = defineModel("notice", { default: "" });
const props = defineProps<Props>();
const text = ref(notice.value);

const { sendMessage } = useLiveRoomSocket();

const { run } = useRequest(fetchUpdateLiveOpenInfo, {
  manual: true,
  onSuccess: () => {
    notice.value = text.value;
    ElMessage({
      message: "直播间公告更改成功",
      type: "success"
    });
    sendMessage({
      content: "主播修改了直播公告",
      msgType: MessageType.system
    });
  },
  onAfter: () => {
    show.value = false;
  }
});

const onSubmit = () => {
  run({
    id: props.roomId,
    titlePage: "直播公告",
    notice: notice.value
  });
};
</script>

<template>
  <div class="custom-dialog" v-if="show">
    <el-dialog
      v-model="show"
      title="主播公告栏编辑"
      width="520"
      align-center
      destroy-on-close
      :show-close="false"
    >
      <el-input
        v-model="text"
        maxlength="500"
        style="width: 100%"
        placeholder="Please input"
        show-word-limit
        type="textarea"
        input-style="height: 200px"
      />
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="show = false" class="">取消</el-button>
          <el-button type="primary" :disabled="!notice" @click="onSubmit">
            保存
          </el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<style scoped lang="scss">
.custom-dialog :deep() {
  .el-textarea {
    --el-input-bg-color: #eef0f4;
    --el-input-border-color: transparent;
    --el-input-hover-border-color: transparent;

    .el-input__count {
      background-color: #eef0f4;
    }
  }

  .el-dialog {
    --el-dialog-border-radius: 16px;
    --el-dialog-padding-primary: 24px;
  }

  .el-dialog__header {
    padding-bottom: 0;
  }

  .el-dialog__title {
    --el-dialog-title-font-size: 22px;

    font-weight: 600;
  }

  .el-dialog__body {
    padding: 24px;
  }

  .el-dialog__footer {
    padding-top: 0;

    .el-button {
      --el-button-bg-color: #fff;
      --el-button-border-color: #d0d5dd;
      --el-button-text-color: #1a1a1a;
      --el-border-radius-base: 4px;

      padding: 6px 24px !important;

      &:focus,
      &:hover {
        --el-button-hover-text-color: #1a1a1a;
        --el-button-hover-border-color: #4f4f4f;
        --el-button-hover-bg-color: #fff;
      }

      &.el-button--primary {
        --el-button-bg-color: #34a853;
        --el-button-text-color: #fff;
        --el-button-border-color: var(--el-button-bg-color);
        --el-button-active-bg-color: var(--el-button-bg-color);
        --el-button-active-border-color: var(--el-button-bg-color);
        --el-button-disabled-bg-color: #4c6546;
        --el-button-disabled-border-color: var(--el-button-disabled-bg-color);

        &:focus,
        &:hover {
          --el-button-hover-text-color: #fff;
          --el-button-hover-border-color: #128831;
          --el-button-hover-bg-color: #128831;
        }
      }
    }
  }
}
</style>
