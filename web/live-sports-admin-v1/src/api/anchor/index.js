import request from "@/utils/request";
import { parseStrEmpty } from "@/utils/ruoyi";

/** 直播用户分页查询 */
export const liveUsers = (data) => {
  return request.post("/live/page", data);
};
/** 关闭直播间 */
export const closeLive = (id) => {
  return request.post(`/live/close/admin/${id}`);
};

/** 获取主播运营账号分页 */
export const getAnchorList = (data) => {
  return request.post("/live/user/page", data);
};
/** 新增账户 新增助手时候必传所属主播 */
export const creatAnchor = (data) => {
  return request.post("/live/user/create/operate/liveUser", data);
};
// live/user/addLiveUser
/** 账号信息更新 */
export const updateAnchor = (data) => {
  return request.post("/live/user/addLiveUser", data);
};
/** 重置新增账号的密码 */
export const resetPasswd = (id) => {
  return request({
    url: `/live/user/resetPasswd/${id}`,
    method: "put",
  });
};
/** 封禁直播用户 */
export const forbiddenLiveUser = (data) => {
  return request.post(`/live/user/forbiddenLiveUser`, data);
};
/** 注销直播用户 */
export const cancelLiveUser = (id) => {
  return request.post(`/live/user/cancelLiveUser/${id}`);
};
/** 解封直播用户 */
export const uncancelLiveUser = (id) => {
  return request.post(`/live/user/unForbiddenLiveUser/${id}`);
};
/** 查询主播下的所有助手 */
export const getAnchorHelpers = (id) => {
  return request.post(`/live/user/queryHelpersByLiveId/${id}`);
};
/** 直播统计（参数主播ID和统计类型【1：本月 2：近三月 3：全部】） */
export const getAnchorStatistics = (id, type) => {
  return request.post(`/live/statistics/${id}/${type}`);
};
/** 主播详细信息 */
export const getAnchorDetail = (id) => {
  return request({
    url: "/live/user/getLiveUserInfo/" + parseStrEmpty(id),
    method: "get",
  });
};

/** 获取所有未注销的主播列表 */
export const getAllLiveUsers = () => {
  return request({
    url: "/live/user/obtain/liveUsers",
    method: "post",
  });
};

