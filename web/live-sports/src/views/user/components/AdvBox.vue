<template>
  <div class="adv-box flex items-center px-[30px] py-[20px] cursor-pointer" v-if="advInfo.id" @click="handleClick">
    <div class="adv-box__card flex justify-center items-center w-full h-[160px] rounded-[16px] text-[#F5F5F5] text-[24px] relative">
      <UseImg
        class="absolute top-0 left-0 w-full h-full rounded-[inherit] overflow-hidden"
        v-if="advInfo.imgUrl"
        :src="advInfo.imgUrl"
        alt="logo"
      />
    </div>
  </div>
</template>
<script setup lang="ts">
  import UseImg from '@/components/common/UseImg.vue';
  import { useResourceStore } from '@/store/modules/resource';

  const resourceStore = useResourceStore();

  const advInfo = computed(() => {
    return resourceStore.adInfo;
  });

  const handleClick = () => {
    const { targetUrl } = advInfo.value;
    if (!targetUrl) return;
    const hasPrefix = /^(https?:\/\/|\/\/)/.test(targetUrl);
    window.open(hasPrefix ? targetUrl : '//' + targetUrl, '_blink');
  };
</script>
<script lang="ts">
  export default {
    name: 'AdvBox',
  };
</script>

<style scoped lang="scss">
  .adv-box__card {
    background: linear-gradient(179.69deg, #2e2e2e 0.27%, rgba(46, 46, 46, 0) 183.38%);
    @include use-active-style();
  }
</style>
