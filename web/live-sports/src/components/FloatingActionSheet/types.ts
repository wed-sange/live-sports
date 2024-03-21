export enum ApiFormEvents {
  open = 'form.open',
  close = 'form.close',
  afterClose = 'form.afterClose',
  sure = 'form.sure',
  select = 'form.select',
}
export type ActionItem = { name: string; key: string | number; class?: string };
