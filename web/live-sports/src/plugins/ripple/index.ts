import './ripple.scss';
import type { App } from 'vue';
import { rippleDirective } from './ripple';
const useRipple = (app: App<Element>) => {
  app.directive('ripple', rippleDirective);
};
export { rippleDirective, useRipple };
