<template>
  <div :class="`app-header ${barImg ? 'header-img' : iosDivice ? 'ios-header' : 'android-header'}`">
    <img v-if="barImg" class="w-full h-full object-cover" :src="barImg" />
  </div>
</template>

<script setup lang="ts">
  import { isIos } from '@/utils/common';
  const iosDivice = isIos();
</script>
<script lang="ts">
  export default {
    name: 'AppHeader',
    props: {
      barImg: {
        type: String,
        default: '',
      },
    },
  };
</script>
<style scoped lang="scss">
  .app-header {
    height: 0;
    .immersive & {
      height: var(--app-header-height);
    }
  }
  .header-img {
    background: transparent;
  }
  .android-header {
    background: #000;
  }
  .ios-header {
    background: #fff;
  }
</style>
