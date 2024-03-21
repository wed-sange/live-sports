import RippleDirective from './src/ripple-directive';
import type { App } from 'vue';

export { RippleDirective };

/** 默认参数 见./src/option.ts */

/**
 * 使用介绍
 * v-ripple
 * v-ripple="{ color: 'red' }"
 * v-ripple="{ color: 'red', ...其他参数及值 }"
 */

export default {
  title: 'ripple 水波纹',
  category: '通用',
  status: '100%',
  install(app: App): void {
    app.directive('ripple', RippleDirective);
  },
};
