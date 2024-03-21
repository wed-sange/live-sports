package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;

/**
 * @ClassName MatchPlayerStatsListRequest
 * @Description 获取比赛球员统计列表
 
 * @Date 2023/6/29
 * @Version 1.0
 **/
public class MatchPlayerStatsListRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/player_stats/list";

    @Override
    public String getPath() {
        return PATH;
    }
}
