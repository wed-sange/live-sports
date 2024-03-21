package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * null
 */
@Data
public class Archive {
    /**
     * 比赛球员统计地址，数据格式请参考‘获取历史比赛球员统计数据’接口
     */
    @JsonProperty(value = "match_player_stats")
    private String matchPlayerStats;
    /**
     * 比赛阵容地址，数据格式请参考‘获取比赛阵容详情’接口
     */
    @JsonProperty(value = "match_lineup")
    private String matchLineup;
    /**
     * 比赛指数地址，数据格式请参考‘获取单场比赛指数数据’接口
     */
    @JsonProperty(value = "odds_history")
    private String oddsHistory;
    /**
     * 比赛趋势地址，数据格式请参考‘获取比赛趋势详情’接口
     */
    @JsonProperty(value = "match_trend")
    private String matchTrend;
    /**
     * 比赛详情地址，数据格式请参考‘获取历史比赛统计数据’接口
     */
    @JsonProperty(value = "match_detail")
    private String matchDetail;
}