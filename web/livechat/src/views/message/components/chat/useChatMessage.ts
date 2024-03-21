import {
  getMessageHistory,
  getPrivateEarliestUnread,
  getPrivateLatestMessage,
  postReadMessage
} from "@/api/message";
import { IVitalId, MessageHistoryItemData } from "@/api/message/model";
import dayjs from "dayjs";
import { cloneDeep, sortBy, uniqBy } from "lodash";
import { ChatCardData } from "./useCommon";
import { getMessageType } from "../../utils/useMessage";
import { MessageType } from "@/views/message/utils/types";

/**
 * 消息观察者
 *
 * @author
 * @class ChatMessageObserver
 */
class ChatMessageObserver {
  // 用户ID
  userId = "";
  // 主播ID
  anchorId = "";
  // 未读消息ID
  unreadMessageIds: string[] = [];
  // 观察者
  protected observers = [];

  // 消息内容 - 对外允许访问
  public messages: ChatCardData[] = [];

  constructor() {}

  /**
   * 添加监听事件
   *
   * @author
   * @param observer
   */
  addListener(observer: ({ messages }: { messages: ChatCardData[] }) => void) {
    this.observers.push(observer);
  }

  /**
   * 通知消息
   *
   * @author
   */
  notify() {
    this.observers.forEach(observer =>
      observer({
        // 防止外面被篡改
        messages: this.messages
      })
    );
  }

  /**
   * 删除所有的监听
   *
   * @author
   */
  removeAllListener() {
    this.observers = [];
  }
}

/**
 * 处理聊天记录
 *
 * @author
 * @export
 * @class ChatMessageRead
 */
class ChatMessageRead extends ChatMessageObserver implements IVitalId {
  constructor() {
    super();
  }

  /**
   * 添加未读消息
   *
   * @author
   * @param messageId
   */
  addUnReadAbleMessage({
    messageIds,
    messages
  }: {
    messageIds?: string[];
    messages?: MessageHistoryItemData[];
  }) {
    const handleRemoveDuplication = (arr: string[]) => [...new Set(arr)];
    // 处理消息ID
    if (messageIds) {
      this.unreadMessageIds = handleRemoveDuplication(
        this.unreadMessageIds.concat(messageIds)
      );
    }
    // 处理消息体
    if (messages) {
      const messageIds = messages
        .filter(item => item.readable === 0)
        .map(item => `${item.id}`);
      this.unreadMessageIds = handleRemoveDuplication(
        this.unreadMessageIds.concat(messageIds)
      );
    }
    // 后续再做时间间隙请求
    this.fetch();
  }

  /**
   * 请求后端
   *
   * @author
   */
  fetch() {
    const messageIds = cloneDeep(this.unreadMessageIds);
    postReadMessage({
      messageIds: messageIds,
      userId: this.userId,
      anchorId: this.anchorId
    }).then(() => {
      // 释放未读变已读的消息ID
      this.unreadMessageIds = this.unreadMessageIds.filter(
        item => !messageIds.some(elm => item === elm)
      );
    });
  }
}

/**
 * 聊天类
 *
 * @author
 * @export
 * @class Chat
 */
class ChatMessage extends ChatMessageRead implements IVitalId {
  // 这个没办法，老代码乱的太严重了，有些方法太多逻辑了，拿进来复用
  extra: any;
  // 是否懒加载已完成
  finishStatus: {
    up: boolean;
    down: boolean;
  } = {
    up: false,
    down: false
  };

  constructor({ userId, anchorId, extra }) {
    super();
    this.userId = userId;
    this.anchorId = anchorId;
    this.extra = extra;
  }

  /**
   * 初始化
   *
   * @author
   */
  async create() {
    this.messages = await this.getInitHistoryMessage();
    this.notify();
  }

  /**
   * 销毁
   *
   * @author
   */
  async destroy() {
    this.removeAllListener();
    this.messages = [];
  }

  /**
   * 后端数据转前端数据。。。
   * copy原来的代码
   *
   * @author
   * @param data
   */
  translate(data: MessageHistoryItemData[]): ChatCardData[] {
    return data.map(cur => {
      const isMe = cur.identityType !== 0;
      const createByTime = cur.createTime
        ? dayjs(parseFloat(cur.createTime))
        : undefined;
      return this.extra.createChatMessage({
        msgId: (cur.id || "") + "",
        message: cur.content,
        msgType: getMessageType(cur.msgType, "value") as MessageType,
        isMe: isMe,
        createTime: createByTime
      });
    });
  }

  /**
   * 获取初始化聊天列表
   * 点击用户后，渲染用户列表
   *
   * @author
   * @static
   */
  async getInitHistoryMessage() {
    const unReadableAndNearbyList = await getPrivateEarliestUnread({
      // 此处不知道为什么后端查的不是前20和后20，不重要，随便传的44吧
      size: 44,
      userId: this.userId,
      anchorId: this.anchorId
    });

    const unReadableIndex = unReadableAndNearbyList?.findIndex(
      item => !item.readable
    );

    // 如果未读数到之前的历史超过了18条，则直接截断后，返回渲染
    let data = [];
    if (unReadableIndex > 18) {
      // 此处最后一条需要设置为已读
      const messages = unReadableAndNearbyList.splice(0, unReadableIndex + 1);
      this.addUnReadAbleMessage({ messages });
      data = messages;
    } else if (unReadableIndex > -1) {
      // 此处需要设置为已读
      if (unReadableAndNearbyList.length < 44) {
        this.finishStatus.down = true;
        this.finishStatus.up = true;
      }
      this.addUnReadAbleMessage({ messages: unReadableAndNearbyList });
      data = unReadableAndNearbyList;
    } else {
      // 这里必然全是已读的
      data = await getMessageHistory({
        offset: "",
        size: 20,
        userId: this.userId,
        anchorId: this.anchorId,
        type: "0", // 消息类型，0-历史消息，1-离线消息
        chatType: "2", // 群聊还是私聊 1-群聊，2-私聊
        searchType: "2" // 查询类型（判断是用户还是直播端，普通用户查看不了被删除的消息）， live-2, APP-3
      });
      // 触底加载将不再执行
      this.finishStatus.down = true;
      if (data.length < 20) {
        this.finishStatus.up = true;
      }
    }

    return this.translate(data);
  }

  /**
   * 懒加载消息
   *
   * @author
   * @param offsetId
   * @return {*} 返回是否完成
   */
  async getLazyMessage(offsetId, inquiryMode) {
    // 这里必然全是已读的
    const data = await getMessageHistory({
      offset: offsetId,
      size: 20,
      userId: this.userId,
      anchorId: this.anchorId,
      type: "0", // 消息类型，0-历史消息，1-离线消息
      chatType: "2", // 群聊还是私聊 1-群聊，2-私聊
      searchType: "2", // 查询类型（判断是用户还是直播端，普通用户查看不了被删除的消息）， live-2, APP-3
      inquiryMode
    });

    if (!data.length || data.length < 20) {
      // 触底或触底加载将不再执行;
      this.finishStatus[inquiryMode] = true;
    }
    if (!data.length) {
      // 如果没有数据就不做任何变化
      return;
    }

    this.addMessage(this.translate(data));
  }

  /**
   * 添加消息
   * TODO：此处数据量大了会不会卡顿，需要优化
   *
   * @author
   * @param messages
   * @param isCover 是否覆盖
   */
  addMessage(messages: ChatCardData[], isCover?: boolean) {
    if (isCover) {
      this.messages = messages;
    } else {
      // 注意有重叠部分
      this.messages = uniqBy(this.messages.concat(messages), "id");
      // 前端排序，保证顺序正确
      this.messages = sortBy(this.messages, "createByTime");
    }
    this.notify();
  }

  /**
   * 获取最新的消息
   *
   * @author
   */
  async getNewestMessage() {
    // 这里包含了未读的消息数
    const messages = await getPrivateLatestMessage({
      size: 20,
      userId: this.userId,
      anchorId: this.anchorId
    });
    // 后端会全部已读，不需要前端处理
    // this.addUnReadAbleMessage({ messages });
    // 可以继续往上触底更新
    this.finishStatus.up = false;
    this.finishStatus.down = true;
    this.addMessage(this.translate(messages), true);
  }
}

export type IChatMessage = ChatMessage;

// 此处先用单例，后续要做会话缓存可以new多个
let chatInstance;
// 此处只能写成hooks，因为外面有很多hook方法需要内部复用。。。
export const useChatMessage = ({
  userId,
  anchorId,
  extra
}: IVitalId & { extra: any }): { chat: ChatMessage } => {
  if (
    !chatInstance ||
    chatInstance.userId !== userId ||
    chatInstance.anchorId !== anchorId
  ) {
    // 实际这里重新new就会销毁，只是需要处理监听事件或一些内存泄露
    chatInstance?.destroy();
    chatInstance = new ChatMessage({ userId, anchorId, extra });
  }

  return { chat: chatInstance };
};
