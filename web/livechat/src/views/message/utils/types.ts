import { ChatType, LiveStatus } from "@/api/message/enums";

export enum MessageCommand {
  ping = 13,
  pingResponse = 13,
  receive = 11,
  send = 11,
  sendResponse = 12,
  login = 5,
  loginResponse = 6,
  logout = 22,
  logoutResponse = 22,
  joinGroup = 7,
  joinGroupResponse = 9,
  closeLive = 26,
  openLive = 25,
  leaveGroup = 21,
  leaveGroupResponse = 10,
  read = 23,
  matchChange = 30,
  readDotAll = 29,
  anchorReadMessage = 31,
  banned = 32,
  unBanned = 33,
  feedback = 28,
  liveStartNotify = 34,
  updateUrl = 27
}
export enum MessageType {
  text = 0,
  image = 1,
  voice = 2,
  video = 3,
  music = 4,
  news = 5,
  system = 6
}
type MessageModel<T> = {
  command: MessageCommand; // 11
  code: number; //
  success: number; //
  data: T;
};

export type MessageLiveStartNotifyData = MessageModel<{
  awayTeamLogo: string; //客队球队logo
  awayTeamName: string; //客队队名
  homeTeamLogo: string; //主队logo
  homeTeamName: string; //主队队名
  id: number; //直播ID
  matchId: number; //比赛ID
  matchTime: number; //比赛时间分钟时间戳
  matchType: number; //比赛类型
  nickName: string; //主播昵称
  userId: number; //主播ID
  userLogo: string; //主播头像
}>;
export type MessageFeedbackData = MessageModel<{
  noticeId: number; //通知ID
  bizId: number; //业务ID，反馈信息ID
  userId: number; //用户ID
  title: string; //前端展示标题
  reason: string; //禁言原因或者备注信息
  createTime: number; //通知时间
}>;
export type MessageBannedData = MessageModel<{
  noticeId: number; //通知ID
  bizId: number; //业务ID，反馈信息ID
  userId: number; //用户ID
  title: string; //前端展示标题
  reason: string; //禁言原因或者备注信息
  createTime: number; //通知时间
}>;
export type MessageAnchorReadMessageData = MessageModel<{
  anchorId: string; //主播ID
  avatar: string; //主播头像
  chatUserInfo: {
    avatar: string; //用户头像
    content: string; //最新消息内容
    createTime: string; //最新消息时间
    dataType: string; //1普通消息目前只有这个值
    id: number; //消息ID
    identityType: 0 | 1 | 2 | 3; //消息发送者身份：0：普通用户，1主播 2助手 3运营
    msgType: MessageType; //消息类型：(如：0:text、1:image、2:voice、3:video、4:music、5:news)
    nick: string; //用户昵称
    noReadSum: number; //用户发送主播未读消息数量
    userId: string; //用户ID
  };
  nick: string; //主播昵称
  noReadSum: number; //主播未读消息总数
}>;
export type MessageReadDotAllData = MessageModel<{
  totalCount: number; // 未读消息总数
}>;
export type MessageReceiveData = MessageModel<{
  anchorId: string; //（字符串）: 当前聊天主播的id【私聊独有，不管是发送还是接收】
  chatType: number; //聊天类型，群聊：1， 私聊：2
  cmd: number;
  content: string; //消息内容
  createTime?: number; //(长整型): 消息时间
  from: string; //（字符串）: 发送者ID
  groupId?: string; //: 群聊ID【群聊独有】
  id: string; //消息ID
  msgType: MessageType; //消息类型，文字：0， 图片：1
  sendId?: string; //（字符串）:  消息发送标识符
  to: string; //（字符串）: 接收者ID【私聊独有】
  toAvatar: string; // 接收者头像（主播、运营、助手均显示主播头像）
  toNickName: string; // 接收者昵称（主播、运营、助手均显示主播昵称）
  fromAvatar: string; // 发送者头像（主播、运营、助手均显示主播头像）
  fromNickName: string; // 发送者昵称（主播、运营、助手均显示主播昵称）
  level: string; // level: 用户等级（发送者为用户时必填）
  identityType: 0 | 1 | 2 | 3; // 发送者类型：0-APP普通用户，1-LIVE端主播，2-LIVE端助手，3-LIVE端运营
}>;
export type MessageSendData = {
  from: string; //（字符串）: 发送者ID
  sendId?: string; //（字符串）:  消息发送标识符
  cmd: MessageCommand; //（整型）: 11
  anchorId?: string; //（字符串）: 当前聊天主播的id【私聊独有，不管是发送还是接收】
  toAvatar?: string; // 接收者头像（主播、运营、助手均显示主播头像）
  toNickName?: string; // 接收者昵称（主播、运营、助手均显示主播昵称）
  fromAvatar?: string; // 发送者头像（主播、运营、助手均显示主播头像）
  fromNickName?: string; // 发送者昵称（主播、运营、助手均显示主播昵称）
  to?: string; //（字符串）: 接收者ID【私聊独有】
  level?: string; // level: 用户等级（发送者为用户时必填）
  groupId?: string; //: 群聊ID【群聊独有】
  createTime?: number; //(长整型): 消息时间
  msgType: MessageType; //（字符串）：消息类型，文字：0， 图片：1
  chatType: ChatType; //（字符串）: 聊天类型，群聊：1， 私聊：2
  content: string; //(字符串): 消息内容;
  identityType?: 0 | 1 | 2 | 3; // 发送者类型：0-APP普通用户，1-LIVE端主播，2-LIVE端助手，3-LIVE端运营
};
export type MessageSendReadData = {
  cmd: MessageCommand; //（整型）: 23
  messageId: string; //（字符串）：
  read: number; //（整型）: 0-未读， 1-已读
  currentId: string; //（String）: 当前用户ID
  channelType: number; //（整型）: 消息发送平台：2-LIVE, 3-APP
  toId: string; //（字符串）: 接收人ID
};

export type MessageChatServerOption = {
  on: {
    init?: (isConnect: boolean) => void;
    login?: (isLogin: boolean) => void;
    message: (data: any) => void;
  };
};
export type MessageChatServerUserInfo = {
  userId: string;
  token: string;
  groupId?: string;
};
export type MessageResponse<T = any> = {
  success: boolean;
  data: T;
};

export enum MessageCardType {
  SYSTEM = "system",
  MESSAGE = "message"
}
export type MessageCardData = {
  id: string;
  anchorId: string;
  sendUserId: string;
  name: string;
  cover: string;
  message: string;
  readDot: number;
  createTime: string;
  createTimeText: string;
  type: MessageCardType;
  msgType: MessageType;
  liveStatus: LiveStatus;
  setTop?: boolean;
};
