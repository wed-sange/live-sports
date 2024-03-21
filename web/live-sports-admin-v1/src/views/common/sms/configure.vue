<template>
  <div class="configure-wrapper">
    <div class="add-wrapper">
      <el-button type="primary" plain @click="addConfCount" :disabled="loading">添加配置</el-button>
    </div>

    <div v-for="(num, index) in confCount" style="margin-top: 30px">
      <div class="item-header">
        <h1>配置{{ num }}</h1>
        <el-button v-if="index > 0" icon="el-icon-minus" @click="delConfCount(index)" :disabled="loading"></el-button>
      </div>

      <el-form :ref="`formRef_${index}`" :model="formValue[index]" :rules="rules[index]" inline label-width="95px">
        <el-form-item label="平台类型" prop="signature">
          <el-select v-model="formValue[index].supplier" placeholder="请选择平台类型" style="width: 188px">
            <el-option v-for="item in typeOptions" :key="item.flag" :label="item.label" :value="item.flag"/>
          </el-select>
        </el-form-item>

        <el-form-item label="短信签名" prop="signature">
          <el-input v-model="formValue[index].signature" placeholder="请输入短信签名" />
        </el-form-item>

        <el-form-item label="短信模板ID" prop="templateId">
          <el-input v-model="formValue[index].templateId" placeholder="请输入模板ID" />
        </el-form-item>

        <!--  腾讯云专属-->
        <el-form-item label="AppID" prop="sdkAppId" v-if="formValue[index].supplier === 2">
          <el-input v-model="formValue[index].sdkAppId" placeholder="请输入AppID" />
        </el-form-item>

        <el-form-item label="AccessKey" prop="accessKeyId" >
          <el-input v-model="formValue[index].accessKeyId" placeholder="请输入AccessKey" />
        </el-form-item>

        <el-form-item label="SecretKey" prop="accessKeySecret" >
          <el-input v-model="formValue[index].accessKeySecret" placeholder="请输入SecretKey" />
        </el-form-item>

        <!--  阿里云专属-->
        <el-form-item label="SenderId" prop="sdkAppId" v-if="formValue[index].supplier === 1">
          <el-input v-model="formValue[index].sdkAppId" placeholder="请输入SenderId" />
        </el-form-item>

        <el-form-item label="备注信息" prop="remark" >
          <el-input v-model="formValue[index].remark" type="textarea" placeholder="请输入备注信息" :maxlength="128" style="width: 188px" />
        </el-form-item>

        <el-form-item label="状态" prop="openStatus" >
          <el-switch v-model="formValue[index].openStatus" active-text="启用" inactive-text="停用" />
        </el-form-item>

      </el-form>
    </div>

    <div style="text-align: center;margin-top: 70px">
      <el-button @click="resetForm" :disabled="loading">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">保存并启用</el-button>
    </div>


  </div>

</template>

<script>
import { querySmsInfo, updateSmsInfo } from "@/api/common/index";

export default {
  props: {
    typeOptions: {
      type: Array,
      required: true
    }
  },
  data () {
    return {
      loading: false,
      confCount: 0,

      baseFormValue: {
        supplier: 1,
        signature: '',
        templateId: '',
        sdkAppId: '',
        accessKeyId: '',
        accessKeySecret: '',
        remark: '',
        openStatus: true
      },
      formValue: [],

      baseRules: {
        signature: { required: true, message: '请输入短信签名', trigger: 'blur' },
        templateId: { required: true, message: '请输入模板ID', trigger: 'blur' },
        sdkAppId: { required: true, message: '请输入ID', trigger: 'blur' },
        accessKeyId: { required: true, message: '请输入AccessKey', trigger: 'blur' },
        accessKeySecret: { required: true, message: '请输入SecretKey', trigger: 'blur' },
        remark: { },
        openStatus: { required: true },
      },
      rules: [],
    }
  },
  mounted() {
    this.getSmsConf()
  },
  methods: {
    addConfData() {
      this.formValue.push({ ...this.baseFormValue })
      this.rules.push({ ...this.baseRules })
    },

    addConfCount() {
      this.confCount++
      this.addConfData()
    },

    delConfCount(i) {
      this.confCount--
      this.formValue.splice(i, 1)
    },

    // 取消，重置整个表单
    resetForm() {
      this.getSmsConf()

      this.$nextTick(() => {
        for (let i = 0; i < this.confCount;i++) {
          this.$refs[`formRef_${i}`][0].resetFields()
        }
      })
    },

    //  查询回显
    getSmsConf() {
      this.loading = true
      querySmsInfo()
        .then(({ data }) => {
          const { smsDOList } = data
          if (smsDOList && smsDOList.length) {
            this.confCount = smsDOList.length
            this.formValue = smsDOList
            this.rules = Array.from({ length: smsDOList.length }, () => ({
              ...this.baseRules
            }))
          } else {
            this.confCount = 1
            this.addConfData()
          }
        })
        .finally(() => {
          this.loading = false
        })
    },

    // 更新配置信息
    handleSubmit() {
      let isValidate = false
      for (let i = 0; i < this.confCount;i++) {
        this.$refs[`formRef_${i}`][0].validate(bool => {
          if (bool) {
            isValidate = true
          }
        })
      }

      if (isValidate) {
        this.loading = true
        updateSmsInfo({ smsDOList: this.formValue })
          .then(() => {
            this.$message.success("保存成功")
            this.getSmsConf()
          })
          .finally(() => {
            this.loading = false
          })
      }
    }
  }

}
</script>

<style scoped lang="scss">
.configure-wrapper {
  .add-wrapper {
    display: flex;
    justify-content: flex-end;
    margin-bottom: 30px;
  }
  .item-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    h1 {
      line-height: 38px;
      font-weight: bold;
    }
  }
}
</style>
