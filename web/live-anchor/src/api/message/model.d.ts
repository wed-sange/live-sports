export type ListResponse<T = any> = {
  total: number;
  records: T[];
  size: number;
  current: number;
};
export interface AnchorListParams {
  current: number;
  size: number;
}
export type AnchorItemData = {
  id: number; //	主键id	integer
  fromId: string; //	发送用户ID	string
  anchorId: string; //	主播ID	string
  avatar: string; //	发送者头像	string
  nick: string; //	发送者昵称	string
  content: string; //	消息内容	string
  dataType: number; //	数据类型:1:普通消息 2：反馈通知	integer
  createTime: string; //	创建时间	string
  noReadSum: number; //	未读消息数量	integer
  msgType: number; //	消息类型(0:text、1:image、2:voice、3:video、4:music、5:news)	integer
};
export interface UserListParams {
  current: number;
  size: number;
  anchorId: string | number;
}
export type UserItemData = {
  id: number; //	主键id	integer
  fromId: string; //	发送用户ID	string
  anchorId: string; //	主播ID	string
  avatar: string; //	发送者头像	string
  nick: string; //	发送者昵称	string
  content: string; //	消息内容	string
  dataType: number; //	数据类型:1:普通消息 2：反馈通知	integer
  createTime: string; //	创建时间	string
  noReadSum: number; //	未读消息数量	integer
  msgType: number; //	消息类型(0:text、1:image、2:voice、3:video、4:music、5:news)	integer
};
export type LiveItemData = {
  id: number; //	主键id	integer
  fromId: string; //	发送用户ID	string
  anchorId: string; //	主播ID	string
  avatar: string; //	发送者头像	string
  nick: string; //	发送者昵称	string
  content: string; //	消息内容	string
  dataType: number; //	数据类型:1:普通消息 2：反馈通知	integer
  createTime: string; //	创建时间	string
  noReadSum: number; //	未读消息数量	integer
  msgType: number; //	消息类型(0:text、1:image、2:voice、3:video、4:music、5:news)	integer
};

export interface MessageHistoryParams {
  offset: string; // 上一条记录的消息id
  size: number;
  userId?: string; // 当前用户id
  searchId?: string; // 对话的用户id
  type?: string; // 消息类型，0-历史消息，1-离线消息
  groupId?: string; // 群聊Id
  chatType?: string; // 群聊还是私聊 1-群聊，2-私聊
  searchType?: string; // 查询类型（判断是用户还是直播端，普通用户查看不了被删除的消息）， live-2, APP-3
}
export type MessageHistoryItemData = {
  id: number; //	主键id	integer(int64)
  fromId: string; //	发送用户ID	string
  toId: string; //	目标用户ID	string
  anchorId: string; //	主播ID	string
  cmd: number; //	消息命令码	integer(int32)
  msgType: number; //	消息类型;(如：0:text、1:image、2:voice、3:video、4:music、5:news)	integer(int32)
  chatType: number; //	聊天类型;(如1:公聊、2私聊)	integer(int32)
  avatar: string; //	发送者头像	string
  nick: string; //	发送者昵称	string
  identityType: number; //	发送者身份身份(0：普通用户，1主播 2助手 3运营)	integer(int32)
  level: string; //	级别	integer(int32)
  content: string; //	消息内容	string
  groupId: string; //	群组ID	string
  delFlag: number; //	APP用户是否删除 1删除 0正常	integer(int32)
  creator: string; //创建人	string
  updater: string; //更新人	string
  sent: number; //	是否已发送: 0 未发送 1 已发送	integer(int32)
  readable: number; //	是否已读：0 未读 1 已读	integer(int32)
  createTime: string; //	创建时间	string(date-time)
  updateTime: string; //	更新时间	string(date-time)
};
