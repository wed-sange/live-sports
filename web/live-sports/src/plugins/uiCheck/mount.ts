import { createApp } from 'vue';
import ComponentTemplate from '@/components/common/UiCheck/index.vue';

export const openCheck = (): any => {
  const vueMountModel = createApp(ComponentTemplate);
  const vm = vueMountModel.mount(document.createElement('div')) as unknown as typeof ComponentTemplate;
  document.body.append(vm.$el);
  return {
    getVm: () => {
      return vm;
    },
    unmount: () => {
      vueMountModel.unmount();
    },
  };
};
