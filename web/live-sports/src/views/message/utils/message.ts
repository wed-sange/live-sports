import dayjs from 'dayjs';
import defaultLogo from '@/assets/img/user/default_anchor.png';
import notifyLogo from '@/assets/img/message/notify.jpg';
import { getChatAccountInfo } from '@/api/message';
import { getMessageType } from './useMessage';
import { MessageCardData, MessageCardType, MessageReceiveData, MessageType } from './types';
/**
 *
 * @param data 消息数据
 * @returns boolean 消息是否属于群聊消息
 */
export const isGroupMessage = (data: MessageReceiveData['data']) => {
  const isGroup = data.chatType === 1;
  return isGroup;
};
/**
 *
 * @param data 消息数据
 * @returns boolean 消息是否属于私聊消息
 */
export const isPersonalMessage = (data: MessageReceiveData['data']) => {
  const isPersonal = data.chatType === 2;
  return isPersonal;
};
/**
 *
 * @param data 消息数据
 * @returns boolean 消息是否属于普通用户发送的
 */
export const isNormalUserSend = (data: MessageReceiveData['data']) => {
  const isNormalUserSend = data.identityType === 0;
  return isNormalUserSend;
};
/**
 *
 * @param data 消息数据
 * @param anchorId 当前窗口主播id
 * @returns boolean 消息是否属于当前活动窗口
 */
export const isCurrentWindow = (data: MessageReceiveData['data'], anchorId: string) => {
  // 判断消息来源是否属于当前窗口
  let isCurrentWindow = false;
  // 消息聊天对象是否是当前主播，是则是当前窗口
  if (data.anchorId === anchorId) {
    isCurrentWindow = true;
  }
  return isCurrentWindow;
};
export const initChatAccountInfo = async (anchorId: string) => {
  try {
    const response = await getChatAccountInfo(anchorId);
    const data = response || {};
    return {
      name: data.nickName,
      id: anchorId,
      cover: data.head || defaultLogo,
    };
  } catch (error) {
    throw error;
  }
};

// 获取原始用户id
export const getOriginUserId = (_formatUserId: string) => {
  return _formatUserId.split('-')[1] || '';
};
// 组装用户id
export const formatUserId = (anchorId: string, userId: string) => {
  return anchorId + '-' + userId;
};
// 获取传入的时间和当前时间的天数差值
export const getTimeWithNowDayDiff = (time: string | number | Date | dayjs.Dayjs | null | undefined) => {
  const now = dayjs();
  const date = dayjs(time);
  return date.diff(now, 'day');
};
// 格式化消息时间
export const formatMessageTime = (time?: string | number, type?: 'now') => {
  if (!time) {
    return '';
  }
  const date = dayjs(Number(time));
  if (type === 'now') {
    const now = dayjs();
    // 日期的天数是否在当前时间之前，是则是昨天的之前的时间
    const isOldDay = date.isBefore(now, 'day');
    const diffHour = date.diff(now, 'hour');
    const diffDay = date.diff(now, 'day');
    if (date.diff(now, 'minute') === 0) {
      // 1分钟内发布的：刚刚
      return '刚刚';
    } else if (diffHour === 0) {
      // 1小时内发布的：×分钟前
      return Math.abs(date.diff(now, 'minute')) + '分钟前';
    } else if (!isOldDay && diffDay === 0) {
      // 超过1小时，仍在当天：xx:xx
      return date.format('HH:mm');
    } else if (diffDay >= -1 && diffHour >= -24 && diffHour < 0) {
      // 跨天，但少于24小时：昨天xx:xx
      // diffDay 因为跨天，所以存在0 和 -1 的情况，只要diffHour是24小时内则展示昨天xx
      return '昨天' + date.format('HH:mm');
    } else {
      // 原：跨天，超过24小时：xxxx-xx-xx xx：xx
      // 修正：跨天，超过24小时：xxxx-xx-xx
      // 跨天的定义：过了凌晨00:00则为跨天;
      // 跨月的定义：过了对应月份最后一天23:59则为跨月；
      // 跨年的定义：过了当前年份12月31日23:59则为跨年
      return date.format('YYYY-MM-DD');
    }
  } else {
    return date.format('YYYY-MM-DD HH:mm:ss');
  }
  // return time ? (type === 'now' ? dayjs(Number(time)).fromNow() : dayjs(Number(time)).format('YYYY-MM-DD HH:mm:ss')) : '';
};
export const buildMessageItem = ({
  id,
  anchorId,
  sendUserId,
  name,
  cover,
  message,
  readDot,
  createTime,
  type,
  msgType,
}: {
  id: string;
  anchorId?: string;
  sendUserId?: string;
  name?: string;
  cover?: string;
  message?: string;
  readDot?: number;
  createTime?: number | string;
  type?: MessageCardType;
  msgType?: string | number;
}): MessageCardData => {
  const isSys = anchorId === '0';
  return {
    id: id,
    anchorId: anchorId || '',
    sendUserId: sendUserId || '',
    name: isSys ? '系统通知' : name || '',
    cover: isSys ? notifyLogo : cover || defaultLogo,
    message: message || '',
    readDot: readDot || 0,
    createTime: formatMessageTime(createTime),
    createTimeText: formatMessageTime(createTime, 'now'),
    type: isSys ? MessageCardType.SYSTEM : type || MessageCardType.MESSAGE,
    msgType: msgType ? (getMessageType(msgType, 'value') as MessageType) : MessageType.text,
  };
};
