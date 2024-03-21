import {
  UserLoginParams,
  UserInfoData,
  UploadImageParams,
  UpdateUserInfoParams,
  ListResponse,
  UserAdvData,
  SubscribeData,
  SubscribeListParams,
  SubscribeEventParams,
  ActivityListParams,
  ActivityData,
  AnchorFollowingParams,
  AnchorFollowingData,
  LiveHistoryParams,
  LiveHistoryData,
  CountryListData,
} from './model';
import request, { post, get } from '@/utils/useAxiosApi';

// 用户登录
export const userLogin = async (data: UserLoginParams) => {
  return post<string>({
    url: '/common/login',
    data,
  });
};
// 获取用户信息
export const getUserInfo = async () => {
  return get<UserInfoData>({
    url: '/user/getUserInfo',
  });
};

// 退出登录
export const userLogout = async () => {
  return request<null>({
    url: '/user/logout',
    method: 'DELETE',
  });
};

// 国家信息
export const getCountryList = async () => {
  return request<CountryListData[]>({
    url: '/country/list',
    method: 'POST',
  });
};

// 上传头像
export const uploadAvatar = async (data: UploadImageParams) => {
  const formData = new FormData();
  formData.append('file', data.file);
  return post<string>({
    url: '/file/user/icon',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    data: formData,
  });
};
// 上传普通图片
export const uploadCommonImg = async (data: UploadImageParams) => {
  const formData = new FormData();
  formData.append('file', data.file);
  return post<string>({
    url: '/file/common/image',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    data: formData,
  });
};
// 上传聊天图片
export const uploadChatImg = async (data: UploadImageParams) => {
  const formData = new FormData();
  formData.append('file', data.file);
  return post<string>({
    url: '/file/chat/image',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    data: formData,
  });
};
// 更新用户信息
export const updateUserInfo = async (data: UpdateUserInfoParams) => {
  return request<null>({
    url: '/user/updateInfo',
    method: 'PUT',
    data,
  });
};

// 我的订阅
export const getUserAdv = async () => {
  return get<UserAdvData>({
    url: '/advertising/personal/center',
  });
};

// 我的订阅
export const getSubscribeList = async (data: SubscribeListParams) => {
  return post<ListResponse<SubscribeData>>({
    url: '/follow/matchPage',
    data,
  });
};
// 我的订阅-添加订阅
export const addSubscribe = async (data: SubscribeEventParams) => {
  return post<null>({
    url: `/follow/match/follow/${data.matchId}/${data.matchType}`,
  });
};
// 我的订阅-取消订阅
export const removeSubscribe = async (data: SubscribeEventParams) => {
  return post<null>({
    url: `/follow/match/unfollow/${data.matchId}/${data.matchType}`,
  });
};

// 活动中心
export const getActivityList = async (data: ActivityListParams) => {
  return post<ListResponse<ActivityData>>({
    url: '/activity/page',
    data,
  });
};
// 活动中心-活动详情
export const getActivityDetail = async (id: string) => {
  return get<ActivityData>({
    url: `/activity/getActivityInfo/${id}`,
  });
};

// 我的关注
export const getAnchorFollowingList = async (data: AnchorFollowingParams) => {
  return post<ListResponse<AnchorFollowingData>>({
    url: '/follow/anchorPage',
    data,
  });
};
// 我的关注-添加关注主播
export const addAnchorFollowing = async (id: string) => {
  return post<null>({
    url: `/follow/anchor/follow/${id}`,
  });
};
// 我的关注-取消关注主播
export const removeAnchorFollowing = async (id: string) => {
  return post<null>({
    url: `/follow/anchor/unfollow/${id}`,
  });
};

// 观看历史
export const getLiveHistoryList = async (data: LiveHistoryParams) => {
  return post<ListResponse<LiveHistoryData>>({
    url: '/user/live/history/page/list',
    data,
  });
};
// 邮件发送
export const emailSend = async (data: any) => {
  return post<{}>({
    url: '/email/send',
    data,
  });
};
// 手机发送
export const smsSend = async (data: any) => {
  return post<{}>({
    url: '/sms/send',
    data,
  });
};
