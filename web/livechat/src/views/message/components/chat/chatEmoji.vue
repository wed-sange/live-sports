<script setup lang="ts">
defineOptions({
  name: "chatEmoji"
});
import emojiList from "./emojiList";

defineEmits<{
  (event: "emoji-click", emoji: string): void;
}>();
// const handleClick = () => {
//   console.log(emojiList);
// };
</script>

<template>
  <el-popover
    class="outline-none"
    placement="top-end"
    :width="350"
    trigger="click"
  >
    <el-scrollbar height="260px">
      <div class="grid grid-cols-10 gap-x-2 items-center select-none">
        <span
          class="text-2xl cursor-pointer"
          v-for="item in emojiList"
          :key="item"
          v-html="item"
          @click.stop.prevent="$emit('emoji-click', item)"
        />
      </div>
    </el-scrollbar>
    <template #reference>
      <slot />
    </template>
  </el-popover>
</template>
