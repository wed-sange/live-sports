import {
  UserLoginParams,
  UserInfoData,
  UploadImageParams,
  UpdateUserInfoParams,
  UpdatePasswordParams,
  UntopParams
} from "./model";
import { http } from "@/utils/http";

/** 登录 */
export const userLogin = (data?: UserLoginParams) => {
  return http.request<{
    isFirstLogin: boolean;
    token: string;
  }>("post", "/common/login", { data });
};

// 获取用户信息
export const getUserInfo = async () => {
  return http.request<UserInfoData>("get", "/user/getUserInfo");
};
// 登出
export const userLogout = async () => {
  return http.request<UserInfoData>("delete", "/user/logout");
};
// 修改用户信息
export const updateUserInfo = async (data: UpdateUserInfoParams) => {
  return http.request<null>("put", "/user/updateInfo", { data });
};
// 修改用户密码
export const updatePassword = async (data: UpdatePasswordParams) => {
  return http.request<null>("put", "/user/updatePasswd", { data });
};
// 置顶用户 不传userId是置顶主播
export const updateSetTop = async (data: UntopParams) => {
  return http.request<null>("put", "/user/setTop", { data });
};
// 取消置顶
export const updateUnTop = async (data: UntopParams) => {
  return http.request<null>("put", "/user/untop", { data });
};

// 上传头像
export const uploadAvatar = async (data: UploadImageParams) => {
  const formData = new FormData();
  formData.append("file", data.file);
  return http.request<string>("post", "/file/user/icon", {
    data: formData,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
};
// 上传普通图片
export const uploadCommonImg = async (data: UploadImageParams) => {
  const formData = new FormData();
  formData.append("file", data.file);
  return http.request<string>("post", "/file/common/image", {
    data: formData,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
};
// 上传聊天图片
export const uploadChatImg = async (data: UploadImageParams) => {
  const formData = new FormData();
  formData.append("file", data.file);
  return http.request<string>("post", "/file/chat/image", {
    data: formData,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
};

export const uploadChatVideo = async (data: UploadImageParams) => {
  const formData = new FormData();
  formData.append("file", data.file);
  return http.request<string>("post", "/file/chat/video", {
    data: formData,
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
};
