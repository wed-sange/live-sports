<script setup lang="ts">
import { useNav } from "@/layout/hooks/useNav";
import { findRouteByPath, getParentPaths } from "@/router/utils";
import { usePermissionStoreHook } from "@/store/modules/permission";
import { isAllEmpty } from "@pureadmin/utils";
import { computed, ref, watch } from "vue";
import { useRoute } from "vue-router";
import { transformI18n } from "@/plugins/i18n";
import { useMessageStore } from "@/store/modules/message";

defineOptions({
  name: ""
});

const { menuSelect } = useNav();
const route = useRoute();

const subMenuData = ref([]);

const defaultActive = computed(() =>
  !isAllEmpty(route.meta?.activePath) ? route.meta.activePath : route.path
);

function getSubMenuData() {
  let path = "";
  path = defaultActive.value;
  subMenuData.value = [];
  // path的上级路由组成的数组
  const parentPathArr = getParentPaths(
    path,
    usePermissionStoreHook().wholeMenus
  );
  // 当前路由的父级路由信息
  const parenetRoute = findRouteByPath(
    parentPathArr[0] || path,
    usePermissionStoreHook().wholeMenus
  );
  if (!parenetRoute?.children) return;
  subMenuData.value = parenetRoute?.children;
}

watch(
  () => [route.path, usePermissionStoreHook().wholeMenus],
  () => {
    if (route.path.includes("/redirect")) return;
    getSubMenuData();
    menuSelect(route.path);
  }
);

const messageStore = useMessageStore();
const readDotAll = computed(() => messageStore.readDotAll);

const menuList = computed(() => [
  {
    title: "menus.liveBroadcastRoomNews",
    path: "/message/live",
    name: "MessageLive"
  },
  {
    title: "menus.privateChatReply",
    path: "/message/replay",
    name: "Replay",
    readDot: readDotAll.value
  }
]);
</script>

<template>
  <div class="flex justify-center items-center gap-[30px]">
    <div
      v-for="item in menuList"
      :key="item.name"
      class="menu-box min-w-[40px] px-[40px] text-center py-2 rounded-full transition-all duration-300 hover:opacity-80 cursor-pointer select-none"
      :class="
        item.path === route.path
          ? 'bg-[#34A853] text-white font-semibold'
          : 'text-[#51565C] bg-[#E1E4EA]'
      "
      @click="$router.push({ path: item.path })"
    >
      {{ transformI18n(item.title) }}
      <div
        class="message-card__info--dot"
        :class="{ large: item.readDot > 9 }"
        v-show="item.readDot > 0"
      >
        <i>{{ item.readDot >= 100 ? "99+" : item.readDot }}</i>
      </div>
    </div>
  </div>
</template>
<style scoped lang="scss">
.menu-box {
  position: relative;

  .message-card__info--dot {
    position: absolute;
    top: 0;
    right: -4px;
    padding: 0 6px;
    margin: 0 auto;
    overflow: hidden;
    font: 500 12px / 16.8px "PingFang SC";
    color: #fff;
    text-align: center;
    background: #ff5151;
    border: 1px solid #fff;
    border-radius: 150px;

    &.large {
      width: auto;
      padding: 0 6px;
    }

    & > i {
      display: inline-block;
      font-style: normal;
    }
  }
}
</style>
