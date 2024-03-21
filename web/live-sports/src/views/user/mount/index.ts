import { createApp } from 'vue';
import ComponentTemplate from './index.vue';
import { ApiFormEvents } from './types';

export const openLogin = (option?: { sure?: Function | undefined; close?: Function | undefined }): any => {
  const vueMountModel = createApp(ComponentTemplate);
  const vm = vueMountModel.mount(document.createElement('div')) as unknown as typeof ComponentTemplate;
  vm.formMitt.on(ApiFormEvents.sure, (model: any) => {
    if (option && option.sure) option.sure(vm, model);
  });
  vm.formMitt.on(ApiFormEvents.close, (model: any) => {
    if (option && option.close) option.close(vm, model);
  });
  vm.formMitt.on(ApiFormEvents.afterClose, () => {
    vueMountModel.unmount();
  });
  vm.show = true;
  return vm;
};
