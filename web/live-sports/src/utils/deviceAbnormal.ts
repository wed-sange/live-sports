/**
 * 啊，设备的异常表现，真是头疼，希望大家都不要碰到这些情况
 */

/**
 *  移动设备存在工具栏，实际高度设置100vh会超出一整屏
 *  这里获取可视区域高度并注入css变量 --device-height
 */
import { useAppStore } from '@/store/modules/app';
import { getHashParam } from './common';
import { registerHandler } from './nativeUtils.js';
// 移动设备存在工具栏，实际高度获取
const updateWindowHeight = () => {
  const pageHeight = window.innerHeight;
  document.documentElement.style.setProperty('--device-height', `${pageHeight}px`);
};
function createWindowHeightAbr() {
  window.addEventListener('resize', updateWindowHeight);
  updateWindowHeight();
}

/**
 * 处理移动设备键盘弹起之后的留白区域
 */
type inputWhiteScreenByKeyboardAbrHandler = (event: Event, type: 'ios' | 'android') => void;
function bindInputWhiteScreenByKeyboardAbr(
  event: Window | HTMLElement,
  upHandler?: inputWhiteScreenByKeyboardAbrHandler,
  downHandler?: inputWhiteScreenByKeyboardAbrHandler,
) {
  // 判断设备类型
  const ua = window.navigator.userAgent.toLocaleLowerCase();

  const isIOS = /iphone|ipad|ipod/.test(ua);
  const isAndroid = /android/.test(ua);
  if (isIOS) {
    const hashChange = (e) => {
      // IOS 键盘收起后操作
      downHandler && downHandler(e, 'ios');
      window.removeEventListener('hashchange', hashChange);
    };
    event.addEventListener(
      'focus',
      function (e) {
        // IOS 键盘弹起后操作
        upHandler && upHandler(e, 'ios');
        window.addEventListener('hashchange', hashChange);
      },
      true,
    );

    // IOS 键盘收起：IOS 点击输入框以外区域或点击收起按钮，输入框都会失去焦点，键盘会收起，
    event.addEventListener(
      'blur',
      (e) => {
        // IOS 键盘收起后操作
        hashChange(e);
      },
      true,
    );
  }
  if (isAndroid) {
    // Android 键盘收起：Android 键盘弹起或收起页面高度会发生变化，以此为依据获知键盘收起
    let originWidth = document.documentElement.clientWidth || document.body.clientWidth;
    let originHeight = document.documentElement.clientHeight || document.body.clientHeight;

    window.addEventListener(
      'resize',
      function (e) {
        const resizeWidth = document.documentElement.clientWidth || document.body.clientWidth;
        const resizeHeight = document.documentElement.clientHeight || document.body.clientHeight;
        // 证明不是横屏导致的高度变化，需要处理
        if (resizeWidth === originWidth) {
          if (originHeight < resizeHeight) {
            // Android 键盘收起后操作
            downHandler && downHandler(e, 'android');
          } else {
            // Android 键盘弹起后操作
            upHandler && upHandler(e, 'android');
          }
        }
        originWidth = resizeWidth;
        originHeight = resizeHeight;
      },
      false,
    );
  }
}
const judgeIosBar = () => {
  const appStore = useAppStore();
  const resizeHeight = document.documentElement.clientHeight || document.body.clientHeight;
  const checkBar = () => {
    appStore.showBar = resizeHeight === window.innerHeight;
    setTimeout(() => {
      checkBar();
      updateWindowHeight();
    }, 100);
  };
  checkBar();
};
function isInputWhiteScreenByKeyboardAbrElement(event, _className?: string) {
  let res = false;
  let maxI = 5;
  const checkClass = (t) => {
    if (!t || maxI <= 0) return;
    const tagName = t.tagName;
    if (tagName !== 'INPUT' && tagName !== 'TEXTAREA') return;
    const className = t.className;
    res = className.indexOf(_className || 'keyboard-abr') === -1;
    if (!res) {
      maxI--;
      checkClass(t.parentNode);
    }
  };
  checkClass(event);
  return res;
}
function handleTouchInputWhiteScreenByKeyboardAbr(e: Event) {
  //   const tagName = (e.target as HTMLElement).tagName;
  //   if (!isInputWhiteScreenByKeyboardAbrElement(e.target as HTMLElement, 'keyboard-abr-scroll')) {
  //     return true;
  //   }
  //阻止默认的处理方式(阻止下拉滑动的效果)
  e.preventDefault();
  return false;
}
let inputWhiteScreenByKeyboardAbrTimer: number | null = null;
function handleUpInputWhiteScreenByKeyboardAbr(e: Event) {
  const eventTarget = e.target as HTMLElement;
  if (!eventTarget) return;
  if (!isInputWhiteScreenByKeyboardAbrElement(eventTarget)) return;
  inputWhiteScreenByKeyboardAbrTimer && clearTimeout(inputWhiteScreenByKeyboardAbrTimer);
  inputWhiteScreenByKeyboardAbrTimer = null;
  //   eventTarget.scrollIntoView();
  document.body.addEventListener(
    'touchmove',
    handleTouchInputWhiteScreenByKeyboardAbr,
    /**
         如果我们是为了阻止页面滚动添加了上述代码，默认行为就是滚动页
        面，但是如果我们阻止了这一默认行为，浏览器是无法预先知道的，
        必须等待事件监听器执行完成后，才知道要去阻止默认行为。等待监
        听器的执行是耗时的，，有些甚至耗时很明显，这样就会导致页面卡
        顿。即便监听器是个空函数，也会产生一定的卡顿，毕竟空函数的执
        行也会耗时。所以就有了passive属性，如果要阻止默认事件可以设
        置passive：false。
    */
    { passive: false },
  );
}
function handleDownInputWhiteScreenByKeyboardAbr(e: Event) {
  const eventTarget = e.target as HTMLElement;
  if (!eventTarget) return;
  if (!isInputWhiteScreenByKeyboardAbrElement(eventTarget)) return;
  inputWhiteScreenByKeyboardAbrTimer && clearTimeout(inputWhiteScreenByKeyboardAbrTimer);
  inputWhiteScreenByKeyboardAbrTimer = setTimeout(() => {
    window.scroll(0, 0);
  }, 300) as unknown as number;
  document.body.removeEventListener('touchmove', handleTouchInputWhiteScreenByKeyboardAbr);
}
function createInputWhiteScreenByKeyboardAbr() {
  bindInputWhiteScreenByKeyboardAbr(
    document.body,
    (e) => {
      handleUpInputWhiteScreenByKeyboardAbr(e);
    },
    (e) => {
      window.scroll(0, 0);
      document.body.scrollTop = 0;
      handleDownInputWhiteScreenByKeyboardAbr(e);
    },
  );
}
//计算屏幕高度
const useDeviceAbnormal = () => {
  createWindowHeightAbr();
  createInputWhiteScreenByKeyboardAbr();
};
//app壳包系统导航bar高度
const useImmersive = () => {
  const appStore = useAppStore();
  const statusBar = getHashParam('statusBar');
  if (statusBar) {
    appStore.immersive = true;
    document.documentElement.classList.add('immersive');
    document.documentElement.style.setProperty('--app-header-height', statusBar + 'px');
  } else {
    appStore.immersive = false;
    document.documentElement.classList.remove('immersive');
    document.documentElement.style.setProperty('--app-header-height', '0px');
  }
  registerHandler('onBack', () => {
    appStore.backAnimation = true;
    window.history.back();
  });
};

export { useDeviceAbnormal, useImmersive, judgeIosBar };
