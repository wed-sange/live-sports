import { ref, reactive, nextTick } from "vue";
import { defineStore } from "pinia";
import { store } from "@/store";
import { userLogin, getUserInfo as _getUserInfo, userLogout } from "@/api/user";
import { UserInfoData } from "@/api/user/model";
import { storageSession } from "@pureadmin/utils";
import { routerArrays } from "@/layout/types";
import { router, resetRouter } from "@/router";
import { useMultiTagsStoreHook } from "@/store/modules/multiTags";
import { useMessageStore } from "@/store/modules/message";

type CacheDataModel = {
  userInfo: {
    id: string;
    nickname: string;
    account: string;
    notice: string;
    avatar: string;
    // 0-APP普通用户，1-LIVE端主播，2-LIVE端助手，3-LIVE端运营
    identityType: number;
    isFirstLogin: boolean;
    neverCreateOpenInfo: boolean; // 从来没有创建过开播信息
  };
  roles: string[];
  token: string;
};
const cacheKey = "pure-user";
export const useUserStore = defineStore(cacheKey, () => {
  const cacheInfo = storageSession().getItem<CacheDataModel>(cacheKey);
  const messageStore = useMessageStore();
  // 页面级别权限
  const roles = ref(cacheInfo?.roles ?? []);
  const token = ref(cacheInfo?.token ?? "");
  const userInfo = reactive<CacheDataModel["userInfo"]>(
    cacheInfo?.userInfo ?? {
      id: "",
      nickname: "",
      account: "",
      notice: "",
      avatar: "",
      identityType: -1,
      isFirstLogin: false,
      neverCreateOpenInfo: true
    }
  );
  // 用户名
  const username = ref(userInfo.nickname);
  const updateCache = () => {
    storageSession().setItem(cacheKey, {
      userInfo,
      roles: roles.value,
      token: token.value
    });
  };
  const clearFirstLogin = () => {
    userInfo.isFirstLogin = false;
    updateCache();
  };
  const setToken = async (value: string) => {
    token.value = value;
    updateCache();
  };
  const initSetup = () => {
    if (!messageStore.getMessageServer()) {
      messageStore.createMessageServer();
      const messageServer = messageStore.getMessageServer();
      if (messageServer && !messageServer.isConnect()) {
        messageServer.createSocket().then(() => {
          loginWs();
        });
      }
    } else {
      loginWs();
    }
  };
  const loginWs = () => {
    if (!token.value) {
      return;
    }
    const messageServer = messageStore.getMessageServer();
    messageServer?.userLogin({
      userId: userInfo.id,
      token: token.value
    });
  };
  const logoutWs = () => {
    const messageServer = messageStore.getMessageServer();
    messageServer?.userLogout();
  };
  const getUserInfo = async () => {
    try {
      const response = await _getUserInfo();
      const data = response;
      setUserInfo(data);
      initSetup();
    } catch (error) {
      setToken("");
      throw error;
    }
  };
  const setUserInfo = async (data: UserInfoData) => {
    userInfo.id = data.id + "";
    userInfo.nickname = data.nickName;
    userInfo.account = data.account;
    userInfo.notice = data.notice;
    userInfo.avatar = data.head || "";
    const setOpenInfo = !!data.setOpenInfo;
    userInfo.neverCreateOpenInfo = !setOpenInfo;
    // userInfo.neverCreateOpenInfo = true;
    userInfo.identityType = Number(data.identityType || -1);
    username.value = userInfo.nickname;
    updateCache();
  };
  /** 登入 */
  const loginByUsername = async ({
    account,
    pass
  }: {
    account: string;
    pass: string;
  }) => {
    const response = await userLogin({
      account: account,
      passwd: pass
    });
    setToken(response.token);
    userInfo.isFirstLogin = response.isFirstLogin;
    await getUserInfo();
  };
  const logout = async () => {
    if (!token.value) {
      resetRouter();
      router.push("/login");
      return;
    }
    logoutWs();
    userLogout();
    await nextTick();
    token.value = "";
    userInfo.id = "";
    userInfo.nickname = "";
    userInfo.account = "";
    userInfo.notice = "";
    userInfo.avatar = "";
    userInfo.neverCreateOpenInfo = true;
    sessionStorage.clear();
    messageStore.destroyMessageServer();
    useMultiTagsStoreHook().handleTags("equal", [...routerArrays]);
    resetRouter();
    router.push("/login");
  };
  if (token.value) {
    initSetup();
  }
  const clearToken = () => {
    token.value = "";
  };
  return {
    roles,
    token,
    clearFirstLogin,
    userInfo,
    getUserInfo,
    username,
    loginByUsername,
    logout,
    clearToken
  };
});

export function useUserStoreHook() {
  return useUserStore(store);
}
