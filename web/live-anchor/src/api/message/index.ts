import {
  ListResponse,
  AnchorListParams,
  AnchorItemData,
  UserListParams,
  UserItemData,
  LiveItemData,
  MessageHistoryParams,
  MessageHistoryItemData
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
/** 获直播间列表 */
export const getLiveList = () => {
  return http.request<LiveItemData[]>("post", "/msg/publicLivePage");
};
/** 获取历史消息 */
export const getMessageHistory = (data?: MessageHistoryParams) => {
  return http.request<MessageHistoryItemData[] | null>("post", "/msg/history", {
    data
  });
};
