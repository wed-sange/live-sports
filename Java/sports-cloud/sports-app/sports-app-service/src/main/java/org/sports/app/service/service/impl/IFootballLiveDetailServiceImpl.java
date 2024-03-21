package org.sports.app.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CachePenetrationProtect;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.alicp.jetcache.template.QuickConfig;
import com.edi.sdk.core.EdiResponse;
import com.edi.sdk.football.FootballClient;
import com.edi.sdk.football.domain.*;
import com.edi.sdk.football.request.MatchLineupDetailRequest;
import com.edi.sdk.football.request.MatchLiveRequest;
import com.edi.sdk.football.request.OddsHistoryRequest;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.dto.MatchScoreDTO;
import org.sports.admin.manage.service.enums.BasketballStatusType;
import org.sports.admin.manage.service.enums.FootballStatusType;
import org.sports.admin.manage.service.service.IPushMessageService;
import org.sports.admin.manage.service.service.SdkService;
import org.sports.admin.manage.service.vo.SdkMatchVo;
import org.sports.admin.manage.service.vo.SdkRefereeVo;
import org.sports.admin.manage.service.vo.SdkTeamVo;
import org.sports.app.service.constant.LiveCacheConstant;
import org.sports.app.service.converter.MatchLiveConvert;
import org.sports.admin.manage.service.enums.NationalType;
import org.sports.app.service.service.IFootballLiveDetailService;
import org.sports.app.service.utils.MatchUtils;
import org.sports.app.service.utils.OddsCompanyUtil;
import org.sports.app.service.vo.live.*;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class IFootballLiveDetailServiceImpl implements IFootballLiveDetailService {

    @Resource
    private CacheManager cacheManager;
    @Resource
    private FootballClient footballClient;

    @Resource
    private DistributedCache distributedCache;
    @Resource
    private SdkService sdkService;
    private Cache<String, List<TliveVO>> tLiveListCache;
    private Cache<String, List<IncidentsVO>> incidentsListCache;
    private Cache<String, List<StatsVO>> statsListCache;
    private Cache<String, CompanyOdds> companyOddsCache;

    @Resource
    private IPushMessageService pushMessageService;

    @PostConstruct
    public void init() {
        QuickConfig qc = QuickConfig.newBuilder("sports:football:")
                .expire(Duration.ofDays(1))
                .cacheType(CacheType.REMOTE)
                .syncLocal(false)
                .build();
        tLiveListCache = cacheManager.getOrCreateCache(qc);
        incidentsListCache = cacheManager.getOrCreateCache(qc);
        statsListCache = cacheManager.getOrCreateCache(qc);
        QuickConfig qc1 = QuickConfig.newBuilder("sports:football:odds:")
                .expire(Duration.ofSeconds(50))
                .cacheType(CacheType.REMOTE)
                .syncLocal(false)
                .build();
        companyOddsCache = cacheManager.getOrCreateCache(qc1);
    }

    /**
     * 加载比赛实时统计数据
     */
    @Override
    public void loadMatchStatsCache() {
        MatchLiveRequest request = new MatchLiveRequest();
        EdiResponse<List<MatchLive>> response = footballClient.matchLive(request);
        if (success(response)) {
            List<MatchLive> matchLiveList = response.getResults();
            if (CollUtil.isNotEmpty(matchLiveList)) {
                for (MatchLive matchLive : matchLiveList) {
                    Integer matchId = matchLive.getId();
                    String textLiveKey = StrUtil.format(LiveCacheConstant.LIVE_TEXT_LIVE, matchId);
                    List<MatchLive.Tlive> tLiveList = matchLive.getTlive();
                    // 转换为业务需要数据，缓存文字直播信息
                    tLiveListCache.put(textLiveKey, MatchLiveConvert.INSTANCE.convertToTliveVO(tLiveList));
                    String matchStatsKey = StrUtil.format(LiveCacheConstant.LIVE_MATCH_STATS, matchId);
                    List<MatchLive.Stats> statsList = matchLive.getStats();
                    // 转换为业务需要数据，缓存比赛技术统计
                    statsListCache.put(matchStatsKey, MatchLiveConvert.INSTANCE.convertToStatsVO(statsList));
                    String incidentsKey = StrUtil.format(LiveCacheConstant.LIVE_INCIDENTS, matchId);
                    List<MatchLive.Incidents> incidentsList = matchLive.getIncidents();
                    // 转换为业务需要数据，缓存比赛事件
                    List<IncidentsVO> incidentsVOS = MatchLiveConvert.INSTANCE.convertToIncidentsVo(incidentsList);
                    MatchUtils.setPlayerShortName(incidentsVOS);
                    incidentsListCache.put(incidentsKey, incidentsVOS);
                    //查询比赛上半场或者下半场开始时间（秒）
                    List<Object> score = matchLive.getScore();
                    if (!CollectionUtils.isEmpty(score) && score.size() >= 5) {
                        Integer matchStatus = (Integer) score.get(1);//比赛状态
                        if (FootballStatusType.FIRST_HALF.getCode().equals(matchStatus) || FootballStatusType.SECOND_HALF.getCode().equals(matchStatus)) {
                            Integer time = (Integer) score.get(4);
                            distributedCache.put(CacheUtil.buildKey(CacheConstant.FOOTBALL_START_TIME, matchId.toString()), time, 1, TimeUnit.DAYS);
                        }
                    }
                }
                //推送数据到前端
                List<MatchScoreDTO> sendMessage = matchLiveList.parallelStream().map(this::tranMatchLiveToMatchScoreDTO).filter(Objects::nonNull).collect(Collectors.toList());
                pushMessageService.pushMatchRealScore(sendMessage);

                List<MatchScoreDTO> finishedMatch = sendMessage.stream().filter(item -> FootballStatusType.FINISHED.getCode().equals(item.getStatus())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(finishedMatch)) {
                    pushMessageService.pushFinishedMatch(finishedMatch, MatchType.FOOTBALL);
                }

            }
        }
    }

    private MatchScoreDTO tranMatchLiveToMatchScoreDTO(MatchLive match) {
        if (Objects.isNull(match)) {
            return null;
        }
        //查询比赛上半场或者下半场开始时间（秒）
        List<Object> score = match.getScore();
        Integer matchStatus = null;
        Integer startTime = null;
        List<Object> homeScores = null;
        List<Object> awayScores = null;
        if (score.size() > 0) {
            matchStatus = (Integer) score.get(1);//比赛状态
        }
        if (BasketballStatusType.NOT_START.getCode().equals(matchStatus)) {
            return null;
        }
        if (!CollectionUtils.isEmpty(score) && score.size() >= 5) {
            if (FootballStatusType.FIRST_HALF.getCode().equals(matchStatus) || FootballStatusType.SECOND_HALF.getCode().equals(matchStatus) || FootballStatusType.EXTRA_TIME.getCode().equals(matchStatus)) {
                startTime = (Integer) score.get(4);
            }
            homeScores = (List<Object>) score.get(2);
            awayScores = (List<Object>) score.get(3);
        }
        MatchScoreDTO vo = new MatchScoreDTO();
        vo.setMatchId(match.getId());
        vo.setMatchType(MatchType.FOOTBALL);
        vo.setStatus(matchStatus);
        vo.setRunTime(getMatchRuntime(matchStatus, startTime));
        setFootballScore(vo, homeScores, awayScores);
        //这里要把最新的数据放到缓存中用于列表中数据的更新
        distributedCache.put(CacheUtil.buildKey(CacheConstant.APP_FOOTBALL_MATCH_NEWEST_SCORE, match.getId().toString()), vo);
        return vo;
    }

    /**
     * 根据比赛状态和时间计算进行多少分钟
     */
    private Integer getMatchRuntime(Integer statusId, Integer startTime) {
        Integer minutes = null;
        if (Objects.isNull(startTime)) {
            return minutes;
        }
        long l1 = System.currentTimeMillis();//毫秒
        //已开赛
        //上半场：比赛进行分钟数=(当前时间戳-上半场开球时间戳) / 60 + 1
        if (FootballStatusType.FIRST_HALF.getCode().equals(statusId)) {
            //当前时间戳
            long l2 = l1 / 1000;
            long l3 = (l2 - startTime) / 60 + 1;
            minutes = Math.toIntExact(l3);
        }
        //下半场
        if (FootballStatusType.SECOND_HALF.getCode().equals(statusId)) {
            //下半场：比赛进行分钟数=(当前时间戳-下半场开球时间戳) / 60 + 45 + 1
            long l2 = l1 / 1000;
            long l3 = (l2 - startTime) / 60 + 45 + 1;
            minutes = Math.toIntExact(l3);
        }
        return minutes;
    }

    /**
     * `
     * 比分字段说明
     * example：[1, 0, 0, 0, -1, 0, 0]
     * 0:"比分(常规时间) - int"
     * 1:"半场比分 - int"
     * 2:"红牌 - int"
     * 3:"黄牌 - int"
     * 4:"角球，-1表示没有角球数据 - int"
     * 5:"加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int"
     * 6:"点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
     *
     * @param vo         比赛对象
     * @param homeScores 主队得分
     * @param awayScores 客队得分
     */
    public static void setFootballScore(MatchScoreDTO vo, List<Object> homeScores, List<Object> awayScores) {
        if (homeScores.size() != 7 || awayScores.size() != 7) {
            return;
        }
        int homeScore = (int) homeScores.get(0);
        int homeExtraScore = (int) homeScores.get(5);
        int homePKScore = (int) homeScores.get(6);
        int awayScore = (int) awayScores.get(0);
        int awayExtraScore = (int) awayScores.get(5);
        int awayPkScore = (int) awayScores.get(6);
        if (homeExtraScore > 0) {
            vo.setHomeScore(homeExtraScore + homePKScore);
        } else {
            vo.setHomeScore(homeScore + homePKScore);
        }
        if (awayExtraScore > 0) {
            vo.setAwayScore(awayExtraScore + awayPkScore);
        } else {
            vo.setAwayScore(awayScore + awayPkScore);
        }
        vo.setHomeHalfScore((Integer) homeScores.get(1));
        vo.setAwayHalfScore((Integer) awayScores.get(1));
        vo.setRegularTimeCore(homeScore + "-" + awayScore);

    }

    /**
     * 获取比赛阵容
     *
     * @param matchId 比赛ID
     * @return
     */
    @Override
    @Cached(cacheType = CacheType.REMOTE, name = "sports:football:lineup:", key = "#matchId", expire = 1, timeUnit = TimeUnit.HOURS)
    @CachePenetrationProtect
    public MatchLineupDetailVO getMatchLineupDetail(String matchId) {
        MatchLineupDetailRequest request = new MatchLineupDetailRequest();
        request.setId(Integer.parseInt(matchId));
        EdiResponse<MatchLineupDetail> response = footballClient.matchLineupDetail(request);
        if (success(response)) {
            MatchLineupDetail matchLineupDetail = response.getResults();
            if (Objects.nonNull(matchLineupDetail) && Objects.nonNull(matchLineupDetail.getHome())) {
                MatchLineupDetailVO matchLineupDetailVO = MatchLiveConvert.INSTANCE.convertToLineupVO(matchLineupDetail);
                MatchUtils.setPlayerShortName(matchLineupDetailVO);
                SdkMatchVo matchDetail = sdkService.getMatchDetailById(Integer.parseInt(matchId), MatchType.FOOTBALL);
                if (Objects.nonNull(matchDetail)) {
                    Integer refereeId = matchDetail.getRefereeId();
                    if (Objects.nonNull(refereeId)) {
                        SdkRefereeVo refereeVo = sdkService.getFootballRefereeById(refereeId);
                        if (Objects.nonNull(refereeVo)) {
                            Locale locale = LocaleContextHolder.getLocale();
                            if (Objects.equals(locale, Locale.US)) {
                                matchLineupDetailVO.setRefereeName(refereeVo.getNameEn());
                            } else if (Objects.equals(locale, Locale.TRADITIONAL_CHINESE)) {
                                matchLineupDetailVO.setRefereeName(refereeVo.getNameZht());
                            } else {
                                matchLineupDetailVO.setRefereeName(refereeVo.getNameZh());
                            }
                        }
                    }
                    SdkTeamVo homeTeam = sdkService.findTeamById(matchDetail.getHomeTeamId(), MatchType.FOOTBALL);
                    if (Objects.nonNull(homeTeam)) {
                        matchLineupDetailVO.setHomeLogo(NationalType.YES.getCode().equals(homeTeam.getNational()) ? homeTeam.getCountryLogo() : homeTeam.getLogo());
                        if (Strings.isBlank(matchLineupDetailVO.getHomeLogo())) {
                            matchLineupDetailVO.setHomeLogo(homeTeam.getLogo());
                        }
                        matchLineupDetailVO.setHomeMarketValue(homeTeam.getMarketValue());
                        matchLineupDetailVO.setHomeMarketValueCurrency(homeTeam.getMarketValueCurrency());
                    }
                    SdkTeamVo awayTeam = sdkService.findTeamById(matchDetail.getAwayTeamId(), MatchType.FOOTBALL);
                    if (Objects.nonNull(awayTeam)) {
                        matchLineupDetailVO.setAwayLogo(NationalType.YES.getCode().equals(awayTeam.getNational()) ? awayTeam.getCountryLogo() : awayTeam.getLogo());
                        if (Strings.isBlank(matchLineupDetailVO.getAwayLogo())) {
                            matchLineupDetailVO.setAwayLogo(awayTeam.getLogo());
                        }
                        matchLineupDetailVO.setAwayMarketValue(awayTeam.getMarketValue());
                        matchLineupDetailVO.setAwayMarketValueCurrency(awayTeam.getMarketValueCurrency());
                    }
                }
                return matchLineupDetailVO;
            }
        }
        return null;
    }


    /**
     * 查询比赛文字直播
     *
     * @param matchId 比赛ID
     */
    @Override
    public List<TliveVO> getTextLive(String matchId) {
        String textLiveKey = StrUtil.format(LiveCacheConstant.LIVE_TEXT_LIVE, matchId);
        List<TliveVO> tliveVOS = tLiveListCache.get(textLiveKey);
        if (Objects.isNull(tliveVOS)) {
            MatchLiveHistory matchLive = sdkService.getFootballMatchLiveHistory(Integer.valueOf(matchId));
            if (Objects.isNull(matchLive)) {
                return null;
            }
            List<MatchLiveHistory.Tlive> tLiveList = matchLive.getTlive();
            // 转换为业务需要数据，缓存文字直播信息
            tliveVOS = MatchLiveConvert.INSTANCE.convertHistoryToTliveVO(tLiveList);
            if (!CollectionUtils.isEmpty(tliveVOS)) {
                tLiveListCache.put(textLiveKey, tliveVOS);
            }
        }
        if (!CollectionUtils.isEmpty(tliveVOS)) {
            Collections.reverse(tliveVOS);
        }
        return tliveVOS;
    }

    /**
     * 查询比赛事件
     *
     * @param matchId 比赛ID
     */
    @Override
    public List<IncidentsVO> getIncidents(String matchId) {
        String incidentsKey = StrUtil.format(LiveCacheConstant.LIVE_INCIDENTS, matchId);
        List<IncidentsVO> incidentsVOS = incidentsListCache.get(incidentsKey);
        if (Objects.isNull(incidentsVOS)) {
            MatchLiveHistory matchLive = sdkService.getFootballMatchLiveHistory(Integer.valueOf(matchId));
            if (Objects.isNull(matchLive)) {
                return null;
            }
            List<MatchLiveHistory.Incidents> incidentsList = matchLive.getIncidents();
            // 转换为业务需要数据，缓存比赛事件
            incidentsVOS = MatchLiveConvert.INSTANCE.convertHistoryToIncidentsVo(incidentsList);
            MatchUtils.setPlayerShortName(incidentsVOS);
            if (!CollectionUtils.isEmpty(incidentsVOS)) {
                incidentsListCache.put(incidentsKey, incidentsVOS);
            }
        }
        if (!CollectionUtils.isEmpty(incidentsVOS)) {
            Collections.reverse(incidentsVOS);
        }
        return incidentsVOS;
    }

    /**
     * 查询比赛技术统计
     *
     * @param matchId 比赛ID
     */
    @Override
    public List<StatsVO> getMatchStats(String matchId) {
        String matchStatsKey = StrUtil.format(LiveCacheConstant.LIVE_MATCH_STATS, matchId);
        List<StatsVO> statsVOS = statsListCache.get(matchStatsKey);
        if (Objects.isNull(statsVOS)) {
            MatchLiveHistory matchLive = sdkService.getFootballMatchLiveHistory(Integer.valueOf(matchId));
            if (Objects.isNull(matchLive)) {
                return null;
            }
            List<MatchLiveHistory.Stats> statsList = matchLive.getStats();
            // 转换为业务需要数据，缓存比赛技术统计
            statsVOS = MatchLiveConvert.INSTANCE.convertHistoryToStatsVO(statsList);
            if (!CollectionUtils.isEmpty(statsVOS)) {
                statsListCache.put(matchStatsKey, statsVOS);
            }
        }
        return statsVOS;
    }

    /**
     * 加载指数数据
     *
     * @param matchId 比赛ID
     */
    public void loadOddsHistoryData(String matchId) {
        OddsHistoryRequest request = new OddsHistoryRequest();
        request.setId(Integer.parseInt(matchId));
        EdiResponse<OddsHistory> response = footballClient.oddsHistory(request);
        if (success(response)) {
            OddsHistory oddsHistory = response.getResults();
            CompanyOdds companyOdds = new CompanyOdds();
            List<OddsInfoVO> euOddsList = new ArrayList<>();
            List<OddsInfoVO> asiaOddsList = new ArrayList<>();
            List<OddsInfoVO> bsOddsList = new ArrayList<>();
            List<OddsInfoVO> crOddsList = new ArrayList<>();
            oddsHistory.forEach((companyId, value) -> {
                List<Object> euList = value.getEu();
                if (CollUtil.isNotEmpty(euList)) {
                    euOddsList.add(coverOddsInfo(euList, companyId));
                }
                List<Object> asiaList = value.getAsia();
                if (CollUtil.isNotEmpty(asiaList)) {
                    asiaOddsList.add((coverOddsInfo(asiaList, companyId)));
                }
                List<Object> bsList = value.getBs();
                if (CollUtil.isNotEmpty(bsList)) {
                    bsOddsList.add(coverOddsInfo(bsList, companyId));
                }
                List<Object> crList = value.getCr();
                ;
                if (CollUtil.isNotEmpty(crList)) {
                    crOddsList.add(coverOddsInfo(crList, companyId));
                }
            });
            companyOdds.setEuInfo(euOddsList);
            companyOdds.setAsiaInfo(asiaOddsList);
            companyOdds.setBsInfo(bsOddsList);
            companyOdds.setCrInfo(crOddsList);
            String oddKey = StrUtil.format(LiveCacheConstant.ODDS, matchId);
            companyOddsCache.put(oddKey, companyOdds);
        }
    }

    /**
     * 获取比赛四种指数数据
     *
     * @param matchId 比赛ID
     */
    @Override
    @CachePenetrationProtect
    public CompanyOdds getMatchOdds(String matchId) {
        String oddKey = StrUtil.format(LiveCacheConstant.ODDS, matchId);
        CompanyOdds companyOdds = companyOddsCache.get(oddKey);
        if (Objects.nonNull(companyOdds)) {
            return companyOdds;
        } else {
            loadOddsHistoryData(matchId);
            return companyOddsCache.get(oddKey);
        }
    }

    /**
     * 解析指数数据
     */
    private OddsInfoVO coverOddsInfo(List<Object> list, String companyId) {
        OddsInfoVO oddsInfoVO = new OddsInfoVO();
        oddsInfoVO.setCompanyId(companyId);
        oddsInfoVO.setCompanyName(OddsCompanyUtil.getNameById(companyId));
        List<Object> first = (List<Object>) list.get(list.size() - 1);
        String firstHomeWin = first.get(2).toString();
        String firstDraw = first.get(3).toString();
        String firstAwayWin = first.get(4).toString();
        oddsInfoVO.setFirstHomeWin(firstHomeWin);
        oddsInfoVO.setFirstDraw(firstDraw);
        oddsInfoVO.setFirstAwayWin(firstAwayWin);
        oddsInfoVO.setFirstLossRatio(getLossRatio(firstHomeWin, firstDraw, firstAwayWin));
        List<Object> current = (List<Object>) list.get(0);
        String currentHomeWin = current.get(2).toString();
        String currentDraw = current.get(3).toString();
        String currentAwayWin = current.get(4).toString();
        oddsInfoVO.setCurrentHomeWin(currentHomeWin);
        oddsInfoVO.setCurrentDraw(currentDraw);
        oddsInfoVO.setCurrentAwayWin(currentAwayWin);
        oddsInfoVO.setCurrentLossRatio(getLossRatio(currentHomeWin, currentDraw, currentAwayWin));
        oddsInfoVO.setStatus((Integer) current.get(5));
        oddsInfoVO.setClose((Integer) current.get(6));
        return oddsInfoVO;
    }

    /**
     * 计算赔付率
     */
    private String getLossRatio(String homeWin, String draw, String awayWin) {
        if (StrUtil.hasBlank(homeWin, draw, awayWin)) {
            return "";
        }
        // 赔付率 = 1 / (1 / k1 + 1 / k2 + 1 / k3)，k1=胜赔率，k2=平赔率，k3=负赔率
        //定义k1,k2,k3的值
        BigDecimal k1 = new BigDecimal(homeWin);
        BigDecimal k2 = new BigDecimal(draw);
        BigDecimal k3 = new BigDecimal(awayWin);
        if (BigDecimal.ZERO.compareTo(k1) == 0 || BigDecimal.ZERO.compareTo(k2) == 0 || BigDecimal.ZERO.compareTo(k3) == 0) {
            return "";
        }
        //计算1/k1,1/k2,1/k3
        BigDecimal divideK1 = BigDecimal.ONE.divide(k1, MathContext.DECIMAL128);
        BigDecimal divideK2 = BigDecimal.ONE.divide(k2, MathContext.DECIMAL128);
        BigDecimal divideK3 = BigDecimal.ONE.divide(k3, MathContext.DECIMAL128);
        // 计算 1 / k1 + 1 / k2 + 1 / k3
        BigDecimal sum = divideK1.add(divideK2).add(divideK3);
        // 计算 1 / (1 / k1 + 1 / k2 + 1 / k3)
        return BigDecimal.ONE.divide(sum, MathContext.DECIMAL128).setScale(2, RoundingMode.HALF_UP).toString();
    }


    /**
     * 查询单场是否存在 赛况、阵容、指数
     *
     * @param matchId 比赛ID
     * @param status
     */
    @Override
    public MatchDataStataVO getMatchDataStata(Integer matchId, Integer status) {
        String mId = String.valueOf(matchId);
        MatchDataStataVO matchDataStataVO = new MatchDataStataVO();
        // 是否有赛况数据
        matchDataStataVO.setHasStata(CollUtil.isNotEmpty(this.getMatchStats(mId)));
        // 是否有阵容数据
        matchDataStataVO.setHasLineup(Objects.nonNull(this.getMatchLineupDetail(mId)));
        // 是否有指数数据
        CompanyOdds matchOdds = this.getMatchOdds(mId);
        if (Objects.nonNull(matchOdds) && (CollectionUtil.isNotEmpty(matchOdds.getAsiaInfo()) || CollectionUtil.isNotEmpty(matchOdds.getCrInfo()) || CollectionUtil.isNotEmpty(matchOdds.getEuInfo()) || CollectionUtil.isNotEmpty(matchOdds.getBsInfo()))) {
            matchDataStataVO.setHasOdds(true);
        } else {
            matchDataStataVO.setHasOdds(false);
        }
        return matchDataStataVO;
    }

    @Override
    public void loadMatchVideoLine() {
        List<MatchVideo> footballMatchVideoLine = sdkService.getFootballMatchVideoLine();
        if (CollUtil.isNotEmpty(footballMatchVideoLine)) {
            footballMatchVideoLine.forEach(item -> {
                //比赛线路不为空的时候才展示
                if (Objects.nonNull(item.getPushurl1()) || Objects.nonNull(item.getPushurl2()) || Objects.nonNull(item.getPushurl3()) || Objects.nonNull(item.getPushurl4())) {
                    distributedCache.put(CacheUtil.buildKey(CacheConstant.FOOTBALL_VIDEO_LINES, String.valueOf(item.getMatchId())), item, 1, TimeUnit.DAYS);
                }
            });
        }
    }

    private <T> boolean success(EdiResponse<T> response) {
        return Objects.nonNull(response) && Objects.nonNull(response.getResults());
    }
}
