<template>
  <page-box @scroll="onScroll" class="bg-[#181819] room-detail h-screen overflow-y-scroll" :class="[hasLive() ? 'live' : '']">
    <template #header>
      <AppHeader :barImg="getBarImg()" />
      <van-sticky :offset-top="0" v-if="showNav">
        <div :class="['title-header-' + matchType]" class="flex w-full h-[88px] relative">
          <div class="mt-[24px] cursor-pointer z-10 click-style" @click="cancelLive()">
            <img class="w-[19px] ml-[31px] h-[32px] object-cover" src="@/assets/img/room/sticky-left.png" alt="" />
          </div>
          <div class="flex items-center w-[456px] ml-[98px] mt-[10px] h-[60px]">
            <div class="w-[60px] h-[60px] flex items-center justify-center">
              <img class="max-w-[60px] max-h-[60px]" :src="getFirstLogo()" />
            </div>
            <div class="flex justify-center items-center w-[117px] text-[30px] font-semibold text-white number-text">
              <span class="minw-w-[20px] flex justify-center items-center"> {{ getFirstScore() }}</span>
            </div>
            <div class="flex items-center justify-center w-[104px] flex-shrink-0 ml-[3px]">
              <span
                class="text-white text-[24px] h-[42px] pt-[2px] flex flex-shrink-0 flex-nowrap items-center justify-center org-url rounded-[40px] px-[16px] font-medium"
                >{{ getMatchStatus(roomData.status) }}</span
              >
            </div>
            <div class="flex justify-center items-center w-[116px] text-[30px] font-semibold text-white number-text">
              <span class="min-w-[20px] flex justify-center items-center">{{ getSecondScore() }}</span>
            </div>
            <div class="w-[60px] h-[60px] flex items-center justify-center">
              <img class="max-w-[60px] max-h-[60px]" :src="getSecondLogo()" />
            </div>
          </div>
          <img @click="onShare" class="w-[40px] mr-[24px] ml-[82px] mt-[20px] h-[40px] bg-contain z-10" :src="shareIcon" />
        </div>
      </van-sticky>
      <div class="relative flex flex-col h-[378px]" ref="topHeader">
        <div class="flex absolute z-20 top-0 left-0 w-full h-[88px] bg-transparent" ref="titleRef">
          <div
            v-if="showCenterTitle(roomData.status)"
            class="flex absolute w-full flex-col items-center justify-center text-[24px] font-medium bg-transparent"
          >
            <span class="mt-[8px]">{{ roomData.competitionName }}</span>
            <span class="text-[22px] mt-[-4px] font-normal"
              >{{ isMatching(roomData.status) ? getMatchTime(roomData) : formatMatchDate(roomData.matchTime)
              }}<span v-if="isMatching(roomData.status)" class="state-dot">'</span></span
            >
          </div>
          <div class="pt-[22px] cursor-pointer click-style z-10" @click="cancelLive()">
            <img class="ml-[30px] w-[19px] h-[35px] bg-contain" src="@/assets/img/room/back-icon.png" alt="" />
          </div>
          <img
            v-if="showCenterTitle(roomData.status)"
            @click.stop="onShare"
            class="click-style w-[40px] h-[40px] bg-contain absolute top-[20px] z-30 right-[24px]"
            :src="shareIcon"
          />
        </div>

        <div class="relative" v-if="liveRoomInfo">
          <LivePlayer
            v-if="liveRoomInfo?.playUrl && !showPlayerError && liveRoomInfo.liveStatus != 3"
            :videoUrl="liveRoomInfo?.playUrl"
            @on-action="onShowAction"
            @on-share="onShare"
            :title="getRoomTitle()"
            @on-error="onErrorPlayer"
            ref="playerRef"
            :backgroundBk="matchType == 1 ? footerBk : basketBk"
            :key="liveRoomInfo?.playUrl"
            :poster="liveRoomInfo.titlePage"
            :advertItem="advertItem"
            @on-jump="onJump"
          />
          <div class="w-full items-center flex flex-col justify-center" v-else>
            <div
              v-if="showPlayerError || !showCenterTitle(roomData.status)"
              class="truncate text-[28px] font-medium max-w-[600px] break-all absolute top-[20px] z-10 left-[64px]"
            >
              {{ getRoomTitle() }}
            </div>
            <img
              @click.stop="onShare"
              class="click-style w-[40px] h-[40px] bg-contain absolute top-[20px] z-30 right-[24px]"
              :src="shareIcon"
            />
            <img class="w-full h-[378px] bg-contain absolute top-0 left-0" :src="matchType == 1 ? footerBk : basketBk" />
            <div class="flex flex-col absolute top-[147px] pl-[2px]" v-if="showPlayerError && liveRoomInfo.liveStatus != 3">
              <span class="text-[#fff] text-[26px] font-medium">加载失败,请重试或切换信号</span>
              <div class="flex mt-[28px] ml-[6.5px]">
                <span
                  @click="onPlayerReload"
                  class="rounded-[40px] click-style pt-[1px] text-[24px] font-medium org-url w-[128px] h-[50px] flex items-center justify-center"
                  >重新加载</span
                >
                <span
                  @click="onShowAction"
                  class="rounded-[40px] click-style pt-[1px] ml-[50px] text-[24px] font-medium org-url w-[128px] h-[50px] flex items-center justify-center"
                  >信号源</span
                >
              </div>
            </div>
            <div class="w-full flex z-10 items-center justify-center text-[26px] mt-[158px]" v-else-if="liveRoomInfo.liveStatus == 3">
              <span class="bg-[#E8EAEF] ml-[1px] w-[48px] h-[0.5px] mt-[-1px]"></span>
              <span class="mx-[10px]">主播暂未开播</span>
              <span class="bg-[#E8EAEF] w-[48px] h-[0.5px] mt-[-1px]"></span>
            </div>
            <span
              v-if="liveRoomInfo.liveStatus == 3"
              @click="onShowAction"
              class="z-10 ml-[1px] click-style rounded-[40px] text-[24px] font-medium org-url pt-[4px] mt-[18px] w-[104px] h-[42px] flex justify-center"
              >信号源</span
            >
          </div>
        </div>
        <div class="w-full h-[378px] relative flex" v-else-if="loaded && !liveId">
          <img class="w-full h-[378px] bg-contain absolute top-0 left-0" :src="matchType == 1 ? footerBk : basketBk" />
          <div class="flex flex-col absolute left-[72px] items-center w-[200px] z-[1] top-[145px]">
            <div class="w-[80px] h-[80px] flex items-center justify-center">
              <img class="max-w-[80px] max-h-[80px] bg-cover" :src="getFirstLogo()" />
            </div>
            <span class="text-[24px] max-w-[200px] mt-[10px] line-clamp-2 break-all font-medium">{{ getFirstName() }}</span>
          </div>
          <div class="w-full flex justify-center">
            <div class="flex flex-col flex-shrink-0 items-center text-[24px] text-white z-[1] mt-[88px]" v-show="loaded">
              <span class="text-[rgba(255, 255, 255, 0.87)] text-[20px] mt-[30px]">{{ getMatchStatus(roomData.status) }}</span>
              <span
                class="mt-[19px] rounded-[10px] number-text text-white text-[36px] min-w-[138px] px-[12px] h-[68px] flex items-center justify-center font-semibold border-[#E8EAEF] border-[0.7px]"
              >
                {{ getMatchData(roomData.status) }}
              </span>
              <span
                @click="onShowAction"
                class="rounded-[40px] click-style pt-[1px] text-[24px] font-medium org-url mt-[19px] w-[104px] h-[42px] flex items-center justify-center"
                >信号源</span
              >
            </div>
          </div>
          <div class="flex flex-col absolute right-[67px] items-center w-[200px] z-[1] top-[145px]">
            <div class="w-[80px] h-[80px] flex items-center justify-center">
              <img class="max-w-[80px] max-h-[80px] bg-contain" :src="getSecondLogo()" />
            </div>
            <span class="text-[24px] max-w-[200px] mt-[10px] line-clamp-2 break-all font-medium">{{ getSecondName() }}</span>
            <span class="text-[24px] font-medium mt-[-2px]" v-if="matchType == 2">(主)</span>
          </div>
        </div>
      </div>

      <div class="h-[96px] flex flex-shrink-0" v-if="bannerItem && bannerItem.imgUrl">
        <div class="w-full cursor-pointer" v-ripple @click="onJump(bannerItem.targetUrl)">
          <img :src="bannerItem.imgUrl || defaultRoomHeaderBk" class="h-[96px] w-full bg-contain" />
        </div>
      </div>
      <AnchorInfo
        v-if="anchorInfo && !anchorInfo.pureFlow"
        :hotValue="liveRoomInfo?.hotValue"
        :anchorInfo="anchorInfo"
        @on-attention="onAttentionAnchor"
      />
      <div :class="`w-full bg-[#27272A] h-[1px] ${anchorInfo && !anchorInfo.pureFlow ? 'mb-[2px]' : ''} `"></div>
      <div
        :class="`bg-[#181819] ${
          hasLive()
            ? appStore.showBar
              ? 'live-tab-btm-bar'
              : 'live-tab'
            : active == 0
            ? appStore.showBar
              ? 'chat-page-bar'
              : 'chat-page'
            : ''
        }`"
      >
        <van-tabs
          v-model:active="active"
          background="#181819"
          title-active-color="#FFF"
          title-inactive-color="#94999F"
          line-height="0.53333333vw"
          line-width="4.53333333vw"
          :sticky="!hasLive()"
          :offset-top="'42px'"
          shrink
          animated
          swipeable
        >
          <van-tab :title="item.title" v-for="(item, index) in list" :key="item.title + index">
            <!-- <div class="w-full bg-[#27272A] h-[1px]"></div> -->
            <!-- 除了聊天其他tab -->
            <KeepAlive v-show="index == 0">
              <div class="h-full">
                <!-- 聊天tab -->
                <component
                  ref="chartModalRef"
                  :is="Chat"
                  :matchName="{ homeName: roomData.homeName, awayName: roomData.awayName }"
                  :matchId="matchId"
                  :matchType="matchType"
                  :anchorInfo="anchorInfo"
                  @close-live="onCloseLive"
                />
              </div>
            </KeepAlive>
            <KeepAlive v-show="index > 0">
              <component
                :matchName="{
                  homeName: roomData.homeName,
                  homeLogo: roomData.homeLogo,
                  awayLogo: roomData.awayLogo,
                  awayName: roomData.awayName,
                }"
                :is="item.component"
                :matchId="matchId"
                :matchType="matchType"
                :anchorInfo="anchorInfo"
                :liveId="exceptLiveId"
              />
            </KeepAlive>
          </van-tab>
        </van-tabs>
      </div>
    </template>
    <IsMuted
      @on-back="cancelLive"
      ref="mutedRef"
      @is-muted="onIsMuted"
      v-if="anchorInfo && !anchorInfo.pureFlow"
      :anchor-id="anchorInfo.id"
    />
    <van-action-sheet safe-area-inset-bottom :closeable="true" close-on-click-action v-model:show="showAction">
      <div class="top-line-item ml-[310px] w-[72px] h-[8px] z-20" v-pressed-slide="handlePressedSlide"></div>
      <div class="text-[#37373D] border-b-[0.5px] border-[#E8EAEF] text-[30px] w-full h-[88px] flex items-center justify-center font-medium"
        >选择信号源</div
      >
      <div
        @click="onSelect(item)"
        :class="`flex click-style text-[28px] h-[100px] ${index != actions.length - 1 ? 'border-b-[0.5px]  border-[#E8EAEF]' : ''}  ${
          currentLiveId == item.liveId ? 'text-[#34A853] font-semibold' : 'text-[#37373D]'
        } h-[120px] items-center justify-center w-full`"
        v-for="(item, index) in actions"
        :key="index"
      >
        {{ item.nickName || '未知信号源' }}</div
      >
      <div
        @click="onCancelPop"
        class="flex text-[30px] click-style mt-[30px] bg-[#F2F3F7] font-medium text-[#94999F] rounded-[48px] h-[80px] items-center justify-center w-full"
      >
        取消</div
      >
    </van-action-sheet>
  </page-box>
</template>
<script setup name="RoomDetail">
  import { showToast } from 'vant';
  import { useRoute, useRouter } from 'vue-router';
  import dayjs from 'dayjs';
  import apis from '@/api/room';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  import { copyText } from '@/utils/copyText';
  import { useUserStore } from '@/store/modules/user';
  import { useMessageStore } from '@/store/modules/message';
  import { MessageCommand } from '@/views/message/utils/types';
  import { getCurrentEventState } from '@/components/SportsCard/useIndex';
  import { useAppStore } from '@/store/modules/app';
  import { pressedSlideDirective as vPressedSlide } from '@/plugins/pressedSlide';

  import barFooter from '@/assets/img/room/bar-footer.png';
  import barBasket from '@/assets/img/room/bar-basket.png';
  import barBlack from '@/assets/img/room/bar-black.png';
  import shareIcon from '@/assets/img/room/share-icon.png';
  import basketBk from '@/assets/img/room/room-basket-bk.png';
  import footerBk from '@/assets/img/room/room-footer-bk.png';
  import defaultRoomHeaderBk from '@/assets/img/room/default-room-notice-bk.png';
  import defaultTeamBasketballLogoSrc from '@/assets/img/sportsCard/icon_basketball_team.png';
  import defaultTeamFootballLogoSrc from '@/assets/img/sportsCard/icon_football_team.png';

  const IsMuted = defineAsyncComponent(() => import('./components/IsMuted.vue'));
  //直播播放器
  const LivePlayer = defineAsyncComponent(() => import('./components/LivePlayer.vue'));
  //聊天tab
  const Chat = defineAsyncComponent(() => import('./components/Chat/index.vue'));
  //篮球赛况
  const BasketStatus = defineAsyncComponent(() => import('./components/MatchStatus/BasketStatus.vue'));
  //足球赛况
  const FootStatus = defineAsyncComponent(() => import('./components/MatchStatus/FootStatus.vue'));
  //主播tab
  const Anchor = defineAsyncComponent(() => import('./components/Anchor/index.vue'));
  //主播信息
  const AnchorInfo = defineAsyncComponent(() => import('./components/AnchorInfo.vue'));
  //直播列表
  const Lives = defineAsyncComponent(() => import('./components/Lives/index.vue'));
  //足球阵容
  const FootLineUp = defineAsyncComponent(() => import('./components/LineUp/FootLineUp.vue'));
  //篮球阵容
  const BasketLineUp = defineAsyncComponent(() => import('./components/LineUp/BasketballLU.vue'));
  //赔率
  const Odds = defineAsyncComponent(() => import('./components/Odds/index.vue'));

  const messageStore = useMessageStore();
  const appStore = useAppStore();
  const showAction = ref(false);
  const actions = ref([]);
  const titleRef = ref();
  const bannerItem = ref(null);
  const chartModalRef = ref();
  const advertItem = ref(null);
  const active = ref(0);
  const route = useRoute();
  const router = useRouter();
  const { query } = route;
  const matchId = query.id;
  const matchType = query.type;
  const pureFlow = query.pureFlow;

  let liveId = query.liveId;
  const roomData = ref({});
  const anchorInfo = ref(null);
  const liveRoomInfo = ref(null);
  const showPlayerError = ref(false);
  const playerRef = ref();
  const loaded = ref(false);
  const mutedRef = ref();
  const userStore = useUserStore();
  const topHeader = ref('');
  const isMuted = ref(false);

  const list = ref([
    {
      title: '聊天',
    },
  ]);
  const currentLiveId = ref('');
  const exceptLiveId = ref('');
  const showNav = ref(false);
  let scrollTimer;
  const footerStatus = ref({ 9: '推迟', 10: '中断', 11: '腰斩', 12: '取消', 13: '待定' });
  const bsStatus = ref({ 11: '中断', 12: '取消', 13: '延期', 14: '腰斩', 15: '待定' });

  onMounted(async () => {
    createLoading({ overlay: false });
    loaded.value = false;
    firstLoad();
    onWsListen();
  });
  onActivated(() => {
    playerRef.value?.onPlay();
  });
  onDeactivated(() => {
    if (appStore.clearRoom) {
      onDestroyMsg();
    }
    playerRef.value?.onPause();
  });
  const onIsMuted = (value) => {
    isMuted.value = value;
    const onCheckChat = () => {
      if (!chartModalRef.value || !chartModalRef.value[0]) {
        setTimeout(() => {
          onCheckChat();
        }, 100);
      } else {
        chartModalRef.value[0]?.initMuted(value);
      }
    };
    nextTick(onCheckChat);
  };
  const getBarImg = () => {
    if (liveRoomInfo.value && !showPlayerError.value) {
      return barBlack;
    }
    if (matchType == 1) {
      return barFooter;
    } else {
      return barBasket;
    }
  };
  const onWsListen = () => {
    const onCheck = () => {
      const messageServer = messageStore.getMessageServer();
      if (!messageServer) {
        setTimeout(() => {
          onCheck();
        }, 1000);
        return;
      }
      messageServer.chatEmitter.on('messageAll', onUpdateRoomData);
    };
    onCheck();
  };
  const onDestroyMsg = () => {
    const messageServer = messageStore.getMessageServer();
    messageServer?.chatEmitter.off('messageAll', onUpdateRoomData);
  };
  onUnmounted(() => {
    console.log('onUnmounted');
    onDestroyMsg();
  });

  const hasLive = () => {
    return liveRoomInfo.value && liveRoomInfo.value.playUrl;
  };
  const handlePressedSlide = (start, move) => {
    if (move.y - start.y >= 10) {
      showAction.value = false;
    }
  };
  const getUserId = () => {
    return userStore.userInfo?.id;
  };

  const onUpdateRoomData = (res) => {
    const { data, command } = res;
    if (command === MessageCommand.matchChange && data && data.length > 0) {
      const findItem = data.find((item) => item.matchId == matchId);
      if (findItem) {
        roomData.value = { ...roomData.value, ...findItem };
      }
    } else if (command === MessageCommand.updateUrl && currentLiveId.value == data.id) {
      if (liveRoomInfo.value) {
        liveRoomInfo.value.playUrl = '';
        let playUrl = data.playUrl;
        if (playUrl.indexOf('://') == -1) {
          showPlayerError.value = true;
        } else {
          showPlayerError.value = false;
        }
        liveRoomInfo.value.playUrl = playUrl;
      } //主播id
    } else if (data && query.userId && query.userId + '' == data.anchorId + '') {
      if (command === MessageCommand.closeLive || command === MessageCommand.openLive) {
        if (command === MessageCommand.openLive) {
          if (data.matchId != matchId) {
            return;
          }
          router.replace({
            path: '/roomDetail',
            query: { liveId: data.id, userId: data.anchorId, type: data.matchType, id: data.matchId },
          });
        } else {
          //todo主播下播逻辑
          showPlayerError.value = false;
          liveRoomInfo.value.playUrl = '';
          liveRoomInfo.value.liveStatus = 3;
        }
      }
    } else if (command === MessageCommand.tickOut && data && data.bizId === query.liveId && data.userId == getUserId()) {
      mutedRef.value.onShowTickOut();
    }
  };

  const onScroll = (e) => {
    if (!titleRef.value) {
      return;
    }
    if (scrollTimer) {
      clearTimeout(scrollTimer);
      scrollTimer = null;
    }
    scrollTimer = setTimeout(() => {
      showNav.value = titleRef.value.offsetHeight < e.target.scrollTop;
    }, 0);
  };
  const onCloseLive = () => {
    if (liveRoomInfo.value) {
      delete liveRoomInfo.value.playUrl;
    }
  };
  const getFirstName = () => {
    if (matchType == 1) {
      return roomData.value.homeName;
    }
    return roomData.value.awayName;
  };
  const getFirstScore = () => {
    if (matchType == 1) {
      return roomData.value.homeScore;
    }
    return roomData.value.awayScore;
  };
  const getSecondScore = () => {
    if (matchType == 1) {
      return roomData.value.awayScore;
    }
    return roomData.value.homeScore;
  };
  const getFirstLogo = () => {
    if (matchType == 1) {
      return roomData.value.homeLogo || defaultTeamFootballLogoSrc;
    }
    return roomData.value.awayLogo || defaultTeamBasketballLogoSrc;
  };
  const getSecondLogo = () => {
    if (matchType == 1) {
      return roomData.value.awayLogo || defaultTeamFootballLogoSrc;
    }
    return roomData.value.homeLogo || defaultTeamBasketballLogoSrc;
  };
  const getSecondName = () => {
    if (matchType == 1) {
      return roomData.value.awayName;
    }
    return roomData.value.homeName;
  };
  const firstLoad = () => {
    apis.getAdvertising().then((res) => {
      console.log('getAdvertising', res);
      if (res && res.length > 0) {
        const randomNum = Math.floor(Math.random() * res.length);
        advertItem.value = res[randomNum];
      }
    });
    apis.getAdvertisingLive().then((res) => {
      bannerItem.value = { ...res };
    });
    onMatchDetail();
  };
  const getLiveRoomInfo = (id) => {
    if (userStore.token) {
      apis.addLiveHistory(id);
    }
    apis.getLivingInfo({ id }).then((res) => {
      if (res.playUrl.indexOf('://') == -1) {
        res.playUrl = 'http://www.baidu.com';
        showPlayerError.value = true;
      }
      liveRoomInfo.value = res;
      loaded.value = true;
    });
  };
  const onMatchDetail = () => {
    apis
      .getMatchDetail(matchId, matchType)
      .then((res) => {
        const { anchorList, matchData } = res;
        const { hasLineup, hasOdds, hasStata } = matchData || {};
        actions.value = [];
        const addOtherTabs = () => {
          if (hasStata) {
            list.value.push({
              title: '赛况',
              component: markRaw(matchType == 1 ? FootStatus : BasketStatus),
            });
          }
          if (hasLineup) {
            list.value.push({
              title: '阵容',
              component: markRaw(matchType == 1 ? FootLineUp : BasketLineUp),
            });
          }
          if (hasOdds) {
            list.value.push({
              title: '指数',
              component: markRaw(Odds),
            });
          }
          roomData.value = res;
        };
        let findItem;
        const onPureFlow = () => {
          currentLiveId.value = findItem?.liveId;
          exceptLiveId.value = undefined;
          const onCheckChat = () => {
            if (!chartModalRef.value || !chartModalRef.value[0]) {
              setTimeout(() => {
                onCheckChat();
              }, 100);
            } else {
              chartModalRef.value[0]?.initFirst(null);
            }
          };
          nextTick(onCheckChat);
          addOtherTabs();
          list.value.push({
            title: '直播',
            component: markRaw(Lives),
          });
          loaded.value = true;
        };
        if (pureFlow) {
          //纯净流模式
          findItem = anchorList.find((item) => item.pureFlow);
          onPureFlow();
        } else {
          if (query.liveId) {
            //直播间进来 选中当前的直播间
            findItem = { liveId: query.liveId, userId: query.userId };
          }
        }
        if (anchorList && anchorList.length > 1 && !pureFlow) {
          if (!findItem) {
            findItem = anchorList[0];
          }
          getLiveRoomInfo(findItem.liveId);
          liveId = findItem.liveId;
          addOtherTabs();
          getAnchorDetail(findItem.userId, findItem.liveId, () => {});
        } else {
          //只有纯净流情况
          if (anchorList && anchorList.length === 1 && !pureFlow) {
            findItem = anchorList[0];
            onPureFlow();
          }
        }
        closeLoading();
      })
      .catch(() => {
        closeLoading();
      });
  };

  const formatMatchDate = (time) => {
    if (time) {
      return dayjs(parseFloat(time)).format('MM-DD HH:mm');
    }
    return '';
  };
  const getMatchTime = (item) => {
    if (matchType == 2) {
      const itemData = getCurrentEventState('basketball', item.status + '');
      return itemData.name;
    }
    return item.runTime;
  };
  const isMatching = (status) => {
    if (matchType == 1) {
      if (status > 1 && status < 8 && status != 3) {
        return true;
      }
      return false;
    } else {
      if (status > 1 && status < 10) {
        return true;
      }
      return false;
    }
  };
  const getMatchData = (status) => {
    if (matchType == 1) {
      if (status >= 1 && status <= 8) {
        return status == 1 ? 'VS' : getFirstScore() + '-' + getSecondScore();
      }
      return 'VS';
    } else {
      if (status >= 1 && status <= 10) {
        return status == 1 ? 'VS' : getFirstScore() + '-' + getSecondScore();
      }
      return 'VS';
    }
  };
  const getMatchStatus = (status) => {
    if (matchType == 1) {
      if (status >= 1 && status <= 8) {
        return status == 1 ? '未开赛' : status == 3 ? '中场' : status == 8 ? '已完场' : '比赛中';
      }
      return footerStatus.value[status] || '比赛异常';
    } else {
      if (status >= 1 && status <= 10) {
        return status == 1 ? '未开赛' : status == 10 ? '已完场' : '比赛中';
      }
      return bsStatus.value[status] || '比赛异常';
    }
  };
  const showCenterTitle = (status) => {
    if (!loaded.value) {
      return false;
    }
    if (liveRoomInfo.value) {
      if (liveRoomInfo.value.playUrl || showPlayerError.value) {
        return false;
      }
      if (status == 1) {
        return true;
      }
      if (matchType == 1 && status == 8) {
        return true;
      } else {
        if (status == 10) {
          return true;
        }
      }
      return false;
    } else {
      return true;
    }
  };
  const onJump = (url) => {
    if (!url) {
      return;
    }
    window.location.href = url;
  };
  const getRoomTitle = () => {
    let matchTitle = roomData.value.competitionName + ' ' + getFirstName();
    matchTitle += roomData.value.status == 1 ? 'vs' : ' ' + getFirstScore() + ' : ' + getSecondScore() + ' ';
    return matchTitle + getSecondName();
  };
  const onErrorPlayer = () => {
    showPlayerError.value = true;
  };
  const onAttentionAnchor = (id, isFocus) => {
    if (!userStore.token) {
      router.push(`/login?redirect=${route.fullPath}`);
      return;
    }
    if (isFocus) {
      apis.onUnAttentionAnchor(id).then(() => {
        anchorInfo.value.focus = false;
        anchorInfo.value.fansCount = Math.max(anchorInfo.value.fansCount - 1, 0);
        // showToast({
        //   className: 'custom-toast-white',
        //   message: '已取关',
        // });
      });
      return;
    }
    apis.onAttentionAnchor(id).then(() => {
      anchorInfo.value.focus = true;
      anchorInfo.value.fansCount = Math.max(anchorInfo.value.fansCount + 1, 0);
      // showToast({
      //   className: 'custom-toast-white',
      //   message: '已关注',
      // });
    });
  };
  const getAnchorDetail = (id, liveId, resolve) => {
    currentLiveId.value = liveId;
    exceptLiveId.value = liveId;
    apis.getLiveUserInfo(id).then((res) => {
      res.fansCount = res.fansCount || 0;
      const fromNickName = res.nickName;
      anchorInfo.value = {
        ...res,
      };
      const onCheckChat = () => {
        if (!chartModalRef.value || !chartModalRef.value[0]) {
          setTimeout(() => {
            onCheckChat();
          }, 100);
        } else {
          chartModalRef.value[0]?.initNotice(res.notice);
          chartModalRef.value[0]?.initFirst({ anchorId: res.id, fromNickName, content: res.firstMessage });
        }
      };
      nextTick(onCheckChat);
      list.value.push({
        title: '主播',
        component: markRaw(Anchor),
      });
      resolve && resolve();
      list.value.push({
        title: '直播',
        component: markRaw(Lives),
      });
    });
  };
  const onShare = () => {
    const value = window.location.href;
    if (liveId && userStore.token) {
      apis.postShareLive(liveId);
    }
    copyText(value).then((success) => {
      showToast({
        // icon: 'custom-icon-success',
        className: 'custom-toast-white',
        message: success ? '复制链接成功' : '该浏览器不支持复制功能',
        // overlay: true,
      });
    });
  };
  const onShowAction = () => {
    apis.getMatchAnchors(matchId, matchType).then((anchorList) => {
      if (anchorList) {
        actions.value = [];
        if (liveId) {
          const hasCurrentLive = anchorList.find((item) => item.liveId + '' == liveId + '');
          if (!hasCurrentLive) {
            anchorList = [{ liveId, nickName: anchorInfo.value.nickName }, ...anchorList];
          }
        }
        anchorList.map((item) => {
          actions.value.push(item);
        });
      }
      showAction.value = true;
    });
  };
  const onPlayerReload = () => {
    showPlayerError.value = false;
    setTimeout(() => {
      playerRef.value?.onShowLoading();
    }, 100);
  };
  const onSelect = (data) => {
    if (data.liveId == liveId || (data.pureFlow && !liveId)) {
      onCancelPop();
      return;
    }
    setTimeout(() => {
      const { userId, pureFlow } = data;
      anchorInfo.value = data;
      if (pureFlow) {
        router.replace({ path: '/roomDetail', query: { type: matchType, id: matchId, pureFlow } });
      } else {
        router.replace({ path: '/roomDetail', query: { liveId: data.liveId, userId, type: matchType, id: matchId } });
      }
      onCancelPop();
    }, 200);
  };
  const onCancelPop = () => {
    showAction.value = false;
  };
  const cancelLive = () => {
    setTimeout(() => {
      appStore.backAnimation = true;
      window.history.back();
    }, 200);
  };
  watch(
    () => active.value,
    (value1, value2) => {
      if (!chartModalRef.value || !chartModalRef.value[0]) {
        return;
      }
      if (value1 == 1 && value2 == 0) {
        chartModalRef.value[0]?.onChangeHeight(false);
      } else if (value1 == 0) {
        chartModalRef.value[0]?.onChangeHeight(true);
      }
    },
    {
      immediate: true,
    },
  );
</script>
<style scoped lang="scss">
  :deep().van-action-sheet__content {
    margin: 20px 24px 60px 24px;
  }

  .top-line-item {
    background: url('@/assets/img/room/top-line.png') no-repeat center;
    background-size: contain;
  }

  .room-detail {
    height: 100%;
    background: #181819;
    font-family: PingFang SC;
    &.live {
      overflow-y: hidden !important;
    }

    .live-tab {
      .van-tabs__content {
        .van-swipe-item {
          height: calc(100vh - 666px - var(--app-header-height));
          overflow-y: scroll;
        }
      }
    }
    .live-tab-btm-bar {
      .van-tabs__content {
        .van-swipe-item {
          // height: calc(100vh - 836px - var(--app-header-height));
          height: calc(var(--device-height) - 89.13vw - var(--app-header-height));
          overflow-y: scroll;
        }
      }
    }

    .chat-page {
      .van-tabs__content {
        .van-swipe-item {
          height: calc(100vh - 564px - var(--app-header-height));
          overflow-y: scroll;
        }
      }
    }
    .chat-page-bar {
      .van-tabs__content {
        .van-swipe-item {
          // height: calc(var(--device-height) - 666px - var(--app-header-height));
          // height: calc(100vh - 736px - var(--app-header-height));
          height: calc(var(--device-height) - 75.2vw - var(--app-header-height));
          overflow-y: scroll;
        }
      }
    }

    .number-text {
      font-family: MiSans;
      line-height: normal;
      letter-spacing: 0.1px;
    }

    .state-dot {
      animation: sportsStateOpacity 1s linear infinite;
      padding-left: 2px;
    }

    .org-url {
      background: rgba(255, 255, 255, 0.25);
    }

    .title-header-2 {
      display: flex;
      width: 100%;
      height: 88px;
      background: url('@/assets/img/room/basket-header.png') no-repeat center;
      background-size: cover;
    }
    .title-header-1 {
      display: flex;
      width: 100%;
      height: 88px;
      background: url('@/assets/img/room/footer-header.png') no-repeat center;
      background-size: cover;
    }

    :deep().van-tabs__line {
      background: #34a853;
      margin-left: -36px;
      bottom: 30px;
    }
    :deep().van-tab--shrink {
      padding: 0;
    }
    :deep().van-tab__text {
      font-size: 28px;
      font-weight: 400;
      margin-top: 0px;
    }
    --van-tabs-line-height: 79px;
    --van-padding-xs: 6px;
    :deep().van-tab--active {
      .van-tab__text {
        font-size: 30px;
        font-weight: 500;
        line-height: normal;
        padding-top: 2px;
      }
    }
    :deep().van-tab:active {
      opacity: 0.3;
    }
    :deep().van-tabs__nav--line {
      // padding-bottom: 30px;
      // border-radius: 80px;
    }
    :deep().van-tab {
      padding: 0 72px 0 0;
    }
    .chat-item {
      background: linear-gradient(180deg, #fbe457 0%, #e27836 98.96%);
    }
    .share-item {
      background: linear-gradient(180deg, #593ec6 0%, #4327cf 100%);
    }

    :deep().van-tabs__wrap {
      height: 78px;
      line-height: 78px;
      background: transparent;
      border-bottom: 1px solid #27272a;
    }
    :deep(.van-tabs) {
      height: 100%;
      display: flex;
      flex-flow: column;
      --van-padding-xs: 24px;

      .van-tabs__content {
        flex: 1 1 auto;
        .van-tab__panel {
          height: 100%;
          // padding-left: 24px;
        }
      }
    }
  }
</style>
