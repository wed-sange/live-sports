<template>
  <el-form ref="formRef" :model="formValue" :rules="rules" label-width="125px">
    <el-form-item>
      <h3>短信设置</h3>
    </el-form-item>

    <el-form-item label="验证方式" prop="vaTypeList">
      <el-select v-model="formValue.vaTypeList" multiple placeholder="请选择方式" style="width: 100%">
        <el-option v-for="item in validateList" :key="item.id" :label="item.label" :value="item.id"/>
      </el-select>
    </el-form-item>

    <el-form-item label="使用方式" prop="useType">
      <el-radio-group v-model="formValue.useType">
        <el-radio :label="1">随机使用</el-radio>
        <el-radio :label="2">轮替使用</el-radio>
      </el-radio-group>
    </el-form-item>

    <el-form-item v-if="formValue.useType === 2" label="轮替次数" prop="ratateNum">
      <el-input-number v-model="formValue.ratateNum" :min="1" :max="100" label="" placeholder="请输入次数" />
    </el-form-item>

    <el-form-item>
      <h3>系统通知</h3>
    </el-form-item>

    <el-form-item label="异常次数" prop="errorNum">
      <el-input-number v-model="formValue.errorNum" :min="1" :max="100" label="" placeholder="请输入次数" />
    </el-form-item>

    <el-form-item label="系统通知联系人" prop="contactInfo">
      <el-input v-model="formValue.contactInfo" :controls="false" placeholder="请输入手机号" :maxlength="15" clearable />
    </el-form-item>

    <el-form-item style="margin-top: 70px">
      <el-button @click="resetForm" :disabled="loading">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">保存并启用</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { getSmsVaList, getSmsRuleInfo, updateSmsRuleInfo } from "@/api/common/index";

export default {
  props: {
    typeOptions: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      loading: false,
      validateList: [],

      formValue: {
        vaTypeList: [],
        useType: 1,
        ratateNum: 20,
        errorNum: 10,
        contactInfo: ''
      },
      rules: {
        vaTypeList: { required: true, message: '请选择方式', trigger: 'blur' },
        useType: { required: true },
        ratateNum: { required: true, message: '请输入次数', trigger: 'blur' },
        errorNum: { required: true, message: '请输入次数', trigger: 'blur' },
        contactInfo: { required: true, message: '请输入手机号', trigger: 'blur' },
      }
    }
  },
  mounted() {
    this.getSmsValidate()
    this.getSmsRuleData()
  },
  methods: {
    //  获取验证方式
    getSmsValidate() {
      this.loading = true
      getSmsVaList()
        .then(({ data }) => {
          this.validateList = data.map((v, i) => ({ ...v, label: `配置${i + 1}（${this.typeOptions.find(a => a.flag === v.supplier).label}）` }))
        })
        .finally(() => {
          this.loading = false
        })
    },

    // 查询回显
    getSmsRuleData() {
      this.loading = true
      getSmsRuleInfo()
        .then(({ data }) => {
          if (data) {
            this.formValue = data
          }
        })
        .finally(() => {
          this.loading = false
        })
    },

    // 取消，重置整个表单
    resetForm() {
      this.getSmsRuleData()
    },

    // 提交
    handleSubmit() {
      this.$refs.formRef.validate(bool => {
        if (!bool) return
        const params = { ...this.formValue }
        if (this.formValue.useType === 1) {
          params.ratateNum = 0
        }

        this.loading = true
        updateSmsRuleInfo(params)
          .then(() => {
            this.$message.success("保存成功")
          })
          .finally(() => {
            this.loading = false
          })
      })
    }
  }
}
</script>

<style scoped lang="scss">
.el-form {
  max-width: 450px;
  h3 {
    font-weight: bold;
    margin-top: 30px;
  }
}

</style>
