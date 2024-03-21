export type BroadcastItemData = {
  id: number; //	开播信息配置ID	integer(int64)
  titlePage: string; //	直播封面	string
  notice: string; //	直播公告	string
  firstMessage: string; //	开播首条聊天	string
  remark: string; //	备注名称	string
  used: number; //	是否使用中：1使用中 0未使用	integer(int32)
  createTime: string;
};
export interface UpdateBroadcastParams {
  id?: string; //	开播信息配置ID	integer(int64)
  titlePage: string; //	直播封面	string
  notice: string; //	直播公告	string
  firstMessage: string; //	开播首条聊天	string
  remark: string; //	备注名称	string
  used?: number; //	是否使用中：1使用中 0未使用	integer(int32)
}
