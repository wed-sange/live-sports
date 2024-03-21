<template>
  <div
    v-if="show"
    class="ui-check-box"
    :class="[dragClass]"
    @click="btnShow = !btnShow"
    @dragover.prevent="handleDragOver"
    @dragenter.prevent
    @dragleave.prevent="handleDragLeave"
    @drop.prevent="handleDrop"
  >
    <div class="ui-check-box__inner">
      <div class="ui-check-body" :style="boxStyle">
        <img :src="previewUrl" />
      </div>
      <div class="ui-check-btn" v-show="btnShow" @click.stop>
        <div class="ui-check-group">
          <van-button plain type="success" @click.stop="resetState">重置</van-button>
          <van-button plain type="success" @click.stop="chooseFile">选择一个图片</van-button>
          <input type="file" ref="inputRef" id="file-input" @change="handleFileSelect($event)" />
        </div>
        <div class="ui-check-group">
          <div class="ui-check-group__label">透明度:</div>
          <div class="ui-check-group__value"><van-slider v-model="boxOpacity" :min="0" :max="100" /></div>
        </div>
        <div class="ui-check-group">
          <div class="ui-check-group__label">偏移位置:</div>
          <div class="ui-check-group__value">
            <div>left:<van-stepper v-model="boxLeft" theme="round" button-size="22" min="-Infinity" /></div>
            <div>top:<van-stepper v-model="boxTop" theme="round" button-size="22" min="-Infinity" /></div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
  const boxOpacity = ref(50);
  const boxLeft = ref(0);
  const boxTop = ref(0);
  const show = ref(false);
  const btnShow = ref(true);
  const boxStyle = computed(() => {
    return {
      opacity: boxOpacity.value / 100,
      transform: `translate(${boxLeft.value}px,${boxTop.value}px)`,
    };
  });
  const resetState = () => {
    revokeFileUrl(previewUrl.value);
    previewUrl.value = '';
    boxOpacity.value = 50;
    boxLeft.value = 0;
    boxTop.value = 0;
  };
  const dragClass = ref('');
  const handleDragOver = (event) => {
    event.preventDefault();
    event.stopPropagation();
    dragClass.value = 'highlight';
    // 设置 effectAllowed，避免默认打开新标签页
    event.dataTransfer.effectAllowed = 'copy';
  };

  const handleDragLeave = (event) => {
    event.preventDefault();
    event.stopPropagation();
    dragClass.value = '';
  };

  const handleDrop = (event) => {
    event.preventDefault();
    event.stopPropagation();

    const files = event.dataTransfer.files;
    dragClass.value = '';
    handleFiles(files);
    event.dataTransfer.clearData();
  };
  const inputRef = ref<HTMLInputElement | null>(null);
  const chooseFile = () => {
    inputRef.value && inputRef.value.click();
  };
  const handleFileSelect = (event) => {
    const files = event.target.files;
    handleFiles(files);
    inputRef.value!.value = '';
  };
  const previewUrl = ref('');
  const createFileUrl = (file: File) => {
    return URL.createObjectURL(file);
  };
  const revokeFileUrl = (url) => {
    url.indexOf('blob:') === 0 && URL.revokeObjectURL(url);
  };
  const handleFiles = (files) => {
    revokeFileUrl(previewUrl.value);
    previewUrl.value = createFileUrl(files[0]);
  };
  const toggleShow = () => {
    show.value = !show.value;
  };
  defineExpose({
    toggleShow,
  });
</script>
<script lang="ts">
  export default {
    name: 'UiCheck',
  };
</script>
<style scoped lang="scss">
  .ui-check-box {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    z-index: 99999;
    &.highlight {
      &::before {
        content: '';
        border: 8px dashed #f3f;
        display: block;
        position: absolute;
        top: 0;
        left: 0;
        z-index: 4;
        width: 100%;
        height: 100%;
      }
    }
    .ui-check-box__inner {
      width: 100%;
      height: 100%;
      overflow: hidden;
    }
    .ui-check-body {
      width: 100%;
      min-height: 100%;
      img {
        width: 100%;
        height: auto;
      }
    }
    .ui-check-btn {
      position: absolute;
      bottom: 0;
      left: 0;
      background: #fff;
      z-index: 2;
      padding: 32px;
      width: 100%;
    }
    .ui-check-group {
      width: 100%;
      display: flex;
      align-items: center;
      padding: 20px 0;
      .ui-check-group__label {
        font-size: 28px;
        color: #333;
        flex: 0 0 200px;
        width: 200px;
      }
      .ui-check-group__value {
        flex: 0 0 calc(100% - 200px);
        width: calc(100% - 200px);
      }
      .van-button {
        & ~ .van-button {
          margin-left: 12px;
        }
      }
    }
    #file-input {
      opacity: 0;
      height: 0;
      width: 0;
      pointer-events: none;
      position: absolute;
    }
  }
</style>
