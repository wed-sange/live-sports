import axios from 'axios';
import { API_TARGET_URL, API_TEST_URL } from '../../../../build/constant';
const BASE_URL = import.meta.env.MODE === 'development' ? API_TEST_URL : API_TARGET_URL;

axios.defaults.baseURL = BASE_URL;

const service = axios.create({
  timeout: 40000,
  headers: {
    'X-Requested-With': 'XMLHttpRequest',
    'Content-Type': 'application/json; charset=UTF-8',
  },
});
service.interceptors.request.use(
  (config) => {
    return config;
  },
  (error) => {
    Promise.reject(error);
  },
);

// response interceptor
service.interceptors.response.use(
  (response) => {
    const res = response.data;
    return res;
  },
  (error) => {
    console.log('error', error);
  },
);
export default service;
