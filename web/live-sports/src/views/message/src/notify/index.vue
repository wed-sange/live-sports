<template>
  <page-box>
    <template #header><NavBar title="系统通知" left-arrow @click-left="onLeftClick" /></template>
    <div class="w-full h-full">
      <van-list
        v-model:loading="loading"
        :finished="finished"
        :finished-text="list.length === 0 ? '' : ''"
        v-model:error="error"
        error-text="请求失败，点击重新加载"
        @load="onLoad"
        class="pt-[30px]"
      >
        <template v-for="item in list" :key="item.id">
          <NotifyCard :item="item" />
        </template>
      </van-list>
    </div>
  </page-box>
</template>
<script setup lang="ts">
  import NotifyCard from './NotifyCard.vue';
  import { useIndex } from './useIndex';
  const { list, error, loading, finished, onLoad } = useIndex();
  const router = useRouter();
  const onLeftClick = () => {
    router.go(-1);
  };
</script>
<script lang="ts">
  export default {
    name: 'MessageNotify',
  };
</script>
