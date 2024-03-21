<template>
  <van-overlay :show="showVideo" @click="$emit('on-close')" class="w-full h-full flex items-center justify-center">
    <img
      @click.stop="$emit('on-close')"
      class="left-[18px] top-[32px] fixed z-[10] back-icon w-[32px] h-[32px] object-contain"
      :src="backIcon"
    />
    <div class="flex items-center max-h-[80vh] justify-center">
      <video
        class="object-contain max-h-screen w-screen"
        @click.stop
        controls
        :src="videoUrl"
        playsinline
        webkit-playsinline
        :poster="poster"
        autoplay
      ></video>
    </div>
  </van-overlay>
</template>
<script setup lang="ts">
  import backIcon from '@/assets/img/common/icon_nav_bar_left_w.png';
  withDefaults(
    defineProps<{
      videoUrl?: string;
      poster?: string;
    }>(),
    {
      src: '',
      poster: '',
    },
  );
  const showVideo = ref(true);
</script>

<script lang="ts">
  export default {
    name: 'ChatVideo',
  };
</script>
