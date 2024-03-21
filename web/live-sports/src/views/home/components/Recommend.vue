<template>
  <!-- <skeleton-home v-if="showSkeleton" /> -->
  <div class="w-full h-full overflow-auto" ref="scrollRef" v-show="loaded">
    <div class="h-full">
      <div class="overflow-hidden mt-[24px]" @touchstart.stop>
        <swiper
          effect="fade"
          class="my-swipe"
          :spaceBetween="50"
          :autoplay="{
            delay: 3000,
            disableOnInteraction: false,
          }"
          :modules="[Autoplay, Pagination, Thumbs, Virtual]"
          :pagination="{ clickable: true }"
          :slides-per-view="1"
          :touch-move-stop-propagation="true"
          :touch-start-force-prevent-default="true"
          touch-events-target="container"
          :virtual="true"
          @swiper="setSwiperRef"
        >
          <swiper-slide v-for="(item, index) in bannerList" :key="index" :virtualIndex="index">
            <div v-ripple class="relative rounded-[20px] w-full h-full flex items-center justify-center" @click="onJump(item.targetUrl)">
              <img class="w-[702px] max-h-[330px] rounded-[20px] object-cover" :src="item.imgUrl" />
              <div class="filter-bg w-[702px] h-[168px] absolute bottom-0 left-[24px] z-1"></div>
            </div>
          </swiper-slide>
        </swiper>
      </div>

      <div class="w-[702px] justify-between text-[28px] mt-[30px] items-center flex mx-[24px]">
        <div class="flex items-center flex-shrink-0 w-[520px] truncate">
          <span class="text-[#37373D] text-[32px] font-medium">热门比赛 </span>
        </div>
        <span class="text-[#37373D] text-[26px] font-normal number-text">{{ totalHotNum }}场</span>
      </div>
      <div class="flex mt-[24px] overflow-x-scroll whitespace-nowrap pr-[24px]" @touchend.stop @touchstart.stop @touchmove.stop>
        <div
          v-ripple
          class="ml-[24px] px-[24px] text-[#37373D] w-[452px] h-[244px] bg-[#fff] rounded-[20px] shrink-0 flex flex-col"
          @click="
            $router.push({
              path: '/roomDetail',
              query: {
                id: item.matchId,
                liveId: item.anchorList && item.anchorList[0] ? item.anchorList[0].id : undefined,
                type: item.matchType,
                userId: item.anchorList && item.anchorList[0] ? item.anchorList[0].userId : undefined,
              },
            })
          "
          v-for="item in hotMatchList"
          :key="item.matchId"
        >
          <div class="flex items-center justify-between text-[20px] mt-[22px] h-[36px]">
            <div class="flex items-center">
              <div class="text-[22px]">
                {{ item.competitionName }}
              </div>
              <match-status :matchData="item" />
            </div>
            <img v-if="item.status > 1" class="w-[86px] h-full mt-[2px] bg-contain" :src="dotIcon" />
            <div v-if="item.status <= 1" class="text-[#94999F] mt-[2px] text-[22px] number-text">{{ timeFormat(item) }} </div>
          </div>

          <div class="flex mt-[20px] items-center justify-between text-[24px]">
            <div class="flex items-center">
              <div class="w-[60px] flex items-center justify-center h-[60px]">
                <DefaultImg class="max-w-[60px] max-h-[60px] bg-contain" :src="item.homeLogo || defaultTeamLogo" type="default" />
              </div>
              <span class="ml-[20px] max-w-[180px] truncate">{{ item.homeName }}</span>
            </div>
            <div class="text-[#37373D] text-[22px] font-semibold number-text">{{ item.status < 2 ? '' : item.homeScore }}</div>
          </div>

          <div class="flex justify-between items-center mt-[20px] text-[24px]">
            <div class="flex items-center">
              <div class="w-[60px] flex items-center justify-center h-[60px]">
                <DefaultImg class="max-w-[60px] max-h-[60px] bg-contain" :src="item.awayLogo || defaultTeamLogo" type="default" />
              </div>
              <span class="ml-[20px] max-w-[180px] truncate">{{ item.awayName }}</span>
            </div>
            <div class="text-[#37373D] text-[22px] font-semibold number-text">{{ item.status < 2 ? '' : item.awayScore }}</div>
          </div>
        </div>
      </div>

      <div class="mt-[29px] mx-[24px]">
        <div class="font-medium text-[32px] text-[#37373D]">正在直播</div>
        <div class="mt-[22px] pb-[12px]">
          <VirtualListScroller
            v-show="list.length > 0"
            v-model="loading"
            :finished="finished"
            :finished-text="list.length > 0 ? LOAD_MORE_TEXT : ''"
            :noBottom="true"
            @load="onLoad"
            :list="list"
          >
            <template #default="{ itemData }">
              <div class="grid grid-cols-2 pb-[16px] gap-x-[14px]">
                <template v-for="subItem in itemData.data" :key="subItem.id">
                  <room textColor="#37373D" :data="subItem" />
                </template>
              </div>
            </template>
          </VirtualListScroller>
        </div>
        <div class="mt-[54px]" v-if="!loading && list.length == 0">
          <empty-data text="暂无主播" icon="anchor" />
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
  // import Swiper core and required modules

  // Import Swiper Vue.js components
  import { Swiper, SwiperSlide } from 'swiper/vue';
  import { Autoplay, Pagination, Thumbs, Virtual } from 'swiper/modules';
  import dayjs from 'dayjs';
  import apis from '@/api/home/index';
  import { useMessageStore } from '@/store/modules/message';
  import { MessageCommand } from '@/views/message/utils/types';
  import { useResourceStore } from '@/store/modules/resource';
  import { onClosePage } from '@/utils/closeStartPage';
  import { LOAD_MORE_TEXT } from '../../../../build/constant';
  import { createLoading, closeLoading } from '@/components/SysLoading';
  import { useRestoreScroll } from '@/plugins/restoreScroll';
  import { VirtualListScroller } from '@/components/common/VirtualListScroller';
  import DefaultImg from '@/components/common/DefaultImg.vue';
  import defaultTeamLogo from '@/assets/img/sportsCard/default_logo.png';
  import dotIcon from '@/assets/img/home/live-dot.png';

  // Import Swiper styles
  import 'swiper/css';
  import 'swiper/css/pagination';
  import 'swiper/css/virtual';

  const resourceStore = useResourceStore();
  const EmptyData = defineAsyncComponent(() => import('@/components/common/EmptyData.vue'));
  const MatchStatus = defineAsyncComponent(() => import('@/components/common/MatchStatus.vue'));
  const Room = defineAsyncComponent(() => import('@/components/common/RoomItem.vue'));
  const refreshing = ref(false);
  const messageStore = useMessageStore();
  const showSkeleton = ref(true);
  const finished = ref(false);
  const bannerList = computed(() => {
    return resourceStore.bannerList;
  });
  const totalHotNum = ref(0);
  const hotMatchList = ref([]);
  const dateStr = ref('');
  const list = ref([]);
  const loading = ref(true);
  const queryParams = ref({ current: 1, size: 20 });
  const scrollRef = ref();
  const swiperRef = ref();
  const loaded = ref(false);
  const loadingTime = ref(10);
  let timeTimeout;
  let historyId = 0;
  useRestoreScroll({
    el: scrollRef,
  });

  const setSwiperRef = (swiper) => {
    swiperRef.value = swiper;
  };

  onMounted(async () => {
    const timeStatus = document.querySelector('.time-loading-status');
    //结束index.html里的重试事件
    timeStatus.innerHTML = 2;
    //进入到首页第一次10秒中，如果预加载接口数据，如果加载失败每隔五秒重试一次
    loadingTime.value = 10;
    onFirstLoad();
  });
  const onFirstLoad = () => {
    onLoadAllData(false);
    onWsListen();
    onListenLoading();
  };
  const onListenLoading = () => {
    const loadingToast = document.querySelector('.loading-toast-icon');
    timeTimeout = setTimeout(() => {
      loadingTime.value--;
      if (loadingTime.value < 0) {
        if (loadingToast) {
          loadingToast.style.display = 'flex';
        }
        setTimeout(() => {
          loadingTime.value = 5;
          onClearTimer();
          loadingToast.style.display = 'none';
          onFirstLoad();
        }, 1500);
        return;
      }
      onListenLoading();
    }, 1000);
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
      messageServer.chatEmitter.on('messageAll', (res) => {
        setTimeout(() => {
          if (res.command === MessageCommand.openLive) {
            onAddLiveItem(res.data);
          } else if (res.command === MessageCommand.closeLive) {
            onRemoveLiveItem(res.data);
          }
        }, 200);
      });
    };
    onCheck();
  };
  //im 通知新增主播开播
  const onAddLiveItem = (item) => {
    let allList = [];
    let findData = false;
    if (list.value && list.value.length > 0) {
      list.value.forEach((itemData) => {
        itemData &&
          itemData.data.forEach((childItemData) => {
            if (childItemData.userId + '' == item.anchorId + '') {
              findData = true;
            }
            allList.push(childItemData);
          });
      });
    }
    if (findData) {
      return;
    }
    allList.push({ ...item, userId: item.anchorId, titlePage: item.coverImg });
    allList.sort(function (a, b) {
      return b.hotValue - a.hotValue;
    });
    let newIndex = 0;
    let newList = [];
    allList.forEach((cur) => {
      if (newList[newIndex]) {
        newList[newIndex].data.push(cur);
        newIndex++;
      } else {
        newList[newIndex] = {
          id: historyId + '',
          data: [cur],
        };
        historyId++;
      }
    });
    list.value = newList;
    // console.log("data",list.value)
    // list.value.push({ ...item, userId: item.anchorId, titlePage: item.coverImg });
    // list.value.sort(function (a, b) {
    //   return b.hotValue - a.hotValue;
    // });
  };
  //im 通知已开播主播关播
  const onRemoveLiveItem = (data) => {
    let newListData = [];
    if (list.value && list.value.length > 0) {
      list.value.forEach((item) => {
        item.data &&
          item.data.forEach((childItem) => {
            if (childItem.userId + '' != data.anchorId + '') {
              newListData.push(childItem);
            }
          });
      });
      let newList = [];
      let newIndex = 0;
      newListData.forEach((cur) => {
        if (newList[newIndex]) {
          newList[newIndex].data.push(cur);
          newIndex++;
        } else {
          newList[newIndex] = {
            id: historyId + '',
            data: [cur],
          };
          historyId++;
        }
      });
      list.value = newList;
    }

    // const index = list.value.findIndex((item) => item.userId + '' == data.anchorId + '');
    // if (index == -1) {
    //   return;
    // }
    // list.value.splice(index, 1);
  };
  const onLoadAllData = (isRefresh) => {
    loading.value = true;
    getCurrentDate();
    if (!isRefresh) {
      createLoading({ overlay: false });
    }
    //推荐获取热门比赛列表， 正在直播列表 接口响应后关闭启动页面
    Promise.all([getHotGames(), getHomeLives(isRefresh)]).then(() => {
      closeLoading();
      refreshing.value = false;
      onClearTimer();
      setTimeout(() => {
        loaded.value = true;
        onClosePage();
      }, 600);
    });
  };
  const onClearTimer = () => {
    if (timeTimeout) {
      clearTimeout(timeTimeout);
      timeTimeout = null;
    }
  };

  const onLoad = () => {
    loading.value = true;
    if (refreshing.value) {
      list.value = [];
      refreshing.value = false;
    }
    getHomeLives();
  };
  const timeFormat = (item) => {
    return dayjs(parseFloat(item.matchTime || item.openTime)).format('MM-DD HH:mm');
  };
  const getHotGames = () => {
    setTimeout(() => {
      getHotGames();
    }, 60000);
    return new Promise((resolve) => {
      apis.getHotMaths({ top: 20 }).then((res) => {
        nextTick(() => {
          totalHotNum.value = res.length;
          res &&
            res.map((item) => {
              const { awayName, homeName, matchType, awayScore, homeScore, awayLogo, homeLogo, awayHalfScore, homeHalfScore } = item;
              if (matchType == 2) {
                item.homeLogo = awayLogo;
                item.awayLogo = homeLogo;
                item.homeName = awayName;
                item.awayName = homeName;
                item.homeScore = awayScore;
                item.awayScore = homeScore;
                item.homeHalfScore = awayHalfScore;
                item.awayHalfScore = homeHalfScore;
              }
              return item;
            });
          hotMatchList.value = res;
          showSkeleton.value = false;
          if (res && res[0]) {
            dateStr.value = dayjs(parseFloat(res[0].matchTime)).format('M月D日');
          }
          resolve();
        });
      });
    });
  };
  const getHomeLives = () => {
    return new Promise((resolve) => {
      apis.getHomeLiving(queryParams.value).then((res) => {
        let { total, records } = res;
        const oldIndex = list.value.length - 1;
        const lastItem = oldIndex >= 0 ? list.value[oldIndex] : {};
        if (lastItem.data && lastItem.data.length === 1 && records.length > 0) {
          lastItem.data.push(records[0]);
          records = records.slice(1);
        }
        let newIndex = 0;
        const newList = [];
        records.forEach((cur) => {
          if (newList[newIndex]) {
            newList[newIndex].data.push(cur);
            newIndex++;
          } else {
            newList[newIndex] = {
              id: historyId + '',
              data: [cur],
            };
            historyId++;
          }
        });
        list.value.push(...newList);
        loading.value = false;
        if (queryParams.value.current * queryParams.value.size >= total || total === undefined) {
          finished.value = true;
        }
        queryParams.value.current++;
        resolve();
      });
    });
  };
  const getCurrentDate = () => {
    dateStr.value = dayjs(new Date()).format('M月D日');
  };

  const onJump = (url) => {
    if (url) {
      window.location.href = url;
    }
  };
</script>
<style scoped lang="scss">
  .swiper {
    --swiper-theme-color: rgba(255, 255, 255, 0.5);
    :deep().swiper-pagination-bullet {
      width: 10px;
      height: 10px;
      border-radius: 40px;
      background: rgba(255, 255, 255, 0.5);
      flex-shrink: 0;
      margin-right: 6px;
    }
    :deep().swiper-pagination-horizontal {
      bottom: 13px;
      left: -20px;
      z-index: 10;
    }
    :deep().swiper-pagination-bullet-active {
      width: 40px;
      height: 10px;
      border-radius: 50px;
      background: #fff;
    }
  }

  .number-text {
    font-family: MiSans;
  }
  // :deep().van-swipe__indicator {
  //   width: 10px;
  //   height: 10px;
  //   border-radius: 40px;
  //   background: rgba(255, 255, 255, 0.5);
  // }
  // :deep() .van-swipe__indicator--active {
  //   width: 40px;
  //   height: 10px;
  //   border-radius: 50px;
  // }
  .my-swipe {
    height: 330px;
    color: #fff;
    border-radius: 20px;
    font-size: 20px;
    text-align: center;
    .filter-bg {
      border-radius: 0px 0px 20px 20px;
      background: linear-gradient(180deg, rgba(27, 31, 30, 0) 0%, rgba(27, 31, 30, 0.8) 100%);
    }
  }
  .van-pull-refresh {
    :deep() {
      .van-pull-refresh__track {
        min-height: calc(var(--device-height) - 192px);
      }
    }
  }
  :deep(.vue-recycle-scroller__item-wrapper) {
    margin-bottom: 12px !important;
  }
</style>
