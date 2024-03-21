import request from "@/utils/request";

// 登录方法
export function login(account, passwd, code, secret) {
  const data = {
    account,
    passwd,
    code,
    secret,
  };
  return request({
    url: "/common/login",
    headers: {
      isToken: false,
      repeatSubmit: false,
    },
    method: "post",
    data,
  });
}

// 注册方法
export function register(data) {
  return request({
    url: "/register",
    headers: {
      isToken: false,
    },
    method: "post",
    data: data,
  });
}

// 获取用户详细信息
export function getInfo() {
  return request({
    url: "/system/user/getPermissonsInfo",
    method: "get",
  });
}

// 退出方法
export function logout() {
  return request({
    url: "/system/user/myLogout",
    method: "delete",
  });
}

// 获取验证码
export function getCodeImg() {
  return request({
    url: "/captchaImage",
    headers: {
      isToken: false,
    },
    method: "get",
    timeout: 20000,
  });
}
/** 根据账号获取身份验证信息【二维码-私钥】 */
export function getQRBarcode(account) {
  return request({
    url: `/common/getQRBarcodeURL/${account}`,
    headers: {
      isToken: false,
    },
    method: "get",
    timeout: 20000,
  });
}
