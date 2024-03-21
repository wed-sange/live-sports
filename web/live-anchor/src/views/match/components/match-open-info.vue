<template>
  <div class="match-open-info-box w-full">
    <el-select
      :disabled="disabled"
      v-model="openInfoType"
      placeholder="请选择开播信息"
      :loading="loading"
    >
      <template #prefix>
        <div class="match-open-info__label">开播信息</div>
      </template>
      <el-option
        v-for="item in openInfoList"
        :key="item.value"
        :label="item.label"
        :value="item.value"
      />
    </el-select>
  </div>
</template>
<script setup lang="ts">
import { ref } from "vue";
import { getBroadcastList } from "@/api/broadcast";
withDefaults(
  defineProps<{
    disabled?: boolean;
  }>(),
  {
    disabled: false
  }
);
defineOptions({
  name: "MatchOpenInfo"
});
const loading = ref(false);
const openInfoType = ref("");
const openInfoList = ref<any[]>([]);
const initData = async () => {
  loading.value = true;
  try {
    const response = await getBroadcastList();
    const data = response || [];
    const renderData = data.map(cur => {
      const value = "" + cur.id;
      if (cur.used === 1) {
        openInfoType.value = value;
      }
      return {
        value, // ID
        label: cur.remark || ""
      };
    });
    openInfoList.value = renderData;
  } finally {
    loading.value = false;
  }
};
initData();
const getSelect = () => {
  return openInfoType.value;
};
defineExpose({
  getSelect
});
</script>

<style scoped lang="scss">
.match-open-info-box {
  display: flex;
  justify-content: center;

  :deep(.el-select) {
    width: 458px;

    .el-input__wrapper {
      width: 100%;
      padding: 0 10px 0 0;
    }

    &:hover:not(.el-select--disabled) .el-input__wrapper {
      box-shadow: none !important;
    }

    .el-input {
      width: 100% !important;
      border: 1px solid #d0d5dd;
      border-radius: 4px;

      &.is-disabled .el-input__wrapper,
      &.is-focus .el-input__wrapper,
      .el-input__wrapper.is-focus {
        box-shadow: none !important;
      }
    }
  }

  .match-open-info__label {
    box-sizing: border-box;
    width: 93px;
    height: 50px;
    margin-right: 4px;
    font: 500 14px / 50px "PingFang SC";
    color: #262626;
    background: #f7f7f7;
    border-right: 1px solid #d0d5dd;
    border-radius: 4px 0 0 4px;
  }
}
</style>
