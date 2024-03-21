import request from "@/utils/request";
import { parseStrEmpty } from "@/utils/ruoyi";

// 查询用户列表
export function listUser(data) {
  return request({
    url: "/app/user/page",
    method: "post",
    data,
  });
}

// 查询用户详细
export function appUserDetail(userId) {
  return request({
    url: "/app/user/getUserInfo/" + parseStrEmpty(userId),
    method: "get",
  });
}
/** 禁言App用户 */
export function appUserForbidden(data) {
  return request({
    url: "/app/user/forbidden",
    method: "put",
    data,
  });
}

/** 解除禁言App用户 */
export function appUserUnForbidden(id) {
  return request({
    url: `app/user/free/${id}`,
    method: "put",
  });
}
