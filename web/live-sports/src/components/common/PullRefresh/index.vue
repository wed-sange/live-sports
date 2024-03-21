<template>
  <van-pull-refresh v-model="isLoading" head-height="66" @refresh="onRefresh">
    <!-- 下拉提示，通过 scale 实现一个缩放效果 -->
    <template #pulling>
      <IconLoading />
    </template>

    <!-- 释放提示 -->
    <template #loosing>
      <IconLoading />
    </template>

    <!-- 加载提示 -->
    <template #loading>
      <IconLoading />
    </template>
    <slot></slot>
  </van-pull-refresh>
</template>
<script setup lang="ts">
  import IconLoading from './IconLoading/index.vue';
  const props = withDefaults(
    defineProps<{
      modelValue: boolean;
    }>(),
    {
      modelValue: false,
    },
  );
  const emit = defineEmits(['update:modelValue', 'refresh']);
  const isLoading = computed({
    get() {
      return props.modelValue;
    },
    set(val) {
      emit('update:modelValue', val);
    },
  });
  const onRefresh = () => {
    emit('refresh');
  };
</script>
<script lang="ts">
  export default {
    name: 'PullRefresh',
  };
</script>
