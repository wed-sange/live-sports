import request from "@/utils/request";

/** 聊天记录列表 */
export const getChartList = (data) => {
  return request.post("chat/message/page", data);
};
