/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sports.app.service.service.impl;


import com.alicp.jetcache.anno.Cached;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Table;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.HotMatchDO;
import org.sports.admin.manage.dao.entity.MyFollowDO;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.LiveIdTypeRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.enums.BasketballStatusType;
import org.sports.admin.manage.service.enums.FootballStatusType;
import org.sports.admin.manage.service.service.IHotMatchService;
import org.sports.admin.manage.service.service.ILiveService;
import org.sports.admin.manage.service.service.ILiveUserService;
import org.sports.admin.manage.service.service.SdkService;
import org.sports.admin.manage.service.vo.*;
import org.sports.admin.manage.service.enums.NationalType;
import org.sports.app.service.enums.QueryMatchStatus;
import org.sports.app.service.req.MatchCountRequest;
import org.sports.app.service.req.MatchPageRequest;
import org.sports.app.service.req.MatchRequest;
import org.sports.app.service.req.OngoingMatchRequest;
import org.sports.app.service.service.IBasketballLiveDetailService;
import org.sports.app.service.service.IFootballLiveDetailService;
import org.sports.app.service.service.IMyFollowService;
import org.sports.app.service.service.IScheduleService;
import org.sports.app.service.utils.MatchUtils;
import org.sports.app.service.vo.HotMatchVo;
import org.sports.app.service.vo.MatchIdType;
import org.sports.admin.manage.service.dto.MatchScoreDTO;
import org.sports.app.service.vo.MatchVO;
import org.sports.app.service.vo.live.LiveUserVO;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.convention.errorcode.BaseErrorCode;
import org.sports.springboot.starter.convention.exception.RemoteException;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.sports.app.service.constant.DefaultConstant.*;


@Service
@Slf4j
public class IScheduleServiceImpl implements IScheduleService {


    @Resource
    private IHotMatchService hotMatchService;

    @Resource
    private ILiveService liveService;

    @Resource
    private IMyFollowService myFollowService;

    @Resource
    private SdkService sdkService;

    @Resource
    private IBasketballLiveDetailService basketballLiveDetailService;

    @Resource
    private IFootballLiveDetailService footballLiveDetailService;

    @Resource
    private ILiveUserService liveUserService;

    @Resource
    private DistributedCache distributedCache;

    private final ExecutorService executor = Executors.newFixedThreadPool(5);

    /**
     * 根据前端传的查询条件进行分页查询
     */
    @Override
    public PageResponse<MatchVO> getMatchPage(Long userId, MatchPageRequest request) {
        List<SdkMatchVo> allList = getMatchList(request.getMatchType(), Objects.isNull(request.getCompetitionId()) ? null : Collections.singletonList(request.getCompetitionId()),
                request.getStartTime(), request.getEndTime(), false, QueryMatchStatus.getQueryMatchStatusByCode(request.getStatus()));

        //已完成的比赛倒序排序
        if (QueryMatchStatus.FINISHED.getCode().equals(request.getStatus())) {
            allList.sort(Comparator.comparing(SdkMatchVo::getMatchTime).reversed().thenComparing(SdkMatchVo::getId));
        } else {
            allList = MatchUtils.sortMatch(allList);//对结果进行排序：进行中的-> 未开始的->已结束的->异常的
        }
        List<MatchVO> resultList;
        int fromIndex = (request.getCurrent() - 1) * request.getSize();
        if (fromIndex >= allList.size()) {
            fromIndex = allList.size();
        }
        int toIndex = Math.min(fromIndex + request.getSize(), allList.size());
        resultList = allList.subList(fromIndex, toIndex)
                .parallelStream().map(item -> transSdkMatchVoToMatchVo(item, !QueryMatchStatus.FINISHED.getCode().equals(request.getStatus()))).filter(Objects::nonNull).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(resultList)) {
            //批量设置比赛对应的直播列表
            batchSetAnchorList(resultList);
            //批量设置是否关注比赛
            batchSetIsFocusMatch(userId, resultList);
        }
        return PageResponse.<MatchVO>builder().current(request.getCurrent()).size(request.getSize()).total((long) allList.size()).records(resultList).build();
    }


    @Override
    public MatchVO getMatchInfoByMatchId(Integer matchId, MatchType matchType) {
        MatchVO matchVO = transSdkMatchVoToMatchVo(sdkService.getMatchDetailById(matchId, matchType), false);
        if (Objects.isNull(matchVO)) {
            throw new RemoteException("调用远程数据接口异常", BaseErrorCode.CLIENT_ERROR);
        }
        if (MatchType.BASKETBALL.equals(matchType)) {
            matchVO.setMatchData(basketballLiveDetailService.getMatchDataStata(matchId, matchVO.getStatus()));
        } else if (MatchType.FOOTBALL.equals(matchType)) {
            matchVO.setMatchData(footballLiveDetailService.getMatchDataStata(matchId, matchVO.getStatus()));
        }
        batchSetAnchorList(Collections.singletonList(matchVO));
        if (CollectionUtils.isEmpty(matchVO.getAnchorList())) {
            matchVO.setAnchorList(Lists.newArrayList());
        }
        return matchVO;
    }

    @Override
    public Table<Integer, MatchType, MatchVO> getBatchMatchInfoByMatchId(List<MatchIdType> matchIdList) {
        Table<Integer, MatchType, MatchVO> table = HashBasedTable.create();
        matchIdList.parallelStream().forEachOrdered(item -> {
            MatchVO matchInfo = getMatchInfoByMatchId(item.getMatchId(), item.getMatchType());
            if (Objects.nonNull(matchInfo)) {
                table.put(item.getMatchId(), item.getMatchType(), matchInfo);
            }
        });
        return table;
    }


    @Override
    public List<MatchVO> getLatestMatch(MatchRequest req) {
        if (Objects.isNull(req.getTop())) {
            req.setTop(LATEST_MATCH_COUNT);
        }
        //这里获取今天和明天的比赛
        List<SdkMatchVo> matchList = getMatchList(req.getMatchType(), null, LocalDate.now(ZoneOffset.UTC), LocalDate.now(ZoneOffset.UTC).plusDays(TOMORROW), true, QueryMatchStatus.RUNNING_AND_UPCOMING);
        matchList = MatchUtils.sortMatch(matchList);
        //对结果进行排序：进行中的 未开始的
        List<MatchVO> resultList = matchList.stream().limit(req.getTop()).map(item -> transSdkMatchVoToMatchVo(item, false)).filter(Objects::nonNull).collect(Collectors.toList());
        //如果今日有比赛只返回今日的
        List<MatchVO> todayList = resultList.stream().filter(item -> (item.getMatchTime().isAfter(LocalDate.now().atStartOfDay()) && item.getMatchTime().isBefore(LocalDate.now().plusDays(1).atStartOfDay())) || item.getMatchTime().isEqual(LocalDate.now().atStartOfDay())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(todayList)) {
            batchSetAnchorList(todayList);
            return todayList;
        }
        batchSetAnchorList(resultList);
        return resultList;
    }

    @Override
    public PageResponse<MatchVO> getMyFollowMatchList(Long userId, PageRequest pageRequest) {
        List<MyFollowDO> myFollowMatchList = myFollowService.getMyFollowMatchList(userId);
        //返回结果集
        List<MatchVO> resultList = Lists.newArrayList();
        List<MatchVO> upCompingtList = Lists.newArrayList();//未开始
        List<MatchVO> ongoingList = Lists.newArrayList();//除了未开始和已完成的都排前面
        List<MatchVO> finishedList = Lists.newArrayList();//已完成
        List<MatchVO> errorList = Lists.newArrayList();//异常
        Table<Integer, MatchType, MatchVO> matchVoTable = getBatchMatchInfoByMatchId(myFollowMatchList.stream().map(this::getMatchIdType).collect(Collectors.toList()));
        myFollowMatchList.forEach(item -> {
            MatchVO matchInfo = matchVoTable.get(Integer.valueOf(String.valueOf(item.getBizId())), item.getMatchType());
            if (Objects.nonNull(matchInfo)) {
                matchInfo.setFocus(Boolean.TRUE);
                if (MatchType.BASKETBALL.equals(item.getMatchType())) {
                    if (BasketballStatusType.NOT_START.getCode().equals(matchInfo.getStatus())) {
                        upCompingtList.add(matchInfo);
                    } else if (BasketballStatusType.FINISHED.getCode().equals(matchInfo.getStatus())) {
                        finishedList.add(matchInfo);
                    } else if (BasketballStatusType.getRunningStatus().contains(matchInfo.getStatus())) {
                        ongoingList.add(matchInfo);
                    } else {
                        errorList.add(matchInfo);
                    }
                } else if (MatchType.FOOTBALL.equals(item.getMatchType())) {
                    if (FootballStatusType.NOT_START.getCode().equals(matchInfo.getStatus())) {
                        upCompingtList.add(matchInfo);
                    } else if (FootballStatusType.FINISHED.getCode().equals(matchInfo.getStatus())) {
                        finishedList.add(matchInfo);
                    } else if (FootballStatusType.getRunningStatus().contains(matchInfo.getStatus())) {
                        ongoingList.add(matchInfo);
                    } else {
                        errorList.add(matchInfo);
                    }
                }
            }
        });

        //对结果进行排序
        upCompingtList.sort(Comparator.comparing(MatchVO::getMatchTime));
        ongoingList.sort(Comparator.comparing(MatchVO::getMatchTime));
        finishedList.sort(Comparator.comparing(MatchVO::getMatchTime));
        errorList.sort(Comparator.comparing(MatchVO::getMatchTime));
        resultList.addAll(ongoingList);
        resultList.addAll(upCompingtList);
        resultList.addAll(finishedList);
        resultList.addAll(errorList);
        return PageResponse.<MatchVO>builder()
                .current(pageRequest.getCurrent())
                .size(pageRequest.getSize())
                .total((long) resultList.size())
                .records(getSubMatchPageList(resultList, pageRequest))
                .build();
    }

    private MatchIdType getMatchIdType(MyFollowDO myFollowDO) {
        if (Objects.isNull(myFollowDO)) {
            return null;
        }
        return MatchIdType.builder().matchId(Integer.valueOf(String.valueOf(myFollowDO.getBizId()))).matchType(myFollowDO.getMatchType()).build();
    }

    private List<MatchVO> getSubMatchPageList(List<MatchVO> resultList, PageRequest pageRequest) {
        int startIndex = (pageRequest.getCurrent() - 1) * pageRequest.getSize();
        int endIndex;
        if (startIndex < 0 || startIndex > resultList.size()) {
            return Lists.newArrayList();
        } else {
            endIndex = Math.min(startIndex + pageRequest.getSize(), resultList.size());
            return resultList.subList(startIndex, endIndex);
        }

    }

    @Override
    @Cached(name = CacheConstant.HOT_MATCH_LIVE_LIST, key = "#req.matchType+'_'+#req.top", expire = 1, timeUnit = TimeUnit.MINUTES)
    public List<LiveVo> getOngoingMatch(OngoingMatchRequest req) {
        if (Objects.isNull(req.getTop())) {
            req.setTop(HOT_MATCH_TOP);
        }
        List<HotMatchDO> hotCompetitionIdList = hotMatchService.getHotCompetitionIdList(req.getMatchType());
        List<LiveVo> allLive = Lists.newArrayList();
        if (Objects.isNull(req.getMatchType())) {
            List<LiveVo> footballHotMatchLive = liveService.getHotMatchLive(MatchType.FOOTBALL, LiveStatus.LIVING, hotCompetitionIdList.stream()
                    .filter(item -> item.getMatchType().equals(MatchType.FOOTBALL)).map(HotMatchDO::getCompetitionId).collect(Collectors.toList()), req.getTop());
            List<LiveVo> basketballHotMatchLive = liveService.getHotMatchLive(MatchType.BASKETBALL, LiveStatus.LIVING, hotCompetitionIdList.stream()
                    .filter(item -> item.getMatchType().equals(MatchType.BASKETBALL)).map(HotMatchDO::getCompetitionId).collect(Collectors.toList()), req.getTop());
            if (!CollectionUtils.isEmpty(footballHotMatchLive)) {
                allLive.addAll(footballHotMatchLive);
            }
            if (!CollectionUtils.isEmpty(basketballHotMatchLive)) {
                allLive.addAll(basketballHotMatchLive);
            }
        } else {
            allLive.addAll(liveService.getHotMatchLive(req.getMatchType(), LiveStatus.LIVING, hotCompetitionIdList.stream()
                    .filter(item -> item.getMatchType().equals(req.getMatchType())).map(HotMatchDO::getCompetitionId).collect(Collectors.toList()), req.getTop()));
        }
        //对结果进行排序：进行中的 未开始的
        List<LiveVo> collect = allLive.stream().sorted(Comparator.comparing(LiveVo::getHotValue).reversed()).limit(req.getTop()).collect(Collectors.toList());
        //判断最热和最新（如果一条不处理）
        if (!CollectionUtils.isEmpty(collect)) {
            if (collect.size() > 1) {
                collect.get(0).setHottest(true);//按照热度值排列，第一条是最热
                LiveVo maxOpenTimeUser = collect.stream()
                        .max(Comparator.comparing(LiveVo::getOpenTime)).orElse(null);
                if (Objects.nonNull(maxOpenTimeUser)) {
                    maxOpenTimeUser.setNewest(true);
                }
            }
        }
        return collect;
    }


    @Override
    @Cached(name = CacheConstant.HOT_COMPETITION_LIST, key = "#startTime+'_'+#endTime+'_'+#matchType+'_'+#status", expire = 2, timeUnit = TimeUnit.MINUTES)
    public List<HotMatchVo> getHotMatchList(LocalDate startTime, LocalDate endTime, MatchType matchType, Integer status) {
        Map<String, HotMatchVo> map = new HashMap<>();
        QueryMatchStatus queryMatchStatus = null;
        if(Objects.nonNull(status)||QueryMatchStatus.FINISHED.getCode().equals(status)){
            queryMatchStatus = QueryMatchStatus.FINISHED;
        }else {
            queryMatchStatus = QueryMatchStatus.RUNNING_AND_UPCOMING;
        }
        List<HotMatchDO> hotCompetitionIdList = hotMatchService.getHotCompetitionIdList(matchType);
        if(CollectionUtils.isEmpty(hotCompetitionIdList)){
            return Lists.newArrayList();
        }
        List<SdkMatchVo> allSdkMatchList = getHotSdkMatchList(startTime,endTime,queryMatchStatus,matchType);

        allSdkMatchList.forEach(item -> {
            String key = item.getCompetitionId() + "_" + item.getMatchType().getCode();
            HotMatchVo vo = map.get(key);
            if (Objects.isNull(vo)) {
                vo = new HotMatchVo();
                vo.setCompetitionId(item.getCompetitionId());
                SdkCompetitionVo competitionById = sdkService.findCompetitionById(item.getCompetitionId(), item.getMatchType());
                if (!Objects.isNull(competitionById)) {
                    vo.setCompetitionName(competitionById.getShortNameZh());
                }
                vo.setMatchType(item.getMatchType());
                vo.setMatchCount(1);
            } else {
                vo.setMatchCount(vo.getMatchCount() + 1);
            }
            map.put(key, vo);
        });
        return new ArrayList<>(map.values()).stream().sorted(Comparator.comparing(HotMatchVo::getMatchCount).reversed()).collect(Collectors.toList());
    }

    /**
     * 查询热门比赛列表
     *
     * @param startTime
     * @param endTime
     * @param queryMatchStatus
     * @param matchType
     * @return
     */
    private List<SdkMatchVo> getHotSdkMatchList(LocalDate startTime, LocalDate endTime, QueryMatchStatus queryMatchStatus, MatchType matchType) {
        List<SdkMatchVo> allSdkMatchList = Lists.newArrayList();
        List<HotMatchDO> hotCompetitionIdList = hotMatchService.getHotCompetitionIdList(matchType);
        if (Objects.isNull(matchType)) {
            List<SdkMatchVo> basketballList = null;
            List<SdkMatchVo> footballList = null;
            QueryMatchStatus finalQueryMatchStatus = queryMatchStatus;
            if(CollectionUtils.isEmpty(hotCompetitionIdList)){
                Future<List<SdkMatchVo>> bastketballFuture = executor.submit(() -> sdkService.getMatchList(null, startTime, endTime, MatchType.BASKETBALL, getMatchStatus(finalQueryMatchStatus, MatchType.BASKETBALL)));
                Future<List<SdkMatchVo>> footballFuture = executor.submit(() -> sdkService.getMatchList(null, startTime, endTime, MatchType.FOOTBALL, getMatchStatus(finalQueryMatchStatus, MatchType.FOOTBALL)));
                try {
                    basketballList = bastketballFuture.get(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                try {
                    footballList = footballFuture.get(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            } else {
                List<Integer> basketballComIds = hotCompetitionIdList.stream()
                        .filter(item -> item.getMatchType().equals(MatchType.BASKETBALL)).map(HotMatchDO::getCompetitionId).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(basketballComIds)) {
                    Future<List<SdkMatchVo>> bastketballFuture = executor.submit(() -> sdkService.getMatchList(basketballComIds, startTime, endTime, MatchType.BASKETBALL, getMatchStatus(finalQueryMatchStatus, MatchType.BASKETBALL)));
                    try {
                        basketballList = bastketballFuture.get(5, TimeUnit.SECONDS);
                    }catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
                List<Integer> footballComIds = hotCompetitionIdList.stream()
                        .filter(item -> item.getMatchType().equals(MatchType.FOOTBALL)).map(HotMatchDO::getCompetitionId).collect(Collectors.toList());
                if(!CollectionUtils.isEmpty(footballComIds)) {
                    Future<List<SdkMatchVo>> footballFuture = executor.submit(() -> sdkService.getMatchList(footballComIds, startTime, endTime, MatchType.FOOTBALL, getMatchStatus(finalQueryMatchStatus, MatchType.FOOTBALL)));
                    try {
                        footballList = footballFuture.get(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                }
            }

            if (!CollectionUtils.isEmpty(basketballList)) {
                allSdkMatchList.addAll(basketballList);
            }
            if (!CollectionUtils.isEmpty(footballList)) {
                allSdkMatchList.addAll(footballList);
            }
        } else {
            List<Integer> collect = hotCompetitionIdList.stream()
                    .filter(item -> item.getMatchType().equals(matchType)).map(HotMatchDO::getCompetitionId).collect(Collectors.toList());
            List<SdkMatchVo> matchList = sdkService.getMatchList(collect, startTime, endTime, matchType, getMatchStatus(queryMatchStatus, matchType));
            if (!CollectionUtils.isEmpty(matchList)) {
                allSdkMatchList.addAll(matchList);
            }

        }
        return allSdkMatchList;
    }


    @Override
    public List<MatchCountVO> getMatchTimeCount(MatchCountRequest request) {
        List<MatchCountVO> returnList = Lists.newArrayList();
        if (Objects.isNull(request.getMatchType())) {
            List<MatchCountVO> allMatchTimeCount = Lists.newArrayList();
            List<MatchCountVO> basketballMatchTimeCount = null;
            List<MatchCountVO> footballMatchTimeCount = null;
            Future<List<MatchCountVO>> bastketballFuture = executor.submit(() -> sdkService.getMatchTimeCount(request.getStartTime(), request.getEndTime(), MatchType.BASKETBALL));
            Future<List<MatchCountVO>> footballFuture = executor.submit(() -> sdkService.getMatchTimeCount(request.getStartTime(), request.getEndTime(), MatchType.FOOTBALL));
            try {
                basketballMatchTimeCount = bastketballFuture.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            try {
                footballMatchTimeCount = footballFuture.get(5, TimeUnit.SECONDS);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
            Map<String, Integer> map = new HashMap<>();
            if (!CollectionUtils.isEmpty(basketballMatchTimeCount)) {
                allMatchTimeCount.addAll(basketballMatchTimeCount);
            }
            if (!CollectionUtils.isEmpty(footballMatchTimeCount)) {
                allMatchTimeCount.addAll(footballMatchTimeCount);
            }
            allMatchTimeCount.forEach(item -> {
                Integer matchCount = map.get(item.getTime());
                if (Objects.nonNull(matchCount)) {
                    map.put(item.getTime(), item.getNum() + matchCount);
                } else {
                    map.put(item.getTime(), item.getNum());
                }
            });
            map.forEach((key, value) -> returnList.add(MatchCountVO.builder().time(key).num(value).build()));
            returnList.sort(Comparator.comparing(MatchCountVO::getTime));
            return returnList;
        }
        return sdkService.getMatchTimeCount(request.getStartTime(), request.getEndTime(), request.getMatchType());
    }

    @Override
    public List<LiveUserVO> getAnchorListByMatchId(Integer matchId, MatchType matchType) {
        MatchVO matchVO = new MatchVO();
        matchVO.setMatchId(matchId);
        matchVO.setMatchType(matchType);
        batchSetAnchorList(Collections.singletonList(matchVO));
        if(CollectionUtils.isEmpty(matchVO.getAnchorList())){
            matchVO.setAnchorList(Lists.newArrayList());
        }
        return matchVO.getAnchorList();
    }

    /**
     * 根据比赛类型、赛事、开始时间结束时间查询比赛列表
     *
     * @param matchType      比赛类型
     * @param competitionIds 赛事ID
     * @param startTime      开始时间
     * @param endTime        结束时间
     * @param isHotMatch     是否热门
     * @param matchStatus    比赛状态
     * @return 比赛结果
     */
    private List<SdkMatchVo> getMatchList(MatchType matchType, List<Integer> competitionIds, LocalDate startTime, LocalDate endTime, boolean isHotMatch, QueryMatchStatus matchStatus) {
        List<SdkMatchVo> allList = Lists.newArrayList();

        List<SdkMatchVo> basketBallMatchList = null;
        List<SdkMatchVo> footballMatchList = null;
        if (isHotMatch) {
           return  getHotSdkMatchList(startTime,endTime,matchStatus,matchType);
        } else {
            //篮球和足球
            if (Objects.isNull(matchType)) {
                Future<List<SdkMatchVo>> bastketballFuture = executor.submit(() -> sdkService.getMatchList(competitionIds, startTime, endTime, MatchType.BASKETBALL, getMatchStatus(matchStatus, MatchType.BASKETBALL)));
                Future<List<SdkMatchVo>> footballFuture = executor.submit(() -> sdkService.getMatchList(competitionIds, startTime, endTime, MatchType.FOOTBALL, getMatchStatus(matchStatus, MatchType.FOOTBALL)));
                try {
                    basketBallMatchList = bastketballFuture.get(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                try {
                    footballMatchList = footballFuture.get(5, TimeUnit.SECONDS);
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                if (!CollectionUtils.isEmpty(basketBallMatchList)) {
                    allList.addAll(basketBallMatchList);
                }
                if (!CollectionUtils.isEmpty(footballMatchList)) {
                    allList.addAll(footballMatchList);
                }
                return allList;
            }
        }

        return sdkService.getMatchList(competitionIds, startTime, endTime, matchType, getMatchStatus(matchStatus, matchType));
    }

    private List<Integer> getMatchStatus(QueryMatchStatus matchStatus, MatchType matchType) {
        if (Objects.isNull(matchStatus)) {
            return null;
        }
        if (MatchType.BASKETBALL.equals(matchType)) {
            if (QueryMatchStatus.RUNNING.equals(matchStatus)) {
                return BasketballStatusType.getRunningStatus();
            } else if (QueryMatchStatus.UPCOMING.equals(matchStatus)) {
                return BasketballStatusType.getUpcomingStatus();
            } else if (QueryMatchStatus.RUNNING_AND_UPCOMING.equals(matchStatus)) {
                return BasketballStatusType.getRunningAndUpcomingStatus();
            } else if (QueryMatchStatus.FINISHED.equals(matchStatus)) {
                return BasketballStatusType.getFinishedStatus();
            } else if (QueryMatchStatus.UNFINISHED.equals(matchStatus)) {
                return BasketballStatusType.getUnFinishedStatus();
            }
        } else if (MatchType.FOOTBALL.equals(matchType)) {
            if (QueryMatchStatus.RUNNING.equals(matchStatus)) {
                return FootballStatusType.getRunningStatus();
            } else if (QueryMatchStatus.UPCOMING.equals(matchStatus)) {
                return FootballStatusType.getUpcomingStatus();
            } else if (QueryMatchStatus.RUNNING_AND_UPCOMING.equals(matchStatus)) {
                return FootballStatusType.getRunningAndUpcomingStatus();
            } else if (QueryMatchStatus.FINISHED.equals(matchStatus)) {
                return FootballStatusType.getFinishedStatus();
            }else if (QueryMatchStatus.UNFINISHED.equals(matchStatus)) {
                return FootballStatusType.getUnFinishedStatus();
            }
        }
        return null;
    }

    /**
     *  对象转换
     * @param match 比赛
     * @param filterFinished 是否过滤掉已完赛的，当页面查询不是已完成的时候要过滤掉已完赛的比赛
     * @return
     */
    private MatchVO transSdkMatchVoToMatchVo(SdkMatchVo match, boolean filterFinished) {
        if (Objects.isNull(match)) {
            return null;
        }
        MatchVO vo = new MatchVO();
        vo.setMatchId(match.getId());
        vo.setMatchType(match.getMatchType());
        vo.setCompetitionId(match.getCompetitionId());
        vo.setRunTime(match.getRunTime());
        SdkCompetitionVo competitionVo = sdkService.findCompetitionById(match.getCompetitionId(), match.getMatchType());
        if (Objects.nonNull(competitionVo)) {
            vo.setCompetitionName(competitionVo.getShortNameZh());
        }
        if (Objects.nonNull(match.getMatchTime())) {
            vo.setMatchTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(Long.valueOf(match.getMatchTime()) * 1000), ZoneOffset.UTC));
        }
        SdkTeamVo homeTeam = sdkService.findTeamById(match.getHomeTeamId(), match.getMatchType());
        if (Objects.nonNull(homeTeam)) {
            if (Strings.isNotBlank(homeTeam.getShortNameZh())) {
                vo.setHomeName(homeTeam.getShortNameZh());
            } else {
                vo.setHomeName(homeTeam.getNameZh());
            }
            vo.setHomeLogo(NationalType.YES.getCode().equals(homeTeam.getNational()) ? homeTeam.getCountryLogo() : homeTeam.getLogo());
            if(Strings.isBlank(vo.getHomeLogo())){
                vo.setHomeLogo(homeTeam.getLogo());
            }
        }
        SdkTeamVo awayTeam = sdkService.findTeamById(match.getAwayTeamId(), match.getMatchType());
        if (Objects.nonNull(awayTeam)) {
            if (Strings.isNotBlank(awayTeam.getShortNameZh())) {
                vo.setAwayName(awayTeam.getShortNameZh());
            } else {
                vo.setAwayName(awayTeam.getNameZh());
            }
            vo.setAwayLogo(NationalType.YES.getCode().equals(awayTeam.getNational()) ? awayTeam.getCountryLogo() : awayTeam.getLogo());
            if(Strings.isBlank(vo.getAwayLogo())){
                vo.setAwayLogo(awayTeam.getLogo());
            }
        }
        //设置主客队比分;半场比分;进行到第几节
        if (Objects.nonNull(match.getHomeScores()) && Objects.nonNull(match.getAwayScores())) {
            if (Objects.equals(match.getMatchType(), MatchType.BASKETBALL)) {
                MatchUtils.setBasketballScore(vo, match.getHomeScores(), match.getAwayScores());
            } else {
                MatchUtils.setFootballScore(vo, match.getHomeScores(), match.getAwayScores());
            }
        }
        vo.setStatus(match.getStatusId());
        setNewestScore(vo);//更新最新得分
        if (filterFinished) {
            if (MatchType.BASKETBALL.equals(vo.getMatchType()) && BasketballStatusType.FINISHED.getCode().equals(vo.getStatus())) {
                return null;
            } else if (MatchType.FOOTBALL.equals(vo.getMatchType()) && FootballStatusType.FINISHED.getCode().equals(vo.getStatus())) {
                return null;
            }
        }
        return vo;
    }

    /**
     * 设置最新得分返回前端
     *
     * @param match
     */
    private void setNewestScore(MatchVO match) {
        MatchScoreDTO dto;
        if (MatchType.FOOTBALL.equals(match.getMatchType())) {//足球
            dto = distributedCache.get(CacheUtil.buildKey(CacheConstant.APP_FOOTBALL_MATCH_NEWEST_SCORE, match.getMatchId().toString()), MatchScoreDTO.class);
        } else { //篮球
            dto = distributedCache.get(CacheUtil.buildKey(CacheConstant.APP_BASKETBALL_MATCH_NEWEST_SCORE, match.getMatchId().toString()), MatchScoreDTO.class);
        }
        if (Objects.isNull(dto)) {
            return;
        }
        if (dto.getHomeScore().compareTo(match.getHomeScore()) >= 0 && dto.getAwayScore().compareTo(match.getAwayScore()) >= 0) {
            match.setHomeScore(dto.getHomeScore());
            match.setAwayScore(dto.getAwayScore());
            match.setHomeHalfScore(dto.getHomeHalfScore());
            match.setAwayHalfScore(dto.getAwayHalfScore());
            match.setRunTime(dto.getRunTime());
            match.setStatus(dto.getStatus());
        }
    }

    /**
     * 批量设置是否已关注比赛
     *
     * @param userId
     * @param resultList
     */
    private void batchSetIsFocusMatch(Long userId, List<MatchVO> resultList) {
        if (Objects.isNull(userId)) {
            return;
        }
        List<MyFollowDO> myFollowMatchList = myFollowService.getMyFollowMatchList(userId);
        resultList.forEach(item -> item.setFocus(myFollowMatchList.stream().filter(match -> item.getMatchType().equals(match.getMatchType()) && item.getMatchId().equals(Integer.valueOf(match.getBizId().toString()))).findFirst().isPresent()));
    }

    /**
     * 批量设置比赛当前正在进行的直播列表
     *
     * @param records
     */
    private void batchSetAnchorList(List<MatchVO> records) {
        List<LiveIdTypeRequest> requestList = Lists.newArrayList();
        records.forEach(item -> requestList.add(LiveIdTypeRequest.builder()
                .matchType(item.getMatchType())
                .matchId(item.getMatchId())
                .build()));
        Table<org.sports.admin.manage.dao.enums.MatchType, Integer, List<LiveIdTypeVo>> byIdsAndType = liveService.getByIdsAndType(requestList);
        records.forEach(item -> {
            List<LiveIdTypeVo> liveIdTypeVos = byIdsAndType.get(item.getMatchType(), item.getMatchId());
            if (CollectionUtils.isEmpty(liveIdTypeVos)) {
                return;
            }
            List<Long> userIds = liveIdTypeVos.stream().map(LiveIdTypeVo::getUserId).distinct().collect(Collectors.toList());
            List<org.sports.admin.manage.service.vo.LiveUserVO> dbLiveUser = liveUserService.getLiveUserList(userIds);
            Map<Long, org.sports.admin.manage.service.vo.LiveUserVO> liveUserMap = dbLiveUser.stream()
                    .collect(Collectors.toMap(org.sports.admin.manage.service.vo.LiveUserVO::getId, Function.identity()));
            List<LiveUserVO> liveUserList = Lists.newArrayList();
            liveIdTypeVos.forEach(live -> {
                org.sports.admin.manage.service.vo.LiveUserVO liveUserVO = liveUserMap.get(live.getUserId());
                if (Objects.isNull(liveUserVO)) {
                    liveUserList.add(LiveUserVO.builder().liveId(live.getId().toString()).userId(live.getUserId()).hotValue(live.getHotValue()).openTime(live.getOpenTime()).isPureFlow(false).nickName(live.getNickName()).userLogo(live.getUserLogo()).playUrl(live.getPlayUrl()).build());
                } else {
                    liveUserList.add(LiveUserVO.builder().liveId(live.getId().toString()).userId(live.getUserId()).hotValue(live.getHotValue()).openTime(live.getOpenTime()).isPureFlow(false).nickName(liveUserVO.getNickName()).userLogo(liveUserVO.getHead()).playUrl(live.getPlayUrl()).build());
                }
            });
            liveUserList.sort(Comparator.comparing(LiveUserVO::getHotValue).reversed());
            item.setAnchorList(liveUserList);
        });
    }



}
