package org.sports.app.service.service;

import org.sports.app.service.vo.live.BasketballScoreStatsVO;
import org.sports.app.service.vo.live.BasketballScoreVO;
import org.sports.app.service.vo.live.BasketballStatsVO;
import org.sports.app.service.vo.live.MatchDataStataVO;
import org.springframework.stereotype.Service;

/**
 * 篮球直播详情service
 *

 */
@Service
public interface IBasketballLiveDetailService {

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
    BasketballScoreStatsVO getMatchLineupDetail(String matchId);

    /**
     * 查询比赛得分
     *
     * @param matchId 比赛ID
     */
    BasketballScoreVO getScore(String matchId);

    /**
     * 查询比赛技术统计
     *
     * @param matchId 比赛ID
     */
    BasketballStatsVO getMatchStats(String matchId);

    /**
     * 查询单场是否存在 赛况、阵容、指数
     *
     * @param matchId 比赛ID
     * @param status
     */
    MatchDataStataVO getMatchDataStata(Integer matchId, Integer status);
}
