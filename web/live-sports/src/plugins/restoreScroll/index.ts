import { onActivated, nextTick, type Ref, isRef } from 'vue';
import { onBeforeRouteLeave } from 'vue-router';

type RestoreCacheItem = {
  el: Element;
  pos: [number, number];
};
const restoreCache = new Map<string, RestoreCacheItem[]>();
function updateRestoreCache(key: string, item: RestoreCacheItem) {
  if (!restoreCache.has(key)) {
    restoreCache.set(key, []);
  }
  const list = restoreCache.get(key)!;
  const index = list.findIndex((cur) => cur.el === item.el);
  if (index === -1) {
    list.push(item);
  } else {
    list[index].el = item.el;
    list[index].pos = item.pos;
  }
  restoreCache.set(key, list);
}
function getRestoreCache(key: string, el: Element) {
  if (!restoreCache.has(key)) {
    return [0, 0];
  }
  const list = restoreCache.get(key)!;
  const index = list.findIndex((cur) => cur.el === el);
  if (index === -1) {
    return [0, 0];
  } else {
    return list[index].pos;
  }
}
function clearRestoreCache(key?: string, el?: Element) {
  if (key) {
    if (restoreCache.has(key)) {
      if (el) {
        const list = restoreCache.get(key)!;
        const index = list.findIndex((cur) => cur.el === el);
        if (index !== -1) {
          list.splice(index, 1);
        }
        restoreCache.set(key, list);
      } else {
        restoreCache.delete(key);
      }
    }
  } else {
    restoreCache.clear();
  }
}

type ElType = Ref<Element | undefined | null> | null | string | Element;
export const useRestoreScroll = (opt: { el?: ElType; getEl?: () => ElType }) => {
  const route = useRoute();
  const getCurrentEl = () => {
    let _el: ElType | null = null;
    if (opt.getEl) {
      _el = opt.getEl();
    } else if (opt.el) {
      _el = opt.el;
    }
    let useEl: Element | null | undefined = null;
    if (typeof _el === 'string') {
      useEl = document.querySelector(_el);
    } else if (isRef<Element | null | undefined>(_el)) {
      useEl = _el.value;
    } else {
      useEl = _el;
    }
    if (!useEl) {
      console.error('请传入getEl/el用于缓存的元素，当前传入的值=>', _el);
      throw Error;
    }
    return useEl;
  };
  const saveScroll = () => {
    const el = getCurrentEl();
    updateRestoreCache(route.path, {
      el: el,
      pos: [0, el.scrollTop],
    });
  };

  const restoreScroll = () => {
    nextTick(() => {
      const el = getCurrentEl();
      const pos = getRestoreCache(route.path, el);
      el.scrollTop = pos[1];
      clearRestoreCache(route.path, el);
    });
  };
  const clearCurrentScrollCache = () => {
    clearRestoreCache(route.path);
  };
  const clearAllScrollCache = () => {
    clearRestoreCache();
  };
  onActivated(restoreScroll);
  //@ts-ignore
  onBeforeRouteLeave((to, from, next) => {
    saveScroll();
    next();
  });
  return {
    saveScroll,
    restoreScroll,
    clearCurrentScrollCache,
    clearAllScrollCache,
  };
};
