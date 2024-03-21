import { http } from "@/utils/http";

/** 后台用户分页查询 */
export const userPages = (data?: object) => {
  return http.post("admin/user/page", { data });
};

/** 添加后台用户 */
export const userAdd = data => {
  return http.post("admin/user/add", { data });
};
/** 重置用户密码 */
export const resetUserPsw = id => {
  return http.request("put", `admin/user/resetPasswd/${id}`);
};
/** 修改当前自己密码 */
export const updateMyPasswd = data => {
  return http.request("put", "admin/user/updateMyPasswd", { data });
};
/** 注销账户 */
export const removeUser = id => {
  return http.request("delete", `admin/user/cancel/${id}`);
};
/** 获取直播间热度值 */
export const getLiveConfig = () => {
  return http.request("get", "live/heat/config/getInfo");
};
/** 修改直播间热度值 */
export const updateLiveConfig = data => {
  return http.request("put", "live/heat/config/update", { data });
};
/** 版本列表查询 */
export const getVersions = data => {
  return http.request("post", "version/versionList", { data });
};
/** 新增版本*/
export const addVersion = data => {
  return http.request("post", "version/createVersion", { data });
};
/** 版本删除*/
export const deleteVersion = versionId => {
  return http.request("post", `version/deleteVersion/${versionId}`);
};
/** 上传开播封面*/
export const uploadFile = data => {
  return http.request(
    "post",
    "file/common/image",
    { data },
    { headers: { "Content-Type": "multipart/form-data" } }
  );
};
