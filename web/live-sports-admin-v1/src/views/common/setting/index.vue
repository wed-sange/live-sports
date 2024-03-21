<template>
  <div class="settings">
    <div class="flex flex-row-reverse">
      <el-button type="primary" class="mt-4 mr-4" @click="onUpdate"
        >保存</el-button
      >
    </div>
    <el-form
      ref="ruleForm"
      class="demo-ruleForm"
      status-icon
      label-width="120px"
    >
      <el-form-item label="基础热度值（非热门）" label-width="180px">
        <el-input-number
          clearable
          :min="0"
          class="input-item"
          v-model="config.baseHeat"
          placeholder="请输入"
        />
      </el-form-item>
      <el-form-item label="基础热度值（热门）" label-width="180px">
        <el-input-number
          clearable
          :min="0"
          class="input-item"
          v-model="config.baseHeatHot"
          placeholder="请输入"
        />
      </el-form-item>
      <el-form-item label="直播间消息发送系数" label-width="180px">
        <el-input-number
          clearable
          :min="0"
          v-model="config.msgSendRatio"
          class="input-item"
          type="number"
          placeholder="请输入直播间消息发送系数"
        />
      </el-form-item>
      <el-form-item label="直播间分享系数" label-width="180px">
        <el-input-number
          clearable
          :min="0"
          v-model="config.shareRatio"
          class="input-item"
          type="number"
          placeholder="请输入直播间分享系数"
        />
      </el-form-item>
      <!-- <el-form-item label="30分钟增加值" label-width="180px">
        <div style="display: flex; align-items: center">
          <el-input-number
            clearable
            :min="0"
            class="input-item"
            type="number"
            v-model="config.thirtyAddLower"
            placeholder="请输入增加值"
          />
          <div class="divider-line"></div>
          <el-input-number
            clearable
            class="input-item"
            :min="0"
            type="number"
            v-model="config.thirtyAddUpper"
            placeholder="请输入增加值"
          />
          <span style="font-size: 12px; margin-left: 4px"
            >每直播30分钟，在此区间内随机增加</span
          >
        </div>
      </el-form-item> -->
      <el-form-item label="人数系数值" label-width="180px">
        <el-input-number
          clearable
          :min="0"
          v-model="config.peopleNumberRatio"
          class="input-item"
          type="number"
          placeholder="请输入人数系数值"
        />
      </el-form-item>
      <el-form-item label="CDN推流地址配置" label-width="190px">
        <el-input
          :maxlength="100"
          style="width: 300px"
          v-model="config.streamingAddress"
          placeholder="请填写推流地址"
        />
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getLiveConfig, updateLiveConfig } from "@/api/common/index";
export default {
  name: "CommonSetting",
  data() {
    return {
      config: {
        baseHeat: 0,
        baseHeatHot: 0,
        thirtyAddUpper: 0,
        thirtyAddLower: 0,
        peopleNumberRatio: 0,
        streamingAddress: "",
        msgSendRatio: 0,
        shareRatio: 0,
      },
      loading: true,
    };
  },
  created() {
    this.getConfig();
  },
  methods: {
    getConfig() {
      this.loading = true;
      getLiveConfig().then((res) => {
        this.config = res.data || {
          baseHeat: 0,
          baseHeatHot: 0,
          thirtyAddUpper: 0,
          thirtyAddLower: 0,
          peopleNumberRatio: 0,
          streamingAddress: "",
          msgSendRatio: 0,
          shareRatio: 0,
        };
        this.loading = false;
      });
    },
    onUpdate() {
      updateLiveConfig(this.config).then(() => {
        this.$modal.msgSuccess("修改成功");
      });
    },
  },
};
</script>

<style scoped lang="scss">
.settings {
  display: flex;
  flex-direction: column;

  .divider-line {
    width: 30px;
    height: 1px;
    margin: 0 8px;
    background: rgba(156, 163, 175, 1);
  }

  .input-item {
    width: 160px;
  }

  :deep(.el-form-item__label) {
    justify-content: flex-start;
  }

  .push-input {
    width: 60%;
  }
}
</style>
