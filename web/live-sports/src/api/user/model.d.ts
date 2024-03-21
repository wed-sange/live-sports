export interface UserLoginParams {
  account: string;
  passwd?: string; // 登录密码
  code?: string;
  type: number; // 登录方式 1手机号 2邮箱
  source: number; // 登陆来源 1IOS 2android 3H5 4小程序
  registerAddr?: string;
  token?: string; //图形验证码token
  areaCode?: string; //区号
}
export interface UserInfoData {
  id: number; //	主键id
  tel: string; //	手机号
  email: string; //	邮箱
  name: string; //	昵称
  head: string; //	头像
  lvNum: number; //	等级
  lvName: string; //	等级名称
  growthValue: number; //	成长值
  growthValueNext: number; //	下一阶段成长值
  registerAddr: string; //	注册地址
  ynForbidden: number; //	是否禁言 0否 1普通禁言 2永久禁言
  forbiddenDay: number; //	禁言天数
  forbiddenTime: string; //	禁言期限
  forbiddenDescp: string; //	禁言原因
  createTime: number; //	创建时间
  updateTime: string; //	修改时间
}

export interface UploadImageParams {
  file: Blob;
}

export interface UpdateUserInfoParams {
  name?: string; // 昵称
  head?: string; // 头像
}
export type ListResponse<T = any> = {
  total: number;
  records: T[];
  size: number;
  current: number;
};

export interface UserAdvData {
  id: number; //	广告ID	integer(int64)
  name: string; //	广告名称	string
  text: string; //	文字	string
  imgUrl: string; //	图片地址	string
  targetUrl: string; //	跳转地址	string
}
export interface SubscribeListParams {
  current: number;
  size: number;
}
export interface SubscribeData {
  competitionId: number; // 赛事ID	integer
  competitionName: string; // 	赛事ID	string
  matchId: number; //	比赛ID	integer
  matchType: string; //	比赛类型：1：足球；2：篮球,可用值:1,2	string
  matchTime: string; //	比赛时间	string
  runTime: number; //	比赛进行时间（分钟）进行中足球比赛有此信息	integer
  homeName: string; //	主队名称	string
  homeLogo: string; //	主队Logo	string
  homeScore: number; //	主队比分	integer
  homeHalfScore: number; //	主队半场比分	integer
  awayName: string; //	客队名称	string
  awayLogo: string; //	客队Logo	string
  awayScore: number; //	客队比分	integer
  awayHalfScore: number; //	客队半场比分	integer
  status: number; //	篮球状态:0 比赛异常，说明：暂未判断具体原因的异常比赛，可能但不限于：腰斩、取消等等，建议隐藏处理;1 未开赛;2 第一节;3 第一节完;4 第二节;5 第二节完;6 第三节;7 第三节完;8 第四节;9 加时;10 已完场;11 中断;12 取消;13 延期;14 腰斩;15 待定;足球状态码:0 比赛异常，说明：暂未判断具体原因的异常比赛，可能但不限于：腰斩、取消等等，建议隐藏处理;1 未开赛;2 上半场;3 中场;4 下半场;5 加时赛;6 加时赛(弃用);7 点球决战;8 已完场;9 推迟;10 中断;11 腰斩;12 取消;13 待定	integer
  focus: boolean; //	是否关注	boolean
  anchorList:
    | {
        liveId: number; //	直播间ID	integer
        userId: number; //	主播ID	integer
        nickName: string; //	主播昵称	string
        userLogo: string; //	主播头像	string
      }[]
    | null;
  matchData: {
    hasStata: boolean; //	是否有赛况	boolean
    hasLineup: boolean; //	是否有阵容	boolean
    hasOdds: boolean; //	是否有指数	boolean
  };
}
export type SubscribeEventParams = {
  matchId: string;
  matchType: string;
};

export interface ActivityListParams {
  current: number;
  size: number;
  title?: string;
  status?: number; //0：下架； 1：上架
}
export interface ActivityData {
  id: number; //	文章ID	integer
  title: string; //	标题	string
  mainPic: string; //	封面主图	string
  content: string; //	文章内容	string
  sortOrder: number; //	排序	integer
  updateTime: string; //	发布时间	string
  status: number; //	状态：1：上架；0下：架	integer
}

export interface AnchorFollowingParams {
  current: number;
  size: number;
}

export interface AnchorFollowingData {
  anchorId: number; //	主播ID	integer
  nickName: string; //	昵称	string
  head: string; //	头像	string
  fans: number; //	粉丝数	integer
  liveId: number; //	正在直播ID	integer
  matchId: number; //
  matchType: number; //
}
export interface LiveHistoryParams {
  current: number;
  size: number;
}

export interface LiveHistoryData {
  id: number; //	ID	integer
  userId: number; //	USER_ID	integer
  nickName: string; //	昵称	string
  userLogo: string; //	头像	string
  titlePage: string; //	封面	string
  notice: string; //	公告	string
  firstMessage: string; //	首条消息	string
  competitionId: number; //	赛事ID	integer
  competitionName: string; //	赛事名称	string
  matchId: number; //	比赛ID	integer
  matchType: string; //	比赛类型 1足球，2篮球,可用值:1,2	string
  matchTime: number; //	比赛时间	integer
  homeTeamName: string; //	主队	string
  homeTeamLogo: string; //	主队LOGO	string
  awayTeamName: string; //	客队	string
  awayTeamLogo: string; //	客队LOGO	string
  sourceUrl: string; //	来源地址	string
  playUrl: string; //	播放地址	string
  openTime: string; //	开播时间	string
  liveStatus: string; //	直播状态,1:未开始，2：直播中，3：已关播,可用值:1,2,3	string
  hotValue: number; //	热度	integer
  onlineUser: number; //	在线人数	integer
}
export interface CountryListData {
  cn: string; //	中文	string
  en: string; //	英文	string
  full: string; //	全称	string
  shortName: string; //	简称	string
  dialingCode: string; //	电话区号	string
  icon: string; //		string
}
