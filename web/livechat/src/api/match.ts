import { http } from "@/utils/http";

/** 比赛列表 */
export const matchList = (data?: object) => {
  return http.post("/match/list", { data });
};
/** 赛事列表 */
export const competitionList = (data?: object) => {
  return http.post("/match/competition/list", { data });
};
/** 开播 */
export const liveOpen = (data?: object) => {
  return http.post("/live/open", { data });
};
/** 关播 */
export const liveClose = (data?: object) => {
  return http.post("/live/close", { data });
};
/** 更新直播地址 */
export const liveUpdateAddress = (data?: object) => {
  return http.post("/live/update/address", { data });
};
/** 赛事列表 */
export const matchCompetitions = (data?: object) => {
  return http.post("/match/competition/list", { data });
};
/** 热门赛事列表 */
export const hotList = (data?: object) => {
  return http.post("/list", { data });
};
