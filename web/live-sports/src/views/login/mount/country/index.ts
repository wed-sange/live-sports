import { createApp } from 'vue';
import ComponentTemplate from './index.vue';
import { getCountryList as _getCountryList } from '@/api/user';
import { ApiFormEvents, CountryRenderItemData, CountryItemData } from './types';
const countryList: CountryRenderItemData[] = [];
const countryIndexList: string[] = [];
const getCountryData = async () => {
  if (countryList.length > 0) {
    return {
      indexList: countryIndexList,
      list: countryList,
    };
  }
  const response = await _getCountryList();
  const data = response || [];

  const renderData = data.map<CountryItemData>((cur) => {
    return {
      code: (cur.dialingCode || '') + '',
      name: cur.cn || '',
      icon: cur.icon || '',
      shortName: (cur.shortName || '#').toUpperCase(),
    };
  });
  for (let i = 0; i < renderData.length; i++) {
    const cur = renderData[i];
    let index = countryList.findIndex((item) => item.index === cur.shortName);
    if (index === -1) {
      countryIndexList.push(cur.shortName);
      index =
        countryList.push({
          index: cur.shortName,
          data: [],
        }) - 1;
    }
    countryList[index].data.push(cur);
  }
  countryList.sort((a, b) => (a.index === '#' || a.index > b.index ? 1 : -1));
  countryIndexList.sort((a, b) => (a === '#' || a > b ? 1 : -1));
  return {
    indexList: countryIndexList,
    list: countryList,
  };
};
export const dialogCountry = (option: {
  value: string;
  sure?: (item: CountryItemData, vm: typeof ComponentTemplate) => void | undefined;
  close?: Function | undefined;
}): any => {
  const vueMountModel = createApp(ComponentTemplate);
  const vm = vueMountModel.mount(document.createElement('div')) as unknown as typeof ComponentTemplate;
  vm.formMitt.on(ApiFormEvents.sure, (item: CountryItemData) => {
    if (option && option.sure) option.sure(item, vm);
  });
  vm.formMitt.on(ApiFormEvents.close, () => {
    if (option && option.close) option.close(vm);
  });
  vm.formMitt.on(ApiFormEvents.opened, () => {
    vm.init(option.value, getCountryData);
  });
  vm.formMitt.on(ApiFormEvents.afterClose, () => {
    vueMountModel.unmount();
  });
  vm.show = true;
  return vm;
};
