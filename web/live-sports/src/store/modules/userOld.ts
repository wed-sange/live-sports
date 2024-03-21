// import fetchApi from '../../api/user';
// import { useCookies } from '@vueuse/integrations/useCookies';
// import { defineStore } from 'pinia';
// import { ReqParams } from '@/api/user/model';
// import { removeToken } from '@/utils/auth';
// // const { VITE_TOKEN_KEY } = import.meta.env;
// import { VITE_TOKEN_KEY } from '@/../build/constant';
// const token = useCookies().get(VITE_TOKEN_KEY as string);

// interface StoreUser {
//   token: string;
//   info: string;
// }
// const id = 'app-user';
// export const useUserStore = defineStore({
//   id,
//   state: (): StoreUser => ({
//     token: token,
//     info: '',
//   }),
//   getters: {
//     getUserInfoFun(): any {
//       return this.info || {};
//     },
//   },
//   actions: {
//     setInfo(info: any) {
//       this.info = info ? info : '';
//     },
//     async login(params: ReqParams) {
//       const res = await fetchApi.loginPassword(params);
//       if (res) {
//         // save token
//         // console.log('res');
//         this.token = res.token;
//         useCookies().set(VITE_TOKEN_KEY as string, res.token);
//       }
//       return res;
//     },
//     async getUserInfo() {
//       const res = await fetchApi.getUserInfo();
//       if (res) {
//         this.setInfo(res.data);
//       }
//     },
//     async logout() {
//       this.info = '';
//       removeToken();
//       return 'ok';
//     },
//   },
// });
