import IS_TOUCH_SUPPORTED from './touchSupport';
import type { Directive } from 'vue';

function pressedSlide({ $el, callback }: { $el: HTMLElement; callback?: any }) {
  let _callback = callback;
  let isMouseDown = false;
  let startX = 0;
  let startY = 0;
  let moveX = 0;
  let moveY = 0;

  function handleTouchStart(event: TouchEvent) {
    if (event.touches.length > 1) {
      return;
    }
    isMouseDown = true;
    const curTouch = event.touches[0];
    // const rectInfo = event.target
    //   ? (event.target as any).getBoundingClientRect()
    //   : {
    //       left: 0,
    //       top: 0,
    //     };

    // const offsetX = curTouch.pageX - rectInfo.left;
    // const offsetY = curTouch.pageY - rectInfo.top;
    moveX = startX = curTouch.pageX;
    moveY = startY = curTouch.pageY;
    window.addEventListener('contextmenu', handleMouseLeave, { passive: true });
    $el.addEventListener('touchmove', handleTouchmove, { passive: true });
    $el.addEventListener('touchcancel', handleMouseLeave, { passive: true });
    $el.addEventListener('touchend', handleMouseUp, { passive: true });
  }
  function handleTouchmove(event: TouchEvent) {
    if (event.touches.length > 1) {
      return;
    }
    if (isMouseDown) {
      const curTouch = event.touches[0];
      moveX = curTouch.pageX;
      moveY = curTouch.pageY;
    }
  }
  function handleMouseDown(event: MouseEvent) {
    isMouseDown = true;
    moveX = startX = event.pageX;
    moveY = startY = event.pageY;
    window.addEventListener('contextmenu', handleMouseLeave, { passive: true });
    window.addEventListener('mousemove', handleMousemove, { passive: true });
    window.addEventListener('mouseleave', handleMouseLeave, { passive: true });
    window.addEventListener('mouseup', handleMouseUp, { passive: true });
  }

  function handleMousemove(event: MouseEvent) {
    if (isMouseDown) {
      moveX = event.pageX;
      moveY = event.pageY;
    }
  }
  function handleMouseLeave() {
    if (isMouseDown) {
      handleMouseUp();
    }
  }
  function handleMouseUp() {
    _callback && _callback({ x: startX, y: startY }, { x: moveX, y: moveY });
    isMouseDown = false;
    startX = 0;
    startY = 0;
    moveX = 0;
    moveY = 0;

    window.removeEventListener('contextmenu', handleMouseLeave, { passive: true } as any);
    if (IS_TOUCH_SUPPORTED) {
      $el.removeEventListener('touchmove', handleTouchmove, { passive: true } as any);
      $el.removeEventListener('touchcancel', handleMouseLeave, { passive: true } as any);
      $el.removeEventListener('touchend', handleMouseUp, { passive: true } as any);
    } else {
      window.removeEventListener('mousemove', handleMousemove, { passive: true } as any);
      window.removeEventListener('mouseleave', handleMouseLeave, { passive: true } as any);
      window.removeEventListener('mouseup', handleMouseUp, { passive: true } as any);
    }
  }
  function createListener() {
    if (IS_TOUCH_SUPPORTED) {
      $el.addEventListener('touchstart', handleTouchStart, { passive: true });
    } else {
      $el.addEventListener('mousedown', handleMouseDown, { passive: true });
    }
  }
  function removeListener() {
    if (IS_TOUCH_SUPPORTED) {
      $el.removeEventListener('touchstart', handleTouchStart, { passive: true } as any);
    } else {
      $el.removeEventListener('mousedown', handleMouseDown, { passive: true } as any);
    }
  }
  function init() {
    createListener();
  }
  init();
  return {
    destroy: removeListener,
    update({ updateCallback }: { updateCallback?: any }) {
      if (updateCallback) {
        _callback = updateCallback;
      }
    },
  };
}

type NodeState = Map<HTMLElement, ReturnType<typeof pressedSlide>>;
const nodeList: NodeState = new Map();
const handlePressedSlide = (el, binding) => {
  if (!el) {
    return;
  }
  const func = binding.value;
  const handlers = nodeList.get(el);
  if (handlers) {
    handlers.update({
      updateCallback: func,
    });
  } else {
    const $el = el;
    const r = pressedSlide({ $el: $el, callback: func });
    nodeList.set(el, r);
  }
};
const pressedSlideDirective: Directive = {
  created(el, binding) {
    handlePressedSlide(el, binding);
  },
  beforeMount(el, binding) {
    handlePressedSlide(el, binding);
  },
  mounted(el, binding) {
    handlePressedSlide(el, binding);
  },
  updated(el, binding) {
    handlePressedSlide(el, binding);
  },
  unmounted(el) {
    const handlers = nodeList.get(el);
    handlers?.destroy();
    nodeList.delete(el);
  },
};

export { pressedSlideDirective };
