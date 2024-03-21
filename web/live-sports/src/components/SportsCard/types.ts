import { sportsData } from './useIndex';
export type SportsCardData = {
  name: string; // 比赛名称
  id: string; // id
  matchId: string; // matchId
  date: string; // 比赛日期 YYYY-MM-DD
  time: string; // 比赛时间 hh:mm
  runTime?: string; // 足球特有的 一场会持续多久 hh:mm
  sportsTypeData: (typeof sportsData)[number]; // 比赛类型
  eventState: string; // 比赛状态
  eventStateText: string; // 比赛状态文本
  eventActionText: string; // 比赛场次文案
  eventStateClass: string; // 比赛状态特殊样式
  homeTeamName: string; // 主场团队名称
  homeTeamNoc: string; // 主场团队ICON
  homeTeamScore: number; // 主场团队比分
  homeTeamHalfScore: number; // 主场团队半场比分
  visitTeamName: string; // 客场团队名称
  visitTeamNoc: string; // 客场团队ICON
  visitTeamScore: number; // 客场团队比分
  visitTeamHalfScore: number; // 客场团队半场比分
  isFollow: boolean; // 是否被关注订阅
  hot?: number; //  热度
  cover?: string; // 赛事封面
  anchorList?: SportsAnchorData[]; // 主播列表
  matchType: number; //足球1 or 篮球2
  matchDate?: string; //显示列表比赛时间
};
export type SportsAnchorData = {
  name: string; // 主播名称
  id: string; // id
  cover: string; // 主播头像
  liveStatus: string; // 直播状态
  liveStatusText: string; // 直播状态
  liveId: number; //直播id
};
export type SportsMiniCardData = {
  anchor: SportsAnchorData;
  card: SportsCardData;
};
