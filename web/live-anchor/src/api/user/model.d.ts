export interface UserLoginParams {
  account: string;
  passwd: string;
}
export interface UserInfoData {
  id: number; //	主键id
  account: string; //	账号	string
  nickName: string; //	昵称	string
  identityType: number; //	身份：1：主播；2：助手；3：运营	integer(int32)
  head: string; //	头像	string
  notice: string; //	公告	string
  remarks: string; //	备注	string
  ynForbidden: number; //	是否封禁 0否 1普通封禁 2永久封禁	integer(int32)
  forbiddenDescp: string; //	封禁原因	string
  forbiddenDay: number; //	封禁天数	integer(int32)
  forbiddenTime: string; //	封禁期限	string(date-time)
  assistantCount: number; //	助手数量	integer(int32)
  setOpenInfo: boolean; //	从来没有创建过开播信息	true 代表创建过， false代表没有
}

export interface UpdateUserInfoParams {
  name: string;
  head: string;
}
export interface UpdatePasswordParams {
  oldPasswd: string; // 旧密码
  newPasswd: string; //	新密码
  rePasswd: string; // 确认密码
}
export interface UploadImageParams {
  file: Blob;
}
