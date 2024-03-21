<template>
  <div class="page-box flex flex-col h-full bg-[#F2F3F7]">
    <div class="page-header grow-0 shrink-0 basis-auto" v-if="$slots.header">
      <slot name="header"></slot>
    </div>
    <div class="page-body flex-auto h-full overflow-auto">
      <slot></slot>
    </div>
    <div class="page-footer grow-0 shrink-0 basis-auto" v-if="$slots.footer">
      <slot name="footer"></slot>
    </div>
  </div>
</template>
<script setup lang="ts"></script>
<script lang="ts">
  export default {
    name: 'PageBox',
  };
</script>
