<template>
  <div class="broadcast" style="padding: 26px; background: white;">
    <div class="flex flex-row-reverse">
      <el-button type="primary" @click="onSaveInfo('ruleForm')">保存</el-button>
      <el-button style="margin-right: 8px;" @click="onBack">返回</el-button>
    </div>
    <div style="display: flex;">
      <el-form ref="ruleForm" :model="ruleForm" :rules="rules" class="demo-ruleForm" status-icon label-width="120px">
        <el-form-item style="margin-top: 16px;" label="文章标题" prop="title">
          <div style="width: 400px;">
            <el-input clearable maxlength="30" v-model="ruleForm.title" placeholder="请输入标题" />
          </div>
        </el-form-item>
        <el-form-item label="封面" class="mt-[24px]" prop="mainPic">
          <input type="file" v-show="false" @change="onImgSelect" ref="imgRef" />
          <div style="border-radius: 8px; display: flex;flex-direction: column; justify-content: center;">
            <div v-if="!ruleForm.mainPic" style="width: 120px; height: 120px; cursor: pointer;" class=" upload-icon"
              @click="onUploadImg" />
            <div v-else style="width: 120px; height: 120px;position: relative; cursor: pointer;border-radius: 8px;"
              @click="onUploadImg">
              <img style="width: 120px; height: 120px;position: absolute; cursor: pointer;border-radius: 8px;"
                :src="ruleForm.mainPic" />
              <div
                style="width: 120px; background: #000; height: 30px; opacity: 0.7; display: flex; align-items: center; justify-content: center; position: absolute; bottom: 0;">
                <span class="text-[14px] font-medium text-white">重新上传</span>
              </div>
            </div>
          </div>
        </el-form-item>

        <el-form-item class="mt-[24px]" label="内容" prop="content">
          <div class="border-[1px] border-[#eee] rounded-[4px]">
            <Editor ref="editorRef" :value="ruleForm.content" />
          </div>
        </el-form-item>
        <el-form-item class="mt-[16px]" label="排序值" prop="sortOrder">
          <div class="w-[200px]">
            <el-input-number clearable :min="1" v-model="ruleForm.sortOrder" />
          </div>
        </el-form-item>
        <el-form-item class="mt-[16px]" label="状态" prop="status">
          <div class="w-[200px]">
            <el-select v-model="ruleForm.status" placeholder="请选择">
              <el-option v-for="item in status" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </div>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import router from '@/router'
import { uploadImg } from "@/api/common.js"
import Editor from "@/components/Editor/index"
import {
  getActivityDetail,
  updateActivity,
  createActivity
} from "@/api/operations/index";


export default {
  name: "ActivityDetail",
  data() {
    return {
      status: [
        { label: "上架", value: 1 },
        { label: "下架", value: 0 }
      ],
      loading: true,
      ruleForm: {
        id: 0,
        title: "",
        mainPic: "",
        sortOrder: 0,
        status: 0,
        content: ""
      },
      rules: {
        title: [{ required: true, message: "请输入标题", trigger: "blur" }],
        mainPic: [{ required: true, message: "请添加封面", trigger: "blur" }]
      }
    };
  },
  created() {
    const { query } = router.currentRoute;
    this.getDetail(query.id);
  },
  methods: {
    /** 获取活动详情信息 */
    getDetail(id) {
      if (!parseFloat(id)) {
        return;
      }
      this.loading = true;
      getActivityDetail(id).then((res) => {
        this.loading = false;
        Object.keys(res.data).forEach(key => {
          this.ruleForm[key] = res.data[key];
        });
      });
    },
    onSaveInfo(formName) {
      const onSuccess = () => {
        this.$message.success(this.ruleForm.id ? "修改成功" : "创建成功")
        this.onBack();
      };
      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.ruleForm.content = this.$refs.editorRef.getContent()
          if (this.ruleForm.id) {
            updateActivity(this.ruleForm).then(onSuccess);
          } else {
            createActivity(this.ruleForm).then(onSuccess);
          }
        } else {
          return false;
        }
      });
    },
    onBack() {
      this.$router.replace("/operations/activity")
    },

    onImgSelect(e) {
      const file = e.target.files[0];
      const param = new FormData();
      param.append("file", file);
      e.srcElement.value = "";
      uploadImg(param).then((result) => {
        this.ruleForm.mainPic = result.data;
      });
    },
    onUploadImg() {
      this.$refs.imgRef.click();
    }
  }
};
</script>

<style scoped lang="scss">
.broadcast {
  display: flex;
  flex-direction: column;

  .upload-icon {
    background: url("../../../assets/images/upload-icon.png") no-repeat center;
    background-size: contain;
  }

  .upload-icon:hover {
    background: url("../../../assets/images/hover-upload-icon.png") no-repeat center;
    background-size: contain;
  }

  .el-textarea__inner {
    height: 91px;
    background: #f7f8f7 !important;
  }

  .el-input__count {
    background: #f7f8f7 !important;
  }
}
</style>

