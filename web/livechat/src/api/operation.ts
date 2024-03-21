import { http } from "@/utils/http";

/** 广告记录列表 */
export const getAdList = (data?: object) => {
  return http.post("advertising/page", { data });
};
/** 修改广告 */
export const updateAd = (data?: object) => {
  return http.post("advertising/updateAdvertising", { data });
};
/** 修改广告 */
export const deleteAd = id => {
  return http.post(`advertising/deleteAdvertising/${id}`);
};
/** 新增广告 */
export const addAd = data => {
  return http.post("advertising/createAdvertising", { data });
};
/** 新增广告 */
export const getAdDetail = id => {
  return http.get(`advertising/getAdvertisingInfo/${id}`);
};

/** 意见反馈记录列表 */
export const getFeedbacks = (data?: object) => {
  return http.post("feedback/page/admin", { data });
};
/** 意见反馈忽略该条记录 */
export const feedbackIgnore = (data?: object) => {
  return http.post("feedback/admin/ignore", { data });
};
/** 意见反馈回复该条记录 */
export const feedbackReply = (data?: object) => {
  return http.post("feedback/admin/reply", { data });
};
/** 意见反馈回复详情 */
export const getFeedbackDetail = id => {
  return http.get(`feedback/detail/admin/${id}`);
};
