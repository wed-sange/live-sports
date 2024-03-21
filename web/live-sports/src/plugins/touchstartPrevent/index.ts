import type { App } from 'vue';
import { touchstartPreventDirective } from './touchstartPrevent';
const useTouchstartPrevent = (app: App<Element>) => {
  app.directive('touchstart-prevent', touchstartPreventDirective);
};
export { touchstartPreventDirective, useTouchstartPrevent };
