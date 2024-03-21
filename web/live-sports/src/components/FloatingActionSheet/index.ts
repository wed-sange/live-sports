import { createApp } from 'vue';
import ComponentTemplate from './index.vue';
import { usePressedSlide } from '@/plugins/pressedSlide';
import { ApiFormEvents, ActionItem } from './types';

export const floatingActionSheet = (option: {
  actions: ActionItem[];
  select?: (item: ActionItem, vm: typeof ComponentTemplate) => void | undefined;
  cancelText?: string | undefined;
  closeOnClickAction?: boolean;
  close?: Function | undefined;
}): any => {
  const vueMountModel = createApp(ComponentTemplate);
  usePressedSlide(vueMountModel);
  const vm = vueMountModel.mount(document.createElement('div')) as unknown as typeof ComponentTemplate;
  vm.formMitt.on(ApiFormEvents.select, (item: ActionItem) => {
    if (option && option.select) option.select(item, vm);
  });
  vm.formMitt.on(ApiFormEvents.close, () => {
    if (option && option.close) option.close(vm);
  });
  vm.formMitt.on(ApiFormEvents.afterClose, () => {
    vueMountModel.unmount();
  });
  vm.init(option.actions, option?.cancelText, option?.closeOnClickAction);
  vm.show = true;
  return vm;
};
