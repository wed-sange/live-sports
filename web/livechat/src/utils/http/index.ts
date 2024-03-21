import Axios, {
  AxiosInstance,
  AxiosRequestConfig,
  CustomParamsSerializer
} from "axios";
import JSONBig from "json-bigint";
import {
  PureHttpError,
  RequestMethods,
  PureHttpResponse,
  PureHttpRequestConfig
} from "./types.d";
import { stringify } from "qs";
import NProgress from "../progress";
import { useUserStore, useUserStoreHook } from "@/store/modules/user";
import { ElMessage } from "element-plus";
const { VITE_BASE_API } = import.meta.env;

// 相关配置请参考：www.axios-js.com/zh-cn/docs/#axios-request-config-1
const defaultConfig: AxiosRequestConfig = {
  // 请求超时时间
  timeout: 60000,
  withCredentials: true,
  headers: {
    Accept: "*/*",
    "Content-Type": "application/json;charset=UTF-8",
    "X-Requested-With": "XMLHttpRequest"
  },
  transformResponse: [
    function (data) {
      try {
        return JSONBig.parse(data);
      } catch (err) {
        return data;
      }
    }
  ],
  // 数组格式参数序列化（https://github.com/axios/axios/issues/5142）
  paramsSerializer: {
    serialize: stringify as unknown as CustomParamsSerializer
  }
};

class PureHttp {
  constructor() {
    this.httpInterceptorsRequest();
    this.httpInterceptorsResponse();
  }

  /** token过期后，暂存待执行的请求 */
  private static requests = [];

  /** 防止重复刷新token */
  private static isRefreshing = false;

  /** 初始化配置对象 */
  private static initConfig: PureHttpRequestConfig = {};

  /** 保存当前Axios实例对象 */
  private static axiosInstance: AxiosInstance = Axios.create(defaultConfig);

  // /** 重连原始请求 */
  // private static retryOriginalRequest(config: PureHttpRequestConfig) {
  //   return new Promise(resolve => {
  //     PureHttp.requests.push((token: string) => {
  //       config.headers["Authorization"] = formatToken(token);
  //       resolve(config);
  //     });
  //   });
  // }

  /** 请求拦截 */
  private httpInterceptorsRequest(): void {
    PureHttp.axiosInstance.interceptors.request.use(
      async (config: PureHttpRequestConfig): Promise<any> => {
        // 开启进度条动画
        if (!config || !config.data || !config.data.noProgress) {
          NProgress.start();
        } else {
          delete config.data.noProgress;
        }

        /** 请求白名单，放置一些不需要token的接口（通过设置请求白名单，防止token过期后再请求造成的死循环问题） */
        const whiteList = ["/refreshToken", "/login"];
        const userStore = useUserStore();
        const token = userStore.token;
        if (whiteList.some(v => config.url.indexOf(v) === -1)) {
          if (token) {
            config.headers = {
              ...config.headers,
              sportstoken: token
            };
          }
        }
        // 优先判断post/get等方法是否传入回调，否则执行初始化设置等回调
        if (typeof config.beforeRequestCallback === "function") {
          config.beforeRequestCallback(config);
          return config;
        }
        if (PureHttp.initConfig.beforeRequestCallback) {
          PureHttp.initConfig.beforeRequestCallback(config);
          return config;
        }
        return config;
      },
      error => {
        return Promise.reject(error);
      }
    );
  }

  /** 响应拦截 */
  private httpInterceptorsResponse(): void {
    const instance = PureHttp.axiosInstance;
    instance.interceptors.response.use(
      (response: PureHttpResponse) => {
        const $config = response.config;
        // 关闭进度条动画
        NProgress.done();
        // 优先判断post/get等方法是否传入回调，否则执行初始化设置等回调
        if (typeof $config.beforeResponseCallback === "function") {
          $config.beforeResponseCallback(response);
          return this.formatResponseData(response);
        }
        if (PureHttp.initConfig.beforeResponseCallback) {
          PureHttp.initConfig.beforeResponseCallback(response);
          return this.formatResponseData(response);
        }
        return this.formatResponseData(response);
      },
      (error: PureHttpError) => {
        const $error = error;
        $error.isCancelRequest = Axios.isCancel($error);
        // 关闭进度条动画
        NProgress.done();
        // 所有的响应异常 区分来源为取消请求/非取消请求
        return Promise.reject($error);
      }
    );
  }
  private formatResponseData(response: any) {
    if (response.data && response.data.code && response.data.code === 200) {
      return response.data;
    } else {
      if (response.data && response.data.code && response.data.code === 401) {
        if (response.config.url.indexOf("/user/logout") === -1) {
          console.log("userStore", useUserStore().token);
          if (useUserStore().token) {
            ElMessage.error(response.data.msg || "账号被禁用,请联系管理员");
          }
          useUserStore().logout();
        }
      }
      return Promise.reject(response);
    }
  }

  /** 通用请求工具函数 */
  public request<T>(
    method: RequestMethods,
    url: string,
    param?: AxiosRequestConfig,
    axiosConfig?: PureHttpRequestConfig
  ): Promise<T> {
    const config = {
      method,
      url,
      ...param,
      ...axiosConfig,
      baseURL: VITE_BASE_API
    } as PureHttpRequestConfig;

    // 单独处理自定义请求/响应回调
    return new Promise((resolve, reject) => {
      PureHttp.axiosInstance
        .request(config)
        .then((response: any) => {
          resolve(response.data);
        })
        .catch(error => {
          if (error.data && error.data.code == 401) {
            // console.log("userStore", useUserStore().token);
            // ElMessage.error(error.data.msg || "账号被禁用,请联系管理员");
            // setTimeout(() => {
            useUserStoreHook().logout();
            // }, 5000);
            return;
          }
          if (error.data && error.data.msg) {
            if (config.url.indexOf("/user/logout") === -1) {
              ElMessage.error(error.data.msg);
            }
          }
          reject(error);
        });
    });
  }

  /** 单独抽离的post工具函数 */
  public post<T, P>(
    url: string,
    params?: AxiosRequestConfig<T>,
    config?: PureHttpRequestConfig
  ): Promise<P> {
    return this.request<P>("post", url, params, config);
  }
  /** 单独抽离的put工具函数 */
  public put<T, P>(
    url: string,
    params?: AxiosRequestConfig<T>,
    config?: PureHttpRequestConfig
  ): Promise<P> {
    return this.request<P>("put", url, params, config);
  }

  /** 单独抽离的get工具函数 */
  public get<T, P>(
    url: string,
    params?: AxiosRequestConfig<T>,
    config?: PureHttpRequestConfig
  ): Promise<P> {
    return this.request<P>("get", url, params, config);
  }
}

export const http = new PureHttp();
