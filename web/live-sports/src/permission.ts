/**
 * @name permission
 * @description 全局路由过滤
 */
import router from './router';
import { useUserStore } from '@/store/modules/user';

const whitList = ['/login'];
router.beforeEach(async (to, from, next) => {
  const isWhitePage = whitList.includes(to.path);
  const isAuthPage = to.meta.auth === true;
  if (isWhitePage || !isAuthPage) {
    next();
  } else {
    const userStore = useUserStore();
    if (userStore.token) {
      next();
    } else {
      next({
        path: `/login?redirect=${from.fullPath}`,
        replace: false,
      });
    }
  }
});
