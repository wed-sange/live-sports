package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;

/**
 * @ClassName MatchTeamStatsListRequest
 * @Description 获取比赛球队统计列表
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
public class MatchTeamStatsListRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/team_stats/list";

    @Override
    public String getPath() {
        return PATH;
    }
}
