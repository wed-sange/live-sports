export interface BannerInfo {
  id: string; //	广告id
  name: sting; //	广告名称
  text: sting; //	文字
  imgUrl: sting; //	图片地址
  targetUrl: sting; //	跳转地址
}
export interface MatchInfo {
  matchType: string; //	比赛类型：1：足球；2：篮球,可用值:1,2
  matchTime: sting; //	比赛时间
  homeScore: sting; //	主队比分
  homeHalfScore: sting; //	主队半场比分
  awayName: sting; //	客队名称
  focus: boolean; //是否关注
  status: number; //篮球状态:0 比赛异常，说明：暂未判断具体原因的异常比赛，可能但不限于：腰斩、取消等等，建议隐藏处理;1 未开赛;2 第一节;3 第一节完;4 第二节;5 第二节完;6 第三节;7 第三节完;8 第四节;9 加时;10 已完场;11 中断;12 取消;13 延期;14 腰斩;15 待定;足球状态码:0 比赛异常，说明：暂未判断具体原因的异常比赛，可能但不限于：腰斩、取消等等，建议隐藏处理;1 未开赛;2 上半场;3 中场;4 下半场;5 加时赛;6 加时赛(弃用);7 点球决战;8 完场;9 推迟;10 中断;11 腰斩;12 取消;13 待定
}
export interface LiveInfo {
  matchId: string; //	比赛id
  competitionName: sting; //	赛事名称
  compositionId: sting; //	赛事id
  nickName: sting; //	直播昵称
  hotValue: sting; //	总热度
  titlePage: sting; //	封面
  matchType: number; //	比赛类型 1足球，2篮球,可用值:1,2
  homeTeamName: sting; //	主队
  homeTeamLogo: sting; //	主队logo
  awayTeamName: sting; //	客队
  awayTeamLogo: sting; //	客队logo
  sourceUrl: sting; //	来源地址
  playUrl: sting; //	播放地址
  liveStatus: number; //直播状态,可用值:1,2,3
}
export interface NewsInfo {
  id: string; //	新闻id
  title: sting; // 标题
  pic: sting; //	图片地址
  tournament: sting; //	联赛名称
  content: sting; //	新闻内容
  sourceUrl: sting; //	来源地址
  publishTime: sting; //	发布时间
}
