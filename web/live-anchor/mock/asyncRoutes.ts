// 模拟后端动态生成路由
import { MockMethod } from "vite-plugin-mock";

export default [
  {
    url: "/getAsyncRoutes",
    method: "get",
    response: () => {
      return {
        success: true,
        data: []
      };
    }
  }
] as MockMethod[];
