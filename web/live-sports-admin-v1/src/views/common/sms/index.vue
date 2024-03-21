<template>
  <el-tabs v-model="activeName" class="sms-tabs">
    <el-tab-pane label="短信配置" name="configure">
      <configure v-if="activeName === 'configure'" :typeOptions="typeOptions" />
    </el-tab-pane>

    <el-tab-pane label="短信功能设置" name="setting">
      <setting v-if="activeName === 'setting'" :typeOptions="typeOptions" />
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import configure from "./configure.vue"
import setting from "./setting.vue"

export default {
  name: "AnchorManage",
  components: { configure, setting },
  data() {
    return {
      activeName: 'configure',
      typeOptions: [
        { label: '阿里云', flag: 1, key: 'ali' },
        { label: '腾讯云', flag: 2, key: 'tencent' },
      ]
    }
  }
}
</script>

<style scoped lang="scss">
.el-tabs {
  padding: 16px 20px;
}


</style>
<style lang="scss">
.sms-tabs .el-tabs__content {
  padding: 20px;
}
</style>
