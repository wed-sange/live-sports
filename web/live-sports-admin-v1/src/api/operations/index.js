import http from "@/utils/request";

/** 广告记录列表 */
export const getAdList = (data) => {
  return http.post("advertising/page", data);
};
/** 修改广告 */
export const updateAd = (data) => {
  return http.post("advertising/updateAdvertising", data);
};
/** 修改广告 */
export const deleteAd = (id) => {
  return http.post(`advertising/deleteAdvertising/${id}`);
};
/** 新增广告 */
export const addAd = (data) => {
  return http.post("advertising/createAdvertising", data);
};
/** 新增广告 */
export const getAdDetail = (id) => {
  return http.get(`advertising/getAdvertisingInfo/${id}`);
};

/** 意见反馈记录列表 */
export const getFeedbacks = (data) => {
  return http.post("feedback/page/admin", data);
};
/** 意见反馈忽略该条记录 */
export const feedbackIgnore = (data) => {
  return http.post("feedback/admin/ignore", data);
};
/** 意见反馈回复该条记录 */
export const feedbackReply = (data) => {
  return http.post("feedback/admin/reply", data);
};
/** 意见反馈回复详情 */
export const getFeedbackDetail = (id) => {
  return http.get(`feedback/detail/admin/${id}`);
};
/** 活动分页 */
export const getActivities = (data) => {
  return http.post("activity/page", data);
};
/** 活动详情 */
export const getActivityDetail = (id) => {
  return http.get(`activity/getActivityInfo/${id}`);
};
/** 删除活动 */
export const deleteActivity = (id) => {
  return http.post(`activity/deleteActivity/${id}`);
};
/** 修改活动 */
export const updateActivity = (data) => {
  return http.post("activity/updateActivity", data);
};
/** 新增活动 */
export const createActivity = (data) => {
  return http.post("activity/createActivity", data);
};
