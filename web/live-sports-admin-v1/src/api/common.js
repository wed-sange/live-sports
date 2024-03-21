import http from "@/utils/request";

/** 上传普通图片*/
export const uploadImg = (data) => {
  return http.request({
    url: "/file/common/image",
    method: "post",
    data,
    headers: { "Content-Type": "multipart/form-data" },
  });
};
/** 上传头像*/
export const uploadAvatar = (data) => {
  return http.request(
    "post",
    "file/user/icon",
    { data },
    { headers: { "Content-Type": "multipart/form-data" } }
  );
};
