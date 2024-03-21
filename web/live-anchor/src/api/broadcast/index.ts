import { BroadcastItemData, UpdateBroadcastParams } from "./model";
import { http } from "@/utils/http";

/** 获取主播开播信息列表 */
export const getBroadcastList = () => {
  return http.request<BroadcastItemData[]>(
    "get",
    "/live/openinfo/getOpenInfoList"
  );
};
/** 删除主播开播信息 */
export const removeBroadcast = (id: string) => {
  return http.request<BroadcastItemData>(
    "delete",
    `/live/openinfo/delOpenInfo/${id}`
  );
};
/** 查询主播开播信息明细 */
export const queryBroadcastDetail = (id: string) => {
  return http.request<BroadcastItemData>(
    "get",
    `/live/openinfo/getOpenInfo/${id}`
  );
};
/** 新增主播开播信息 */
export const addBroadcast = (data: UpdateBroadcastParams) => {
  return http.request<BroadcastItemData>(
    "put",
    `/live/openinfo/insertOrUpdate`,
    {
      data
    }
  );
};
/** 更新主播开播信息 */
export const updateBroadcast = (data: UpdateBroadcastParams) => {
  return http.request<BroadcastItemData>(
    "put",
    `/live/openinfo/insertOrUpdate`,
    {
      data
    }
  );
};
