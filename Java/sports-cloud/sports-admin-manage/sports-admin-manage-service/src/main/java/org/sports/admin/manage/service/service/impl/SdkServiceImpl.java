package org.sports.admin.manage.service.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson2.JSON;
import com.alicp.jetcache.anno.Cached;
import com.edi.commons.model.vo.PageVo;
import com.edi.sdk.basketball.BasketballClient;
import com.edi.sdk.core.EdiResponse;
import com.edi.sdk.football.FootballClient;
import com.edi.sdk.football.domain.*;
import com.edi.sdk.football.request.*;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.enums.MatchStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.MatchListRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.enums.BasketballStatusType;
import org.sports.admin.manage.service.enums.FootballStatusType;
import org.sports.admin.manage.service.service.SdkService;
import org.sports.admin.manage.service.vo.*;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 调用sdk实现类，这里由于sdk调用远程接口，会造成很多失败接口，如果实时调用成功，使用成功的数据，否则使用缓存的数据，缓存数据可能会造成不实时
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class SdkServiceImpl implements SdkService {
    private final FootballClient footballClient;
    private final BasketballClient basketballClient;
    @Resource
    private DistributedCache distributedCache;
    private final ExecutorService executor = Executors.newFixedThreadPool(6);

    @Override
    @Cached(name = CacheConstant.MATCH_TEAM, key = "#matchType+':'+#teamId", expire = 1, timeUnit = TimeUnit.DAYS)
    public SdkTeamVo findTeamById(Integer teamId, MatchType matchType) {
        switch (matchType) {
            case FOOTBALL: {
                TeamListRequest request = new TeamListRequest();
                request.setId(teamId);
                request.setLimit(1);
                log.info("远程请求足球团队接口参数：{}", JSON.toJSONString(request));
                EdiResponse<List<Team>> listEdiResponse = footballClient.teamList(request);
                log.info("远程请求足球团队接口返回：{}", JSON.toJSONString(listEdiResponse));
                if (success(listEdiResponse)) {
                    List<Team> results = listEdiResponse.getResults();
                    if (!CollectionUtils.isEmpty(results)) {
                        SdkTeamVo bean = BeanUtil.copyProperties(results.get(0), SdkTeamVo.class);
                        bean.setMatchType(MatchType.FOOTBALL);
                        if (Strings.isBlank(bean.getShortNameZh())) {
                            bean.setShortNameZh(bean.getNameZh());
                        }
                        if (Strings.isBlank(bean.getShortNameZh())) {
                            bean.setShortNameZh(bean.getShortNameEn());
                        }
                        if (Strings.isBlank(bean.getShortNameZh())) {
                            bean.setShortNameZh(bean.getNameEn());
                        }
                        if (Objects.equals(teamId, bean.getId())) {
                            return bean;
                        }
                    }
                }
                break;
            }
            case BASKETBALL: {
                com.edi.sdk.basketball.request.TeamListRequest basketball = new com.edi.sdk.basketball.request.TeamListRequest();
                basketball.setId(teamId);
                basketball.setLimit(1);
                EdiResponse<List<com.edi.sdk.basketball.domain.Team>> basketballResponse = basketballClient.teamList(basketball);
                if (success(basketballResponse)) {
                    List<com.edi.sdk.basketball.domain.Team> results = basketballResponse.getResults();
                    if (!CollectionUtils.isEmpty(results)) {
                        SdkTeamVo bean = BeanUtil.copyProperties(results.get(0), SdkTeamVo.class);
                        bean.setMatchType(MatchType.BASKETBALL);
                        if (Strings.isBlank(bean.getShortNameZh())) {
                            bean.setShortNameZh(bean.getNameZh());
                        }
                        if (Strings.isBlank(bean.getShortNameZh())) {
                            bean.setShortNameZh(bean.getShortNameEn());
                        }
                        if (Strings.isBlank(bean.getShortNameZh())) {
                            bean.setShortNameZh(bean.getNameEn());
                        }
                        if (Objects.equals(teamId, bean.getId())) {
                            return bean;
                        }
                    }
                }
                break;
            }
            default:
                return null;
        }
        return null;
    }

    @Override
    @Cached(name = CacheConstant.MATCH_COMPETITION_DETAIL, key = "#matchType+':'+#competitionId", expire = 1, timeUnit = TimeUnit.DAYS)
    public SdkCompetitionVo findCompetitionById(Integer competitionId, MatchType matchType) {
        switch (matchType) {
            case FOOTBALL: {
                com.edi.sdk.football.request.CompetitionListRequest request = new com.edi.sdk.football.request.CompetitionListRequest();
                request.setId(competitionId);
                request.setLimit(1);
                EdiResponse<List<com.edi.sdk.football.domain.Competition>> response = footballClient.competitionList(request);
                if (success(response)) {
                    List<com.edi.sdk.football.domain.Competition> results = response.getResults();
                    if (!CollectionUtils.isEmpty(results)) {
                        SdkCompetitionVo bean = BeanUtil.toBean(results.get(0), SdkCompetitionVo.class);
                        bean.setMatchType(MatchType.FOOTBALL);
                        if (Objects.isNull(bean.getShortNameZh())) {
                            bean.setShortNameZh(bean.getShortNameEn());
                        }
                        if (Objects.equals(competitionId, bean.getId())) {
                            return bean;
                        }
                    }
                }
                break;
            }
            case BASKETBALL: {
                com.edi.sdk.basketball.request.CompetitionListRequest request = new com.edi.sdk.basketball.request.CompetitionListRequest();
                request.setId(competitionId);
                request.setLimit(1);
                EdiResponse<List<com.edi.sdk.basketball.domain.Competition>> response = basketballClient.competitionList(request);
                if (success(response)) {
                    List<com.edi.sdk.basketball.domain.Competition> results = response.getResults();
                    if (!CollectionUtils.isEmpty(results)) {
                        SdkCompetitionVo bean = BeanUtil.toBean(results.get(0), SdkCompetitionVo.class);
                        bean.setMatchType(MatchType.BASKETBALL);
                        if (Objects.isNull(bean.getShortNameZh())) {
                            bean.setShortNameZh(bean.getShortNameEn());
                        }
                        if (Objects.equals(competitionId, bean.getId())) {
                            return bean;
                        }
                    }
                }
                break;
            }
            default:
                return null;
        }
        return null;
    }

    @Override
    @Cached(name = CacheConstant.COMPETITION_SEARCH, key = "#matchType+':'+#name", expire = 1, timeUnit = TimeUnit.DAYS)
    public List<SdkCompetitionVo> searchCompetition(String name, MatchType matchType) {
        switch (matchType) {
            case FOOTBALL: {
                CompetitionSearchRequest request = new CompetitionSearchRequest();
                request.setName(name);
                log.info("远程请求足球赛事搜索接口参数：{}", JSON.toJSONString(request));
                EdiResponse<List<Competition>> response = footballClient.competitionSearchRequest(request);
                log.info("远程请求足球赛事搜索接口返回：{}", JSON.toJSONString(response));
                if (success(response)) {
                    List<SdkCompetitionVo> sdkCompetitionVos = BeanUtil.copyToList(response.getResults(), SdkCompetitionVo.class);
                    sdkCompetitionVos.forEach(item -> {
                        item.setMatchType(matchType);
                        if (Objects.isNull(item.getShortNameZh())) {
                            item.setShortNameZh(item.getShortNameEn());
                        }
                    });
                    return sdkCompetitionVos;
                }
                break;
            }
            case BASKETBALL: {
                //篮球
                com.edi.sdk.basketball.request.CompetitionSearchRequest request1 = new com.edi.sdk.basketball.request.CompetitionSearchRequest();
                request1.setName(name);
                log.info("远程请求篮球赛事搜索接口参数：{}", JSON.toJSONString(request1));
                EdiResponse<List<com.edi.sdk.basketball.domain.Competition>> response = basketballClient.competitionSearchRequest(request1);
                log.info("远程请求篮球赛事接搜索口返回：{}", JSON.toJSONString(response));
                if (success(response)) {
                    List<SdkCompetitionVo> sdkCompetitionVos = BeanUtil.copyToList(response.getResults(), SdkCompetitionVo.class);
                    sdkCompetitionVos.forEach(item -> {
                        item.setMatchType(matchType);
                        if (Objects.isNull(item.getShortNameZh())) {
                            item.setShortNameZh(item.getShortNameEn());
                        }
                    });
                    return sdkCompetitionVos;
                }
                break;
            }
            default:
                return null;
        }
        return null;
    }

    @Override
    public PageResponse<SdkMatchVo> page(MatchListRequest request) {
        LocalDate matchDate = request.getMatchDate();
        switch (request.getMatchType()) {
            case FOOTBALL: {
                MatchScheduleSearchRequest request1 = new MatchScheduleSearchRequest();
                request1.setStartMatchTime(matchDate.atStartOfDay());
                request1.setEndMatchTime(matchDate.atTime(LocalTime.MAX));
                request1.setSize(request.getSize());
                request1.setNumber(request.getCurrent());

                request1.setCompetitionIds(request.getCompetitionIds());
                if (MatchStatus.RUNNING.equals(request.getMatchStatus())) {
                    request1.setStatus(FootballStatusType.getRunningStatus());
                } else if (MatchStatus.UN_RUNNING.equals(request.getMatchStatus())) {
                    request1.setStatus(FootballStatusType.getUpcomingStatus());
                } else {
                    request1.setStatus(FootballStatusType.getRunningAndUpcomingStatus());
                }
                EdiResponse<PageVo<Match>> football = footballClient.matchScheduleSearchRequest(request1);
                if (success(football)) {
                    PageVo<Match> results = football.getResults();
                    List<SdkMatchVo> sdkMatchVos = BeanUtil.copyToList(results.getContent(), SdkMatchVo.class);
                    sdkMatchVos.forEach(item -> {
                        item.setMatchType(request.getMatchType());
                        item.setMatchStatus(FootballStatusType.getMatchStatus(item.getStatusId()));
                    });
                    return PageResponse.<SdkMatchVo>builder()
                            .total(results.getTotalElements())
                            .size(results.getSize())
                            .current(results.getNumber())
                            .records(sdkMatchVos)
                            .build();
                }
                break;
            }
            case BASKETBALL: {
                com.edi.sdk.basketball.request.MatchScheduleSearchRequest request2 = new com.edi.sdk.basketball.request.MatchScheduleSearchRequest();
                request2.setStartMatchTime(matchDate.atStartOfDay());
                request2.setEndMatchTime(matchDate.atTime(LocalTime.MAX));
                request2.setSize(request.getSize());
                request2.setNumber(request.getCurrent());

                request2.setCompetitionIds(request.getCompetitionIds());
                if (MatchStatus.RUNNING.equals(request.getMatchStatus())) {
                    request2.setStatus(BasketballStatusType.getRunningStatus());
                } else if (MatchStatus.UN_RUNNING.equals(request.getMatchStatus())) {
                    request2.setStatus(BasketballStatusType.getUpcomingStatus());
                } else {
                    request2.setStatus(BasketballStatusType.getRunningAndUpcomingStatus());
                }
                EdiResponse<PageVo<com.edi.sdk.basketball.domain.Match>> response = basketballClient.matchScheduleSearchRequest(request2);
                if (success(response)) {
                    PageVo<com.edi.sdk.basketball.domain.Match> results = response.getResults();
                    List<SdkMatchVo> sdkMatchVos = BeanUtil.copyToList(results.getContent(), SdkMatchVo.class);
                    sdkMatchVos.forEach(item -> {
                        item.setMatchType(request.getMatchType());
                        item.setMatchStatus(BasketballStatusType.getMatchStatus(item.getStatusId()));
                    });
                    return PageResponse.<SdkMatchVo>builder()
                            .total(results.getTotalElements())
                            .size(results.getSize())
                            .current(results.getNumber())
                            .records(sdkMatchVos)
                            .build();
                }
                break;
            }
            default:
                return null;
        }
        return null;
    }

    @Override
    @Cached(name = CacheConstant.MATCH_COMPETITION_LIST, key = "#startMatchTime+'_'+#endMatchTime+'_'+#matchType", expire = 1, timeUnit = TimeUnit.HOURS)
    public List<SdkCompetitionVo> searchCompetitionByTime(MatchType matchType, LocalDateTime startMatchTime, LocalDateTime endMatchTime) {
        switch (matchType) {
            case FOOTBALL: {
                SearchByTimeCompetitionRequest request = new SearchByTimeCompetitionRequest();
                request.setStartMatchTime(startMatchTime);
                request.setEndMatchTime(endMatchTime);
                EdiResponse<List<Competition>> response = footballClient.searchByTimeCompetitionRequest(request);
                if (success(response)) {
                    List<SdkCompetitionVo> sdkCompetitionVos = BeanUtil.copyToList(response.getResults(), SdkCompetitionVo.class);
                    sdkCompetitionVos.forEach(item -> {
                        item.setMatchType(matchType);
                        if (Objects.isNull(item.getShortNameZh())) {
                            item.setShortNameZh(item.getShortNameEn());
                        }
                    });
                    return sdkCompetitionVos;
                }
                break;
            }
            case BASKETBALL: {
                com.edi.sdk.basketball.request.SearchByTimeCompetitionRequest request = new com.edi.sdk.basketball.request.SearchByTimeCompetitionRequest();
                request.setStartMatchTime(startMatchTime);
                request.setEndMatchTime(endMatchTime);
                EdiResponse<List<com.edi.sdk.basketball.domain.Competition>> response = basketballClient.searchByTimeCompetitionRequest(request);
                if (success(response)) {
                    List<SdkCompetitionVo> sdkCompetitionVos = BeanUtil.copyToList(response.getResults(), SdkCompetitionVo.class);
                    sdkCompetitionVos.forEach(item -> {
                        item.setMatchType(matchType);
                        if (Objects.isNull(item.getShortNameZh())) {
                            item.setShortNameZh(item.getShortNameEn());
                        }
                    });
                    return sdkCompetitionVos;
                }
            }
            break;
        }

        return null;
    }

    @Override
    @Cached(name = CacheConstant.CALENDAR_MATCH_COUNT, key = "#startTime+'_'+#endTime+'_'+#matchType", expire = 1, timeUnit = TimeUnit.DAYS, cacheNullValue = false)
    public List<MatchCountVO> getMatchTimeCount(LocalDate startTime, LocalDate endTime, MatchType matchType) {
        switch (matchType) {
            case FOOTBALL:
                return getFootballMatchTimeCount(startTime, endTime);
            case BASKETBALL:
                return getBasketballMatchTimeCount(startTime, endTime);
        }
        return null;
    }

    @Override
    @Cached(name = CacheConstant.MATCH_LIST, key = "#startTime+'_'+#endTime+'_'+#competitionIds+'_'+#matchType+'_'+#status", expire = 1, timeUnit = TimeUnit.MINUTES)
    public List<SdkMatchVo> getMatchList(List<Integer> competitionIds, LocalDate startTime, LocalDate endTime, MatchType matchType, List<Integer> status) {
        List<SdkMatchVo> resultList = Lists.newArrayList();
        switch (matchType) {
            case FOOTBALL:
                //如果查询的是全部比赛，需要先按照先查询进行中的，未开始的，其他状态分别查询
                if (CollectionUtils.isEmpty(status)) {
                    Future<List<SdkMatchVo>> ongoingFuture = executor.submit(() -> getFootballMatchList(competitionIds, startTime, endTime, FootballStatusType.getRunningStatus()));
                    Future<List<SdkMatchVo>> upcomingFuture = executor.submit(() -> getFootballMatchList(competitionIds, startTime, endTime, FootballStatusType.getUpcomingStatus()));
                    Future<List<SdkMatchVo>> otherFuture = executor.submit(() -> getFootballMatchList(competitionIds, startTime, endTime, FootballStatusType.getOtherStatus()));
                    List<SdkMatchVo> ongoingList = null;
                    List<SdkMatchVo> upcomingList = null;
                    List<SdkMatchVo> otherList = null;
                    try {
                        ongoingList = ongoingFuture.get(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    try {
                        upcomingList = upcomingFuture.get(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    try {
                        otherList = otherFuture.get(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    if (!CollectionUtils.isEmpty(ongoingList)) {
                        resultList.addAll(ongoingList);
                    }
                    if (!CollectionUtils.isEmpty(upcomingList)) {
                        resultList.addAll(upcomingList);
                    }
                    if (!CollectionUtils.isEmpty(otherList)) {
                        resultList.addAll(otherList);
                    }
                    return resultList;
                } else {
                    return getFootballMatchList(competitionIds, startTime, endTime, status);
                }
            case BASKETBALL:
                //如果查询的是全部比赛，需要先按照先查询进行中的，未开始的，其他状态分别查询
                if (CollectionUtils.isEmpty(status)) {
                    Future<List<SdkMatchVo>> ongoingFuture = executor.submit(() -> getBasketballMatchList(competitionIds, startTime, endTime, BasketballStatusType.getRunningStatus()));
                    Future<List<SdkMatchVo>> upcomingFuture = executor.submit(() -> getBasketballMatchList(competitionIds, startTime, endTime, BasketballStatusType.getUpcomingStatus()));
                    Future<List<SdkMatchVo>> otherFuture = executor.submit(() -> getBasketballMatchList(competitionIds, startTime, endTime, BasketballStatusType.getOtherStatus()));
                    List<SdkMatchVo> ongoingList = null;
                    List<SdkMatchVo> upcomingList = null;
                    List<SdkMatchVo> otherList = null;
                    try {
                        ongoingList = ongoingFuture.get(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    try {
                        upcomingList = upcomingFuture.get(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    try {
                        otherList = otherFuture.get(5, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    if (!CollectionUtils.isEmpty(ongoingList)) {
                        resultList.addAll(ongoingList);
                    }
                    if (!CollectionUtils.isEmpty(upcomingList)) {
                        resultList.addAll(upcomingList);
                    }
                    if (!CollectionUtils.isEmpty(otherList)) {
                        resultList.addAll(otherList);
                    }
                    return resultList;
                } else {
                    return getBasketballMatchList(competitionIds, startTime, endTime, status);
                }
        }
        return null;
    }


    @Override
    @Cached(name = CacheConstant.MATCH_DETAILS, key = "#matchType+'_'+#matchId", expire = 1, timeUnit = TimeUnit.MINUTES, cacheNullValue = false)
    public SdkMatchVo getMatchDetailById(Integer matchId, MatchType matchType) {
        switch (matchType) {
            case FOOTBALL:
                return getFootballMatchById(matchId);
            case BASKETBALL:
                return getBasketballMatchById(matchId);
        }
        return null;
    }



    @Override
    @Cached(name = CacheConstant.REFEREE, key = "#refereeId", expire = 1, timeUnit = TimeUnit.DAYS, cacheNullValue = true)
    public SdkRefereeVo getFootballRefereeById(Integer refereeId) {
        if (refereeId > 0) {
            com.edi.sdk.football.request.RefereeListRequest request = new com.edi.sdk.football.request.RefereeListRequest();
            request.setLimit(1);
            request.setId(refereeId);
            log.info("远程请求足球教练列表参数：{}", JSON.toJSONString(request));
            EdiResponse<List<com.edi.sdk.football.domain.Referee>> listEdiResponse = footballClient.refereeList(request);
            log.info("远程请求足球教练列表返回结果：{}", JSON.toJSONString(listEdiResponse));
            if (success(listEdiResponse) && !CollectionUtils.isEmpty(listEdiResponse.getResults())) {
                Referee referee = listEdiResponse.getResults().get(0);
                if (Objects.equals(referee.getId(), refereeId)) {
                    SdkRefereeVo bean = BeanUtil.toBean(referee, SdkRefereeVo.class);
                    bean.setMatchType(MatchType.FOOTBALL);
                    if (Objects.equals(refereeId, bean.getId())) {
                        return bean;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public List<MatchVideo> getFootballMatchVideoLine() {
            com.edi.sdk.football.request.MatchLiveVideoRequest request = new com.edi.sdk.football.request.MatchLiveVideoRequest();
            log.info("远程刷新比赛线路列表参数：{}", JSON.toJSONString(request));
            EdiResponse<List<com.edi.sdk.football.domain.MatchVideo>> listEdiResponse = footballClient.searchVideoList(request);
            log.info("远程刷新比赛线路列表返回结果：{}", JSON.toJSONString(listEdiResponse));
            if (Objects.nonNull(listEdiResponse)&& !CollectionUtils.isEmpty(listEdiResponse.getData())) {
                List<MatchVideo> matchVideoList = listEdiResponse.getData();
                if (!CollectionUtils.isEmpty(matchVideoList)) {
                    return matchVideoList;
                }
            }
        return null;
    }

    @Override
    public List<SdkMatchVo> getRecentMatchList() {
        List<SdkMatchVo> resultList = Lists.newArrayList();
        Future<List<SdkMatchVo>> footballFuture = executor.submit(() -> getFootballRecentMatchList());
        Future<List<SdkMatchVo>> basketballFuture = executor.submit(() -> getBasketballRecentMatchList());
        try {
            List<SdkMatchVo> footballRecentMatchList = footballFuture.get(5, TimeUnit.SECONDS);
            if (!CollectionUtils.isEmpty(footballRecentMatchList)) {
                resultList.addAll(footballRecentMatchList);
            }
        } catch (Exception e) {
            log.error("获取足球最新比赛变动数据异常：{}", e);
        }
        try {
            List<SdkMatchVo> basketballRecentMatchList = basketballFuture.get(5, TimeUnit.SECONDS);
            if (!CollectionUtils.isEmpty(basketballRecentMatchList)) {
                resultList.addAll(basketballRecentMatchList);
            }
        } catch (Exception e) {
            log.error("获取篮球最新比赛变动数据异常：{}", e);
        }
        return resultList;
    }

    /**
     * 篮球最新变动数据
     */
    private List<SdkMatchVo> getBasketballRecentMatchList() {
        String key = CacheConstant.SDK_BASKETBALL_RECENT_MATCH_TIME;
        Integer maxTime = distributedCache.get(key, Integer.class);
        if (Objects.isNull(maxTime)) {
            maxTime = getRecentTime();
        }
        List<SdkMatchVo> sdkMatchVos = null;
        com.edi.sdk.basketball.request.RecentMatchListRequest request = new com.edi.sdk.basketball.request.RecentMatchListRequest();
        request.setTime(maxTime);
        request.setLimit(1000);
        EdiResponse<List<com.edi.sdk.basketball.domain.Match>> response = basketballClient.recentMatchList(request);
        if (success(response)) {
            maxTime = response.getQuery().getMaxTime();
            if (Objects.nonNull(maxTime) && maxTime > 0) {
                distributedCache.put(key, maxTime, 1, TimeUnit.HOURS);
            }
            sdkMatchVos = BeanUtil.copyToList(response.getResults(), SdkMatchVo.class);
            sdkMatchVos.forEach(item -> {
                item.setMatchType(MatchType.BASKETBALL);
                item.setMatchStatus(BasketballStatusType.getMatchStatus(item.getStatusId()));
            });
        }
        return sdkMatchVos;
    }

    /**
     * 足球最新变动数据
     */
    private List<SdkMatchVo> getFootballRecentMatchList() {
        String key = CacheConstant.SDK_FOOTBALL_RECENT_MATCH_TIME;
        Integer maxTime = distributedCache.get(key, Integer.class);
        if (Objects.isNull(maxTime)) {
            maxTime = getRecentTime();
        }
        List<SdkMatchVo> sdkMatchVos = null;
        RecentMatchListRequest request = new RecentMatchListRequest();
        request.setTime(maxTime);
        request.setLimit(1000);
        EdiResponse<List<RecentMatch>> response = footballClient.recentMatchList(request);
        if (success(response)) {
            maxTime = response.getQuery().getMaxTime();
            if (Objects.nonNull(maxTime) && maxTime > 0) {
                distributedCache.put(key, maxTime, 1, TimeUnit.HOURS);
            }
            sdkMatchVos = BeanUtil.copyToList(response.getResults(), SdkMatchVo.class);
            sdkMatchVos.forEach(item -> {
                item.setMatchType(MatchType.FOOTBALL);
                item.setRunTime(getMatchRuntime(item.getId(), item.getStatusId(), item.getMatchTime()));
                item.setMatchStatus(FootballStatusType.getMatchStatus(item.getStatusId()));
            });
        }
        return sdkMatchVos;
    }

    /**
     * 当前时间往前推5分钟
     *
     * @return
     */
    private Integer getRecentTime() {
        return Math.toIntExact(LocalDateTime.now().plusMinutes(-5).atZone(ZoneId.of("Asia/Shanghai")).toInstant().toEpochMilli() / 1000);
    }

    private SdkMatchVo getFootballMatchById(Integer matchId) {
        com.edi.sdk.football.request.MatchListRequest request = new com.edi.sdk.football.request.MatchListRequest();
        request.setLimit(1);
        request.setId(matchId);
        EdiResponse<List<com.edi.sdk.football.domain.Match>> listEdiResponse = footballClient.matchList(request);
        String cacheKey = CacheUtil.buildKey(CacheConstant.SDK_FOOTBALL_MATCH, matchId.toString());
        if (success(listEdiResponse) && !CollectionUtils.isEmpty(listEdiResponse.getResults())) {
            SdkMatchVo item = BeanUtil.toBean(listEdiResponse.getResults().get(0), SdkMatchVo.class);
            item.setMatchType(MatchType.FOOTBALL);
            item.setRunTime(getMatchRuntime(item.getId(), item.getStatusId(), item.getMatchTime()));
            item.setMatchStatus(FootballStatusType.getMatchStatus(item.getStatusId()));
            if (Objects.equals(matchId, item.getId())) {
                distributedCache.put(cacheKey, item, 1, TimeUnit.DAYS);
                return item;
            }
        }
        return distributedCache.get(cacheKey, SdkMatchVo.class);
    }

    @Override
    public MatchLiveHistory getFootballMatchLiveHistory(Integer matchId) {
        com.edi.sdk.football.request.MatchLiveHistoryRequest request = new com.edi.sdk.football.request.MatchLiveHistoryRequest();
        request.setId(matchId);
        EdiResponse<MatchLiveHistory> listEdiResponse = footballClient.matchLiveHistory(request);
        String cacheKey = CacheUtil.buildKey(CacheConstant.SDK_FOOTBALL_MATCH_LIVE_HISTORY, matchId.toString());
        if (success(listEdiResponse)) {
            MatchLiveHistory data = listEdiResponse.getResults();
            if (Objects.nonNull(data) &&Objects.equals(matchId, data.getId())) {
                distributedCache.put(cacheKey, data, 1, TimeUnit.DAYS);
                return data;
            }
        }
        return distributedCache.get(cacheKey, MatchLiveHistory.class);
    }

    @Override
    public com.edi.sdk.basketball.domain.MatchLiveHistory getbasketballMatchLiveHistory(Integer matchId) {
        com.edi.sdk.basketball.request.MatchLiveHistoryRequest request = new com.edi.sdk.basketball.request.MatchLiveHistoryRequest();
        request.setId(matchId);
        EdiResponse<com.edi.sdk.basketball.domain.MatchLiveHistory> listEdiResponse = basketballClient.matchLiveHistory(request);
        String cacheKey = CacheUtil.buildKey(CacheConstant.SDK_BASKETBALL_MATCH_LIVE_HISTORY, matchId.toString());
        if (success(listEdiResponse)) {
            com.edi.sdk.basketball.domain.MatchLiveHistory data = listEdiResponse.getResults();
            if (Objects.nonNull(data) && Objects.equals(matchId, data.getId())) {
                distributedCache.put(cacheKey, data, 1, TimeUnit.DAYS);
                return data;
            }
        }
        return distributedCache.get(cacheKey, com.edi.sdk.basketball.domain.MatchLiveHistory.class);
    }

    private SdkMatchVo getBasketballMatchById(Integer matchId) {
        com.edi.sdk.basketball.request.MatchListRequest request = new com.edi.sdk.basketball.request.MatchListRequest();
        request.setLimit(1);
        request.setId(matchId);
        EdiResponse<List<com.edi.sdk.basketball.domain.Match>> listEdiResponse = basketballClient.matchList(request);
        String cacheKey = CacheUtil.buildKey(CacheConstant.SDK_BASKETBALL_MATCH, matchId.toString());
        if (success(listEdiResponse) && !CollectionUtils.isEmpty(listEdiResponse.getResults())) {
            SdkMatchVo item = BeanUtil.toBean(listEdiResponse.getResults().get(0), SdkMatchVo.class);
            item.setMatchType(MatchType.BASKETBALL);
            item.setMatchStatus(BasketballStatusType.getMatchStatus(item.getStatusId()));
            if (Objects.equals(matchId, item.getId())) {
                distributedCache.put(cacheKey, item, 1, TimeUnit.DAYS);
                return item;
            }
        }
        return distributedCache.get(cacheKey, SdkMatchVo.class);
    }

    private List<MatchCountVO> getFootballMatchTimeCount(LocalDate startTime, LocalDate endTime) {
        MatchTimeCountListRequest request = new MatchTimeCountListRequest();
        request.setStartMatchTime(startTime.atStartOfDay());
        request.setEndMatchTime(endTime.atTime(LocalTime.MAX));
        EdiResponse<List<MatchTimeCountList>> listEdiResponse = footballClient.matchTimeCountListRequest(request);
        if (success(listEdiResponse)) {
            List<MatchCountVO> returnList = Lists.newArrayList();
            listEdiResponse.getResults().forEach(item -> returnList.add(MatchCountVO.builder().time(item.get_id()).num(item.getNum()).build()));
            return returnList;
        }
        return null;
    }

    private List<MatchCountVO> getBasketballMatchTimeCount(LocalDate startTime, LocalDate endTime) {
        com.edi.sdk.basketball.request.MatchTimeCountListRequest request = new com.edi.sdk.basketball.request.MatchTimeCountListRequest();
        request.setStartMatchTime(startTime.atStartOfDay());
        request.setEndMatchTime(endTime.atTime(LocalTime.MAX));
        EdiResponse<List<com.edi.sdk.basketball.domain.MatchTimeCountList>> listEdiResponse = basketballClient.matchTimeCountListRequest(request);
        if (success(listEdiResponse)) {
            List<MatchCountVO> returnList = Lists.newArrayList();
            listEdiResponse.getResults().forEach(item -> returnList.add(MatchCountVO.builder().time(item.get_id()).num(item.getNum()).build()));
            return returnList;
        }
        return null;
    }

    private List<SdkMatchVo> getFootballMatchList(List<Integer> competitionIds, LocalDate startTime, LocalDate endTime, List<Integer> status) {
        List<SdkMatchVo> sdkMatchVos;
        com.edi.sdk.football.request.MatchScheduleSearchRequest request = new com.edi.sdk.football.request.MatchScheduleSearchRequest();
        request.setSize(2000);
        request.setNumber(1);
        request.setCompetitionIds(competitionIds);
        request.setStatus(status);
        request.setStartMatchTime(startTime.atStartOfDay());
        request.setEndMatchTime(endTime.atTime(LocalTime.MAX));
        EdiResponse<com.edi.commons.model.vo.PageVo<com.edi.sdk.football.domain.Match>> listEdiResponse = footballClient.matchScheduleSearchRequest(request);
        String cacheKey = CacheUtil.buildKey(CacheConstant.SDK_FOOTBALL_MATCH_LIST, JSON.toJSONString(competitionIds), startTime.toString(), endTime.toString(), JSON.toJSONString(status));
        if (success(listEdiResponse)) {
            sdkMatchVos = BeanUtil.copyToList(listEdiResponse.getResults().getContent(), SdkMatchVo.class);
            sdkMatchVos.forEach(item -> {
                item.setMatchType(MatchType.FOOTBALL);
                item.setRunTime(getMatchRuntime(item.getId(), item.getStatusId(), item.getMatchTime()));
                item.setMatchStatus(FootballStatusType.getMatchStatus(item.getStatusId()));
            });
            distributedCache.put(cacheKey, sdkMatchVos, 1, TimeUnit.DAYS);
        } else {
            sdkMatchVos = distributedCache.getList(cacheKey, SdkMatchVo.class);
        }
        return sdkMatchVos;
    }

    private List<SdkMatchVo> getBasketballMatchList(List<Integer> competitionIds, LocalDate startTime, LocalDate endTime, List<Integer> status) {
        List<SdkMatchVo> sdkMatchVos;
        com.edi.sdk.basketball.request.MatchScheduleSearchRequest request = new com.edi.sdk.basketball.request.MatchScheduleSearchRequest();
        request.setSize(2000);
        request.setNumber(1);
        request.setStatus(status);
        request.setCompetitionIds(competitionIds);
        request.setStartMatchTime(startTime.atStartOfDay());
        request.setEndMatchTime(endTime.atTime(LocalTime.MAX));
        EdiResponse<PageVo<com.edi.sdk.basketball.domain.Match>> listEdiResponse = basketballClient.matchScheduleSearchRequest(request);
        String cacheKey = CacheUtil.buildKey(CacheConstant.SDK_BASKETBALL_MATCH_LIST, JSON.toJSONString(competitionIds), startTime.toString(), endTime.toString(), JSON.toJSONString(status));
        if (success(listEdiResponse)) {
            sdkMatchVos = BeanUtil.copyToList(listEdiResponse.getResults().getContent(), SdkMatchVo.class);
            sdkMatchVos.forEach(
                    item -> {
                        item.setMatchType(MatchType.BASKETBALL);
                        item.setMatchStatus(BasketballStatusType.getMatchStatus(item.getStatusId()));
                    }
            );
            distributedCache.put(cacheKey, sdkMatchVos, 1, TimeUnit.DAYS);
        } else {
            sdkMatchVos = distributedCache.getList(cacheKey, SdkMatchVo.class);
        }
        return sdkMatchVos;
    }

    /**
     * 根据比赛状态和时间计算进行多少分钟
     *
     * @param statusId
     * @param matchTime
     * @return
     */
    private Integer getMatchRuntime(Integer matchId, Integer statusId, Integer matchTime) {
        Integer minutes = null;
        if (Objects.isNull(matchTime)) {
            return null;
        }
        Integer startTime = distributedCache.get(CacheUtil.buildKey(CacheConstant.FOOTBALL_START_TIME, matchId.toString()), Integer.class);

        long l1 = System.currentTimeMillis();//毫秒
        //已开赛
        //上半场：比赛进行分钟数=(当前时间戳-上半场开球时间戳) / 60 + 1
        if (FootballStatusType.FIRST_HALF.getCode().equals(statusId)) {
            if (Objects.isNull(startTime)) {
                startTime = matchTime;
            }
            //当前时间戳
            long l2 = l1 / 1000;
            long l3 = (l2 - startTime) / 60 + 1;
            minutes = Math.toIntExact(l3);
        }
        //下半场
        if (FootballStatusType.SECOND_HALF.getCode().equals(statusId) || FootballStatusType.EXTRA_TIME.getCode().equals(statusId)) {
            //下半场：比赛进行分钟数=(当前时间戳-下半场开球时间戳) / 60 + 45 + 1
            long l2 = l1 / 1000;
            long l3;
            if (Objects.isNull(startTime)) {
                l3 = (l2 - matchTime) / 60 + 1;
            } else {
                l3 = (l2 - startTime) / 60 + 45 + 1;
            }
            minutes = Math.toIntExact(l3);
        }
        return minutes;
    }

    private <T> boolean success(EdiResponse<T> response) {
        return Objects.nonNull(response) && Objects.nonNull(response.getResults());
    }
}
