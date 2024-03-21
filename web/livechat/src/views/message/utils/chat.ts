import dayjs from "dayjs";
import defaultUserLogo from "@/assets/message/default_user.png";
import defaultAnchorLogo from "@/assets/message/default_anchor.png";
import { MessageType } from "./types";
export type ChatCardData = {
  id: string;
  type: MessageType;
  message: string;
  createByTime: string;
  accountId: string;
  accountName: string;
  accountAvatar: string;
  isMe: boolean;
  tip: string;
  level: string;
  // 消息状态 如果没有status，则默认为终止状态
  // '发送中' | '上传中' | '发送失败' | '状态终止'
  status?: "sending" | "uploading" | "failed" | "end";
  // 当type是需要上传的文件类型时会存在此字段，用于操作文件上传
  file?: File;
};
export const buildChatMessage = ({
  msgId,
  message,
  msgType,
  createTime,
  isMe,
  accountId,
  accountName,
  accountAvatar,
  tip,
  level,
  status,
  file
}: {
  msgId: string;
  message: string;
  msgType: MessageType;
  createTime?: dayjs.Dayjs;
  isMe: boolean;
  accountId?: string;
  accountName?: string;
  accountAvatar?: string;
  tip?: string;
  level?: string;
  status?: ChatCardData["status"];
  file?: File;
}): ChatCardData => {
  return {
    id: msgId,
    type: msgType,
    message: message,
    createByTime: createTime ? createTime.format("YYYY-MM-DD HH:mm:ss") : "",
    accountId: accountId || "",
    accountName: accountName || "",
    accountAvatar:
      accountAvatar || (isMe ? defaultUserLogo : defaultAnchorLogo),
    isMe,
    level: level || "",
    tip: tip || "",
    status: status,
    file: file
  };
};
const levelNames = {
  "1": "黄铜",
  "2": "白银",
  "3": "黄金",
  "4": "铂金",
  "5": "钻石",
  "6": "星耀"
};
export const formatLevelName = (level: string | number): string => {
  return levelNames[level + ""] || "未知";
};

export const useShowDateTip = () => {
  const tipCache = new Map<string, any>();
  let suffix = "";
  const updateTipCacheSuffix = (val: string) => {
    suffix = val;
  };
  const getShowDateTip = (createTime?: dayjs.Dayjs) => {
    let tip = "";
    if (createTime) {
      const createByDay = createTime.format("YYYY-MM-DD HH");
      const key = suffix + createByDay;
      const createByTime = createTime.format("YYYY-MM-DD HH:mm:ss");
      if (!tipCache.has(key)) {
        tipCache.set(key, true);
        tip = createByTime;
      }
    }
    return tip;
  };
  const clearTipCache = () => {
    tipCache.clear();
    updateTipCacheSuffix("");
  };
  return {
    updateTipCacheSuffix,
    clearTipCache,
    getShowDateTip
  };
};
