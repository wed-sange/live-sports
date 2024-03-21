export type ListResponse<T = any> = {
  total: number;
  records: T[];
  size: number;
  current: number;
};
export interface MemberListParams {
  current: number;
  size: number;
  userName?: string;
}
export type MemberItemData = {
  anchorId: number; //	主播ID	integer
  nickName: string; //	昵称	string
  head: string; //	头像	string
  fans: number; //	粉丝数	integer
  liveId: number | null; //	正在直播ID
  shortName: string; //	昵称首拼字母
};
export interface MessageListParams {
  current: number;
  size: number;
  userName?: string;
}
export type MessageItemData = {
  id: number; //	主键id
  fromId: string; //	发送用户ID
  anchorId: string; //	主播ID 当id为0则代表为反馈通知
  avatar: string; //	发送者头像
  nick: string; //	发送者昵称
  content: string; //	消息内容
  msgType: number; //消息类型(0:text、1:image、2:voice、3:video、4:music、5:news)
  createTime: string; //	创建时间
  noReadSum: number; //	未读消息数量
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

export interface ChatAccountInfoData {
  id: number; //		用户ID	integer(int64)
  nickName: string; //		昵称	string
  head: string; //		头像	string
  notice: string; //		公告	string
  fansCount: number; //		粉丝数量	integer(int32)
}
export interface FeedbackListParams {
  current: number;
  size: number;
}
export type FeedbackItemData = {
  id: number; //	ID	integer(int64)
  feedbackUserId: number; //	反馈用户ID	integer(int64)
  feedbackUserName: string; //	反馈用户名	string
  feedbackTime: string; //	反馈时间	string(date-time)
  replyTime: string; //	处理时间	string(date-time)
  feedbackType: string; //	反馈类型：1：更新问题，2：直播相关，3：产品体验，4：聊天相关，5：比赛相关，6：其他,可用值:1,2,3,4,5,6	string
  feedbackContent: string; //	反馈内容	string
  feedbackResult: string; //	反馈结果	string
  ignoreFlag: boolean; //	是否忽略	boolean
  feedbackStatus: string; //	反馈状态 未处理、已处理,可用值:1,2	string
  readFlag: boolean; //	是否已读	boolean
  feedbackImage: string[]; //	反馈图片	array	string
};

export interface FeedbackFormParams {
  feedbackType: string; //	反馈类型：1：更新问题，2：直播相关，3：产品体验，4：聊天相关，5：比赛相关，6：其他,可用值:1,2,3,4,5,6
  feedbackContent: string; //	反馈内容
  feedbackImage: string[]; //	反馈图片
}

export interface SysNotifyListParams {
  current: number;
  size: number;
}
export type SysNotifyItemData = {
  id: number; //	主ID	integer
  type: number; //	通知类型(1反馈结果 2禁言通知 3解禁通知)	integer
  title: string; //	通知标题	string
  notice: string; //	通知内容	string
  readFlag: boolean; //	是否已读	boolean
  createTime: string; //	通知时间	string
  bizId: string; //	业务ID	string
};
