import activity from '@/assets/img/user/icon_activity.png';
import following from '@/assets/img/user/icon_following.png';
import history from '@/assets/img/user/icon_history.png';
import level from '@/assets/img/user/icon_my_level.png';
import feedback from '@/assets/img/user/icon_my_feedback.png';
import edit from '@/assets/img/user/icon_my_edit.png';
import notify from '@/assets/img/user/icon_my_notify.png';
import share from '@/assets/img/user/icon_my_share.png';
import setting from '@/assets/img/user/user_set_icon.png';
import subscribe from '@/assets/img/user/icon_subscribe.png';
import levelNew from '@/assets/img/user/user_level_new.png';
import defaultUser from '@/assets/img/user/default_user_w.png';
import userBg from '@/assets/img/user/user_bg.png';

import homeSearch from '@/assets/img/home/home-search-icon.png';
import hotWhite from '@/assets/img/home/hot-white.png';

import tabHomeActive from '@/assets/img/tab/tab-home-active.png';
import tabHome from '@/assets/img/tab/tab-home.png';
import tabMessageActive from '@/assets/img/tab/tab-message-active.png';
import tabMessage from '@/assets/img/tab/tab-message.png';
import tabMatchActive from '@/assets/img/tab/tab-match-active.png';
import tabMatch from '@/assets/img/tab/tab-match.png';
import tabMineActive from '@/assets/img/tab/tab-mine-active.png';
import tabMine from '@/assets/img/tab/tab-mine.png';
import calenderIcon from '@/assets/img/match/calendar-icon.png';
import defaultImg from '@/assets/img/user/default-profile.png';

import shareIcon from '@/assets/img/room/share-icon.png';
import emptyChat from '@/assets/img/common/empty_chat.png';
import emptyLives from '@/assets/img/common/empty_lives.png';
import starIcon from '@/assets/img/sportsCard/star.jpg';
import emptyData from '@/assets/img/common/empty_data.png';

import { useResourceStore } from '@/store/modules/resource';

//预加载静态资源
const modules = [
  tabHomeActive,
  tabHome,
  tabMessageActive,
  tabMatchActive,
  tabMatch,
  tabMineActive,
  tabMessage,
  tabMine,
  homeSearch,
  subscribe,
  activity,
  following,
  history,
  level,
  feedback,
  edit,
  notify,
  share,
  setting,
  levelNew,
  defaultUser,
  calenderIcon,
  defaultImg,
  userBg,
  hotWhite,
  shareIcon,
  emptyChat,
  emptyLives,
  starIcon,
  emptyData,
];
const createImgLoad = (src) => {
  return new Promise((resolve) => {
    const img = new Image();
    img.onload = () => {
      resolve({
        src,
        success: true,
      });
    };
    img.onerror = () => {
      resolve({
        src,
        success: false,
      });
    };
    img.src = src;
  });
};

export const loadResources = async () => {
  const list: Promise<any>[] = [];
  // 个人中心广告动态加载
  const resourceStore = useResourceStore();
  const response = (await resourceStore.getAdInfo()) as any;
  if (response) {
    createImgLoad(response.imgUrl);
  }
  //首页推荐banner动态加载
  const bannerRes = (await resourceStore.getBannerList()) as [];
  bannerRes &&
    bannerRes.forEach((item: any) => {
      createImgLoad(item.imgUrl);
    });
  modules.forEach((item) => {
    list.push(createImgLoad(item));
  });
  return Promise.allSettled(list);
};
