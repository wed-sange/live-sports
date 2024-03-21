import IS_TOUCH_SUPPORTED from './touchSupport';
import type { Directive, DirectiveBinding } from 'vue';

function touchstartPrevent({ $el, callback }: { $el: HTMLElement; callback?: any }) {
  let _callback = callback;

  function handleTouchStart(event: TouchEvent) {
    if (event.touches.length > 1) {
      return;
    }
    event.preventDefault();
    _callback && _callback(event);
    return false;
  }
  function createListener() {
    if (IS_TOUCH_SUPPORTED) {
      $el.addEventListener('touchstart', handleTouchStart, { passive: false });
    }
  }
  function removeListener() {
    if (IS_TOUCH_SUPPORTED) {
      $el.removeEventListener('touchstart', handleTouchStart, { passive: false } as any);
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

type DirectiveBindingOptionHandler = (event: TouchEvent) => void;
type DirectiveBindingOptions =
  | DirectiveBindingOptionHandler
  | {
      handler?: DirectiveBindingOptionHandler;
      target?: any;
    };
type NodeState = Map<HTMLElement, ReturnType<typeof touchstartPrevent>>;
const nodeList: NodeState = new Map();
const handleTouchstartPrevent = (el, binding: DirectiveBinding<DirectiveBindingOptions>) => {
  if (!el) {
    return;
  }
  const opt = binding.value;
  let func: DirectiveBindingOptionHandler | null = null;
  if (!opt || typeof opt === 'function') {
    func = opt || null;
  } else {
    func = opt.handler || null;
  }
  const handlers = nodeList.get(el);
  if (handlers) {
    handlers.update({
      updateCallback: func,
    });
  } else {
    const target = !opt || typeof opt === 'function' ? '' : opt.target || '';
    let $el = el;
    if (target) {
      $el = el.querySelector(target) || $el;
    }
    const r = touchstartPrevent({ $el: $el, callback: func });
    nodeList.set(el, r);
  }
};
const touchstartPreventDirective: Directive<any, DirectiveBindingOptions> = {
  created(el, binding) {
    handleTouchstartPrevent(el, binding);
  },
  beforeMount(el, binding) {
    handleTouchstartPrevent(el, binding);
  },
  mounted(el, binding) {
    handleTouchstartPrevent(el, binding);
  },
  updated(el, binding) {
    handleTouchstartPrevent(el, binding);
  },
  unmounted(el) {
    const handlers = nodeList.get(el);
    handlers?.destroy();
    nodeList.delete(el);
  },
};

export { touchstartPreventDirective };
