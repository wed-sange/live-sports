export enum ApiFormEvents {
  open = 'form.open',
  opened = 'form.opened',
  close = 'form.close',
  afterClose = 'form.afterClose',
  sure = 'form.sure',
}

export type CountryItemData = {
  code: string;
  name: string;
  icon: string;
  shortName: string;
};
export type CountryRenderItemData = {
  index: string;
  data: CountryItemData[];
};
