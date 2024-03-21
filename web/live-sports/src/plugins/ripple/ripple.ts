import IS_TOUCH_SUPPORTED from './touchSupport';
import type { Directive } from 'vue';
type RippleRef = {
  state: string;
  element: HTMLElement;
};
const DEFAULT_COLOR = 'rgba(255,255,255,.2)';
function rippleEffect({ $el, color, stopPropagation }: { $el: HTMLElement; color?: string; stopPropagation?: boolean }) {
  let _color = color || DEFAULT_COLOR;
  let _stopPropagation = !!stopPropagation;
  const RIPPLE_FADE_IN_DURATION = 450;
  const RIPPLE_FADE_OUT_DURATION = 400;
  const rippleRefs: RippleRef[] = [];
  let isMouseDown = false;

  const rEl = document.createElement('div');
  rEl.classList.add('ripple-link');
  $el.insertBefore(rEl, $el.childNodes[0]);
  //   $el.appendChild(rEl);
  function updateClass() {
    if (!$el.classList.contains('ripple-box')) $el.classList.add('ripple-box');
  }
  function fadeInRipple(pageX, pageY) {
    // rippleRefs.map((index) => {
    //   fadeOutRipple(index);
    // });
    const container = rEl.getBoundingClientRect();
    const scrollPosition = getViewportScrollPosition();

    pageX -= scrollPosition.left;
    pageY -= scrollPosition.top;
    const radius = distanceToFurthestCorner(pageX, pageY, container) * 1.1;
    const duration = RIPPLE_FADE_IN_DURATION;
    const offsetX = pageX - container.left;
    const offsetY = pageY - container.top;

    const ripple = document.createElement('div');
    ripple.classList.add('ripple-element');

    ripple.style.left = `${offsetX - radius}px`;
    ripple.style.top = `${offsetY - radius}px`;
    ripple.style.height = `${radius * 2}px`;
    ripple.style.width = `${radius * 2}px`;
    if (_color) {
      ripple.style.backgroundColor = _color;
    }
    ripple.style.transitionDuration = `${duration}ms`;

    rEl.appendChild(ripple);
    enforceStyleRecalculation(ripple);

    ripple.style.transform = 'scale(2)';
    const rippleRef = {
      state: 'HIDDEN',
      element: ripple,
    };
    rippleRef.state = 'FADING_IN';
    rippleRefs.push(rippleRef);
    const timer = setTimeout(() => {
      rippleRef.state = 'VISIBLE';
      if (!isMouseDown) {
        fadeOutRipple(rippleRef);
      }
      clearTimeout(timer);
    }, duration);
  }
  function fadeOutRipple(rippleRef: RippleRef) {
    const rippleEl = rippleRef.element;
    rippleEl.style.transitionDuration = `${RIPPLE_FADE_OUT_DURATION}ms`;
    rippleEl.style.opacity = '0';
    rippleRef.state = 'FADING_OUT';
    const timer = setTimeout(() => {
      if (rippleEl.parentNode) {
        rippleRef.state = 'HIDDEN';
        rippleRefs.splice(0, 1);
        rippleEl.parentNode.removeChild(rippleEl);
      }
      clearTimeout(timer);
    }, RIPPLE_FADE_OUT_DURATION);
  }
  function distanceToFurthestCorner(x: number, y: number, rect: DOMRect) {
    const distX = Math.max(Math.abs(x - rect.left), Math.abs(x - rect.right));
    const distY = Math.max(Math.abs(y - rect.top), Math.abs(y - rect.bottom));
    return Math.sqrt(distX * distX + distY * distY);
  }
  function enforceStyleRecalculation(element: HTMLElement) {
    // 让浏览器强制重新布局
    window.getComputedStyle(element).getPropertyValue('opacity');
  }
  function getViewportScrollPosition(documentRect?: DOMRect) {
    if (!documentRect) {
      documentRect = _cacheViewportGeometry();
    }

    // The top-left-corner of the viewport is determined by the scroll position of the document
    // body, normally just (scrollLeft, scrollTop). However, Chrome and Firefox disagree about
    // whether `document.body` or `document.documentElement` is the scrolled element, so reading
    // `scrollTop` and `scrollLeft` is inconsistent. However, using the bounding rect of
    // `document.documentElement` works consistently, where the `top` and `left` values will
    // equal negative the scroll position.
    const top = -documentRect.top || document.body.scrollTop || window.scrollY || document.documentElement.scrollTop || 0;

    const left = -documentRect.left || document.body.scrollLeft || window.scrollX || document.documentElement.scrollLeft || 0;

    return { top, left };
  }
  function _cacheViewportGeometry() {
    return document.documentElement.getBoundingClientRect();
  }
  function handleTouchStart(event: TouchEvent) {
    if (event.touches.length > 1) {
      return;
    }
    isMouseDown = true;
    const curTouch = event.touches[0];
    fadeInRipple(curTouch.pageX, curTouch.pageY);
    window.addEventListener('contextmenu', handleMouseLeave, { once: true, passive: true });
    window.addEventListener('touchmove', handleMouseLeave, { once: true, passive: true });
    $el.addEventListener('touchcancel', handleMouseLeave, { once: true, passive: true });
    $el.addEventListener('touchend', handleMouseUp, { once: true, passive: true });
    if (_stopPropagation) event.stopPropagation();
  }
  function handleMouseDown(event: MouseEvent) {
    isMouseDown = true;
    fadeInRipple(event.pageX, event.pageY);
    window.addEventListener('contextmenu', handleMouseLeave, { once: true, passive: true });
    $el.addEventListener('mouseleave', handleMouseLeave, { once: true, passive: true });
    $el.addEventListener('mouseup', handleMouseUp, { once: true, passive: true });
    if (_stopPropagation) event.stopPropagation();
  }
  function handleMouseLeave() {
    if (isMouseDown) {
      handleMouseUp();
    }
  }
  function handleMouseUp() {
    isMouseDown = false;
    rippleRefs.map((index) => {
      if (index.state === 'VISIBLE') {
        fadeOutRipple(index);
      }
    });

    window.removeEventListener('contextmenu', handleMouseLeave, { once: true, passive: true } as any);
    if (IS_TOUCH_SUPPORTED) {
      window.removeEventListener('touchmove', handleMouseLeave, { once: true, passive: true } as any);
      $el.removeEventListener('touchcancel', handleMouseLeave, { once: true, passive: true } as any);
      $el.removeEventListener('touchend', handleMouseUp, { once: true, passive: true } as any);
    } else {
      $el.removeEventListener('mouseleave', handleMouseLeave, { once: true, passive: true } as any);
      $el.removeEventListener('mouseup', handleMouseUp, { once: true, passive: true } as any);
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
    updateClass();
    createListener();
  }
  init();
  return {
    destroy: removeListener,
    update({ updateColor, updateStopPropagation }: { updateColor?: string; updateStopPropagation?: boolean }) {
      updateClass();
      if (updateColor) {
        _color = updateColor;
      }
      _stopPropagation = !!updateStopPropagation;
    },
  };
}

type NodeState = Map<HTMLElement, ReturnType<typeof rippleEffect>>;
const nodeList: NodeState = new Map();
const handleRipple = (el, binding) => {
  if (!el) {
    return;
  }
  let color = '';
  const stopPropagation = !!binding.modifiers.stop;
  if (typeof binding.value === 'object') {
    color = (binding.value as any).color || '';
  } else {
    color = (binding.value || '') + '';
  }
  const handlers = nodeList.get(el);
  if (handlers) {
    handlers.update({
      updateColor: color,
      updateStopPropagation: stopPropagation,
    });
  } else {
    const target = binding.value ? binding.value.target || '' : '';
    let $el = el;
    if (target) {
      $el = el.querySelector(target) || $el;
    }
    const r = rippleEffect({ $el: $el, color: color ? color : undefined, stopPropagation });
    nodeList.set(el, r);
  }
};
const rippleDirective: Directive = {
  created(el, binding) {
    handleRipple(el, binding);
  },
  beforeMount(el, binding) {
    handleRipple(el, binding);
  },
  mounted(el, binding) {
    handleRipple(el, binding);
  },
  updated(el, binding) {
    handleRipple(el, binding);
  },
  unmounted(el) {
    const handlers = nodeList.get(el);
    handlers?.destroy();
    nodeList.delete(el);
  },
};

export { rippleDirective };
