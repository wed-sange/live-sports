import { http } from "@/utils/http";

/** 上传普通图片*/
export const uploadImg = data => {
  return http.request(
    "post",
    "file/common/image",
    { data },
    { headers: { "Content-Type": "multipart/form-data" } }
  );
};
/** 上传头像*/
export const uploadAvatar = data => {
  return http.request(
    "post",
    "file/user/icon",
    { data },
    { headers: { "Content-Type": "multipart/form-data" } }
  );
};
