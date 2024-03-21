<script setup lang="ts">
import "animate.css";
// 引入 src/components/ReIcon/src/offlineIcon.ts 文件中所有使用addIcon添加过的本地图标

import "@/components/ReIcon/src/offlineIcon";
import { setType } from "./types";
import { useLayout } from "./hooks/useLayout";
import { useResizeObserver } from "@vueuse/core";
import { useAppStoreHook } from "@/store/modules/app";
import { useSettingStoreHook } from "@/store/modules/settings";
import { deviceDetection, useGlobal } from "@pureadmin/utils";
import { useDataThemeChange } from "@/layout/hooks/useDataThemeChange";
import {
  h,
  ref,
  reactive,
  computed,
  onMounted,
  onBeforeMount,
  defineComponent
} from "vue";

import navbar from "./components/navbar.vue";
import tag from "./components/tag/index.vue";
import appMain from "./components/appMain.vue";
import setting from "./components/setting/index.vue";
import TopMenu from "./components/sidebar/topMenu.vue";
import Horizontal from "./components/sidebar/horizontal.vue";
import DialogPersonal from "./components/personal/index.vue";
import DialogPassword from "./components/password/index.vue";
import backTop from "@/assets/svg/back_top.svg?component";
import { useNav } from "@/layout/hooks/useNav";

import { useTranslationLang } from "@/layout/hooks/useTranslationLang";

const { logout, username, userAvatar } = useNav();

const { t } = useTranslationLang();

import logoTitle from "@/assets/common/logo-title.png";

const dialogPersonalRef = ref<InstanceType<typeof DialogPersonal>>();
const openDialogPersonal = () => {
  dialogPersonalRef.value?.open();
};
const dialogPasswordRef = ref<InstanceType<typeof DialogPassword>>();
const openDialogPassword = () => {
  dialogPasswordRef.value?.open();
};
const appWrapperRef = ref();
const { layout } = useLayout();
const isMobile = deviceDetection();
const pureSetting = useSettingStoreHook();
const { $storage } = useGlobal<GlobalPropertiesApi>();

const set: setType = reactive({
  sidebar: computed(() => {
    return useAppStoreHook().sidebar;
  }),

  device: computed(() => {
    return useAppStoreHook().device;
  }),

  fixedHeader: computed(() => {
    return pureSetting.fixedHeader;
  }),

  classes: computed(() => {
    return {
      hideSidebar: !set.sidebar.opened,
      openSidebar: set.sidebar.opened,
      withoutAnimation: set.sidebar.withoutAnimation,
      mobile: set.device === "mobile"
    };
  }),

  hideTabs: computed(() => {
    return $storage?.configure.hideTabs;
  })
});

function setTheme(layoutModel: string) {
  window.document.body.setAttribute("layout", layoutModel);
  $storage.layout = {
    layout: `${layoutModel}`,
    theme: $storage.layout?.theme,
    darkMode: $storage.layout?.darkMode,
    sidebarStatus: $storage.layout?.sidebarStatus,
    epThemeColor: $storage.layout?.epThemeColor
  };
}

function toggle(device: string, bool: boolean) {
  useAppStoreHook().toggleDevice(device);
  useAppStoreHook().toggleSideBar(bool, "resize");
}

// 判断是否可自动关闭菜单栏
let isAutoCloseSidebar = true;

useResizeObserver(appWrapperRef, entries => {
  if (isMobile) return;
  const entry = entries[0];
  const { width } = entry.contentRect;
  width <= 760 ? setTheme("vertical") : setTheme(useAppStoreHook().layout);
  /** width app-wrapper类容器宽度
   * 0 < width <= 760 隐藏侧边栏
   * 760 < width <= 990 折叠侧边栏
   * width > 990 展开侧边栏
   */
  if (width > 0 && width <= 760) {
    toggle("mobile", false);
    isAutoCloseSidebar = true;
  } else if (width > 760 && width <= 990) {
    if (isAutoCloseSidebar) {
      toggle("desktop", false);
      isAutoCloseSidebar = false;
    }
  } else if (width > 990 && !set.sidebar.isClickCollapse) {
    toggle("desktop", true);
    isAutoCloseSidebar = true;
  } else {
    toggle("desktop", false);
    isAutoCloseSidebar = false;
  }
});

onMounted(() => {
  if (isMobile) {
    toggle("mobile", false);
  }
});

onBeforeMount(() => {
  useDataThemeChange().dataThemeChange();
});

const layoutHeader = defineComponent({
  render() {
    return h(
      "div",
      {
        class: { "fixed-header": set.fixedHeader }
      },
      {
        default: () => [
          !pureSetting.hiddenSideBar &&
          (layout.value.includes("vertical") || layout.value.includes("mix"))
            ? h(navbar)
            : null,
          !pureSetting.hiddenSideBar && layout.value.includes("horizontal")
            ? h(Horizontal)
            : null,
          h(tag)
        ]
      }
    );
  }
});
</script>

<template>
  <div class="flex flex-col">
    <div
      class="w-full h-[78px] flex justify-between fixed top-0 left-0 z-10 py-[24px]"
    >
      <div class="flex gap-x-16 items-center">
        <img
          :src="logoTitle"
          class="w-[200px] bg-contain ml-[22px] select-none"
        />
        <TopMenu />
      </div>

      <div class="mr-[52px] flex items-center">
        <!-- 退出登录 -->
        <el-dropdown popper-class="app-user-dropdown" trigger="click">
          <span class="app-user-info flex items-center">
            <img
              :src="userAvatar"
              class="w-[37.4px] h-[36.26px] rounded-[50px] object-cover"
            />
            <p
              v-if="username"
              class="app-user-info__name text-[#595959] px-[8px]"
            >
              {{ username || "xxx" }}
            </p>
            <el-icon :size="20">
              <svg
                xmlns="http://www.w3.org/2000/svg"
                width="20"
                height="20"
                viewBox="0 0 20 20"
                fill="none"
              >
                <path
                  d="M10.0009 11.8926L15.1752 7.02335C15.3933 6.8181 15.7431 6.81889 15.9629 7.02571C16.1864 7.23604 16.1842 7.58277 15.9654 7.78862L10.4523 12.9766C10.3256 13.0959 10.1552 13.1448 9.9932 13.1261C9.83593 13.1398 9.67102 13.0911 9.54941 12.9766L4.03633 7.78862C3.81362 7.57904 3.81466 7.23667 4.03884 7.02571M10.0009 11.8926L4.82651 7.02335C4.61226 6.82173 4.25785 6.8196 4.03884 7.02571M10.0009 11.8926L10.09 11.9765L10.0009 12.0711L9.91178 11.9765L10.0009 11.8926ZM4.03884 7.02571L4.12786 7.12031L4.03884 7.02571C4.03884 7.02571 4.03884 7.02571 4.03884 7.02571Z"
                  fill="#595959"
                  stroke="#595959"
                  stroke-width="0.26"
                />
              </svg>
            </el-icon>
          </span>
          <template #dropdown>
            <el-dropdown-menu class="logout">
              <el-dropdown-item @click="openDialogPersonal">
                {{ t("user.profile") }}
              </el-dropdown-item>
              <el-dropdown-item @click="openDialogPassword">
                {{ t("user.changePsw") }}
              </el-dropdown-item>
              <el-dropdown-item @click="logout">
                {{ t("buttons.hsLoginOut") }}
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div ref="appWrapperRef" :class="['app-wrapper', set.classes]">
      <!-- <Vertical
        v-show="
          !pureSetting.hiddenSideBar &&
          (layout.includes('vertical') || layout.includes('mix'))
        "
      /> -->
      <div
        :class="[
          'main-container',
          pureSetting.hiddenSideBar ? 'main-hidden' : ''
        ]"
      >
        <div class="w-full h-full" v-if="set.fixedHeader">
          <app-main :fixed-header="set.fixedHeader" />
        </div>
        <el-scrollbar v-else>
          <el-backtop
            title="回到顶部"
            target=".main-container .el-scrollbar__wrap"
          >
            <backTop />
          </el-backtop>
          <layout-header />
          <!-- 主体内容 -->
          <app-main :fixed-header="set.fixedHeader" />
        </el-scrollbar>
      </div>
      <!-- 系统设置 -->
      <setting />
    </div>
  </div>
  <DialogPersonal ref="dialogPersonalRef" />
  <DialogPassword ref="dialogPasswordRef" />
</template>

<style lang="scss" scoped>
.app-wrapper {
  position: relative;
  width: 100%;
  height: 100%;
  background: #eef0f4;

  &::after {
    display: table;
    clear: both;
    content: "";
  }

  &.mobile.openSidebar {
    position: fixed;
    top: 0;
  }
}

.app-mask {
  position: absolute;
  top: 0;
  z-index: 999;
  width: 100%;
  height: 100%;
  background: #000;
  opacity: 0.3;
}

.re-screen {
  margin-top: 12px;
}

.app-user-info {
  &__name {
    font: 400 16px / normal "PingFang SC";
  }
}
</style>
<style lang="scss">
.message-box.main-content {
  margin: 0;

  .message-inner-box {
    overflow: hidden;
    border-radius: 16px;
  }
}

.app-user-dropdown.el-popper {
  --el-popper-border-radius: 16px;
  --el-border-radius-base: var(--el-popper-border-radius);

  .el-popper__arrow {
    opacity: 0;
  }

  .el-dropdown-menu {
    padding: 24px 7px;
    overflow: hidden;

    .el-dropdown-menu__item {
      justify-content: center;
      width: 106px;
      padding: 8px 0;
      font: 500 14px / normal "PingFang SC";
      color: #434343;
      border-radius: 8px;

      &:not(.is-disabled):focus {
        color: #fff;
        background-color: #34a853;
      }
    }
  }
}
</style>
