import { createRouter, createWebHashHistory, Router } from 'vue-router';
import routes from './routes';
import { useAppStore } from '@/store/modules/app';

const router: Router = createRouter({
  history: createWebHashHistory(),
  routes: routes,
});
const noRoomCaches = ['HomePage', 'Match', 'RoomDetail', 'UserSubscribe', 'UserFollowing', 'UserHistory'];
const userRoomCaches = ['UserSubscribe', 'UserFollowing', 'UserHistory'];
//路由拦截，跳转后
router.afterEach((to, from) => {
  const appStore = useAppStore();
  let transition;
  if (to.meta.level == 1 && from.meta.level == 1) {
    //是否是一级页面
    // transition = null;
  } else if (!to.meta.level || !from.meta.level) {
    //是否第一次进入或者刷新
    // transition = null;
  } else if (to.meta.level > from.meta.level) {
    //进入动画
    transition = 'page-left';
    appStore.backAnimation = false;
  } else {
    //退出动画
    if (appStore.backAnimation) {
      transition = 'page-right';
    }
    appStore.backAnimation = false;
  }
  to.meta.transitionName = transition;
});

router.beforeEach((to, from, next) => {
  const appStore = useAppStore();
  const toName = to.name as string;
  const fromName = from.name as string;
  //从直播间返回首页或者别的列表需要清理直播间里状态，但直播间跳转私聊页面不清理直播间里状态
  appStore.clearRoom = fromName === 'RoomDetail' && toName !== 'MessageChat';
  if (toName === 'RoomDetail') {
    //从首页 赛程 订阅 观看历史 进入直播间，直播页面不用缓存
    if (!fromName || noRoomCaches.includes(fromName)) {
      appStore.onRemoveRouterName('RoomDetail');
    }
    if (userRoomCaches.includes(fromName)) {
      //从列表进入直播间，fromName列表页面需要缓存
      appStore.onAddRouterName(fromName);
    }
  } else if (toName === 'MessageChat') {
    if (fromName === 'RoomDetail') {
      appStore.onAddRouterName('RoomDetail');
    }
  } else if (fromName === 'User') {
    //从个人中心进入对应的列表,列表不缓存
    if (userRoomCaches.includes(toName)) {
      appStore.onRemoveRouterName(toName);
    }
  }
  next();
});

export default router;
