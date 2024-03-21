import { createApp } from 'vue';
import ComponentTemplate from './index.vue';
import { ApiFormEvents, LoadingOption } from './types';

export const openLoading = (option: {
  opt?: LoadingOption;
  sure?: (vm: typeof ComponentTemplate) => void | undefined;
  close?: Function | undefined;
}): any => {
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
  vm.changeOption(option.opt);
  vm.show = true;
  return vm;
};

type LoadingInstanceType = {
  key: number;
  opt?: LoadingOption;
};
let loadingKey = 0;
let loadingInstanceCache: LoadingInstanceType[] = [];
function updateLoading(type: 'add' | 'remove', instance?: LoadingInstanceType): void {
  if (!loadingInstance) {
    return;
  }
  if (type === 'add') {
    loadingInstanceCache.push(instance!);
    loadingInstance.changeOption(instance!.opt);
  } else {
    let _instance: LoadingInstanceType | undefined;
    if (instance) {
      _instance = loadingInstanceCache.find((cur) => cur.key === instance.key) || loadingInstanceCache.shift();
    } else {
      _instance = loadingInstanceCache.shift();
    }
    loadingInstance.changeOption(_instance?.opt);
  }
}

let loadingIndex = 0;
let loadingInstance: ReturnType<typeof openLoading> | null = null;
export const resetLoading = () => {
  if (loadingInstance) {
    loadingInstance.close();
  }
  loadingInstanceCache = [];
  loadingIndex = 0;
  loadingKey = 0;
  loadingInstance = null;
};
const _closeLoading = (instance?: LoadingInstanceType) => {
  loadingIndex--;
  updateLoading('remove', instance);
  if (loadingIndex <= 0) {
    resetLoading();
    return;
  }
};
export const closeLoading = () => {
  _closeLoading();
};
export const createLoading = (opt?: LoadingOption) => {
  const oldIndex = loadingIndex;
  loadingIndex++;
  if (oldIndex === 0 && loadingInstance === null) {
    loadingInstance = openLoading({});
  }
  const key = loadingKey++;
  const instance = {
    key,
    opt: opt,
  };
  updateLoading('add', instance);
  return {
    close: () => {
      _closeLoading(instance);
    },
  };
};
