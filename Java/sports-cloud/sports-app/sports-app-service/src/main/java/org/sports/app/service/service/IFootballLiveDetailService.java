package org.sports.app.service.service;

import org.sports.app.service.vo.live.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 足球直播详情service
 *

 */
@Service
public interface IFootballLiveDetailService {

    /**
     * 加载比赛实时统计数据
     */
    void loadMatchStatsCache();


    /**
     * 查询比赛阵容
     *
     * @param matchId 比赛ID
     * @return
     */
    MatchLineupDetailVO getMatchLineupDetail(String matchId);


    /**
     * 查询比赛文字直播
     *
     * @param matchId 比赛ID
     */
    List<TliveVO> getTextLive(String matchId);

    /**
     * 查询比赛事件
     *
     * @param matchId 比赛ID
     */
    List<IncidentsVO> getIncidents(String matchId);

    /**
     * 查询比赛技术统计
     *
     * @param matchId 比赛ID
     */
    List<StatsVO> getMatchStats(String matchId);

    /**
     * 获取比赛四种指数数据
     *
     * @param matchId 比赛ID
     */
    CompanyOdds getMatchOdds(String matchId);

    /**
     * 查询单场是否存在 赛况、阵容、指数
     *
     * @param matchId 比赛ID
     * @param status
     */
    MatchDataStataVO getMatchDataStata(Integer matchId, Integer status);

    /**
     * 同步足球比赛线路
     */
    void loadMatchVideoLine();
}
