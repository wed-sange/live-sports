import { reactive, onActivated, nextTick } from 'vue';
import { onBeforeRouteLeave } from 'vue-router';

// 传入需要获取滚动条的id,定位到上一次的地方
export function backToPosition(id) {
  onBeforeRouteLeave((to, from, next) => {
    console.log('to', to, from);
    savePosition();
    next();
  });
  const state = reactive({
    scroll: 0,
  });

  const savePosition = () => {
    state.scroll = document.querySelector(id)?.scrollTop;
  };

  const recoverPosition = () => {
    nextTick(() => {
      const element = document.querySelector(id);
      element ? (element.scrollTop = state.scroll) : '';
    });
  };
  onActivated(recoverPosition);
}
