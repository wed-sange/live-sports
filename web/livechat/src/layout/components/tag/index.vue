<script setup lang="ts">
import { $t } from "@/plugins/i18n";
import { emitter } from "@/utils/mitt";
import { useTags } from "../../hooks/useTag";
import { getTopMenu } from "@/router/utils";
import { useResizeObserver, useFullscreen } from "@vueuse/core";
import { isEqual, isAllEmpty, debounce } from "@pureadmin/utils";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import { ref, watch, unref, nextTick, onBeforeUnmount } from "vue";

import Fullscreen from "@iconify-icons/ri/fullscreen-fill";

const {
  route,
  router,
  showTags,
  instance,
  multiTags,
  tagsViews,
  showModel,
  translateX,
  activeIndex,
  onMounted
} = useTags();

const tabDom = ref();
const containerDom = ref();
const scrollbarDom = ref();
const isShowArrow = ref(false);
const topPath = getTopMenu()?.path;
const { isFullscreen } = useFullscreen();

const dynamicTagView = async () => {
  await nextTick();
  const index = multiTags.value.findIndex(item => {
    if (!isAllEmpty(route.query)) {
      return isEqual(route.query, item.query);
    } else if (!isAllEmpty(route.params)) {
      return isEqual(route.params, item.params);
    } else {
      return route.path === item.path;
    }
  });
  moveToView(index);
};

const moveToView = async (index: number): Promise<void> => {
  await nextTick();
  const tabNavPadding = 10;
  if (!instance.refs["dynamic" + index]) return;
  const tabItemEl = instance.refs["dynamic" + index][0];
  const tabItemElOffsetLeft = (tabItemEl as HTMLElement)?.offsetLeft;
  const tabItemOffsetWidth = (tabItemEl as HTMLElement)?.offsetWidth;
  // 标签页导航栏可视长度（不包含溢出部分）
  const scrollbarDomWidth = scrollbarDom.value
    ? scrollbarDom.value?.offsetWidth
    : 0;

  // 已有标签页总长度（包含溢出部分）
  const tabDomWidth = tabDom.value ? tabDom.value?.offsetWidth : 0;

  scrollbarDomWidth <= tabDomWidth
    ? (isShowArrow.value = true)
    : (isShowArrow.value = false);
  if (tabDomWidth < scrollbarDomWidth || tabItemElOffsetLeft === 0) {
    translateX.value = 0;
  } else if (tabItemElOffsetLeft < -translateX.value) {
    // 标签在可视区域左侧
    translateX.value = -tabItemElOffsetLeft + tabNavPadding;
  } else if (
    tabItemElOffsetLeft > -translateX.value &&
    tabItemElOffsetLeft + tabItemOffsetWidth <
      -translateX.value + scrollbarDomWidth
  ) {
    // 标签在可视区域
    translateX.value = Math.min(
      0,
      scrollbarDomWidth -
        tabItemOffsetWidth -
        tabItemElOffsetLeft -
        tabNavPadding
    );
  } else {
    // 标签在可视区域右侧
    translateX.value = -(
      tabItemElOffsetLeft -
      (scrollbarDomWidth - tabNavPadding - tabItemOffsetWidth)
    );
  }
};

function dynamicRouteTag(value: string): void {
  const hasValue = multiTags.value.some(item => {
    return item.path === value;
  });

  function concatPath(arr: object[], value: string) {
    if (!hasValue) {
      arr.forEach((arrItem: any) => {
        if (arrItem.path === value || arrItem.path === value) {
          useMultiTagsStoreHook().handleTags("push", {
            path: value,
            meta: arrItem.meta,
            name: arrItem.name
          });
        } else {
          if (arrItem.children && arrItem.children.length > 0) {
            concatPath(arrItem.children, value);
          }
        }
      });
    }
  }
  concatPath(router.options.routes as any, value);
}

function showMenus(value: boolean) {
  Array.of(1, 2, 3, 4, 5).forEach(v => {
    tagsViews[v].show = value;
  });
}

function disabledMenus(value: boolean) {
  Array.of(1, 2, 3, 4, 5).forEach(v => {
    tagsViews[v].disabled = value;
  });
}

/** 检查当前右键的菜单两边是否存在别的菜单，如果左侧的菜单是顶级菜单，则不显示关闭左侧标签页，如果右侧没有菜单，则不显示关闭右侧标签页 */
function showMenuModel(
  currentPath: string,
  query: object = {},
  refresh = false
) {
  const allRoute = multiTags.value;
  const routeLength = multiTags.value.length;
  let currentIndex = -1;
  if (isAllEmpty(query)) {
    currentIndex = allRoute.findIndex(v => v.path === currentPath);
  } else {
    currentIndex = allRoute.findIndex(v => isEqual(v.query, query));
  }

  showMenus(true);

  if (refresh) {
    tagsViews[0].show = true;
  }

  /**
   * currentIndex为1时，左侧的菜单顶级菜单，则不显示关闭左侧标签页
   * 如果currentIndex等于routeLength-1，右侧没有菜单，则不显示关闭右侧标签页
   */
  if (currentIndex === 1 && routeLength !== 2) {
    // 左侧的菜单是顶级菜单，右侧存在别的菜单
    tagsViews[2].show = false;
    Array.of(1, 3, 4, 5).forEach(v => {
      tagsViews[v].disabled = false;
    });
    tagsViews[2].disabled = true;
  } else if (currentIndex === 1 && routeLength === 2) {
    disabledMenus(false);
    // 左侧的菜单是顶级菜单，右侧不存在别的菜单
    Array.of(2, 3, 4).forEach(v => {
      tagsViews[v].show = false;
      tagsViews[v].disabled = true;
    });
  } else if (routeLength - 1 === currentIndex && currentIndex !== 0) {
    // 当前路由是所有路由中的最后一个
    tagsViews[3].show = false;
    Array.of(1, 2, 4, 5).forEach(v => {
      tagsViews[v].disabled = false;
    });
    tagsViews[3].disabled = true;
  } else if (currentIndex === 0 || currentPath === `/redirect${topPath}`) {
    // 当前路由为顶级菜单
    disabledMenus(true);
  } else {
    disabledMenus(false);
  }
}

watch(route, () => {
  activeIndex.value = -1;
  dynamicTagView();
});

watch(isFullscreen, () => {
  tagsViews[6].icon = Fullscreen;
  tagsViews[6].text = $t("buttons.hswholeFullScreen");
});

onMounted(() => {
  if (!instance) return;

  // 根据当前路由初始化操作标签页的禁用状态
  showMenuModel(route.fullPath);

  // 触发隐藏标签页
  emitter.on("tagViewsChange", (key: any) => {
    if (unref(showTags as any) === key) return;
    (showTags as any).value = key;
  });

  // 改变标签风格
  emitter.on("tagViewsShowModel", key => {
    showModel.value = key;
  });

  //  接收侧边栏切换传递过来的参数
  emitter.on("changLayoutRoute", indexPath => {
    dynamicRouteTag(indexPath);
    setTimeout(() => {
      showMenuModel(indexPath);
    });
  });

  useResizeObserver(
    scrollbarDom,
    debounce(() => dynamicTagView())
  );
});

onBeforeUnmount(() => {
  // 解绑`tagViewsChange`、`tagViewsShowModel`、`changLayoutRoute`公共事件，防止多次触发
  emitter.off("tagViewsChange");
  emitter.off("tagViewsShowModel");
  emitter.off("changLayoutRoute");
});
</script>

<template>
  <div ref="containerDom" class="tags-view" v-if="!showTags" />
</template>

<style lang="scss" scoped>
@import url("./index.scss");
</style>
