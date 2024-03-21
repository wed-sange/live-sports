import http from "@/utils/request";

/** 后台用户分页查询 */
export const userPages = (data) => {
  return http.post("admin/user/page", data);
};

/** 添加后台用户 */
export const userAdd = (data) => {
  return http.post("admin/user/add", data);
};
/** 重置用户密码 */
export const resetUserPsw = (id) => {
  return http.request("put", `admin/user/resetPasswd/${id}`);
};
/** 修改当前自己密码 */
export const updateMyPasswd = (data) => {
  return http.request("put", "admin/user/updateMyPasswd", data);
};
/** 注销账户 */
export const removeUser = (id) => {
  return http.request("delete", `admin/user/cancel/${id}`);
};
/** 获取直播间热度值 */
export const getLiveConfig = () => {
  return http.request({
    url: "/live/heat/config/getInfo",
    method: "get",
  });
};
/** 修改直播间热度值 */
export const updateLiveConfig = (data) => {
  return http.request({
    method: "put",
    url: "live/heat/config/update",
    data,
  });
};
/** 版本列表查询 */
export const getVersions = (data) => {
  return http.request({
    method: "post",
    url: "version/versionList",
    data,
  });
};
/** 新增版本*/
export const addVersion = (data) => {
  return http.request({
    method: "post",
    url: "version/createVersion",
    data,
  });
};
/** 版本删除*/
export const deleteVersion = (versionId) => {
  return http.request({
    method: "post",
    url: `version/deleteVersion/${versionId}`,
  });
};

/** 上传apk*/
export const uploadFile = (data) => {
  return http.request({
    method: "post",
    url: "file/app/source",
    data,
    headers: { "Content-Type": "multipart/form-data" },
  });
};

/** 短信配置查询*/
export const querySmsInfo = () => {
  return http.request({
    method: "post",
    url: "sms/smsAllList",
  });
};

/** 保存短信配置*/
export const updateSmsInfo = (data) => {
  return http.request({
    method: "post",
    url: "sms/createSms",
    data
  });
};

/** 短信功能设置验证方式*/
export const getSmsVaList = () => {
  return http.request({
    method: "post",
    url: "sms/smsVaList",
  });
};

/** 获取短信功能设置验证方式*/
export const getSmsRuleInfo = () => {
  return http.request({
    method: "post",
    url: "sms/smsRuleInfo",
  });
};

/** 获取短信功能设置验证方式*/
export const updateSmsRuleInfo = (data) => {
  return http.request({
    method: "post",
    url: "sms/createSmsRule",
    data
  });
};
