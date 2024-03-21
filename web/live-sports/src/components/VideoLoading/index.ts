import { createApp } from 'vue';
import ComponentTemplate from './index.vue';
import { ApiFormEvents } from './types';

export const openLoading = (option: { sure?: (vm: typeof ComponentTemplate) => void | undefined; close?: Function | undefined }): any => {
  const vueMountModel = createApp(ComponentTemplate);
  const root = document.createElement('div');
  const vm = vueMountModel.mount(root) as unknown as typeof ComponentTemplate;
  document.body.appendChild(root);
  vm.formMitt.on(ApiFormEvents.sure, () => {
    if (option && option.sure) option.sure(vm);
  });
  vm.formMitt.on(ApiFormEvents.close, () => {
    if (option && option.close) option.close(vm);
  });
  vm.formMitt.on(ApiFormEvents.opened, () => {
    // vm.init(option.value, getCountryData);
  });
  vm.formMitt.on(ApiFormEvents.afterClose, () => {
    vueMountModel.unmount();
    document.body.removeChild(root);
    root.remove && root.remove();
  });
  vm.show = true;
  return vm;
};

const loadingIndex = 0;
let loadingInstance: ReturnType<typeof openLoading> | null = null;
export const resetLoading = () => {
  if (loadingInstance) {
    loadingInstance.close();
  }
  loadingIndex === 0;
  loadingInstance = null;
};
export const closeLoading = () => {
  resetLoading();
};
export const showLoading = () => {
  loadingInstance = openLoading({});
};
