export class EventCache {
  cacheList = new Map<string, any[]>();
  has(key: string, handler?: any) {
    const hasCache = this.cacheList.has(key);
    if (!hasCache) return false;
    if (handler === undefined) return true;
    const handlers = this.cacheList.get(key)!;
    const index = handlers.findIndex((eventHandler) => eventHandler === handler);
    return index !== -1;
  }
  once(key: string, handler: any) {
    const handlers = this.cacheList.get(key) || [];
    const _handler = Array.isArray(handler) ? handler : [handler];
    _handler.forEach((eventHandler) => {
      const newHandler = (...args) => {
        eventHandler(...args);
        this.remove(key, newHandler);
      };
      handlers.push(newHandler);
    });
    this.cacheList.set(key, handlers);
  }
  add(key: string, handler: any) {
    const handlers = this.cacheList.get(key) || [];
    if (Array.isArray(handler)) {
      handlers.push(...handler);
    } else {
      handlers.push(handler);
    }
    this.cacheList.set(key, handlers);
  }
  remove(key: string, handler?: any) {
    const hasCache = this.cacheList.has(key);
    if (!hasCache) return false;
    if (handler === undefined) {
      this.cacheList.delete(key);
      return true;
    }
    const handlers = this.cacheList.get(key)!;
    const index = handlers.findIndex((eventHandler) => eventHandler === handler);
    if (index === -1) {
      return false;
    }
    handlers.splice(index, 1);
    if (handlers.length === 0) {
      this.cacheList.delete(key);
    } else {
      this.cacheList.set(key, handlers);
    }
    return true;
  }
  execute(key: string, ...payloads: any[]) {
    const hasCache = this.cacheList.has(key);
    if (!hasCache) return false;
    const handlers = this.cacheList.get(key)!;
    handlers.forEach((eventHandler) => {
      eventHandler(...payloads);
    });
  }
  clear() {
    this.cacheList.clear();
  }
}
/**
 *
 * @param opt {
 *  timeout undefined 不会创建超时处理，number创建对应毫秒超时处理，1000就是1s之后超时
 * }
 */
type UnknownFunction = (...args: any[]) => any;
export const createResponseTimeout = (opt?: { timeout?: number }) => {
  const defaultOpt = {
    timeout: opt && opt.timeout,
    maxCount: 10000,
    timerCount: 0,
    timers: {},
  };
  return {
    create<T extends UnknownFunction>(mainFun: T, timeoutFun: UnknownFunction, timeout?: number) {
      let tag: number | null = null;
      let timer =
        timeout === undefined && defaultOpt.timeout === undefined
          ? null
          : setTimeout(() => {
              timeoutFun();
              tag = 1;
              cleanTimer();
            }, timeout || defaultOpt.timeout);
      const cleanTimer = () => {
        if (timer) {
          clearTimeout(timer);
          delete defaultOpt.timers[timerCount!];
          timer = null;
          timerCount = null;
        }
      };
      let timerCount: number | null = null;
      if (timer) {
        timerCount = defaultOpt.timerCount++;
        if (defaultOpt.timerCount > defaultOpt.maxCount) {
          defaultOpt.timerCount = 0;
        }
        defaultOpt.timers[timerCount] = timer;
      }
      return (...args: Parameters<T>): ReturnType<T> | undefined => {
        if (tag) {
          tag = null;
          return undefined;
        }
        cleanTimer();
        return mainFun(...args);
      };
    },
    clear() {
      Object.keys(defaultOpt.timers).forEach((key) => {
        const timer = defaultOpt.timers[key];
        timer && clearTimeout(timer);
      });
      defaultOpt.timers = {};
    },
  };
};
