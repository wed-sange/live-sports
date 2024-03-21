<template>
  <div class="chat-image">
    <img v-show="imgSrc" class="chat-image__img" :src="imgSrc" loading="lazy" @load="onSourceLoad($event)" alt="image" />
  </div>
</template>
<script setup lang="ts">
  import { ref, watch } from 'vue';
  const props = withDefaults(
    defineProps<{
      src: string;
    }>(),
    {
      src: '',
    },
  );
  const imgSrc = ref('');
  let currentImage: HTMLImageElement | null = null;
  const emit = defineEmits<{
    (event: 'on-load', imgEvent: Event): void;
  }>();
  const onSourceLoad = ($event: Event) => {
    emit('on-load', $event);
  };
  const createImgLoad = (src: string) => {
    if (currentImage) {
      currentImage.onload = null;
      currentImage.onerror = null;
    }
    const img = new Image();
    img.onload = () => {
      imgSrc.value = src;
      // onLoad();
    };
    img.onerror = () => {
      imgSrc.value = src;
      // onLoad();
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
    name: 'ChatImage',
  };
</script>
