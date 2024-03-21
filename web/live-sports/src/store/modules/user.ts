import { ref, reactive } from 'vue';
import { defineStore } from 'pinia';
import { userLogin, getUserInfo as _getUserInfo, userLogout } from '@/api/user';
import { useMessageStore } from '@/store/modules/message';
const levelNames = { 1: '黄铜', 2: '白银', 3: '黄金', 4: '铂金', 5: '钻石', 6: '星耀' };
const growthTotals = { 1: 0, 2: 300, 3: 1000, 4: 3000, 5: 10000, 6: 50000 };
export const useUserStore = defineStore(
  'app-user',
  () => {
    const messageStore = useMessageStore();
    const token = ref('');
    const userInfo = reactive<{
      id: string;
      nickname: string;
      phone: string;
      email: string;
      avatar: string;
      levelName: string;
      level: string;
      growth: number;
      growthNextTotal: number;
      isBanned: boolean;
      createTime: number;
    }>({
      id: '',
      nickname: '',
      phone: '',
      email: '',
      avatar: '',
      levelName: '',
      level: '',
      growth: 0,
      growthNextTotal: 0,
      isBanned: false,
      createTime: -1,
    });
    const adInfo = ref({ imgUrl: '' });
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
        token: token.value,
      });
    };
    const logoutWs = () => {
      const messageServer = messageStore.getMessageServer();
      messageServer?.userLogout();
    };
    const updateUserBanned = (isBanned: boolean) => {
      userInfo.isBanned = isBanned;
    };
    const getUserInfo = async () => {
      try {
        const response = await _getUserInfo();
        const data = response;
        userInfo.id = data.id + '';
        userInfo.nickname = data.name;
        userInfo.phone = data.tel;
        userInfo.email = data.email;
        userInfo.avatar = data.head;
        userInfo.levelName = data.lvName;
        userInfo.level = Math.min(6, Number(data.lvNum || '')) + '';
        const nextLevel = Math.min(6, Number(userInfo.level) + 1);
        userInfo.growthNextTotal = growthTotals[nextLevel];
        userInfo.growth = Number(data.growthValue || '');
        userInfo.isBanned = data.ynForbidden !== 0;
        userInfo.createTime = data.createTime;
        initSetup();
      } catch (error) {
        token.value = '';
      }
    };
    const convertLevelName = (level) => {
      return levelNames[level];
    };

    const login = async ({ phone, code, tokenStr, areaCode }: { phone: string; code: string; tokenStr: string; areaCode: string }) => {
      const response = await userLogin({
        account: phone,
        code,
        type: 1,
        source: 3,
        token: tokenStr,
        areaCode,
      });
      token.value = response;
      await getUserInfo();
    };
    const loginEmail = async ({ email, code, tokenStr }: { email: string; code: string; tokenStr: string }) => {
      const response = await userLogin({
        account: email,
        code,
        type: 2,
        source: 3,
        token: tokenStr,
      });
      token.value = response;
      await getUserInfo();
    };
    const logout = async () => {
      // messageStore.destroyMessageServer();
      logoutWs();
      userLogout();
      setTimeout(() => {
        token.value = '';
        userInfo.id = '';
        userInfo.nickname = '';
        userInfo.phone = '';
        userInfo.email = '';
        userInfo.avatar = '';
        userInfo.levelName = '';
        userInfo.level = '';
      }, 0);
    };
    return { token, userInfo, adInfo, getUserInfo, login, loginEmail, logout, convertLevelName, updateUserBanned };
  },
  {
    persist: {
      afterRestore: (ctx) => {
        const messageStore = useMessageStore();
        messageStore.createMessageServer();
        const messageServer = messageStore.getMessageServer();
        messageServer?.createSocket().then(() => {
          if (ctx.store.token) {
            messageServer?.userLogin({
              userId: ctx.store.userInfo.id,
              token: ctx.store.token,
            });
          }
        });
        if (ctx.store.token) {
          ctx.store.getUserInfo();
        }
      },
    },
  },
);
