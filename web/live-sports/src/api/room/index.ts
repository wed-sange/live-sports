import { post, get } from '@/utils/useAxiosApi';
import qs from 'qs';

const getLivingInfo = async (data: any) =>
  post<{}>({
    url: 'home/living/info',
    data: qs.stringify(data),
    headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
  });

const getLiveUserInfo = async (id) =>
  get<{}>({
    url: `live/detail/live/user/info/${id}`,
  });

const addLiveHistory = async (id) =>
  get<{}>({
    url: `user/live/history/add/${id}`,
  });

const onAttentionAnchor = async (id: any) =>
  post<{}>({
    url: `follow/anchor/follow/${id}`,
  });
const onUnAttentionAnchor = async (id: any) =>
  post<{}>({
    url: `follow/anchor/unfollow/${id}`,
  });

const getMsgHistory = async (data: any) =>
  post<{}>({
    url: 'msg/history',
    data,
  });

const getMatchDetail = async (matchId, matchType) => post<{}>({ url: `schedule/match/${matchId}/${matchType}` });
const getMatchAnchors = async (matchId, matchType) => post<{}>({ url: `schedule/match/anchorList/${matchId}/${matchType}` });

const getFootballLineup = async (matchId) => get<{}>({ url: `live/detail/football/lineup/${matchId}` });

const getBasketballLineup = async (matchId) => get<{}>({ url: `live/detail/basketball/match/lineup/${matchId}` });

const getFootOdds = async (matchId) => get<{}>({ url: `live/detail/football/odds/${matchId}` });

const getMatchBasketStats = async (matchId) => get<{}>({ url: `live/detail/basketball/match/stats/${matchId}` });

const getMatchFootStats = async (matchId) => get<{}>({ url: `live/detail/football/stats/${matchId}` });

const getMatchFootText = async (matchId) => get<{}>({ url: `live/detail/football/text/live/${matchId}` });

const getMatchBasketballScore = async (matchId) => get<{}>({ url: `live/detail/basketball/match/score/${matchId}` });

const getMatchFootIncidents = async (matchId) => get<{}>({ url: `live/detail/football/incidents/${matchId}` });

const getAdvertising = async () => get<{}>({ url: '/advertising/text/scroll/list' });

const getAdvertisingLive = async () => get<{}>({ url: '/advertising/live/show' });

const postShareLive = async (id: any) =>
  post<{}>({
    url: `/follow/live/share/${id}`,
  });
const getAnchorControlUserInfo = async (id) => get<{}>({ url: `user/getAnchorControlUserInfo/${id}` });

export default {
  getMatchDetail,
  getFootballLineup,
  getBasketballLineup,
  getFootOdds,
  getMatchBasketStats,
  getMatchFootStats,
  getMatchFootText,
  getMatchBasketballScore,
  getMatchFootIncidents,
  getLivingInfo,
  getLiveUserInfo,
  onAttentionAnchor,
  onUnAttentionAnchor,
  getAdvertising,
  getAdvertisingLive,
  getMsgHistory,
  addLiveHistory,
  getMatchAnchors,
  postShareLive,
  getAnchorControlUserInfo,
};
