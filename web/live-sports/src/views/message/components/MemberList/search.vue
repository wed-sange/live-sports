<template>
  <div class="w-full h-full overflow-auto">
    <van-pull-refresh class="min-h-full" v-model="refreshing" @refresh="onRefresh">
      <EmptyData class="pt-[18vh]" v-if="list.length === 0 && (refreshing || finished)" />
      <van-list
        v-model:loading="loading"
        :finished="finished"
        :finished-text="list.length === 0 ? '' : ''"
        v-model:error="error"
        error-text="请求失败，点击重新加载"
        @load="onLoad"
        class="pt-[24px]"
      >
        <template v-for="item in list" :key="item.id">
          <MemberCard :item="item" :remove="false" />
        </template>
      </van-list>
    </van-pull-refresh>
  </div>
</template>
<script setup lang="ts">
  import MemberCard from './MemberCard.vue';
  import { useSearch } from './useSearch';
  const { list, error, loading, finished, refreshing, onLoad, onRefresh, handleSearch } = useSearch();
  defineExpose({
    handleSearch,
  });
</script>
<script lang="ts">
  export default {
    name: 'MemberSearchList',
  };
</script>
