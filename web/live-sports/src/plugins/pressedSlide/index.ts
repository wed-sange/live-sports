import type { App } from 'vue';
import { pressedSlideDirective } from './pressedSlide';
const usePressedSlide = (app: App<Element>) => {
  app.directive('pressed-slide', pressedSlideDirective);
};
export { pressedSlideDirective, usePressedSlide };
