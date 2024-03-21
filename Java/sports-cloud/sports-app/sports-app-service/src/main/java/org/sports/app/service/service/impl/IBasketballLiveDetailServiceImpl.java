package org.sports.app.service.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alicp.jetcache.Cache;
import com.alicp.jetcache.CacheManager;
import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.template.QuickConfig;
import com.edi.sdk.basketball.BasketballClient;
import com.edi.sdk.basketball.domain.MatchLive;
import com.edi.sdk.basketball.domain.MatchLiveHistory;
import com.edi.sdk.basketball.request.MatchLiveRequest;
import com.edi.sdk.core.EdiResponse;
import com.google.common.collect.Lists;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.dto.MatchScoreDTO;
import org.sports.admin.manage.service.enums.BasketballStatusType;
import org.sports.admin.manage.service.service.IPushMessageService;
import org.sports.admin.manage.service.service.SdkService;
import org.sports.app.service.constant.LiveCacheConstant;
import org.sports.app.service.service.IBasketballLiveDetailService;
import org.sports.app.service.utils.MatchUtils;
import org.sports.app.service.vo.live.*;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class IBasketballLiveDetailServiceImpl implements IBasketballLiveDetailService {

    @Resource
    private CacheManager cacheManager;
    @Resource
    private BasketballClient basketballClient;
    private Cache<String, BasketballScoreVO> scoreCache;
    private Cache<String, BasketballStatsVO> teamStatsListCache;
    private Cache<String, BasketballScoreStatsVO> scoreStatsCache;
    @Resource
    private DistributedCache distributedCache;
    @Resource
    private IPushMessageService pushMessageService;
    @Resource
    private SdkService sdkService;

    @PostConstruct
    public void init() {
        QuickConfig qc = QuickConfig.newBuilder("sports:basketball:")
                .expire(Duration.ofDays(1))
                .cacheType(CacheType.REMOTE)
                .syncLocal(false)
                .build();
        scoreCache = cacheManager.getOrCreateCache(qc);
        teamStatsListCache = cacheManager.getOrCreateCache(qc);
        scoreStatsCache = cacheManager.getOrCreateCache(qc);
    }

    /**
     * 加载比赛实时统计数据
     */
    @Override
    public void loadMatchStatsCache() {
        MatchLiveRequest request = new MatchLiveRequest();
        EdiResponse<List<MatchLive>> response = basketballClient.matchLive(request);
        if (success(response)) {
            List<MatchLive> matchLiveList = response.getResults();
            if (CollUtil.isNotEmpty(matchLiveList)) {
                for (MatchLive matchLive : matchLiveList) {
                    Integer matchId = matchLive.getId();
                    List<Object> scoreList = matchLive.getScore();
                    if (CollUtil.isNotEmpty(scoreList)) {
                        String matchScoreKey = StrUtil.format(LiveCacheConstant.LIVE_MATCH_SCORE, matchId);
                        // 转换为业务需要数据，缓存得分记录
                        BasketballScoreVO scoreVO = new BasketballScoreVO();
                        scoreVO.setStatus(Integer.parseInt(scoreList.get(1).toString()));
                        scoreVO.setTimeRemaining(Integer.parseInt(scoreList.get(2).toString()));
                        List<Integer> homeScoreList = (List<Integer>) scoreList.get(3);
                        List<Integer> awayScoreList = (List<Integer>) scoreList.get(4);
                        scoreVO.setHomeScoreList(homeScoreList);
                        scoreVO.setAwayScoreList(awayScoreList);
                        List<List<Integer>> overTimeScores = matchLive.getOverTimeScores();
                        if (CollUtil.isNotEmpty(overTimeScores)) {
                            scoreVO.setHomeOverTimeScoresList(overTimeScores.get(0));
                            scoreVO.setAwayOverTimeScoresList(overTimeScores.get(1));
                        } else if (homeScoreList.get(4) > 0 || awayScoreList.get(4) > 0) {
                            scoreVO.setHomeOverTimeScoresList(CollUtil.toList(homeScoreList.get(4)));
                            scoreVO.setAwayOverTimeScoresList(CollUtil.toList(awayScoreList.get(4)));
                        }
                        scoreCache.put(matchScoreKey, scoreVO);
                    }
                    List<Object> playerList = matchLive.getPlayers();
                    if (CollUtil.isNotEmpty(playerList)) {
                        String playScoreKey = StrUtil.format(LiveCacheConstant.LIVE_PLAYER_SCORE, matchId);
                        // 转换为业务需要数据，缓存比赛球员技术统计
                        BasketballScoreStatsVO scoreStatsVO = new BasketballScoreStatsVO();
                        scoreStatsVO.setHome(MatchUtils.coverPlayStats((List<Object>) playerList.get(0)));
                        scoreStatsVO.setAway(MatchUtils.coverPlayStats((List<Object>) playerList.get(1)));
                        scoreStatsCache.put(playScoreKey, scoreStatsVO);
                        // 转换为业务需要数据，缓存比赛球队技术统计
                        String matchStatsKey = StrUtil.format(LiveCacheConstant.LIVE_MATCH_STATS, matchId);
                        BasketballStatsVO statsVOList = new BasketballStatsVO();
                        statsVOList.setHome(new BasketballTeamDataStatsVO(CollUtil.toList(playerList.get(2).toString().split("\\^"))));
                        statsVOList.setAway(new BasketballTeamDataStatsVO(CollUtil.toList(playerList.get(3).toString().split("\\^"))));
                        teamStatsListCache.put(matchStatsKey, statsVOList);
                    }
                }
                //推送数据到前端
                List<MatchScoreDTO> sendMessage = matchLiveList.parallelStream().map(this::tranMatchLiveToMatchScoreDTO).filter(Objects::nonNull).collect(Collectors.toList());
                pushMessageService.pushMatchRealScore(sendMessage);
                List<MatchScoreDTO> finishedMatch = sendMessage.stream().filter(item -> BasketballStatusType.FINISHED.getCode().equals(item.getStatus())).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(finishedMatch)) {
                    pushMessageService.pushFinishedMatch(finishedMatch, MatchType.BASKETBALL);
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
        List<Object> homeScores = null;
        List<Object> awayScores = null;
        if (score.size() > 0) {
            matchStatus = (Integer) score.get(1);//比赛状态
        }
        if (BasketballStatusType.NOT_START.getCode().equals(matchStatus)) {
            return null;
        }
        if (!CollectionUtils.isEmpty(score) && score.size() >= 5) {
            homeScores = (List<Object>) score.get(3);
            awayScores = (List<Object>) score.get(4);
        }
        MatchScoreDTO vo = new MatchScoreDTO();
        vo.setMatchId(match.getId());
        vo.setMatchType(MatchType.BASKETBALL);
        vo.setStatus(matchStatus);
        setBasketballScore(vo, homeScores, awayScores);
        vo.setScoresDetail(getScoresDetail(matchStatus, homeScores, awayScores, match.getOverTimeScores()));
        //这里要把最新的数据放到缓存中用于列表中数据的更新
        distributedCache.put(CacheUtil.buildKey(CacheConstant.APP_BASKETBALL_MATCH_NEWEST_SCORE, match.getId().toString()), vo);
        return vo;
    }

    private List<List<Integer>> getScoresDetail(Integer statusId, List<Object> homeScores, List<Object> awayScores, List<List<Integer>> overTimeScores) {
        List<List<Integer>> scoresDetail = Lists.newArrayList();
        if (homeScores.size() != 5 || awayScores.size() != 5) {
            return scoresDetail;
        }
        List<Integer> home = Lists.newArrayList();
        List<Integer> away = Lists.newArrayList();
        home.addAll(homeScores.subList(0, 4).stream().map(item -> (Integer) item).collect(Collectors.toList()));
        away.addAll(awayScores.subList(0, 4).stream().map(item -> (Integer) item).collect(Collectors.toList()));
        if (CollectionUtils.isEmpty(overTimeScores)) {
            if (BasketballStatusType.EXTRA_TIME.getCode().equals(statusId)) {
                home.add((Integer) homeScores.get(4));
                away.add((Integer) awayScores.get(4));
            }
        } else {
            if (overTimeScores.size() == 2) {
                home.addAll(overTimeScores.get(0));
                away.addAll(overTimeScores.get(1));
            }
        }
        scoresDetail.add(home);
        scoresDetail.add(away);
        return scoresDetail;
    }

    /**
     * 设置比赛半场比分，当前比分，当前进行阶段
     * 比分字段说明
     * example：[0, 0, 0, 0, 0]
     * Enum: Array[5]
     * 0:"第1节分数 - int"
     * 1:"第2节分数 - int"
     * 2:"第3节分数 - int"
     * 3:"第4节分数 - int"
     * 4:"加时分数 - int"
     *
     * @param vo         比赛对象
     * @param homeScores 主队得分
     * @param awayScores 客队得分
     */
    public static void setBasketballScore(MatchScoreDTO vo, List<Object> homeScores, List<Object> awayScores) {
        int totalScore = 0;
        int halfScore = 0;
        for (int i = 0; i < homeScores.size(); i++) {
            int score = (int) homeScores.get(i);
            if (i < 2) {
                halfScore += score;
            }
            totalScore += score;
        }
        vo.setHomeScore(totalScore);
        vo.setHomeHalfScore(halfScore);
        totalScore = 0;
        halfScore = 0;
        for (int i = 0; i < awayScores.size(); i++) {
            int score = (int) awayScores.get(i);
            if (i < 2) {
                halfScore += score;
            }
            totalScore += score;
        }
        vo.setAwayScore(totalScore);
        vo.setAwayHalfScore(halfScore);
        vo.setRegularTimeCore(vo.getHomeScore() + "-" + vo.getAwayScore());
    }



    /**
     * 获取比赛阵容
     *
     * @param matchId 比赛ID
     * @return
     */
    @Override
    public BasketballScoreStatsVO getMatchLineupDetail(String matchId) {
        String playScoreKey = StrUtil.format(LiveCacheConstant.LIVE_PLAYER_SCORE, matchId);
        BasketballScoreStatsVO scoreStatsVO = scoreStatsCache.get(playScoreKey);
        if (Objects.isNull(scoreStatsVO)) {
            MatchLiveHistory matchLive = sdkService.getbasketballMatchLiveHistory(Integer.valueOf(matchId));
            if (Objects.isNull(matchLive)) {
                return null;
            }
            List<Object> playerList = matchLive.getPlayers();
            if (CollectionUtil.isNotEmpty(playerList) && playerList.size() >= 2) {
                scoreStatsVO = new BasketballScoreStatsVO();
                // 转换为业务需要数据，缓存比赛球员技术统计
                scoreStatsVO.setHome(MatchUtils.coverPlayStats((List<Object>) playerList.get(0)));
                scoreStatsVO.setAway(MatchUtils.coverPlayStats((List<Object>) playerList.get(1)));
                scoreStatsCache.put(playScoreKey, scoreStatsVO);
            }
        }
        return scoreStatsVO;
    }


    /**
     * 查询比赛得分
     *
     * @param matchId 比赛ID
     */
    @Override
    public BasketballScoreVO getScore(String matchId) {
        String scoreKey = StrUtil.format(LiveCacheConstant.LIVE_MATCH_SCORE, matchId);
        BasketballScoreVO scoreVO = scoreCache.get(scoreKey);
        if (Objects.isNull(scoreVO)) {

            MatchLiveHistory matchLive = sdkService.getbasketballMatchLiveHistory(Integer.valueOf(matchId));
            if (Objects.isNull(matchLive)) {
                return null;
            }
            List<Object> scoreList = matchLive.getScore();
            if (CollUtil.isNotEmpty(scoreList)) {
                scoreVO = new BasketballScoreVO();
                String matchScoreKey = StrUtil.format(LiveCacheConstant.LIVE_MATCH_SCORE, matchId);
                // 转换为业务需要数据，缓存得分记录
                scoreVO.setStatus(Integer.parseInt(scoreList.get(1).toString()));
                scoreVO.setTimeRemaining(Integer.parseInt(scoreList.get(2).toString()));
                List<Integer> homeScoreList = (List<Integer>) scoreList.get(3);
                List<Integer> awayScoreList = (List<Integer>) scoreList.get(4);
                scoreVO.setHomeScoreList(homeScoreList);
                scoreVO.setAwayScoreList(awayScoreList);
                List<List<Integer>> overTimeScores = matchLive.getOverTimeScores();
                if (CollUtil.isNotEmpty(overTimeScores)) {
                    scoreVO.setHomeOverTimeScoresList(overTimeScores.get(0));
                    scoreVO.setAwayOverTimeScoresList(overTimeScores.get(1));
                } else if (homeScoreList.get(4) > 0 || awayScoreList.get(4) > 0) {
                    scoreVO.setHomeOverTimeScoresList(CollUtil.toList(homeScoreList.get(4)));
                    scoreVO.setAwayOverTimeScoresList(CollUtil.toList(awayScoreList.get(4)));
                }
                scoreCache.put(matchScoreKey, scoreVO);
            }
        }
        return scoreVO;
    }

    /**
     * 查询比赛技术统计
     *
     * @param matchId 比赛ID
     */
    @Override
    public BasketballStatsVO getMatchStats(String matchId) {
        String matchStatsKey = StrUtil.format(LiveCacheConstant.LIVE_MATCH_STATS, matchId);
        BasketballStatsVO statsVOList = teamStatsListCache.get(matchStatsKey);
        if (Objects.isNull(statsVOList)) {

            MatchLiveHistory matchLive = sdkService.getbasketballMatchLiveHistory(Integer.valueOf(matchId));
            if (Objects.isNull(matchLive)) {
                return null;
            }
            List<Object> playerList = matchLive.getPlayers();
            if (CollectionUtil.isNotEmpty(playerList) && playerList.size() >= 4) {
                statsVOList = new BasketballStatsVO();
                statsVOList.setHome(new BasketballTeamDataStatsVO(CollUtil.toList(playerList.get(2).toString().split("\\^"))));
                statsVOList.setAway(new BasketballTeamDataStatsVO(CollUtil.toList(playerList.get(3).toString().split("\\^"))));
                teamStatsListCache.put(matchStatsKey, statsVOList);
            }
        }
        return statsVOList;
    }

    @Override
    public MatchDataStataVO getMatchDataStata(Integer matchId, Integer status) {
        String mId = String.valueOf(matchId);
        MatchDataStataVO matchDataStataVO = new MatchDataStataVO();
        // 是否有赛况数据
        if (BasketballStatusType.FINISHED.getCode().equals(status)) {
            matchDataStataVO.setHasStata(Boolean.TRUE);
        } else {
            matchDataStataVO.setHasStata(Objects.nonNull(this.getScore(mId)));
        }
        // 是否有阵容数据
        matchDataStataVO.setHasLineup(Objects.nonNull(this.getMatchLineupDetail(mId)));
        // 是否有指数数据
        matchDataStataVO.setHasOdds(Boolean.FALSE);
        return matchDataStataVO;
    }

    private <T> boolean success(EdiResponse<T> response) {
        return Objects.nonNull(response) && Objects.nonNull(response.getResults());
    }
}
