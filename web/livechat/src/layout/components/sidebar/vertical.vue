<script setup lang="ts">
import { useRoute } from "vue-router";
import { emitter } from "@/utils/mitt";
import SidebarItem from "./sidebarItem.vue";
import { useNav } from "@/layout/hooks/useNav";
import { responsiveStorageNameSpace } from "@/config";
import { storageLocal, isAllEmpty } from "@pureadmin/utils";
import { findRouteByPath, getParentPaths } from "@/router/utils";
import { usePermissionStoreHook } from "@/store/modules/permission";
import { ref, computed, watch, onMounted, onBeforeUnmount } from "vue";
import closeIcon from "@/assets/login/logout-icon.png";

const { logout } = useNav();

const route = useRoute();
const showLogo = ref(
  storageLocal().getItem<StorageConfigs>(
    `${responsiveStorageNameSpace()}configure`
  )?.showLogo ?? true
);

const { device, pureApp, isCollapse, menuSelect } = useNav();

const subMenuData = ref([]);

const menuData = computed(() => {
  return pureApp.layout === "mix" && device.value !== "mobile"
    ? subMenuData.value
    : usePermissionStoreHook().wholeMenus;
});

const loading = computed(() =>
  pureApp.layout === "mix" ? false : menuData.value.length === 0 ? true : false
);

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
function onLogout() {
  logout();
}

watch(
  () => [route.path, usePermissionStoreHook().wholeMenus],
  () => {
    if (route.path.includes("/redirect")) return;
    getSubMenuData();
    menuSelect(route.path);
  }
);

onMounted(() => {
  getSubMenuData();

  emitter.on("logoChange", key => {
    showLogo.value = key;
  });
});

onBeforeUnmount(() => {
  // 解绑`logoChange`公共事件，防止多次触发
  emitter.off("logoChange");
});
</script>

<template>
  <div
    v-loading="loading"
    class="flex flex-col relative"
    :class="['sidebar-container', showLogo ? 'has-logo' : '']"
  >
    <el-scrollbar
      wrap-class="scrollbar-wrapper"
      :class="[device === 'mobile' ? 'mobile flex-grow' : 'pc flex-grow']"
    >
      <el-menu
        router
        unique-opened
        mode="vertical"
        class="outer-most select-none"
        :collapse="isCollapse"
        :default-active="defaultActive"
        :collapse-transition="false"
        menu-trigger="click"
      >
        <sidebar-item
          v-for="routes in menuData"
          :key="routes.path"
          :item="routes"
          :base-path="routes.path"
          class="outer-most select-none"
        />
      </el-menu>
      <div
        @click="onLogout"
        class="w-[160px] cursor-pointer flex items-center justify-center absolute bottom-[29px] h-[52px] bg-[#F7F7F8] rounded-[8px] ml-[10px]"
      >
        <img class="w-[20px] h-[20px] mr-[8px]" :src="closeIcon" />
        <span class="text-[#333334] text-[14px] font-semibold">退出登录</span>
      </div>
    </el-scrollbar>
    <!-- <leftCollapse
      v-if="device !== 'mobile'"
      :is-active="pureApp.sidebar.opened"
      @toggleClick="toggleSideBar"
    /> -->
  </div>
</template>

<style scoped lang="scss">
:deep(.el-loading-mask) {
  opacity: 0.45;
}
</style>
