type VisualViewportCallback = (e: VisualViewport | null) => void;
type VisualViewportOptions = { target?: Window; callback?: VisualViewportCallback };
function visualViewportResize(opt?: VisualViewportOptions) {
  const _opt = {
    target: opt?.target || window,
    callback: opt?.callback,
  };
  const isSupport = () => {
    return !!_opt.target.visualViewport;
  };
  const handleVisualViewportResize = () => {
    _opt.callback && _opt.callback(_opt.target.visualViewport);
  };
  const createListener = () => {
    if (!isSupport()) {
      return;
    }
    _opt.target.visualViewport?.addEventListener('resize', handleVisualViewportResize);
  };
  const destroyListener = () => {
    if (!isSupport()) {
      return;
    }
    _opt.target.visualViewport?.removeEventListener('resize', handleVisualViewportResize);
  };
  const init = () => {
    createListener();
  };
  init();
  return {
    destroy: () => {
      destroyListener();
    },
  };
}

const updateWindowHeight = (pageHeight: number) => {
  document.documentElement.style.setProperty('--device-height', `${pageHeight}px`);

  window.scroll(0, 0);
  document.body.scrollTop = 0;
};
visualViewportResize({
  callback: (v) => {
    updateWindowHeight(v?.height || window.innerHeight);
  },
});
