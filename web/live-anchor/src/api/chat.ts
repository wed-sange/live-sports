import { http } from "@/utils/http";

/** 聊天记录列表 */
export const getChartList = (data?: object) => {
  return http.post("chat/message/page", { data });
};
