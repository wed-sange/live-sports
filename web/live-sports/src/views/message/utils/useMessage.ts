import { showLoadingToast, showToast } from 'vant';
import { createLoading, closeLoading } from '@/components/SysLoading';
import mitt from 'mitt';
import JSONbig from 'json-bigint';

import { clearAllReadDot, clearReadDot } from '@/api/message';
import {
  MessageType,
  MessageChatServerOption,
  MessageChatServerUserInfo,
  MessageCommand,
  MessageResponse,
  MessageReceiveData,
  MessageReadDotAllData,
  MessageSendData,
  MessageSendReadData,
  MessageAnchorReadMessageData,
  MessageBannedData,
  MessageFeedbackData,
  MessageLiveStartNotifyData,
} from './types';
import { EventCache, createResponseTimeout } from './eventCache';
import { useWebSocket } from './useWebSocket';
import { getCustomerId } from '@/utils/common';
const devMode = import.meta.env.MODE;
const customerId = getCustomerId();
//******
// const WS_SERVER = "ws://192.168.102.6:8888";
// const WS_SERVER = 'ws://******:8888';
const WS_SERVER = devMode === 'development' ? 'ws://******:6006/ws-sports-chat' : 'wss://******/ws-sports-chat';
export const getMessageType = (value: string | number, returnValue?: 'key' | 'value'): MessageType | number | null => {
  const _returnValue = returnValue || 'key';
  const current = Object.keys(MessageType).find((key) => MessageType[key] === value);
  if (!current) {
    return null;
  } else {
    return _returnValue === 'key' ? current : MessageType[current];
  }
};
class MessageChatServer {
  options: MessageChatServerOption = {
    on: {
      init: () => {},
      login: () => {},
      message: () => {},
    },
  };
  promiseCache = new EventCache();
  wrapResolve = createResponseTimeout({ timeout: 1000 });
  websocketInstance: ReturnType<typeof useWebSocket> | null = null;
  // 当前ws连接状态
  isConnect = false;
  // 当前用户是否登录
  isLogin = false;
  // 缓存的用户信息
  userInfo: MessageChatServerUserInfo = {
    userId: '',
    token: '',
    groupId: '',
  };
  constructor(options: MessageChatServerOption) {
    this.options = options;
    // 初始化ws连接参数和事件监听
    this.websocketInstance = useWebSocket({
      url: WS_SERVER,
      onOpen: () => {
        console.log(new Date(), 'WebSocket已连接: ', WS_SERVER);
        this.onWsOpen();
      },
      onMessage: (e) => {
        this.onWsMessage(e);
      },
      onErrorReconnect: (count, maxCount) => {
        console.log('onErrorReconnect', count, maxCount);
        if (!this.reconnectLoading) {
          // 当count等于maxCount代表重连开始
          // 触发重连相关处理
          this.onWsErrorReconnect();
        }
        if (count <= 0) {
          // 当count小于等于0代表重连结束
          this.reconnectLoading && this.reconnectLoading.close();
          this.reconnectLoading = null;
          this.sendPingRepeatTimer && clearTimeout(this.sendPingRepeatTimer);
          this.sendPingRepeatTimer = null;
          showToast({
            className: 'custom-toast',
            message: '连接失败，请重试',
          });
        }
      },
    });
  }

  // 监听ws连接成功
  async onWsOpen() {
    this.isConnect = true;
    this.options.on.init && this.options.on.init(this.isConnect);
    this.promiseCache.execute('create-socket', true);
    this.reconnectState = 'end';
    // 如果重连成功且存在这些信息，则自动登录和加入房组
    if (this.reconnectLoading) {
      if (this.userInfo.token) {
        await this.loginWs({
          userId: this.userInfo.userId,
          token: this.userInfo.token,
        });
      }
      this.joinGroup(this.userInfo.groupId || '');
      if (this.reconnectState === 'end') {
        this.reconnectState = 'none';
        this.reconnectLoading.close();
        this.reconnectLoading = null;
      }
    }
    this.sendPing();
  }
  onWsMessage(e: MessageEvent<any>) {
    const _data = e && e.data;
    let data: any = {};
    try {
      data = JSONbig.parse(_data);
    } catch (error) {
      data = _data;
    }
    if (data.command === MessageCommand.pingResponse) {
      this.promiseCache.execute('ping' + MessageCommand.ping, true);
      return;
    }
    const handlePromise = {
      [MessageCommand.logoutResponse]: 'logout' + MessageCommand.logout,
      [MessageCommand.loginResponse]: 'login' + MessageCommand.login,
      [MessageCommand.sendResponse]: 'send' + MessageCommand.send,
      [MessageCommand.joinGroupResponse]: 'joinGroup' + MessageCommand.joinGroup,
      [MessageCommand.leaveGroupResponse]: 'leaveGroup' + MessageCommand.leaveGroup,
    };
    for (const key in handlePromise) {
      if (data.command === Number(key)) {
        this.promiseCache.execute(handlePromise[key], {
          success: data.success === 0,
          data,
        });
        break;
      }
    }
    this.options.on.message(data);
  }
  // 连接ws，该函数可以被重复调用，当重复调用时内部会存在tag
  createSocket() {
    return new Promise<MessageResponse>((resolve) => {
      this.websocketInstance!.createWebsocket();
      this.promiseCache.remove('create-socket');
      this.promiseCache.once('create-socket', resolve);
    });
  }
  closeSocket() {
    this.websocketInstance?.closeWebSocket();
  }
  sendWsPush(data: any) {
    const isSend = this.websocketInstance!.sendMessage(data);
    if (!isSend) this.reconnectWebsocket();
  }
  // 心跳检测
  sendPingRepeatTimer: number | null = null;
  sendPing() {
    return new Promise<MessageResponse<any>>((resolve) => {
      this.sendPingRepeatTimer && clearTimeout(this.sendPingRepeatTimer);
      this.sendPingRepeatTimer = setTimeout(() => {
        this.sendWsPush({
          cmd: MessageCommand.ping,
        });
        // 当心跳超过一秒未处理，则认为断开连接，触发重连
        this.promiseCache.once(
          'ping' + MessageCommand.ping,
          this.wrapResolve.create(
            (data) => {
              this.sendPing();
              this.reconnectLoading && this.reconnectLoading.close();
              this.reconnectLoading = null;
              resolve(data);
            },
            () => {
              this.reconnectWebsocket();
              resolve({
                success: false,
                data: '心跳发送失败',
              });
            },
            1000,
          ),
        );
      }, 5000) as unknown as number;
    });
  }
  // 重连相关逻辑
  reconnectState: 'none' | 'run' | 'end' = 'none';
  reconnectLoading: ReturnType<typeof showLoadingToast> | null = null;
  async onWsErrorReconnect() {
    this.reconnectState = 'run';
    if (this.reconnectLoading) this.reconnectLoading.close();
    this.reconnectLoading = {
      close: () => {},
    } as ReturnType<typeof showLoadingToast>;
    // this.reconnectLoading = showLoadingToast({
    //   duration: 0,
    //   forbidClick: true,
    //   className: 'custom-loading-toast',
    //   message: '重连中，请稍候',
    // });
    // 重置关键状态

    this.sendPingRepeatTimer && clearTimeout(this.sendPingRepeatTimer);
    this.sendPingRepeatTimer = null;
    this.isConnect = false;
    this.isLogin = false;
    this.promiseCache.clear();
    // this.wrapResolve.clear();
  }
  // 业务重连，与ws无强关联
  wsBusConnectCountTimer: number | null = null;
  reconnectWebsocket() {
    this.websocketInstance?.getSocketInstance()?.close();
    this.wsBusConnectCountTimer && clearTimeout(this.wsBusConnectCountTimer);
    this.wsBusConnectCountTimer = setTimeout(() => {
      this.wsBusConnectCountTimer && clearTimeout(this.wsBusConnectCountTimer);
      this.onWsErrorReconnect();
      this.createSocket();
    }, 1000) as unknown as number;
  }
  loginWs(userOption: MessageChatServerUserInfo) {
    return new Promise<MessageResponse>((resolve) => {
      if (!this.isConnect || this.isLogin) {
        resolve({
          success: false,
          data: '用户已登录或websocket未连接',
        });
        return;
      }
      if (this.promiseCache.has('login' + MessageCommand.login)) return;
      this.userInfo.userId = userOption.userId;
      this.userInfo.token = userOption.token;
      this.sendWsPush({
        cmd: MessageCommand.login,
        userId: this.userInfo.userId,
        token: this.userInfo.token,
        loginType: 3,
        source: 3, // 登陆来源 1IOS 2android 3H5 4小程序
      });
      this.promiseCache.once(
        'login' + MessageCommand.login,
        this.wrapResolve.create(
          (arg: MessageResponse) => {
            // 登录成功重置重连次数
            this.isLogin = true;
            this.options.on.login && this.options.on.login(this.isLogin);
            resolve(arg);
          },
          () => {
            // this.reconnectWebsocket();
            console.log('login-failed');
            resolve({
              success: false,
              data: '登录失败',
            });
          },
          1000,
        ),
      );
    });
  }
  logoutWs() {
    return new Promise<MessageResponse>((resolve, reject) => {
      if (!this.isConnect || !this.isLogin) {
        resolve({
          success: false,
          data: '用户未登录或websocket未连接',
        });
        return;
      }
      this.sendWsPush({
        cmd: MessageCommand.logout,
        userId: this.userInfo.userId,
        loginType: 3,
        // source: 3, // 登陆来源 1IOS 2android 3H5 4小程序
      });
      this.resetUserInfo();
      this.promiseCache.once('logout' + MessageCommand.logout, this.wrapResolve.create(resolve, reject));
    });
  }
  resetUserInfo() {
    this.isLogin = false;
    this.userInfo.userId = '';
    this.userInfo.token = '';
    this.userInfo.groupId = '';
  }
  joinGroup(id: string) {
    return new Promise<MessageResponse>((resolve, reject) => {
      if (!id) {
        resolve({
          success: false,
          data: '请传递房组ID',
        });
        return;
      }
      if (!this.isConnect) {
        resolve({
          success: false,
          data: 'websocket未连接',
        });
        return;
      }
      this.sendWsPush({
        cmd: MessageCommand.joinGroup,
        userId: this.userInfo.userId || customerId,
        groupId: id,
        loginType: 3,
      });
      this.userInfo.groupId = id;
      this.promiseCache.once('joinGroup' + MessageCommand.joinGroup, this.wrapResolve.create(resolve, reject));
    });
  }
  leaveGroup(id: string) {
    return new Promise<MessageResponse>((resolve, reject) => {
      if (!this.isConnect) {
        resolve({
          success: false,
          data: 'websocket未连接',
        });
        return;
      }
      this.sendWsPush({
        cmd: MessageCommand.leaveGroup,
        userId: this.userInfo.userId || customerId,
        groupId: id,
        loginType: 3,
      });
      this.promiseCache.once('leaveGroup' + MessageCommand.leaveGroup, this.wrapResolve.create(resolve, reject));
    });
  }
  sendMessage<T = any>(data: MessageSendData) {
    return new Promise<MessageResponse<T>>((resolve, reject) => {
      this.sendWsPush(Object.assign({ identityType: 0 }, data));
      this.promiseCache.once('send' + MessageCommand.send, this.wrapResolve.create(resolve, reject));
    });
  }
  sendReadTag(data: MessageSendReadData) {
    this.sendWsPush(data);
  }
  destroy() {
    this.isConnect = false;
    this.logoutWs();
    this.closeSocket();
    this.promiseCache.clear();
    this.wrapResolve.clear();
    this.sendPingRepeatTimer && clearTimeout(this.sendPingRepeatTimer);
    this.sendPingRepeatTimer = null;

    this.wsBusConnectCountTimer && clearTimeout(this.wsBusConnectCountTimer);
    this.wsBusConnectCountTimer = null;
    this.reconnectState = 'none';
    this.reconnectLoading && this.reconnectLoading.close();
    this.reconnectLoading = null;
    this.resetUserInfo();
  }
}
type ChatMittEvents = {
  onWebsocketConnected: boolean;
  onWebsocketUserLogin: boolean;
  messageAll: any;
  message: MessageReceiveData;
  readDotAll: MessageReadDotAllData;
  anchorReadMessage: MessageAnchorReadMessageData;
  banned: MessageBannedData;
  unBanned: MessageBannedData;
  liveBanned: MessageBannedData;
  liveUnBanned: MessageBannedData;
  feedback: MessageFeedbackData;
  liveStartNotify: MessageLiveStartNotifyData;
  clearDot?: string;
};
export const useMessage = () => {
  const chatEmitter = mitt<ChatMittEvents>();
  const messageChatServer = new MessageChatServer({
    on: {
      init: (isConnect) => {
        chatEmitter.emit('onWebsocketConnected', isConnect);
      },
      login: (isLogin) => {
        chatEmitter.emit('onWebsocketUserLogin', isLogin);
      },
      message: (data: any) => {
        const { command } = data;
        chatEmitter.emit('messageAll', data);
        if (command === MessageCommand.receive) {
          chatEmitter.emit('message', data as MessageReceiveData);
        } else if (command === MessageCommand.readDotAll) {
          chatEmitter.emit('readDotAll', data as MessageReadDotAllData);
        } else if (command === MessageCommand.anchorReadMessage) {
          chatEmitter.emit('anchorReadMessage', data as MessageAnchorReadMessageData);
        } else if (command === MessageCommand.banned) {
          if (data.data && data.data.bizId) {
            // 有bizId 则是直播间禁言
            chatEmitter.emit('liveBanned', data as MessageBannedData);
          } else {
            //系统禁言
            chatEmitter.emit('banned', data as MessageBannedData);
          }
        } else if (command === MessageCommand.unBanned) {
          if (data.data && data.data.bizId) {
            // 有bizId 则是直播间解除禁言
            chatEmitter.emit('liveUnBanned', data as MessageBannedData);
          } else {
            //系统解除禁言
            chatEmitter.emit('unBanned', data as MessageBannedData);
          }
        } else if (command === MessageCommand.feedback) {
          chatEmitter.emit('feedback', data as MessageFeedbackData);
        } else if (command === MessageCommand.liveStartNotify) {
          chatEmitter.emit('liveStartNotify', data as MessageLiveStartNotifyData);
        }
      },
    },
  });
  const sendMessage = async <T = any>(data: MessageSendData) => {
    return messageChatServer.sendMessage<T>(data);
  };
  const sendReadTag = async (data: MessageSendReadData) => {
    return messageChatServer.sendReadTag(data);
  };
  const joinGroup = async (id: string) => {
    return messageChatServer.joinGroup(id);
  };
  const leaveGroup = async (id: string) => {
    return messageChatServer.leaveGroup(id);
  };
  const userLogin = async (userInfo: MessageChatServerUserInfo) => {
    return messageChatServer.loginWs(userInfo);
  };
  const userLogout = async () => {
    clearDot();
    return messageChatServer.logoutWs();
  };
  const createSocket = async () => {
    return messageChatServer.createSocket();
  };
  const isConnect = () => {
    return messageChatServer.isConnect;
  };
  const isLogin = () => {
    return messageChatServer.isLogin;
  };
  const clearDot = async (id?: string, _closeLoading?: boolean) => {
    // const toast = closeLoading
    //   ? null
    //   : showLoadingToast({
    //       duration: 0,
    //       forbidClick: true,
    //       message: '加载中...',
    //     });
    _closeLoading ? null : createLoading();
    try {
      // id === '0' 代表系统消息，系统消息不调用接口
      if (id !== '0') {
        if (id) {
          await clearReadDot(id);
        } else {
          await clearAllReadDot();
        }
      }
      chatEmitter.emit('clearDot', id);
    } catch (e) {
      console.log('error', e);
    } finally {
      _closeLoading ? null : closeLoading();
    }
  };
  const destroy = () => {
    chatEmitter.all.clear();
    messageChatServer.destroy();
  };
  return {
    createSocket,
    destroy,
    chatEmitter,
    sendMessage,
    sendReadTag,
    joinGroup,
    leaveGroup,
    isConnect,
    isLogin,
    clearDot,
    userLogin,
    userLogout,
  };
};

// 主播端登录和发测试消息
// {"cmd":5,"userId":"1681545875324743680","token":"LK4ztjh3vORvPxQFqxTKiATc962EwTneFCEes6z6DG7gTi2aoqVNEFmjLReEn9xK","loginType":2}
// {
//     "identityType": 1,
//     "from": "1681545875324743679",
//     "sendId": "",
//     "cmd": 11,
//     "anchorId": "1681545875324743679",
//     "toAvatar": "http://s1dnvet07.bkt.clouddn.com/user-icon/20230925/IaB5oSYXKkVhBs7t9wTvC.png",
//     "toNickName": "贫僧洗头用飘柔",
//     "fromAvatar": "tttt",
//     "fromNickName": "初始化昵称",
//     "to": "1693864323637190656",
//     "level": "1",
//     "msgType": 0,
//     "chatType": "2",
//     "content": "俺单位"
// }
