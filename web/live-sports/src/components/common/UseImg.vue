<template>
  <div class="use-img">
    <div class="use-img__loading" v-if="imgLoading">
      <slot name="loading"><i v-show="!notFillIcon" class="use-img__icon--loading"></i></slot>
    </div>
    <div class="use-img__error" v-else-if="!imgSrc">
      <slot name="error"><i v-show="!notFillIcon" class="use-img__icon--error"></i></slot>
    </div>
    <img v-if="imgSrc" class="use-img__image" :src="imgSrc" @load="onSourceLoad($event)" @error="onSourceError" :alt="alt" />
  </div>
</template>
<script setup lang="ts">
  import { ref, watch } from 'vue';
  const props = withDefaults(
    defineProps<{
      src: string;
      alt?: string;
      notFillIcon?: boolean;
    }>(),
    {
      src: '',
      alt: 'image',
      notFillIcon: false,
    },
  );
  const imgSrc = ref('');
  const imgLoading = ref(true);
  let currentImage: HTMLImageElement | null = null;
  const emit = defineEmits<{
    (event: 'on-load', imgEvent: Event): void;
  }>();
  const onSourceLoad = ($event: Event) => {
    imgLoading.value = false;
    emit('on-load', $event);
  };
  const onSourceError = () => {
    imgLoading.value = false;
  };
  const createImgLoad = (src: string) => {
    if (currentImage) {
      currentImage.onload = null;
      currentImage.onerror = null;
    }
    imgSrc.value = '';
    const img = new Image();
    img.onload = () => {
      imgSrc.value = src;
    };
    img.onerror = () => {
      imgLoading.value = false;
    };
    img.src = src;
    currentImage = img;
  };
  watch(
    () => props.src,
    () => {
      if (props.src) createImgLoad(props.src);
    },
    {
      immediate: true,
    },
  );
</script>

<script lang="ts">
  export default {
    name: 'UseImg',
  };
</script>
<style lang="scss">
  .use-img {
    position: relative;
    display: block;
    overflow: hidden;
    .use-img__image {
      display: block;
      width: 100%;
      height: 100%;
      border-radius: inherit;
      object-fit: cover;
      overflow: hidden;
    }
    .use-img__error,
    .use-img__loading {
      position: absolute;
      top: 0;
      left: 0;
      z-index: 2;
      width: 100%;
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      border-radius: inherit;
      overflow: hidden;
      background: #e5e7ed;
    }
    .use-img__icon--error,
    .use-img__icon--loading {
      display: inline-block;
      width: 76px;
      height: 64px;
      background: url('data:image/png;base64,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')
          no-repeat,
        transparent;
      background-size: auto 100%;
      background-position: center;
    }
  }
</style>
