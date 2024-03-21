<template>
  <page-box class="pb-[114px]">
    <UserBox />
    <AdvBox />
    <ToolBox />
    <FooterBox />
  </page-box>
</template>
<script setup lang="ts">
  import UserBox from './components/UserBox.vue';
  import AdvBox from './components/AdvBox.vue';
  import ToolBox from './components/ToolBox.vue';
  import FooterBox from './components/FooterBox.vue';
</script>
<script lang="ts">
  export default {
    name: 'User',
  };
</script>
