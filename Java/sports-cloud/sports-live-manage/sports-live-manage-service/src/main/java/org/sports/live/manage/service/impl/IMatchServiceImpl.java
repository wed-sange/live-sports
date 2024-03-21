package org.sports.live.manage.service.impl;

import cn.hutool.extra.pinyin.PinyinUtil;
import com.edi.sdk.football.domain.MatchVideo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.repository.ILiveDao;
import org.sports.admin.manage.dao.req.CompetitionListRequest;
import org.sports.admin.manage.dao.req.MatchListRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.dto.MatchScoreDTO;
import org.sports.admin.manage.service.enums.BasketballStatusType;
import org.sports.admin.manage.service.enums.FootballStatusType;
import org.sports.admin.manage.service.enums.NationalType;
import org.sports.admin.manage.service.service.SdkService;
import org.sports.admin.manage.service.vo.LinesVO;
import org.sports.admin.manage.service.vo.SdkCompetitionVo;
import org.sports.admin.manage.service.vo.SdkMatchVo;
import org.sports.admin.manage.service.vo.SdkTeamVo;
import org.sports.live.manage.service.IMatchService;
import org.sports.live.manage.vo.CompetitionListVo;
import org.sports.live.manage.vo.MatchListVo;
import org.sports.live.manage.vo.MatchLiveVO;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.convention.errorcode.BaseErrorCode;
import org.sports.springboot.starter.convention.exception.RemoteException;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class IMatchServiceImpl implements IMatchService {
    @Resource
    private  ILiveDao liveDao;
    @Resource
    private  SdkService sdkService;
    @Resource
    private DistributedCache distributedCache;

    @Override
    public PageResponse<MatchListVo> list(Long userId, MatchListRequest request) {
        PageResponse<SdkMatchVo> page = sdkService.page(request);
        if (Objects.isNull(page)) {
            return new PageResponse<>(request.getCurrent(), request.getSize());
        }
        List<MatchListVo> collect = page.getRecords().stream()
                .map(match -> {
                    MatchListVo vo = BeanUtil.convert(match, MatchListVo.class);
                    //该场比赛 该主播 直播状态
                    vo.setMatchId(match.getId());
                    SdkTeamVo homeTeam = sdkService.findTeamById(match.getHomeTeamId(), request.getMatchType());
                    if (Objects.nonNull(homeTeam)) {
                        vo.setHomeTeamName(homeTeam.getShortNameZh());
                        vo.setHomeTeamLogo(homeTeam.getLogo());
                    }
                    //客队
                    SdkTeamVo awayTeam = sdkService.findTeamById(match.getAwayTeamId(), request.getMatchType());
                    if (Objects.nonNull(awayTeam)) {
                        vo.setAwayTeamName(awayTeam.getShortNameZh());
                        vo.setAwayTeamLog(awayTeam.getLogo());
                    }

                    SdkCompetitionVo competition = sdkService.findCompetitionById(match.getCompetitionId(), request.getMatchType());
                    //赛事
                    if (Objects.nonNull(competition)) {
                        if (StringUtils.hasText(competition.getShortNameZh())) {
                            vo.setCompetitionName(competition.getShortNameZh());
                        } else {
                            vo.setCompetitionName(competition.getNameZh());
                        }
                    }
                    //设置主客队比分;半场比分;进行到第几节
                    if (Objects.nonNull(match.getHomeScores()) && Objects.nonNull(match.getAwayScores())) {
                        MatchScoreDTO matchScoreDTO;
                        if (Objects.equals(match.getMatchType(), MatchType.BASKETBALL)) {
                            matchScoreDTO =  setBasketballScore(match.getHomeScores(), match.getAwayScores());
                        } else {
                            matchScoreDTO =  setFootballScore(match.getHomeScores(), match.getAwayScores());
                        }
                        if(Objects.nonNull(matchScoreDTO)){
                            vo.setHomeScore(matchScoreDTO.getHomeScore());
                            vo.setHomeHalfScore(matchScoreDTO.getHomeHalfScore());
                            vo.setAwayScore(matchScoreDTO.getAwayScore());
                            vo.setAwayHalfScore(matchScoreDTO.getAwayHalfScore());
                        }
                    }
                    MatchScoreDTO newScore = setNewestScore(vo.getMatchId(),vo.getMatchType(),vo.getHomeScore(),vo.getAwayScore());//更新最新得分
                    if(Objects.nonNull(newScore)){
                        vo.setHomeScore(newScore.getHomeScore());
                        vo.setHomeHalfScore(newScore.getHomeHalfScore());
                        vo.setAwayScore(newScore.getAwayScore());
                        vo.setAwayHalfScore(newScore.getAwayHalfScore());
                    }
                    return vo;
                })
                .collect(Collectors.toList());

        //查询正在直播的比赛
        LiveDO liveDO = liveDao.selectByUserId(userId, LiveStatus.LIVING);
        if (Objects.nonNull(liveDO)) {
            MatchListVo living = BeanUtil.convert(liveDO, MatchListVo.class);
            SdkMatchVo matchDetailById = sdkService.getMatchDetailById(living.getMatchId(), living.getMatchType());
            living.setMatchStatus(matchDetailById.getMatchStatus());

            SdkTeamVo homeTeam = sdkService.findTeamById(matchDetailById.getHomeTeamId(), living.getMatchType());
            if (Objects.nonNull(homeTeam)) {
                living.setHomeTeamName(homeTeam.getShortNameZh());
                living.setHomeTeamLogo(homeTeam.getLogo());
            }
            //客队
            SdkTeamVo awayTeam = sdkService.findTeamById(matchDetailById.getAwayTeamId(), living.getMatchType());
            if (Objects.nonNull(awayTeam)) {
                living.setAwayTeamName(awayTeam.getShortNameZh());
                living.setAwayTeamLog(awayTeam.getLogo());
            }

            SdkCompetitionVo competition = sdkService.findCompetitionById(matchDetailById.getCompetitionId(), living.getMatchType());
            //赛事
            if (Objects.nonNull(competition)) {
                if (StringUtils.hasText(competition.getShortNameZh())) {
                    living.setCompetitionName(competition.getShortNameZh());
                } else {
                    living.setCompetitionName(competition.getNameZh());
                }
            }
            List<MatchListVo> result = new ArrayList<>();
            result.add(living);
            result.addAll(collect);
            collect = result;
        }
//        List<Integer> matchIds = collect.stream().map(MatchListVo::getMatchId).collect(Collectors.toList());
//        Map<Integer, List<LiveDO>> liveDOMap = liveDao.selectByUserId(userId, matchIds, MatchType.FOOTBALL, LiveStatus.LIVING);
//        //比赛 直播状态
//        collect.forEach(vo -> {
//            List<LiveDO> liveDOS = liveDOMap.get(vo.getMatchId());
//            vo.setLiveStatus(CollectionUtils.isEmpty(liveDOS) ? null : LiveStatus.LIVING);
//        });

        return PageResponse.<MatchListVo>builder()
                .records(collect)
                .total(page.getTotal())
                .size(page.getSize())
                .current(page.getCurrent())
                .build();
    }

    @Override
    public List<CompetitionListVo> competitionByTime(CompetitionListRequest request) {
        if(Strings.isBlank(request.getMatchTime())){
            return Collections.emptyList();
        }
        LocalDateTime matchTime = null;
        if(request.getMatchTime().length()>10){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            matchTime = LocalDateTime.parse(request.getMatchTime(), formatter);
        }else {
            LocalDate date = LocalDate.parse(request.getMatchTime());
            LocalTime time = LocalTime.MIDNIGHT;
            matchTime = LocalDateTime.of(date, time);
        }
        List<SdkCompetitionVo> sdkCompetitionVos = sdkService.searchCompetitionByTime(request.getMatchType(), matchTime.with(LocalTime.MIN), matchTime.with(LocalTime.MAX));
        if (CollectionUtils.isEmpty(sdkCompetitionVos)) {
            return Collections.emptyList();
        }
        return sdkCompetitionVos.stream().map(result -> CompetitionListVo.builder()
                        .competitionId(result.getId())
                        .competitionName(result.getShortNameZh())
                        .fullCompetitionName(result.getNameZh())
                        .shortName(PinyinUtil.getFirstLetter(result.getShortNameZh(), "").substring(0, 1).toUpperCase())
                        .build())
                .collect(Collectors.toList());
        //根据时间搜索有比赛的赛事列表
    }

    @Override
    public MatchLiveVO getMatchInfoByMatchId(Integer matchId, MatchType matchType) {
        MatchLiveVO matchVO = transSdkMatchVoToMatchVo(sdkService.getMatchDetailById(matchId, matchType), false);
        if (Objects.isNull(matchVO)) {
            throw new RemoteException("调用远程数据接口异常", BaseErrorCode.CLIENT_ERROR);
        }
        return matchVO;
    }
    /**
     *  对象转换
     * @param match 比赛
     * @param filterFinished 是否过滤掉已完赛的，当页面查询不是已完成的时候要过滤掉已完赛的比赛
     * @return
     */
    private MatchLiveVO transSdkMatchVoToMatchVo(SdkMatchVo match, boolean filterFinished) {
        if (Objects.isNull(match)) {
            return null;
        }
        MatchLiveVO vo = new MatchLiveVO();
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
            MatchScoreDTO matchScoreDTO;
            if (Objects.equals(match.getMatchType(), MatchType.BASKETBALL)) {
                matchScoreDTO =  setBasketballScore(match.getHomeScores(), match.getAwayScores());
            } else {
                matchScoreDTO =  setFootballScore(match.getHomeScores(), match.getAwayScores());
            }
            if(Objects.nonNull(matchScoreDTO)){
                vo.setHomeScore(matchScoreDTO.getHomeScore());
                vo.setHomeHalfScore(matchScoreDTO.getHomeHalfScore());
                vo.setAwayScore(matchScoreDTO.getAwayScore());
                vo.setAwayHalfScore(matchScoreDTO.getAwayHalfScore());
            }
        }
        vo.setStatus(match.getStatusId());
        MatchScoreDTO newScore = setNewestScore(vo.getMatchId(),vo.getMatchType(),vo.getHomeScore(),vo.getAwayScore());//更新最新得分
        if(Objects.nonNull(newScore)){
            vo.setHomeScore(newScore.getHomeScore());
            vo.setHomeHalfScore(newScore.getHomeHalfScore());
            vo.setAwayScore(newScore.getAwayScore());
            vo.setAwayHalfScore(newScore.getAwayHalfScore());
        }
        if (filterFinished) {
            if (MatchType.BASKETBALL.equals(vo.getMatchType()) && BasketballStatusType.FINISHED.getCode().equals(vo.getStatus())) {
                return null;
            } else if (MatchType.FOOTBALL.equals(vo.getMatchType()) && FootballStatusType.FINISHED.getCode().equals(vo.getStatus())) {
                return null;
            }
        }
        //查询比赛线路
        if(MatchType.FOOTBALL.equals(vo.getMatchType())){
            if(FootballStatusType.getRunningAndUpcomingStatus().contains(vo.getStatus())){
                MatchVideo matchVideo = distributedCache.get(CacheUtil.buildKey(CacheConstant.FOOTBALL_VIDEO_LINES, String.valueOf(vo.getMatchId())) ,MatchVideo.class);
                if(Objects.nonNull(matchVideo)) {
                    LinesVO linesVO = new LinesVO();
                    if(Objects.nonNull(matchVideo.getPushurl1())) {
                        linesVO.setLd(LinesVO.PushUrl.builder().m3u8Url(matchVideo.getPushurl1().getM3u8Url()).flvUrl(matchVideo.getPushurl1().getFlvUrl()).rtmpUrl(matchVideo.getPushurl1().getRtmpUrl()).build());
                    }
                    if(Objects.nonNull(matchVideo.getPushurl2())) {
                        linesVO.setChineseHd(LinesVO.PushUrl.builder().m3u8Url(matchVideo.getPushurl2().getM3u8Url()).flvUrl(matchVideo.getPushurl2().getFlvUrl()).rtmpUrl(matchVideo.getPushurl2().getRtmpUrl()).build());
                    }
                    if(Objects.nonNull(matchVideo.getPushurl3())) {
                        linesVO.setEnglishHd(LinesVO.PushUrl.builder().m3u8Url(matchVideo.getPushurl3().getM3u8Url()).flvUrl(matchVideo.getPushurl3().getFlvUrl()).rtmpUrl(matchVideo.getPushurl3().getRtmpUrl()).build());
                    }
                    if(Objects.nonNull(matchVideo.getPushurl4())) {
                        linesVO.setOtherHd(LinesVO.PushUrl.builder().m3u8Url(matchVideo.getPushurl4().getM3u8Url()).flvUrl(matchVideo.getPushurl4().getFlvUrl()).rtmpUrl(matchVideo.getPushurl4().getRtmpUrl()).build());
                    }
                    vo.setLines(linesVO);
                }
            }
        }
        return vo;
    }
    /**
     * 设置最新得分返回前端
     * @param matchId
     * @param matchType
     * @param homeScore
     * @param awayScores
     * @return
     */
    private MatchScoreDTO setNewestScore(Integer matchId, MatchType matchType, Integer homeScore, Integer awayScores) {
        MatchScoreDTO dto;
        if (MatchType.FOOTBALL.equals(matchType)) {//足球
            dto = distributedCache.get(CacheUtil.buildKey(CacheConstant.APP_FOOTBALL_MATCH_NEWEST_SCORE, String.valueOf(matchId)), MatchScoreDTO.class);
        } else { //篮球
            dto = distributedCache.get(CacheUtil.buildKey(CacheConstant.APP_BASKETBALL_MATCH_NEWEST_SCORE, String.valueOf(matchId)), MatchScoreDTO.class);
        }
        if (Objects.isNull(dto)) {
            return null;
        }
        if (dto.getHomeScore().compareTo(homeScore) >= 0 && dto.getAwayScore().compareTo(awayScores) >= 0) {
            return dto;
        }
        return null;
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
     * @param homeScores 主队得分
     * @param awayScores 客队得分
     */
    public static MatchScoreDTO setFootballScore(List<Object> homeScores, List<Object> awayScores) {
        MatchScoreDTO dto = new MatchScoreDTO();
        if (homeScores.size() != 7 || awayScores.size() != 7) {
            return dto;
        }
        int homeScore = (int) homeScores.get(0);
        int homeExtraScore = (int) homeScores.get(5);
        int homePKScore = (int) homeScores.get(6);
        int awayScore = (int) awayScores.get(0);
        int awayExtraScore = (int) awayScores.get(5);
        int awayPkScore = (int) awayScores.get(6);
        if (homeExtraScore > 0) {
            dto.setHomeScore(homeExtraScore + homePKScore);
        } else {
            dto.setHomeScore(homeScore + homePKScore);
        }
        if (awayExtraScore > 0) {
            dto.setAwayScore(awayExtraScore + awayPkScore);
        } else {
            dto.setAwayScore(awayScore + awayPkScore);
        }
        dto.setHomeHalfScore((Integer) homeScores.get(1));
        dto.setAwayHalfScore((Integer) awayScores.get(1));
        return  dto;
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
     * @param homeScores 主队得分
     * @param awayScores 客队得分
     */
    public static MatchScoreDTO setBasketballScore(List<Object> homeScores, List<Object> awayScores) {
        MatchScoreDTO dto = new MatchScoreDTO();
        int totalScore = 0;
        int halfScore = 0;
        for (int i = 0; i < homeScores.size(); i++) {
            int score = (int) homeScores.get(i);
            if (i < 2) {
                halfScore += score;
            }
            totalScore += score;
        }
        dto.setHomeScore(totalScore);
        dto.setHomeHalfScore(halfScore);
        totalScore = 0;
        halfScore = 0;
        for (int i = 0; i < awayScores.size(); i++) {
            int score = (int) awayScores.get(i);
            if (i < 2) {
                halfScore += score;
            }
            totalScore += score;
        }
        dto.setAwayScore(totalScore);
        dto.setAwayHalfScore(halfScore);
        return dto;
    }

}
