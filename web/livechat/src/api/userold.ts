import { http } from "@/utils/http";

export type UserResult = {
  code: Number;
  data: "";
};

export type RefreshTokenResult = {
  success: boolean;
  data: {
    /** `token` */
    accessToken: string;
    /** 用于调用刷新`accessToken`的接口时所需的`token` */
    refreshToken: string;
    /** `accessToken`的过期时间（格式'xxxx/xx/xx xx:xx:xx'） */
    expires: Date;
  };
};

/** 登录 */
export const getLogin = (data?: object) => {
  return http.post("common/login", { data });
};

/** 刷新token */
export const refreshTokenApi = (data?: object) => {
  return http.request<RefreshTokenResult>("post", "/refreshToken", { data });
};

/** 获取App用户分页 */
export const appUsers = (data?: object) => {
  return http.post("app/user/page", { data });
};

/** 获取App用户详情 */
export const appUserDetail = id => {
  return http.get(`app/user/getUserInfo/${id}`);
};
/** 禁言App用户 */
export const appUserForbidden = data => {
  return http.request("put", `app/user/forbidden`, { data });
};
/** 禁言App用户 */
export const appUserUnForbidden = id => {
  return http.request("put", `app/user/free/${id}`);
};
