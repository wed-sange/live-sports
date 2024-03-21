import { isNormalUserSend } from './../../../views/message/utils/message';
import { defineStore } from 'pinia';
import { showLoadingToast } from 'vant';
import { getMessageList, removeMessage } from '@/api/message';
import { usePageListCache } from '@/hook/usePageListCache';
import { useUserStore } from '@/store/modules/user';
import { MessageCardData } from '@/views/message/utils/types';
import { useMessage } from '@/views/message/utils/useMessage';
import {
  isPersonalMessage,
  getTimeWithNowDayDiff,
  formatMessageTime,
  buildMessageItem,
  initChatAccountInfo,
} from '@/views/message/utils/message';

const getServerCacheList: any = [];
export const useMessageStore = defineStore(
  'app-message',
  () => {
    let messageServer: ReturnType<typeof useMessage> | null = null;
    const { getRenderData, clearCache, removeCache } = usePageListCache<MessageCardData>({
      cacheKey: 'anchorId',
    });
    const readDotAll = ref(0);
    const createMessageServer = () => {
      const userStore = useUserStore();
      messageServer = useMessage();
      for (let i = 0; i < getServerCacheList.length; i++) {
        getServerCacheList[i](messageServer);
      }

      const onSysNotifyAddSysMessage = (data) => {
        const anchorId = '0';
        const index = list.value.findIndex((cur) => cur.anchorId === anchorId);
        if (index !== -1) {
          const msgItem = list.value[index];
          list.value.splice(index, 1);
          list.value.unshift(msgItem);
          msgItem.message = data.title;
          msgItem.createTime = formatMessageTime(data.createTime);
          msgItem.createTimeText = formatMessageTime(data.createTime, 'now');
          msgItem.readDot += 1;
        } else {
          const newMsg = getRenderData([
            buildMessageItem({
              id: (data.id || '') + '-' + anchorId,
              anchorId,
              sendUserId: '',
              message: data.title,
              readDot: 1,
              createTime: data.createTime,
            }),
          ])[0];
          newMsg && list.value.unshift(newMsg);
        }

        updateAllMessageTime();
      };
      messageServer.chatEmitter.on('banned', async (res) => {
        const data = res.data || {};
        const sendId = String(data.userId);
        if (sendId === userStore.userInfo.id) {
          userStore.updateUserBanned(true);
          onSysNotifyAddSysMessage(data);
        }
      });
      messageServer.chatEmitter.on('unBanned', async (res) => {
        const data = res.data || {};
        const sendId = String(data.userId);
        if (sendId === userStore.userInfo.id) {
          userStore.updateUserBanned(false);
          onSysNotifyAddSysMessage(data);
        }
      });
      messageServer.chatEmitter.on('feedback', async (res) => {
        const data = res.data || {};
        const sendId = String(data.userId);
        if (sendId === userStore.userInfo.id) {
          onSysNotifyAddSysMessage(data);
        }
      });
      messageServer.chatEmitter.on('readDotAll', async (res) => {
        const data = res.data || {};
        readDotAll.value = Number(data.totalCount || '');
      });
      messageServer.chatEmitter.on('message', async (res) => {
        const data = res.data || {};
        if (!isPersonalMessage(data)) return;
        const index = list.value.findIndex((cur) => cur.anchorId === data.anchorId);
        const isNormalUser = isNormalUserSend(data);
        const fromId = (data.from || '') + '';
        const isMe = fromId === userStore.userInfo.id;

        if (index !== -1) {
          const msgItem = list.value[index];
          list.value.splice(index, 1);
          list.value.unshift(msgItem);
          msgItem.message = data.content;
          msgItem.createTime = formatMessageTime(data.createTime);
          msgItem.createTimeText = formatMessageTime(data.createTime, 'now');
          msgItem.msgType = data.msgType;
          if (!isMe) {
            msgItem.readDot += 1;
            // 如果消息不是自己发的，那聊天对象的信息取from
            msgItem.name = data.fromNickName;
            msgItem.cover = data.fromAvatar;
          } else {
            // 如果消息是自己发的，那聊天对象的信息取to
            msgItem.name = data.toNickName;
            msgItem.cover = data.toAvatar;
          }
        } else {
          const anchorId = (data.anchorId || '') + '';
          const accountInfo = {
            name: '',
            cover: '',
          };
          if (!isNormalUser || isMe) {
            const res = await initChatAccountInfo(anchorId);
            accountInfo.name = res.name;
            accountInfo.cover = res.cover;
          } else {
            accountInfo.name = data.fromNickName;
            accountInfo.cover = data.fromAvatar;
          }
          const newMsg = getRenderData([
            buildMessageItem({
              id: (data.id || '') + '',
              anchorId,
              sendUserId: '',
              name: accountInfo.name,
              cover: accountInfo.cover,
              message: data.content,
              readDot: isMe ? 0 : 1,
              createTime: data.createTime,
              msgType: data.msgType,
            }),
          ])[0];
          newMsg && list.value.unshift(newMsg);
        }
        updateAllMessageTime();
      });
      messageServer.chatEmitter.on('clearDot', (id) => {
        if (id) {
          for (let i = 0; i < list.value.length; i++) {
            if (list.value[i].anchorId === id) {
              list.value[i].readDot = 0;
              break;
            }
          }
        } else {
          for (let i = 0; i < list.value.length; i++) {
            list.value[i].readDot = 0;
          }
        }
        updateAllMessageTime();
      });
    };
    const getMessageServer = () => {
      return messageServer;
    };
    const getMessageServerAsync = () => {
      return new Promise<ReturnType<typeof useMessage>>((resolve) => {
        if (messageServer) {
          resolve(messageServer);
          return;
        } else {
          getServerCacheList.push(resolve);
        }
      });
    };
    const list = ref<MessageCardData[]>([]);

    const currentPage = ref(0);
    const pageSize = ref(10);
    const resetListData = () => {
      list.value = [];
      clearCache();
    };
    const initData = async (page = 1) => {
      try {
        const response = await getMessageList({
          current: page,
          size: pageSize.value,
        });
        if (page <= 1) {
          resetListData();
        }
        const data = response.records || [];
        const renderData = getRenderData(
          data.map<MessageCardData>((cur) => {
            const anchorId = (cur.anchorId || '') + '';
            return buildMessageItem({
              id: (cur.id || '') + '',
              anchorId: anchorId,
              name: cur.nick,
              cover: cur.avatar,
              message: cur.content,
              readDot: Number(cur.noReadSum),
              createTime: cur.createTime,
              msgType: cur.msgType,
            });
          }),
        );
        list.value.push(...renderData);
        currentPage.value = page;
        return response;
      } catch (err) {
        console.log('error', err);
        // throw err;
      }
    };

    type PartialOptional<T> = {
      [K in keyof T]?: T[K] | undefined;
    };
    const updateMessageInfo = (item: PartialOptional<MessageCardData>) => {
      if (!item.anchorId) {
        return;
      }
      const index = list.value.findIndex((cur) => cur.anchorId === item.anchorId);
      if (index !== -1) {
        const msgItem = list.value[index];
        msgItem.name = item.name || '';
        msgItem.cover = item.cover || '';
      }
    };
    let updateAllMessageTimeTimer: number | null = null;
    const updateAllMessageTime = () => {
      if (updateAllMessageTimeTimer) {
        return;
      }
      for (let i = 0; i < list.value.length; i++) {
        const cur = list.value[i];
        const newCreateTimeText = formatMessageTime(+new Date(cur.createTime.replace(/-/g, '/')), 'now');
        const diffDay = getTimeWithNowDayDiff(cur.createTime);

        cur.createTimeText = newCreateTimeText;
        if (diffDay <= -2) {
          break;
        }
      }
      updateAllMessageTimeTimer = setTimeout(() => {
        updateAllMessageTimeTimer && clearTimeout(updateAllMessageTimeTimer);
        updateAllMessageTimeTimer = null;
      }, 300) as unknown as number;
    };
    const removeMessageRecord = async (item: MessageCardData) => {
      const toast = showLoadingToast({
        duration: 0,
        forbidClick: true,
        message: '加载中...',
      });
      try {
        await removeMessage(item.anchorId);
        const index = list.value.findIndex((cur) => cur.anchorId === item.anchorId);
        if (index !== -1) {
          list.value.splice(index, 1);
          removeCache(item.anchorId);
        }
      } finally {
        toast.close();
      }
    };
    const destroyMessageServer = () => {
      messageServer?.destroy();
      messageServer = null;
    };
    return {
      createMessageServer,
      getMessageServer,
      getMessageServerAsync,
      list,
      resetListData,
      currentPage,
      pageSize,
      initData,
      updateMessageInfo,
      updateAllMessageTime,
      removeMessageRecord,
      destroyMessageServer,
      readDotAll,
    };
  },
  {
    persist: false,
  },
);
