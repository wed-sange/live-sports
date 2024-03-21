<!-- eslint-disable vue/no-v-html -->
<template>
  <div
    class="chat-image"
    style="width: 362px; height: 362px; background: #f7f7f7"
  >
    <video
      class="w-[300px]"
      v-show="src"
      :src="coverAndUrl[1]"
      :poster="coverAndUrl[0]"
      loading="lazy"
      @load="onSourceLoad($event)"
      controls
      alt="video"
      style="width: 100%; height: 100%"
    />
  </div>
</template>
<script setup lang="ts">
import { computed } from "vue";

defineOptions({
  name: "ChatVideo"
});
const props = withDefaults(
  defineProps<{
    src: string;
  }>(),
  {
    src: ""
  }
);

const coverAndUrl = computed(() => props.src.split("***"));

// const imgSrc = ref("");
// const currentImage: HTMLImageElement | null = null;
const emit = defineEmits<{
  (event: "on-load", imgEvent: Event): void;
}>();
const onSourceLoad = ($event: Event) => {
  emit("on-load", $event);
};
// const createImgLoad = (src: string) => {
//   if (currentImage) {
//     currentImage.onload = null;
//     currentImage.onerror = null;
//   }
//   const img = new Image();
//   img.onload = () => {
//     imgSrc.value = src;
//     // onLoad();
//   };
//   img.onerror = () => {
//     imgSrc.value = src;
//     // onLoad();
//   };
//   img.src = src;
//   currentImage = img;
// };

// const loadVideo: (file: File) => Promise<HTMLVideoElement> = function (
//   file: File
// ) {
//   return new Promise(function (resolve, reject) {
//     const videoElem = document.createElement("video");
//     const dataUrl = URL.createObjectURL(file);
//     // 当前帧的数据是可用的
//     videoElem.onloadeddata = function () {
//       resolve(videoElem);
//     };
//     videoElem.onerror = function () {
//       reject("video 后台加载失败");
//     };
//     // 设置 auto 预加载数据， 否则会出现截图为黑色图片的情况
//     videoElem.setAttribute("preload", "auto");
//     videoElem.src = dataUrl;
//   });
// };
// watch(
//   () => props.src,
//   () => {
//   if (props.src) createImgLoad(props.src);
//   },
//   {
//     immediate: true
//   }
// );
</script>
