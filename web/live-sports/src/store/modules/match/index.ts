import { defineStore } from 'pinia';
import dayjs from 'dayjs';
import apis from '@/api/match';
import defaultLogoSrc from '@/assets/img/user/default_anchor.png';
import { formatCardData, getCurrentSportType, getCurrentEventState } from '@/components/SportsCard/useIndex';
import { createLoading, resetLoading } from '@/components/SysLoading';

export const useMatchStore = defineStore(
  'app-match',
  () => {
    const matchTabs = ref<any>([]);

    const showCalendar = ref(false);

    const matchPageData = ref<any>({});

    const matchPageScroll = ref<any>({});

    const matchList = ref<any>([]);

    const selectDate = ref('');
    const finishedKey = ref({});
    const loadingKey = ref({});

    const refreshing = ref(false);
    const queryParams = ref({ current: 1, size: 8 });

    const cacheActive = ref<any>({ 0: 0, 1: 0, 2: 0, 3: 0 });

    const liveStatusText = {
      '1': '暂未开播',
      '2': '正在开播',
      '3': '暂未开播', // 观看历史只有正在直播和未开始，已完播状态也属于未开始状态
    };

    // const currentTime = ref(0);
    // match tip缓存
    let matchTipCache = {};
    //tab缓存
    const cacheTabs = ref({});

    //一级筛选
    const oneTabReq = ref({ status: null });
    //一级塞选active
    const oneTabStyle = ref(0);
    //二级筛选
    const twoTabReq = ref({ competitionId: '', startTime: '', endTime: '', matchType: '', status: undefined, sortType: undefined });
    //二类切换
    const onResetSort = (req, isReq) => {
      // req.matchType = req.matchType || 1;
      twoTabReq.value = req;
      if (isReq) {
        setTimeout(() => {
          onGetMatchList();
        }, 20);
      }
    };

    const getCompetitionId = () => {
      if (twoTabReq && twoTabReq.value) {
        return twoTabReq.value.competitionId;
      }
      return '';
    };

    const getTabList = async (data, defaultTabs, reqTime, style) => {
      oneTabStyle.value = style;
      const onSetData = (tabListData) => {
        matchTabs.value = tabListData;
        oneTabReq.value = data;
        onResetSort({ ...data, ...reqTime }, true);
      };
      const onReqData = async () => {
        const reqData = JSON.parse(JSON.stringify(data));
        if (reqData.sortType) {
          delete reqData.matchType;
          delete reqData.sortType;
        }
        createLoading({ overlay: false });
        const response = await apis.getTabSorts(reqData);
        cacheTabs.value[style] = [...defaultTabs, ...response];
        onSetData(cacheTabs.value[style]);
      };
      //缓存大tab类目下的分类列表
      if (cacheTabs.value[style]) {
        const shouldRefresh = cacheTabs.value[style].find((item) => item.shouldRefresh);
        if (shouldRefresh) {
          onReqData();
          return;
        }
        onSetData(cacheTabs.value[style]);
        return;
      }
      onReqData();
    };
    const setReqTime = (value) => {
      const { startTime, endTime } = value;
      twoTabReq.value = { ...twoTabReq.value, startTime, endTime };
      clearCacheMatchList();
      onMatchListRefresh('');
    };
    const clearCacheMatchList = () => {
      matchPageData.value[getListPageKey()] = null;
    };
    const onShowOrHideCalendar = (value) => {
      showCalendar.value = value;
    };
    const clearTabAndList = () => {
      matchTabs.value = [];
    };
    const setOneTabReq = (value, style) => {
      oneTabStyle.value = style;
      oneTabReq.value = value;
    };
    const getListPageKey = () => {
      const listKey = oneTabStyle.value + 'x-' + (getCompetitionId() || '') + '-x' + (twoTabReq.value.matchType || '');
      return listKey;
    };
    const setMatchPageData = (list, current, finished, selectDateTime, listKey) => {
      matchPageData.value[listKey] = { list, current, finished, selectDateTime };
    };
    const getMatchPageData = () => {
      return matchPageData.value[getListPageKey()];
    };
    const getKeyMatchPageData = (key) => {
      return matchPageData.value[key];
    };
    const setMatchPageScroll = (scrollTop, key) => {
      key = key || getListPageKey();
      matchPageScroll.value[key] = scrollTop;
    };
    const getMatchPageScroll = (key) => {
      return matchPageScroll.value[key || getListPageKey()];
    };
    const getKeyList = (key) => {
      const pageListData = matchPageData.value[key];
      if (pageListData) {
        // if (key != getListPageKey()) {
        //   //TODO 防止渲染数据过多
        //   return pageListData.list.slice(0, 4);
        // }
        return pageListData.list;
      }
      return [];
    };

    const onGetMatchList = () => {
      const pageListData = getMatchPageData();
      const onRefreshPage = () => {
        loadingKey.value[getListPageKey()] = true;
        onMatchListRefresh('');
        setTimeout(() => {
          onScrollTop(0);
        }, 0);
      };
      if (pageListData) {
        //除了赛果菜单，如果有比赛已经打完要重新刷新UI
        const shouldRefresh = pageListData.list && pageListData.list.find((item) => item.shouldRefresh);
        if (shouldRefresh) {
          onRefreshPage();
          return;
        }
        loadingKey.value[getListPageKey()] = false;
        const { current } = pageListData;
        finishedKey.value[getListPageKey()] = pageListData.finished;
        queryParams.value.current = current;
        selectDate.value = pageListData.selectDateTime;
        // setTimeout(() => {
        //   const scrollTop = getMatchPageScroll(key);
        //   console.log('xxxScroll', key, scrollTop, matchPageScroll.value);
        //   if (scrollTop) {
        //     onScrollTop(scrollTop);
        //   }
        // }, 0);
        resetLoading();
        return;
      }
      onRefreshPage();
    };

    const onScrollTop = (top) => {
      const currentItem = document.getElementById(getListPageKey());
      if (currentItem) {
        currentItem.scrollTop = top;
      }
    };

    const getMatchList = (loadType, listKey) => {
      const reqParams = { ...queryParams.value, ...oneTabReq.value, ...twoTabReq.value };
      if (!reqParams.startTime || !reqParams.endTime) {
        return;
      }
      loadingKey.value[listKey] = true;
      if (reqParams.current == 1 && loadType != 1) {
        createLoading({ overlay: false });
      }
      if (reqParams.sortType) {
        delete reqParams.sortType;
      }
      apis.getMathList(reqParams).then((res: any) => {
        if (reqParams.current == 1) {
          resetLoading();
        }
        if (res.records && res.records.length == 0) {
          loadingKey.value[listKey] = false;
          finishedKey.value[listKey] = true;
          refreshing.value = false;
          return;
        }
        const weeks = ['周日', '周一', '周二', '周三', '周四', '周五', '周六'];
        const now = dayjs().format('YYYY-MM-DD') + ' ' + weeks[dayjs().day()];
        const newList = formatCardData(res.records).map((cur) => {
          if (cur.matchDate) {
            if (now === cur.matchDate || matchTipCache[cur.matchDate]) {
              cur.matchDate = '';
            } else {
              matchTipCache[cur.matchDate] = true;
            }
          }
          return cur;
        });
        if (!matchPageData.value[listKey]) {
          matchPageData.value[listKey] = { list: [] };
        }
        if (reqParams.current == 1) {
          matchPageData.value[listKey].list = newList;
        } else {
          matchPageData.value[listKey].list = matchPageData.value[listKey].list.concat(newList);
        }
        loadingKey.value[listKey] = false;
        finishedKey.value[listKey] = matchPageData.value[listKey].list.length === res.total;
        queryParams.value.current++;
        refreshing.value = false;
        const selectDateTime = reqParams.status ? reqParams.endTime : reqParams.startTime;
        selectDate.value = selectDateTime;
        setMatchPageData(matchPageData.value[listKey].list, queryParams.value.current, finishedKey.value[listKey], selectDateTime, listKey);
      });
    };
    const onAllRefreshData = () => {
      matchPageData.value = {};
      onMatchListRefresh('');
    };
    //列表数组刷新
    const onMatchListRefresh = (key) => {
      key = key || getListPageKey();
      refreshing.value = false;
      finishedKey.value[key] = false;
      queryParams.value.current = 1;
      matchTipCache = {};
      if (!matchPageData.value[key]) {
        matchPageData.value[key] = { list: [] };
      }
      matchPageData.value[key].list = [];
      getMatchList(0, key);
    };
    //列表数组刷新
    const onPullMatchListRefresh = () => {
      refreshing.value = true;
      finishedKey.value[getListPageKey()] = false;
      queryParams.value.current = 1;
      matchTipCache = {};
      getMatchList(1, getListPageKey());
    };
    const clearData = () => {
      loadingKey.value[getListPageKey()] = true;
      matchTipCache = {};
      queryParams.value.current = 1;
      refreshing.value = false;
    };
    //list加载更多
    const onMatchListLoad = (key) => {
      if (key != getListPageKey()) {
        loadingKey.value[key] = false;
        return;
      }
      loadingKey.value[key] = true;
      getMatchList(0, key);
    };
    //ws 推送数据改变
    const onDataChange = (changeList) => {
      if (!changeList || changeList.length == 0) {
        return;
      }
      Object.keys(matchPageData.value).map((key) => {
        const cacheMatchItem = getKeyMatchPageData(key);
        if (cacheMatchItem && cacheMatchItem.list && cacheMatchItem.list.length > 0) {
          filterList(cacheMatchItem.list, changeList, key);
        }
      });
    };
    const filterList = (cacheList, changeList, key) => {
      const filterListData = cacheList.filter((item) => changeList.find((itemData) => itemData.matchId == item.matchId));
      filterListData.forEach((item) => {
        const findItem = changeList.find((itemData) => itemData.matchId == item.matchId);
        const index = cacheList.findIndex((itemData) => itemData.matchId == item.matchId);
        if (index == -1) {
          return;
        }
        const oldData = cacheList[index];
        const currentSportType = getCurrentSportType(findItem.matchType + '');
        const currentEventState = getCurrentEventState(currentSportType.icon, findItem.status + '');
        let shouldRefresh = false;
        if (oneTabStyle.value == 3) {
          shouldRefresh = true;
        } else {
          if (oldData.matchType == 1) {
            if (oldData.status == 1 && findItem.status == 2) {
              shouldRefresh = true;
            } else if (findItem.status == 8) {
              shouldRefresh = true;
            }
          } else {
            if (findItem.status == 10) {
              shouldRefresh = true;
            }
          }
        }
        cacheList[index].homeTeamScore = findItem.homeScore;
        cacheList[index].homeTeamHalfScore = findItem.homeHalfScore;
        cacheList[index].visitTeamHalfScore = findItem.awayHalfScore;
        cacheList[index].visitTeamScore = findItem.awayScore;
        cacheList[index].status = findItem.status;
        cacheList[index].runTime = findItem.runTime;
        cacheList[index].isChangeed = true;
        cacheList[index].eventState = currentEventState.value || ''; // 比赛状态
        cacheList[index].eventStateText = currentEventState.name || ''; // 比赛状态文本
        cacheList[index].eventStateClass = currentEventState.class || ''; // 比赛状态特殊样式
        cacheList[index].shouldRefresh = shouldRefresh;
      });
      if (matchPageData.value[key]) {
        matchPageData.value[key].list = cacheList;
      }
    };

    const setCurrentActive = (activeId) => {
      cacheActive.value[oneTabStyle.value] = activeId;
    };
    const getCurrentActive = () => {
      return cacheActive.value[oneTabStyle.value];
    };
    const onAddAnchor = (data) => {
      Object.keys(matchPageData.value).map((key) => {
        const cacheMatchItem = getKeyMatchPageData(key);
        if (cacheMatchItem && cacheMatchItem.list && cacheMatchItem.list.length > 0) {
          const index = cacheMatchItem.list.findIndex((item) => item.matchId == data.matchId);
          if (index != -1) {
            cacheMatchItem.list[index].anchorList = cacheMatchItem.list[index].anchorList || [];
            const hasAnchor = cacheMatchItem.list[index].anchorList.find((anchor) => anchor.id + '' === data.anchorId + '');
            if (!hasAnchor) {
              const liveStatus = data.liveStatus;
              const addItemData = {
                name: data.nickName, // 主播名称
                id: (data.anchorId || '') + '', // id
                cover: data.userLogo || defaultLogoSrc, // 主播头像
                liveStatus: liveStatus, // 直播状态
                liveStatusText: liveStatusText[liveStatus] || '', // 直播状态
                matchType: data.matchType,
                matchId: data.matchId,
                liveId: data.id,
              };
              if (cacheMatchItem.list[index].anchorList.length >= data.rank - 1) {
                cacheMatchItem.list[index].anchorList.splice(data.rank - 1, 0, addItemData);
              } else {
                cacheMatchItem.list[index].anchorList.push(addItemData);
              }
            }
          }
        }
      });
    };
    const onRemoveAnchor = (data) => {
      Object.keys(matchPageData.value).map((key) => {
        const cacheMatchItem = getKeyMatchPageData(key);
        if (cacheMatchItem && cacheMatchItem.list && cacheMatchItem.list.length > 0) {
          const index = cacheMatchItem.list.findIndex((item) => item.matchId + '' == data.matchId + '');
          if (index != -1 && cacheMatchItem.list[index].anchorList) {
            const anchorIndex = cacheMatchItem.list[index].anchorList.findIndex((anchor) => anchor.id + '' == data.anchorId + '');
            if (anchorIndex != -1) {
              cacheMatchItem.list[index].anchorList.splice(anchorIndex, 1);
            }
          }
        }
      });
    };
    const getSecondSort = (style) => {
      return cacheTabs.value[style];
    };

    return {
      getTabList,
      matchTabs,
      clearTabAndList,
      oneTabReq,
      setOneTabReq,
      twoTabReq,
      onResetSort,
      oneTabStyle,
      onShowOrHideCalendar,
      showCalendar,
      getCompetitionId,
      setReqTime,
      setMatchPageData,
      getMatchPageData,
      getMatchPageScroll,
      setMatchPageScroll,
      matchPageScroll,
      getMatchList,
      matchList,
      onMatchListLoad,
      finishedKey,
      loadingKey,
      refreshing,
      queryParams,
      onMatchListRefresh,
      onPullMatchListRefresh,
      //退出登录赛程所有数据刷新
      onAllRefreshData,
      onDataChange,
      clearData,
      setCurrentActive,
      getCurrentActive,
      onAddAnchor,
      onRemoveAnchor,
      selectDate,
      getKeyList,
      getSecondSort,
    };
  },
  {
    persist: false,
  },
);
