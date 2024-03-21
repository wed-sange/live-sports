import RoomDetail from '@/views/live/index.vue';
import Search from '@/views/home/src/search/index.vue';
import Match from '@/views/match/index.vue';
import Message from '@/views/message/index.vue';
import Home from '@/views/home/index.vue';
import User from '@/views/user/index.vue';
import Login from '@/views/login/index.vue';
import NewDetail from '@/views/home/src/newsDetail/index.vue';
import Subscribe from '@/views/user/src/subscribe/index.vue';
import PersonalInfo from '@/views/user/src/personalInfo/index.vue';
import ActivityCenter from '@/views/user/src/activity/index.vue';
import ActivityDetail from '@/views/user/src/activity/detail.vue';
import UserFollowing from '@/views/user/src/following/index.vue';
import History from '@/views/user/src/history/index.vue';
import Concat from '@/views/user/src/concat/index.vue';
import Level from '@/views/user/src/level/index.vue';
import Setting from '@/views/user/src/setting/index.vue';
import Notify from '@/views/message/src/notify/index.vue';
import ChatDetail from '@/views/message/src/chat/index.vue';
import MessageSearch from '@/views/message/src/search/index.vue';

const routes = [
  {
    path: '/',
    redirect: '/home',
    children: [
      {
        path: 'home',
        // component: () => import('@/views/home/index.vue'),
        component: Home,
        name: 'HomePage',
        meta: {
          title: '首页',
          keepAlive: true,
          level: 1,
        },
      },
      {
        path: 'match',
        name: 'Match',
        // component: () => import('@/views/match/index.vue'),
        component: Match,
        meta: {
          title: '赛程',
          keepAlive: true,
          level: 1,
        },
      },
      {
        path: 'message',
        name: 'Message',
        // component: () => import('@/views/message/index.vue'),
        component: Message,
        meta: {
          title: '消息',
          auth: true,
          keepAlive: true,
          level: 1,
        },
      },
      {
        path: 'user',
        name: 'User',
        // component: () => import('@/views/user/index.vue'),
        component: User,
        meta: {
          title: '我的',
          keepAlive: true,
          level: 1,
        },
      },
    ],
  },
  {
    path: '/roomDetail',
    name: 'RoomDetail',
    // component: () => import('@/views/home/roomDetail.vue'),
    component: RoomDetail,
    meta: {
      title: '房间详情',
      level: 2,
    },
  },
  {
    path: '/search',
    name: 'search',
    // component: () => import('@/views/home/search.vue'),
    component: Search,
    meta: {
      title: '搜索',
      level: 2,
    },
  },
  {
    path: '/newsDetail',
    // component: () => import('@/views/home/newsDetail.vue'),
    component: NewDetail,
    meta: {
      title: '新闻详情',
      level: 2,
    },
  },
  {
    name: 'login',
    path: '/login',
    // component: () => import('@/views/login/index.vue'),
    component: Login,
    meta: {
      title: '登录',
      keepAlive: true,
      level: 9,
    },
  },
  {
    path: '/user/subscribe',
    name: 'UserSubscribe',
    // component: () => import('@/views/user/src/subscribe/index.vue'),
    component: Subscribe,
    meta: {
      title: '我的订阅',
      auth: true,
      level: 2,
      keepAlive: true,
    },
  },
  {
    path: '/user/personalInfo',
    name: 'UserPersonalInfo',
    // component: () => import('@/views/user/src/personalInfo/index.vue'),
    component: PersonalInfo,
    meta: {
      title: '个人资料',
      auth: true,
      level: 3,
    },
  },
  {
    path: '/user/activity',
    name: 'UserActivity',
    component: ActivityCenter,
    meta: {
      title: '活动中心',
      auth: false,
      keepAlive: true,
      level: 2,
    },
  },
  {
    path: '/user/activityDetail',
    name: 'UserActivityDetail',
    component: ActivityDetail,
    meta: {
      title: '活动详情',
      auth: false,
      level: 3,
    },
  },
  {
    path: '/user/following',
    name: 'UserFollowing',
    component: UserFollowing,
    meta: {
      title: '我的关注',
      auth: true,
      level: 2,
      keepAlive: true,
    },
  },
  {
    path: '/user/history',
    name: 'UserHistory',
    component: History,
    meta: {
      title: '观看历史',
      auth: true,
      level: 2,
      keepAlive: true,
    },
  },
  {
    path: '/user/concat',
    name: 'UserConcat',
    component: Concat,
    meta: {
      title: '联系我们',
      auth: true,
      level: 2,
    },
  },
  {
    path: '/user/level',
    name: 'UserLevel',
    component: Level,
    meta: {
      title: '我的等级',
      auth: true,
      level: 2,
    },
  },
  {
    path: '/user/setting',
    name: 'UserSetting',
    component: Setting,
    meta: {
      title: '设置',
      auth: true,
      level: 2,
    },
  },
  {
    path: '/message/notify',
    name: 'MessageNotify',
    component: Notify,
    meta: {
      title: '反馈通知',
      auth: true,
      level: 2,
    },
  },
  {
    path: '/message/chat',
    name: 'MessageChat',
    component: ChatDetail,
    meta: {
      title: '消息聊天',
      auth: true,
      level: 4,
    },
  },
  {
    path: '/message/search',
    name: 'MessageSearch',
    component: MessageSearch,
    meta: {
      title: '消息搜索',
      auth: true,
      level: 2,
    },
  },
];

export default routes;
