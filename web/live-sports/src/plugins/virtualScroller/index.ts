import type { App } from 'vue';

import 'vue-virtual-scroller/dist/vue-virtual-scroller.css';
import VirtualScroller from 'vue-virtual-scroller';

export const useVirtualScroller = (app: App<Element>) => {
  app.use(VirtualScroller);
};
