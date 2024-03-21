import { useAppStore } from '@/store/modules/app';
const noBackList = ['#/home', '#/match', '#/user', '#/message'];
//全局右滑监听，四个首页，还有列表滚动时不执行返回事件
export function initTouches() {
  const appStore = useAppStore();
  let startX, startY;
  const content = document.getElementById('app') as HTMLElement;
  const router = useRouter();

  content.addEventListener('touchstart', function (e) {
    startX = e.touches[0].clientX;
    startY = e.touches[0].clientY;
  });

  const onBackFunction = () => {
    const hashes = window.location.hash.split('redirect=');
    const url = (hashes ? hashes[1] : '') as string;
    const currentHash = window.location.hash;
    if (appStore.isLiveScroll) {
      return;
    }
    if (currentHash) {
      const hashList = currentHash.split('/');
      const homePath = noBackList.find((item) => currentHash.indexOf(item) != -1);
      const noBack = hashList && hashList.length == 2;
      if (homePath && noBack) {
        //首页四个tab不执行返回
        return;
      }
    }
    if (url) {
      router.replace(url);
    } else {
      window.history.back();
    }
  };
  let backTimer;
  content.addEventListener('touchmove', function (e) {
    const currentX = e.touches[0].clientX;
    const currentY = e.touches[0].clientY;
    const distanceX = currentX - startX;
    const distanceY = currentY - startY;
    if (Math.abs(distanceX) > Math.abs(distanceY) && distanceX > 100) {
      if (backTimer) {
        clearTimeout(backTimer);
        backTimer = null;
      }
      backTimer = setTimeout(() => {
        appStore.backAnimation = true;
        onBackFunction();
      }, 200);
    }
  });
}
