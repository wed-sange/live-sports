import JSONBig from "json-bigint";
type UseWebSocketOption = {
  url: string;
  onOpen?: (e: Event) => void;
  onMessage?: (e: MessageEvent<any>) => void;
  onError?: (e: Event) => void;
  onClose?: (e: CloseEvent) => void;
  onReconnect?: (count: number, maxCount: number) => void;
};
const MAX_RECONNECT_COUNT = 10;
export const useWebSocket = (opt: UseWebSocketOption) => {
  if (!opt.url) {
    console.log("WebSocket url required");
    return;
  }
  const option = {
    url: opt.url,
    onOpen: opt.onOpen,
    onMessage: opt.onMessage,
    onError: opt.onError,
    onClose: opt.onClose,
    onReconnect: opt.onReconnect
  };
  // 重连计数器
  let reconnectCount = MAX_RECONNECT_COUNT;
  let reconnectTimer: number | null = null;
  // 当前连接tag, 单线连接
  // 如果需要多线连接，得拓展为url对应的loading
  let connectLoading = false;
  // 是否是程序主动关闭的
  let isProgramClosed = false;
  let socketInstance: WebSocket | null = null;
  const createWebsocket = () => {
    // 存在连接中的ws，不允许其他连接介入
    if (connectLoading) {
      return;
    }
    connectLoading = true;
    // closeWebSocket();
    socketInstance = new WebSocket(option.url);
    socketInstance.onopen = e => {
      onWsOpen();
      option.onOpen && option.onOpen(e);
    };
    socketInstance.onmessage = e => {
      option.onMessage && option.onMessage(e);
    };
    socketInstance.onerror = e => {
      onWsError();
      option.onError && option.onError(e);
    };
    socketInstance.onclose = e => {
      onWsClose();
      option.onClose && option.onClose(e);
    };
  };
  // 主动关闭连接
  const closeWebSocket = () => {
    isProgramClosed = true;
    socketInstance && socketInstance.close();
    socketInstance = null;
  };
  const onWsOpen = () => {
    connectLoading = false;
    isProgramClosed = false;
    reconnectTimer && clearTimeout(reconnectTimer);
    reconnectTimer = null;
  };
  const onWsError = () => {
    connectLoading = false;
    if (isProgramClosed) {
      isProgramClosed = false;
      return;
    }
    reconnectWebsocket();
    // console.log('onWsError', e);
  };
  const onWsClose = () => {
    connectLoading = false;
    if (isProgramClosed) {
      isProgramClosed = false;
      return;
    }
    reconnectWebsocket();
    // console.log('onWsClose', e);
  };
  // 每次连接之后延迟一秒尝试重连
  const reconnectWebsocket = () => {
    closeWebSocket();
    reconnectTimer = setTimeout(() => {
      reconnectTimer && clearTimeout(reconnectTimer);
      reconnectTimer = null;
      option.onReconnect &&
        option.onReconnect(reconnectCount, MAX_RECONNECT_COUNT);
      if (reconnectCount <= 0) {
        console.log("超出重连限制，请重试");
        reconnectCount = MAX_RECONNECT_COUNT;
        return;
      }
      reconnectCount--;
      createWebsocket();
    }, 1000) as unknown as number;
  };
  // 发送消息
  const sendMessage = (data: any) => {
    if (socketInstance && socketInstance.readyState === 1) {
      // console.log('message', message);
      socketInstance.send(JSONBig.stringify(data));
      return true;
    } else {
      console.error("消息发送失败，websocket已断开");
      return false;
    }
  };
  const getSocketInstance = () => {
    return socketInstance;
  };
  return {
    createWebsocket,
    closeWebSocket,
    sendMessage,
    getSocketInstance
  };
};
// const { createWebsocket } = useWebSocket({
//   url: 'ws://******:6006/ws-sports-chat',
//   onOpen: () => {
//     console.log('open');
//   },
//   onReconnect: (count, maxCount) => {
//     console.log('onReconnect', count, maxCount);
//   },
// })!;
// createWebsocket();