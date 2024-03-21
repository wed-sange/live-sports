import {
  ListResponse,
  AnchorListParams,
  AnchorItemData,
  UserListParams,
  UserItemData,
  LiveItemData,
  MessageHistoryParams,
  MessageHistoryItemData,
  LiveOpenInfo,
  LiveMembers,
  LiveMembersPayload,
  ForbiddenUserPayload,
  KickOutUserPayload,
  UpdateLiveOpenInfoPayload,
  IReadMessageParams,
  IPrivateLatestMessageParams,
  IPrivateEarliestUnreadParams
} from "./model";

import { http } from "@/utils/http";

/** 获取主播列表 */
export const getAnchorList = (data?: AnchorListParams) => {
  return http.request<ListResponse<AnchorItemData>>(
    "post",
    "/msg/privateLivePage",
    { data }
  );
};
/** 获取用户列表 */
export const getUserList = (data?: UserListParams) => {
  return http.request<ListResponse<UserItemData>>(
    "post",
    "/msg/privateUserByLivePage",
    { data }
  );
};

/** 获取用户列表 -- 和上面那个接口一样，名字都没有定义好。。。 */
export const getPrivateUserByLivePage = getUserList;

/** 获直播间列表 */
export const getLiveList = () => {
  return http.request<LiveItemData[]>("post", "/msg/publicLivePage");
};
/** 获取历史消息 */
export const getMessageHistory = (data?: MessageHistoryParams) => {
  return http.request<MessageHistoryItemData[] | null>(
    "post",
    `/msg/history?mode=${data.inquiryMode}`,
    {
      data: {
        ...data,
        // 此处由于后端定义的字段名有问题，所以前端自己规范一次
        userId: data.anchorId,
        searchId: data.userId
      }
    } as any
  );
};

/** 获取开播消息 */
export const fetchLiveOpenInfo = (id: string) => {
  return http.request<LiveOpenInfo>("get", `/live/openinfo/getOpenInfo/${id}`);
};

/** 修改开播消息 */
export const fetchUpdateLiveOpenInfo = (payload: UpdateLiveOpenInfoPayload) => {
  return http.request<{}>("put", `/live/openinfo/insertOrUpdate`, {
    data: payload
  });
};

/** 查询直播间用户列表 */
export const fetchLiveMembers = (payload: LiveMembersPayload) => {
  return http.request<LiveMembers>("post", `/user/queryGroupUserPage`, {
    data: payload
  });
};

/** 禁言用户 */
export const fetchForbiddenUser = (payload: ForbiddenUserPayload) => {
  return http.request<{}>("put", `/user/forbidden`, { data: payload });
};

/** 解除禁言 */
export const fetchUnForbiddenUser = (payload: ForbiddenUserPayload) => {
  return http.request<{}>("put", `/user/unforbidden`, { data: payload });
};

/** 踢出用户 */
export const fetchKickOutUser = (payload: KickOutUserPayload) => {
  return http.request<{}>("put", `/user/kickOut`, { data: payload });
};
/** 消息批量已读 */
export const postReadMessage = (data?: IReadMessageParams) => {
  return http.request<null>("post", "/msg/readMessage", {
    data
  });
};

/** 私聊最新消息列表 */
export const getPrivateLatestMessage = (data?: IPrivateLatestMessageParams) => {
  return http.request<MessageHistoryItemData[] | null>(
    "post",
    "/msg/privateLatestMessage",
    {
      data
    }
  );
};

/** 私聊最早未读消息向前20条 */
export const getPrivateEarliestUnread = (
  data?: IPrivateEarliestUnreadParams
) => {
  return http.request<MessageHistoryItemData[] | null>(
    "post",
    "/msg/privateEarliestUnread",
    {
      data
    }
  );
};
