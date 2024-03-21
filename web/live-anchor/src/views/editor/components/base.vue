<script setup lang="ts">
import { onBeforeUnmount, ref, shallowRef } from "vue";
import "@wangeditor/editor/dist/css/style.css";
import { Editor, Toolbar } from "@wangeditor/editor-for-vue";
import { useUserStore } from "@/store/modules/user";
const { VITE_BASE_API } = import.meta.env;

defineOptions({
  name: "picUpload"
});

const mode = "default";
// 编辑器实例，必须用 shallowRef
const editorRef = shallowRef();

const props = defineProps({
  content: {
    type: String,
    default: ""
  }
});

// 内容 HTML
const valueHtml = ref(props.content);
const toolbarConfig: any = {
  excludeKeys: "fullScreen,uploadVideo,group-image,group-video"
};
const editorConfig = { placeholder: "请输入内容...", MENU_CONF: {} };
const userStore = useUserStore();
// 更多详细配置看 https://www.wangeditor.com/v5/menu-config.html#%E4%B8%8A%E4%BC%A0%E5%9B%BE%E7%89%87
editorConfig.MENU_CONF["uploadImage"] = {
  // 服务端上传地址，根据实际业务改写
  server: VITE_BASE_API + "/file/common/image",
  // form-data 的 fieldName，根据实际业务改写
  fieldName: "file",
  meta: {
    sportstoken: userStore.token
  },
  // 选择文件时的类型限制，根据实际业务改写
  allowedFileTypes: ["image/png", "image/jpg", "image/jpeg"],
  // 自定义插入图片
  customInsert(res: any, insertFn) {
    if (res.data) {
      setTimeout(() => {
        insertFn(res.data);
      }, 100);
    }
  }
};
editorConfig.MENU_CONF["uploadVideo"] = {
  // 服务端上传地址，根据实际业务改写
  server: VITE_BASE_API + "/file/app/source",
  // form-data 的 fieldName，根据实际业务改写
  fieldName: "file",
  maxFileSize: 500 * 1024 * 1024, // 5M
  // 选择文件时的类型限制，根据实际业务改写
  allowedFileTypes: ["video/*"],

  // 跨域是否传递 cookie ，默认为 false
  withCredentials: true,

  // 超时时间，默认为 30 秒
  timeout: 60 * 1000, // 60 秒
  onBeforeUpload(file: File) {
    // TS 语法
    // onBeforeUpload(file) {      // JS 语法
    // file 选中的文件，格式如 { key: file }
    console.log("file", file);
    return file;

    // 可以 return
    // 1. return file 或者 new 一个 file ，接下来将上传
    // 2. return false ，不上传这个 file
  },
  // 上传错误，或者触发 timeout 超时
  onError(file: File, err: any, res: any) {
    // TS 语法
    // onError(file, err, res) {               // JS 语法
    console.log(`${file.name} 上传出错`, err, res);
  },
  // 上传进度的回调函数
  onProgress(progress: number) {
    // TS 语法
    // onProgress(progress) {       // JS 语法
    // progress 是 0-100 的数字
    console.log("progress", progress);
  },

  // 单个文件上传成功之后
  onSuccess(file: File, res: any) {
    // TS 语法
    // onSuccess(file, res) {          // JS 语法
    console.log(`${file.name} 上传成功`, res);
  },

  // 单个文件上传失败
  onFailed(file: File, res: any) {
    // TS 语法
    // onFailed(file, res) {          // JS 语法
    console.log(`${file.name} 上传失败`, res);
  },
  // 自定义插入图片
  customInsert(res: any, insertFn) {
    console.log("resres", res);
    if (res.data) {
      setTimeout(() => {
        insertFn(res.data);
      }, 100);
    }
  }
};

const handleCreated = editor => {
  // 记录 editor 实例，重要！
  editorRef.value = editor;
};

// 组件销毁时，也及时销毁编辑器
onBeforeUnmount(() => {
  const editor = editorRef.value;
  if (editor == null) return;
  editor.destroy();
});
const setContent = value => {
  valueHtml.value = value;
};
const getContent = () => {
  return valueHtml.value;
};
const getSaveContent = () => {
  return valueHtml.value;
};
defineExpose({
  setContent,
  getContent,
  getSaveContent
});
</script>

<template>
  <div class="wangeditor">
    <Toolbar
      :editor="editorRef"
      :defaultConfig="toolbarConfig"
      :mode="mode"
      style="border-bottom: 1px solid #ccc"
    />
    <Editor
      v-model="valueHtml"
      :defaultConfig="editorConfig"
      :mode="mode"
      style="height: 500px; overflow-y: hidden"
      @onCreated="handleCreated"
    />
  </div>
</template>
