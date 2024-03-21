import {
  ListResponse,
  MemberListParams,
  MemberItemData,
  MessageListParams,
  MessageItemData,
  MessageHistoryParams,
  MessageHistoryItemData,
  ChatAccountInfoData,
  FeedbackListParams,
  FeedbackItemData,
  FeedbackFormParams,
  SysNotifyListParams,
  SysNotifyItemData,
} from './model';
import request, { post, get } from '@/utils/useAxiosApi';

// 好友列表
export const getMemberList = async (data: MemberListParams) => {
  return post<ListResponse<MemberItemData>>({
    url: '/follow/myFriendsPage',
    data,
  });
};
// 好友列表-删除好友
export const removeMember = async (id: string) => {
  return post<null>({
    url: `/follow/anchor/unfriend/${id}`,
  });
};
// 消息列表
export const getMessageList = async (data: MessageListParams) => {
  return post<ListResponse<MessageItemData>>({
    url: '/msg/msgPage',
    data,
  });
};
// 获取消息历史记录
export const getMessageHistory = async (data: MessageHistoryParams) => {
  return post<MessageHistoryItemData[] | null>({
    url: '/msg/history',
    data,
  });
};
// 获取聊天账户信息
export const getChatAccountInfo = async (id: string) => {
  return get<ChatAccountInfoData>({
    url: `/live/detail/live/user/info/${id}`,
  });
};

// 删除消息记录
export const removeMessage = async (id: string) => {
  return request<null>({
    url: '/msg/delRecordsByAnchorId',
    method: 'PUT',
    data: {
      anchorId: id,
    },
  });
};

// 反馈通知-列表
export const getFeedbackList = async (data: FeedbackListParams) => {
  return post<ListResponse<FeedbackItemData>>({
    url: '/feedback/page/user',
    data,
  });
};

// 反馈通知-详情
export const getFeedbackDetail = async (id: string) => {
  return get<FeedbackItemData>({
    url: `/feedback/detail/user/${id}`,
  });
};

// 新增问题反馈
export const addFeedback = async (data: FeedbackFormParams) => {
  return post<null>({
    url: '/feedback/submit',
    data,
  });
};

// 清除未读消息-单个
export const clearReadDot = async (id: string) => {
  return request<null>({
    url: '/msg/cancelUnread',
    method: 'PUT',
    data: {
      anchorId: id,
    },
  });
};
// 清除未读消息-所有
export const clearAllReadDot = async () => {
  return request<null>({
    url: '/msg/cancelAllUnread',
    method: 'PUT',
  });
};

// 系统通知-列表
export const getSysNotifyList = async (data: SysNotifyListParams) => {
  return post<ListResponse<SysNotifyItemData>>({
    url: '/user/notice/getInfos',
    data,
  });
};
// 系统通知-主动读取
export const readSysNotify = async (id: string | number) => {
  return request<null>({
    url: `/user/notice/read/${id}`,
    method: 'PUT',
  });
};
