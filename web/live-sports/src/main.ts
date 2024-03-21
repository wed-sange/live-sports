import './assets/app.css';
import './styles/index.scss';
import { createApp } from 'vue';
import App from './App.vue';
import { i18n } from '@/i18n';
import router from '@/router';
import store from '@/store';
import { useVirtualScroller } from './plugins/virtualScroller';
import rippleDirective from './plugins/ripples/index';
import LayoutPanel from '@/layout/LayoutPanel.vue';
//防御xss攻击
import VueDOMPurifyHTML from 'vue-dompurify-html';
//计算屏幕
import { useDeviceAbnormal } from './utils/deviceAbnormal';
import './plugins/vant';
import './plugins/dayjs';
//权限拦截
import './permission';
//ui 还原度对比
// import './plugins/uiCheck/index';
//是一个浏览器的默认行为,用于提高滚动性能和防止滚动阻塞
import 'default-passive-events';
//项目字体
import './assets/font/iconfont.css';

//初始化屏幕iOS innerHeight
useDeviceAbnormal();

const app = createApp(App);
app.use(VueDOMPurifyHTML);
// 路由
app.use(router);

// 国际化
app.use(i18n);

// 状态管理
app.use(store);

// 虚拟滚动
useVirtualScroller(app);

// 水波纹指令
app.use(rippleDirective);

//通用布局组件
app.component('LayoutPanel', LayoutPanel);
//初始化拉流播放器
NodePlayer.load(() => {
  app.mount('#app');
});

// app.mount('#app');
