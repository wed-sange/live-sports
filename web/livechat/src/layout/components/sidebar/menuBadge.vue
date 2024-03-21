<!-- eslint-disable vue/no-v-html -->
<template>
  <div class="message-badge" :class="{ large: num > 9 }">
    <span class="message-badge__text">{{ num > 99 ? "99+" : num }}</span>
  </div>
</template>
<script setup lang="ts">
defineOptions({
  name: "MenuBadge"
});
withDefaults(
  defineProps<{
    num: number;
  }>(),
  {
    num: 0
  }
);
</script>
