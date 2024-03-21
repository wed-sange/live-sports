import axios, { AxiosInstance, AxiosRequestConfig, AxiosResponse, CustomParamsSerializer } from 'axios';
import JSONbig from 'json-bigint';
import { API_TARGET_URL, API_TEST_URL } from '../../build/constant';
import { showToast } from 'vant';
import { useUserStore } from '@/store/modules/user';
import { stringify } from 'qs';
// baseURL
const BASE_URL = import.meta.env.MODE === 'development' ? API_TEST_URL : API_TARGET_URL;

const defaultConfig: AxiosRequestConfig = {
  // 请求超时时间
  timeout: 50000,
  baseURL: BASE_URL,
  withCredentials: false,
  transformResponse: [
    (data) => {
      try {
        return JSONbig.parse(data);
      } catch {
        try {
          return JSON.parse(data);
        } catch {
          return data;
        }
      }
    },
  ],
  headers: {
    Accept: '*/*',
    'Content-Type': 'application/json;charset=UTF-8',
    'X-Requested-With': 'XMLHttpRequest',
  },
  // 数组格式参数序列化（https://github.com/axios/axios/issues/5142）
  paramsSerializer: {
    serialize: stringify as unknown as CustomParamsSerializer,
  },
};

// create an axios instance
const instance = axios.create(defaultConfig);

// request interceptor
instance.interceptors.request.use(
  (config) => {
    // do something before request is sent
    const userStore = useUserStore();
    const token = userStore.token;
    if (token) {
      config.headers = {
        ...config.headers,
        sportstoken: token,
      };
    }
    return config;
  },
  (error) => {
    console.log(error); // for debug
    return Promise.reject(error);
  },
);
const showToastStr = (message) => {
  const hashStr = window.location.hash;
  const isRoom = hashStr && hashStr.indexOf('#/roomDetail') != -1;
  showToast({
    className: isRoom ? 'custom-toast-white' : 'custom-toast',
    message,
  });
};

// response interceptor
instance.interceptors.response.use(
  /**
   * If you want to get http information such as headers or status
   * Please return  response => response
   */

  /**
   * Determine the request status by custom code
   * Here is just an example
   * You can also judge the status by HTTP Status Code
   */
  (response) => {
    // console.log('response', response.data);
    const res = response.data;

    // if the custom code is not 200, it is judged as an error.

    if (res.code !== 200 && !res.success) {
      showToastStr(res.message || res.msg || '接口访问失败');
      // 412: Token expired;
      if (res.code === 401) {
        // store.dispatch('user/userLogout');
        // const userStore = useUserStore();
        // userStore.logout();
      }
      return Promise.reject(res.message || 'Error');
    } else {
      return res.data || res.repData;
    }
  },
  (error) => {
    showToastStr(error.message || error.msg || '接口访问失败');
    return Promise.reject(error.message);
  },
);

const request = <T = any>(config: AxiosRequestConfig | string, options?: AxiosRequestConfig): Promise<T> => {
  if (typeof config === 'string') {
    if (!options) {
      return instance.request<T, T>({
        url: config,
      });
    } else {
      return instance.request<T, T>({
        url: config,
        ...options,
      });
    }
  } else {
    return instance.request<T, T>(config);
  }
};
export function get<T = any>(config: AxiosRequestConfig, options?: AxiosRequestConfig): Promise<T> {
  return request({ ...config, method: 'GET' }, options);
}

export function post<T = any>(config: AxiosRequestConfig, options?: AxiosRequestConfig): Promise<T> {
  return request({ ...config, method: 'POST' }, options);
}

export default request;
export type { AxiosInstance, AxiosResponse };
