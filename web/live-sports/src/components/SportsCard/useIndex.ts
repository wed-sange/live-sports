import { SubscribeData, LiveHistoryData } from '@/api/user/model';
import { SportsCardData, SportsMiniCardData } from './types';
import defaultLogoSrc from '@/assets/img/user/default_anchor.png';
// import defaultTeamLogoSrc from '@/assets/img/sportsCard/default_logo.png';
import defaultTeamBasketballLogoSrc from '@/assets/img/sportsCard/icon_basketball_team.png';
import defaultTeamFootballLogoSrc from '@/assets/img/sportsCard/icon_football_team.png';
import dayjs from 'dayjs';
export const sportsData: {
  icon: 'basketball' | 'football';
  value: string;
  name: string;
}[] = [
  {
    icon: 'basketball',
    value: '2',
    name: '篮球',
  },
  {
    icon: 'football',
    value: '1',
    name: '足球',
  },
];
export const getCurrentSportType = (type: string): (typeof sportsData)[number] => {
  const index = sportsData.findIndex((cur) => cur.value === type);
  if (index === -1) {
    return {} as (typeof sportsData)[number];
  } else {
    return sportsData[index];
  }
};
type EventStateItemData = {
  value: string;
  name: string;
  class?: string;
};
export const eventStateData: {
  [key: string]: EventStateItemData[];
} = {
  basketball: [
    { value: '0', name: '比赛异常' },
    { value: '1', name: '未开赛' },
    { value: '2', name: '第一节', class: 'run' },
    { value: '3', name: '第一节完', class: 'run' },
    { value: '4', name: '第二节', class: 'run' },
    { value: '5', name: '第二节完', class: 'run' },
    { value: '6', name: '第三节', class: 'run' },
    { value: '7', name: '第三节完', class: 'run' },
    { value: '8', name: '第四节', class: 'run' },
    { value: '9', name: '加时', class: 'run' },
    { value: '10', name: '已完场', class: 'end' },
    { value: '11', name: '中断' },
    { value: '12', name: '取消' },
    { value: '13', name: '延期' },
    { value: '14', name: '腰斩' },
    { value: '15', name: '待定' },
  ],
  football: [
    { value: '0', name: '比赛异常' },
    { value: '1', name: '未开赛' },
    // { value: '2', name: '上半场', class: 'run' },
    { value: '2', name: '', class: 'run' },
    // { value: '3', name: '中场', class: 'run' },
    { value: '3', name: '中场', class: 'run' },
    // { value: '4', name: '下半场', class: 'run' },
    { value: '4', name: '', class: 'run' },
    { value: '5', name: '加时赛', class: 'run' },
    { value: '6', name: '加时赛(弃用)', class: 'run' },
    { value: '7', name: '点球决战', class: 'run' },
    { value: '8', name: '已完场', class: 'end' },
    { value: '9', name: '推迟' },
    { value: '10', name: '中断' },
    { value: '11', name: '腰斩' },
    { value: '12', name: '取消' },
    { value: '13', name: '待定' },
  ],
};
export const getCurrentEventState = (type: string, status: string): EventStateItemData => {
  const statusList = eventStateData[type];
  if (!statusList) {
    return {} as EventStateItemData;
  }
  const index = statusList.findIndex((cur) => cur.value === status);
  if (index === -1) {
    return {} as EventStateItemData;
  } else {
    return statusList[index];
  }
};
export const teamData = {
  china: {
    icon: 'china',
    name: '中国',
  },
  usa: {
    icon: 'usa',
    name: '美国',
  },
};
export const liveStatusText = {
  '1': '暂未开播',
  '2': '正在开播',
  '3': '暂未开播', // 观看历史只有正在直播和未开始，已完播状态也属于未开始状态
};
const getDefaultTeamLogo = (type: (typeof sportsData)[number]['icon']) => {
  return type === 'basketball' ? defaultTeamBasketballLogoSrc : defaultTeamFootballLogoSrc;
};
const weeks = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
export const formatCardData = (data: SubscribeData[]) => {
  let renderData: SportsCardData[] = [];
  renderData = data.map((cur) => {
    const currentSportType = getCurrentSportType(cur.matchType + '');
    const currentEventState = getCurrentEventState(currentSportType.icon, cur.status + '');
    const defaultTeamLogo = getDefaultTeamLogo(currentSportType.icon);
    const anchorList: SportsCardData['anchorList'] = (cur.anchorList || []).map((curAnchor) => {
      const liveStatus = curAnchor.liveId ? '2' : '1';
      return {
        name: curAnchor.nickName, // 主播名称
        id: (curAnchor.userId || '') + '', // id
        cover: curAnchor.userLogo || defaultLogoSrc, // 主播头像
        liveStatus: liveStatus, // 直播状态
        liveStatusText: liveStatusText[liveStatus] || '', // 直播状态
        matchType: cur.matchType,
        matchId: cur.matchId,
        liveId: curAnchor.liveId,
      };
    });
    const matchDate = dayjs(Number(cur.matchTime)).format('YYYY-MM-DD') + ' ' + weeks[dayjs(Number(cur.matchTime)).day()];
    const item: SportsCardData = {
      name: cur.competitionName || '', // 比赛名称
      id: (cur.competitionId || '') + '', // id
      matchId: (cur.matchId || '') + '', // matchId
      date: cur.matchTime ? dayjs(Number(cur.matchTime)).format('YYYY-MM-DD') : '', // 比赛日期 YYYY-MM-DD
      time: cur.matchTime ? dayjs(Number(cur.matchTime)).format('HH:mm') : '', // HH:mm
      runTime: (cur.runTime || '') + '', // 足球特有的 一场会持续多久 HH:mm
      sportsTypeData: currentSportType, // 比赛类型
      eventState: currentEventState.value || '', // 比赛状态
      eventStateText: currentEventState.name || '', // 比赛状态文本
      eventStateClass: currentEventState.class || '', // 比赛状态特殊样式
      eventActionText: '半场', // 比赛场次文案
      homeTeamName: cur.homeName || '', // 主场团队名称
      homeTeamNoc: cur.homeLogo || defaultTeamLogo, // 主场团队ICON
      homeTeamScore: Number(cur.homeScore || ''), // 主场团队比分
      homeTeamHalfScore: Number(cur.homeHalfScore || ''), // 主场团队半场比分
      visitTeamName: cur.awayName || '', // 客场团队名称
      visitTeamNoc: cur.awayLogo || defaultTeamLogo, // 客场团队ICON
      visitTeamScore: Number(cur.awayScore || ''), // 客场团队比分
      visitTeamHalfScore: Number(cur.awayHalfScore || ''), // 客场团队半场比分
      isFollow: !!cur.focus, // 是否被关注订阅
      anchorList,
      matchType: Number(cur.matchType || ''),
      matchDate,
    };
    return item;
  });
  return renderData;
};

export const formatMiniCardData = (data: LiveHistoryData[]) => {
  let renderData: SportsMiniCardData[] = [];
  renderData = data.map((cur) => {
    const currentSportType = getCurrentSportType(cur.matchType + '');
    const defaultTeamLogo = getDefaultTeamLogo(currentSportType.icon);
    const item: SportsMiniCardData = {
      anchor: {
        name: cur.nickName, // 主播名称
        id: (cur.userId || '') + '', // id
        cover: cur.userLogo || defaultLogoSrc, // 主播头像
        liveStatus: (cur.liveStatus || '') + '', // 直播状态
        liveStatusText: liveStatusText[cur.liveStatus] || '', // 直播状态
        liveId: cur.id, //直播id
      },
      card: {
        name: cur.competitionName || '', // 比赛名称
        id: (cur.competitionId || '') + '', // id
        matchId: (cur.matchId || '') + '', // matchId
        date: cur.matchTime ? dayjs(Number(cur.matchTime)).format('YYYY-MM-DD') : '', // 比赛日期 YYYY-MM-DD
        time: cur.matchTime ? dayjs(Number(cur.matchTime)).format('HH:mm') : '', // HH:mm
        runTime: '', // 足球特有的 一场会持续多久 HH:mm
        sportsTypeData: currentSportType, // 比赛类型
        eventState: '', // 比赛状态
        eventStateText: '', // 比赛状态文本
        eventStateClass: '', // 比赛状态特殊样式
        eventActionText: '半场', // 比赛场次文案
        homeTeamName: cur.homeTeamName || '', // 主场团队名称
        homeTeamNoc: cur.homeTeamLogo || defaultTeamLogo, // 主场团队ICON
        homeTeamScore: 0, // 主场团队比分
        homeTeamHalfScore: 0, // 主场团队半场比分
        visitTeamName: cur.awayTeamName || '', // 客场团队名称
        visitTeamNoc: cur.awayTeamLogo || defaultTeamLogo, // 客场团队ICON
        visitTeamScore: 0, // 客场团队比分
        visitTeamHalfScore: 0, // 客场团队半场比分
        hot: Number(cur.hotValue || ''), //  热度
        cover: cur.titlePage || '', // 赛事封面
        isFollow: false, // 是否被关注订阅
        matchType: Number(cur.matchType || ''),
        matchDate: '',
      },
    };
    return item;
  });
  return renderData;
};
