<template>
  <div>
    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body @opened="modalOpened"
      @close="closeDialog">
      <el-row style="display: flex; align-items: center; justify-content: center;">
        <el-col :xs="24" :md="12" :style="{ height: '350px', width: imgOptions.autoCropWidth + 'px' }">
          <vue-cropper ref="cropper" :img="imgOptions.img" :info="true" :autoCrop="imgOptions.autoCrop"
            :autoCropWidth="imgOptions.autoCropWidth" :autoCropHeight="imgOptions.autoCropHeight"
            :fixedBox="imgOptions.fixedBox" :outputType="imgOptions.outputType" @realTime="realTime" v-if="visible" />
        </el-col>
        <!-- <el-col :xs="24" :md="12" :style="{ height: '350px' }">
          <div class="avatar-upload-preview">
            <img :src="previews.url" :style="previews.img" />
          </div>
        </el-col> -->
      </el-row>
      <br />
      <el-row>
        <el-col :lg="2" :sm="3" :xs="3">
          <el-upload action="#" :http-request="requestUpload" :show-file-list="false" :before-upload="beforeUpload">
            <el-button size="small">
              选择
              <i class="el-icon-upload el-icon--right"></i>
            </el-button>
          </el-upload>
        </el-col>
        <el-col :lg="{ span: 1, offset: 2 }" :sm="2" :xs="2">
          <el-button icon="el-icon-plus" size="small" @click="changeScale(1)"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :sm="2" :xs="2">
          <el-button icon="el-icon-minus" size="small" @click="changeScale(-1)"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :sm="2" :xs="2">
          <el-button icon="el-icon-refresh-left" size="small" @click="rotateLeft()"></el-button>
        </el-col>
        <el-col :lg="{ span: 1, offset: 1 }" :sm="2" :xs="2">
          <el-button icon="el-icon-refresh-right" size="small" @click="rotateRight()"></el-button>
        </el-col>
        <el-col :lg="{ span: 2, offset: 6 }" :sm="2" :xs="2">
          <el-button type="primary" size="small" @click="onUploadImg()">提 交</el-button>
        </el-col>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import { VueCropper } from "vue-cropper";
import { uploadImg } from "@/api/common";
import { debounce } from '@/utils'

export default {
  components: { VueCropper },
  props: {
    imgOptions: {
      type: Object,
    }
  },
  data() {
    return {
      // 是否显示弹出层
      open: false,
      // 是否显示cropper
      visible: false,
      // 弹出层标题
      title: "图片剪裁",
      previews: {},
      resizeHandler: null
    };
  },
  created() {
    console.log("imgOptions", this.imgOptions)
    setTimeout(() => {
      this.editCropper()
    }, 1000);
  },
  methods: {
    // 编辑头像
    editCropper() {
      this.open = true;
    },
    // 打开弹出层结束时的回调
    modalOpened() {
      this.visible = true;
      if (!this.resizeHandler) {
        this.resizeHandler = debounce(() => {
          this.refresh()
        }, 100)
      }
      window.addEventListener("resize", this.resizeHandler)
    },
    // 刷新组件
    refresh() {
      this.$refs.cropper.refresh();
    },
    // 覆盖默认的上传行为
    requestUpload() {
    },
    // 向左旋转
    rotateLeft() {
      this.$refs.cropper.rotateLeft();
    },
    // 向右旋转
    rotateRight() {
      this.$refs.cropper.rotateRight();
    },
    // 图片缩放
    changeScale(num) {
      num = num || 1;
      this.$refs.cropper.changeScale(num);
    },
    // 上传预处理
    beforeUpload(file) {
      if (file.type.indexOf("image/") == -1) {
        this.$modal.msgError("文件格式错误，请上传图片类型,如：JPG，PNG后缀的文件。");
      } else {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
          this.imgOptions.img = reader.result;
        };
      }
    },
    // 上传图片
    onUploadImg() {
      this.$refs.cropper.getCropBlob(data => {
        let formData = new FormData();
        formData.append("file", data);
        uploadImg(formData).then(response => {
          this.open = false;
          this.$emit('onImg', response.data)
          this.visible = false;
          this.open = false;
        });
      });
    },
    // 实时预览
    realTime(data) {
      console.log("datadatadata", data)
      this.previews = data;
    },
    // 关闭窗口
    closeDialog() {
      // this.imgOptions.img = store.getters.avatar
      this.$emit('close')
      this.visible = false;
      this.open = false
      window.removeEventListener("resize", this.resizeHandler)
    }
  }
};
</script>
<style scoped lang="scss">
.user-info-head {
  position: relative;
  display: inline-block;
  height: 120px;
}

.user-info-head:hover:after {
  content: '+';
  position: absolute;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  color: #eee;
  background: rgba(0, 0, 0, 0.5);
  font-size: 24px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  cursor: pointer;
  line-height: 110px;
  border-radius: 50%;
}
</style>
