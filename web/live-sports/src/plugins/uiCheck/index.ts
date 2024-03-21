import { openCheck } from './mount';
// @ts-ignore
const IS_TOUCH_SUPPORTED = 'ontouchstart' in window || (window.DocumentTouch && document instanceof DocumentTouch);

interface BoxInfoModel {
  width: number;
  height: number;
}
class UiDesignControl {
  option: any = {
    el: document.body,
    onClick: null,
  };
  controlEl: any = null;
  isFixed = false;
  isMove = false;
  isMouseDown = false;
  startX = 0;
  startY = 0;
  currentX = 0;
  currentY = 0;
  clientW = 0;
  clientH = 0;
  constructor(opt) {
    this.option.el = opt.el;
    this.option.onClick = opt.onClick || null;
    this.init();
  }
  setPageArea() {
    const page = this._getPageArea();
    this.clientW = page.width;
    this.clientH = page.height;
  }
  _getPageArea() {
    if (document.compatMode === 'BackCompat') {
      return {
        width: Math.max(document.body.scrollWidth, document.body.clientWidth),
        height: Math.max(document.body.scrollHeight, document.body.clientHeight),
      };
    } else {
      return {
        width: Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth),
        height: Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight),
      };
    }
  }
  onTouchstart(event: TouchEvent) {
    if (event.touches.length > 1) {
      return;
    }
    const rectInfo = event.target
      ? (event.target as any).getBoundingClientRect()
      : {
          left: 0,
          top: 0,
        };
    const curTouch = event.touches[0];
    this.isMouseDown = true;
    this.isMove = true;
    this.setPageArea();
    const boxPadding = 10;
    const offsetX = curTouch.pageX - rectInfo.left;
    const offsetY = curTouch.pageY - rectInfo.top;
    this.startX = offsetX + boxPadding;
    this.startY = offsetY + boxPadding;
  }
  onTouchmove(event: TouchEvent) {
    if (event.touches.length > 1) {
      return;
    }
    if (this.isMouseDown) {
      const curTouch = event.touches[0];
      this.updateCoor((c, box) => {
        return {
          x: c.width - box.width - (curTouch.pageX - this.startX),
          y: c.height - box.height - (curTouch.pageY - this.startY),
        };
      });
    }
  }
  onMousedown(event: MouseEvent) {
    this.isMouseDown = true;
    this.isMove = true;
    this.setPageArea();
    const boxPadding = 10;
    this.startX = event.offsetX + boxPadding;
    this.startY = event.offsetY + boxPadding;
  }
  onMousemove(event) {
    if (this.isMouseDown) {
      this.updateCoor((c, box) => {
        return {
          x: c.width - box.width - (event.pageX - this.startX),
          y: c.height - box.height - (event.pageY - this.startY),
        };
      });
    }
  }

  updateCoor(func: (c: BoxInfoModel, box: BoxInfoModel) => any) {
    // function updateCoor( func: () => ApiDragModel):void {
    const box: BoxInfoModel = {
      width: 60,
      height: 60,
    };
    const coor = func({ width: this.clientW, height: this.clientH }, box);
    const _currentX = Math.max(0, coor.x);
    const _currentY = Math.max(0, coor.y);
    this.currentX = Math.min(_currentX, this.clientW - box.width);
    this.currentY = Math.min(_currentY, this.clientH - box.height);
    if (this.isMove) {
      this.controlEl.style.bottom = this.currentY + 'px';
      this.controlEl.style.right = this.currentX + 'px';
    }
  }
  onMouseLeave() {
    this.onMouseup();
  }
  onMouseup() {
    if (!this.isMouseDown) {
      return;
    }
    // console.log('onMouseup');
    this.isMouseDown = false;
    this.clientW = 0;
    this.clientH = 0;
    this.startX = 0;
    this.startY = 0;
  }
  createDom() {
    const iconSvg = `
    <svg xmlns="http://www.w3.org/2000/svg" style="width: 55%; height: 55%" width="36" height="36" viewBox="0 0 36 36">
        <path fill="currentColor" d="M34.87 32.21L30 27.37V8.75l-2.3-4.23a2 2 0 0 0-3.54 0L22 8.76v10.65L3.71 1.21A1 1 0 0 0 2 1.92V10h2.17v1.6H2V18h2.17v1.6H2v6.65h2.17v1.6H2v5.07a1 1 0 0 0 1 1h31.16a1 1 0 0 0 .71-1.71ZM10 26v-9.06L19.07 26Zm18 2.11h-4v-2.43h4Zm0-4h-4V9.25l1.94-3.77L28 9.26Z" class="clr-i-solid clr-i-solid-path-1"/>
        <path fill="none" d="M0 0h36v36H0z"/>
    </svg>`;
    const controlEl = document.createElement('div');
    controlEl.style.cssText = `
        position: absolute;
        user-select: none;
        bottom: 0;
        right: 0;
        z-index: 99999;
        width: 60px;
        height: 60px;
        background: #fff;
        border-radius: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        overflow: hidden;
        color: #17B3A3;
        box-shadow: 0 2px 4px 1px rgba(40, 120, 255, 0.08), 0 0 6px 1px rgba(0, 0, 0, 0.08);
    `;
    controlEl.innerHTML = iconSvg;
    this.controlEl = controlEl;
    this.option.el.appendChild(controlEl);
  }
  eventDestroy: any = null;
  init() {
    this.createDom();
    const resizeFunc = () => {
      this.setPageArea();
      this.updateCoor(() => {
        return {
          x: this.currentX,
          y: this.currentY,
        };
      });
    };
    const touchstartFunc = (event: TouchEvent) => {
      this.onTouchstart(event);
    };
    const touchmoveFunc = (event: TouchEvent) => {
      this.onTouchmove(event);
    };
    const mousedownFunc = (event: MouseEvent) => {
      this.onMousedown(event);
    };
    const mousemoveFunc = (event: MouseEvent) => {
      this.onMousemove(event);
    };
    const mouseupFunc = () => {
      this.onMouseup();
    };
    const onclickFunc = (event) => {
      this.option.onClick && this.option.onClick(event);
    };
    if (IS_TOUCH_SUPPORTED) {
      this.controlEl.addEventListener('touchstart', touchstartFunc);
      window.addEventListener('touchmove', touchmoveFunc);
      window.addEventListener('touchend', mouseupFunc);
      window.addEventListener('touchcancel', mouseupFunc);
    } else {
      this.controlEl.addEventListener('mousedown', mousedownFunc);
      window.addEventListener('mousemove', mousemoveFunc);
      window.addEventListener('mouseup', mouseupFunc);
    }
    window.addEventListener('resize', resizeFunc);
    this.controlEl.addEventListener('click', onclickFunc);
    this.eventDestroy = () => {
      window.removeEventListener('resize', resizeFunc);
      window.removeEventListener('click', onclickFunc);
      if (IS_TOUCH_SUPPORTED) {
        this.controlEl.removeEventListener('touchstart', touchstartFunc);
        window.removeEventListener('touchmove', touchmoveFunc);
        window.removeEventListener('touchend', mouseupFunc);
        window.removeEventListener('touchcancel', mouseupFunc);
      } else {
        window.removeEventListener('mousemove', mousemoveFunc);
        window.removeEventListener('mouseup', mouseupFunc);
        this.controlEl.removeEventListener('mousedown', mousedownFunc);
      }
    };
  }
  destroy() {
    this.eventDestroy && this.eventDestroy();
    this.eventDestroy = null;
    this.controlEl.remove();
    this.controlEl = null;
  }
}
class UiDesignCheck {
  option = {
    el: document.body,
  };
  control: any = null;
  checkInstance: any = null;
  constructor(opt) {
    const _opt = opt || {};
    this.option.el = _opt.el || document.body;
    this.init();
  }
  creteControl() {
    this.control = new UiDesignControl({
      el: this.option.el,
      onClick: () => {
        this.checkInstance && this.checkInstance.getVm().toggleShow();
      },
    });
  }
  init() {
    this.checkInstance = openCheck();
    this.creteControl();
  }
  destroy() {
    this.checkInstance && this.checkInstance.unmount();
    this.checkInstance = null;
    this.control && this.control.destroy();
    this.control = null;
  }
}

new UiDesignCheck({});
