<template>
  <div class="flex items-center flex-wrap pl-[28px]">
    <div v-for="(emoji, index) in emoji1" :key="index" class="mx-[12px] mb-[16px]">
      <div @click="$emit('on-select', emoji)" class="w-[40px] h-[40px] text-[42px]">{{ emoji }}</div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import emojiObj from '@/assets/emoji.json';
  const emoji1 = ref([] as String[]);
  onMounted(() => {
    Object.keys(emojiObj).map((key, index) => {
      if (index < 80) {
        emoji1.value.push(key);
      }
    });
  });
</script>
<script lang="ts">
  export default {
    name: 'Emoji',
  };
</script>
<style scoped lang="scss"></style>
