import { useAppStore } from '@/store/modules/app';

export function onClosePage() {
  const appStore = useAppStore();
  const loadingContainer = document.querySelector('.loading-container') as HTMLElement;
  const timeStatus = document.querySelector('.time-loading-status') as HTMLElement;
  const onDisplayStart = () => {
    loadingContainer.style.display = 'none';
    timeStatus.innerHTML = '2';
  };
  // const loadingContainer = document.querySelector('.loading-container');
  // const timeStatus = document.querySelector('.time-loading-status');
  // timeStatus.innerHTML = 0;
  // if (loadingContainer && appStore.loadStaticResource) {
  //   loadingContainer.style.display = 'none';
  // }
  const onCheck = () => {
    if (loadingContainer && appStore.loadStaticResource) {
      onDisplayStart();
    } else {
      setTimeout(() => {
        onCheck();
      }, 500);
    }
  };
  onCheck();
}
