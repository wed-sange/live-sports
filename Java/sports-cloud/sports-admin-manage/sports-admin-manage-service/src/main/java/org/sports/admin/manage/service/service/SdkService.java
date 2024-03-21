package org.sports.admin.manage.service.service;

import com.edi.sdk.football.domain.MatchVideo;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.MatchListRequest;
import org.sports.admin.manage.service.vo.*;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface SdkService {
    /**
     * 根据队伍ID获取队伍详情
     *
     * @param teamId    ID
     * @param matchType 比赛类型
     * @return 队伍详情
     */
    SdkTeamVo findTeamById(Integer teamId, MatchType matchType);

    /**
     * 根据赛事ID获取赛事详情
     *
     * @param competitionId ID
     * @param matchType     比赛类型
     * @return 赛事详情
     */
    SdkCompetitionVo findCompetitionById(Integer competitionId, MatchType matchType);

    /**
     * 根据赛事名称模糊搜索赛事
     *
     * @param name      名称
     * @param matchType 比赛类型
     * @return 赛事
     */
    List<SdkCompetitionVo> searchCompetition(String name, MatchType matchType);

    /**
     * 根据时间搜索比赛列表
     *
     * @param request 赛选条件
     * @return 比赛列表
     */
    PageResponse<SdkMatchVo> page(MatchListRequest request);

    /**
     * 根据比赛时间搜索有比赛的赛事列表
     *
     * @param matchType      比赛类型
     * @param startMatchTime 比赛开始时间
     * @param endMatchTime   比赛结束时间
     * @return 赛事列表
     */
    List<SdkCompetitionVo> searchCompetitionByTime(MatchType matchType, LocalDateTime startMatchTime, LocalDateTime endMatchTime);

    /**
     * 根据比赛时间统计每天的比赛场数
     *
     * @param startTime 比赛开始时间
     * @param endTime   比赛结束时间
     * @param matchType 比赛类型
     * @return 统计每天的比赛场数
     */
    List<MatchCountVO> getMatchTimeCount(LocalDate startTime, LocalDate endTime, MatchType matchType);

    /**
     * 根据赛事类型，比赛时间搜索比赛列表
     *
     * @param competitionIds 赛事ID
     * @param startTime      比赛开始时间
     * @param endTime        比赛结束时间
     * @param matchType      比赛类型
     * @param status 比赛状态
     * @return 比赛列表
     */
    List<SdkMatchVo> getMatchList(List<Integer> competitionIds, LocalDate startTime, LocalDate endTime, MatchType matchType, List<Integer> status);

    /**
     * 获取比赛详情
     *
     * @param matchId   比赛ID
     * @param matchType 比赛类型
     * @return 比赛详情
     */
    SdkMatchVo getMatchDetailById(Integer matchId, MatchType matchType);

    /**
     * 获取足球裁判信息
     * @param refereeId 裁判ID
     * @return 裁判信息
     */
    SdkRefereeVo getFootballRefereeById(Integer refereeId);


    List<MatchVideo> getFootballMatchVideoLine();

    List<SdkMatchVo> getRecentMatchList();

    com.edi.sdk.football.domain.MatchLiveHistory getFootballMatchLiveHistory(Integer matchId);

    com.edi.sdk.basketball.domain.MatchLiveHistory getbasketballMatchLiveHistory(Integer matchId);
}
