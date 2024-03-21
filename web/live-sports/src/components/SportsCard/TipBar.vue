<template>
  <div class="sports-tip-bar-box">
    <div class="sports-tip-bar__text">
      <slot>
        <template v-if="tip">{{ tip }}</template>
      </slot>
    </div>
    <div class="sports-tip-bar__line">
      <span class="sports-tip-bar__line--bar"></span>
      <span class="sports-tip-bar__line--bar"></span>
      <span class="sports-tip-bar__line--bar"></span>
      <span class="sports-tip-bar__line--bar"></span>
    </div>
  </div>
</template>
<script setup lang="ts">
  withDefaults(
    defineProps<{
      tip?: string;
    }>(),
    {
      tip: '',
    },
  );
</script>
<script lang="ts">
  export default {
    name: 'TipBar',
  };
</script>
