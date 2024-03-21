import request from "@/utils/request";

/** 热门比赛列表 */
export const matchHotList = (data) => {
  return request.post("match/hot/list", data);
};

/** 搜索联赛 */
export const searchMatch = (data) => {
  return request.post("match/hot/search", data);
};
/** 移除赛事 */
export const hotRemove = (id) => {
  return request.post(`match/hot/remove/${id}`);
};
/** 添加赛事 */
export const hotAdd = (data) => {
  return request.post("match/hot/add", data);
};
