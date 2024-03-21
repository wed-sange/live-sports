import { login, logout, getInfo } from "@/api/login";
import { getToken, setToken, removeToken } from "@/utils/auth";
import router from "@/router";

const user = {
  state: {
    token: getToken(),
    id: "",
    name: "",
    avatar: "",
    roles: [],
    permissions: [],
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token;
    },
    SET_ID: (state, id) => {
      state.id = id;
    },
    SET_NAME: (state, name) => {
      state.name = name;
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar;
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles;
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions;
    },
  },

  actions: {
    // 登录
    Login({ commit }, userInfo) {
      const account = userInfo.username.trim();
      const passwd = userInfo.password;
      const code = userInfo.verifyCode;
      const secret = userInfo.secret;
      return new Promise((resolve, reject) => {
        login(account, passwd, code, secret)
          .then((res) => {
            setToken(res.data);
            commit("SET_TOKEN", res.data);
            resolve();
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 获取用户信息
    GetInfo({ commit, state }) {
      return new Promise((resolve, reject) => {
        getInfo()
          .then((res) => {
            console.log("datattt", res);
            const user = res.user;
            const avatar =
              user.avatar == "" || user.avatar == null
                ? require("@/assets/images/profile.jpg")
                : user.avatar;
            if (res.roles && res.roles.length > 0) {
              // 验证返回的roles是否是一个非空数组
              commit("SET_ROLES", res.roles);
              commit("SET_PERMISSIONS", res.permissions);
            } else {
              commit("SET_ROLES", ["ROLE_DEFAULT"]);
            }
            commit("SET_ID", user.userId);
            commit("SET_NAME", user.userName);
            commit("SET_AVATAR", avatar);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },

    // 退出系统
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        if (getToken()) {
          logout(state.token)
            .then(() => {
              commit("SET_TOKEN", "");
              commit("SET_ROLES", []);
              commit("SET_PERMISSIONS", []);
              removeToken();
              resolve();
            })
            .catch((error) => {
              reject(error);
            });
        } else {
          console.log("logout3");
          commit("SET_TOKEN", "");
          commit("SET_ROLES", []);
          commit("SET_PERMISSIONS", []);
          removeToken();
          resolve();
        }
      });
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise((resolve) => {
        commit("SET_TOKEN", "");
        removeToken();
        resolve();
      });
    },
  },
};

export default user;
