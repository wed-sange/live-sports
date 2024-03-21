<!-- eslint-disable vue/html-self-closing -->
<template>
  <div class="chat-loading-box" :style="{ color: color || undefined }">
    <svg class="chat-loading__circular" viewBox="25 25 50 50">
      <circle cx="50" cy="50" r="20" fill="none" transform="rotate(-90 50 50)"></circle>
    </svg>
  </div>
</template>
<script setup lang="ts">
  withDefaults(
    defineProps<{
      color?: string;
    }>(),
    {
      color: '',
    },
  );
</script>
<script lang="ts">
  export default {
    name: 'ChatLoading',
  };
</script>
