<script setup lang="ts">
import { IconMore } from "@/views/message/components/icon";

defineOptions({
  name: "ChatTop"
});

defineProps({
  top: {
    type: Boolean,
    default: false
  }
});
defineEmits<{
  (event: "set-top"): void;
  (event: "un-set-top"): void;
}>();
</script>

<template>
  <div>
    <el-dropdown trigger="click">
      <el-icon>
        <IconMore class="text-[24px]" />
      </el-icon>
      <template #dropdown>
        <el-dropdown-menu>
          <template v-if="top">
            <el-dropdown-item @click="$emit('un-set-top')"
              >取消置顶</el-dropdown-item
            >
            <el-dropdown-item @click="$emit('set-top')"
              >置顶聊天</el-dropdown-item
            >
          </template>
          <template v-else>
            <el-dropdown-item @click="$emit('set-top')"
              >置顶该聊天</el-dropdown-item
            >
          </template>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>
