// 示列:
// store.state.common.platform 如果有则是在原生环境
// H5调用App  比如调整 商场详情页面 openPage固定表示打开页面
// this.$nativeUtils.callNative("onOpenPage", { key: "goGoodsInfo",id:'商品id' });
// App调用H5
// this.$nativeUtils.registerHandler("AppCallJS", (data) => {
//   Common.showToast("appData" + data);
// });
export function registerHandler(name, fun) {
  window[name] = fun;
}

export function callNative(name, data) {
  const params = data ? JSON.stringify(data) : '';
  if (!window) {
    return;
  }
  if (window.android && window.android[name]) {
    window.android[name](params);
  } else {
    if (window.webkit) {
      window.webkit.messageHandlers[name].postMessage(params);
    }
  }
}
