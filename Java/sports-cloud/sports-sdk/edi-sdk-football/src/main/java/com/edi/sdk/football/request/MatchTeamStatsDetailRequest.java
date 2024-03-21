package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 获取历史比赛球队统计数据
 * 返回已完结历史比赛（限制：30天内）的球队统计数据
 * 请求次数：120次/min
 * <p>
 * 说明：
 * 实时比赛球队统计数据都在比赛球队统计接口获取，比赛球队统计接口返回的是10min内球队统计数据变动的比赛的球队统计数据，需本地记录
 * 若比赛球队统计数据获取有缺失或未获取到，再通过该接口进行查缺补漏
 */
@Data
public class MatchTeamStatsDetailRequest implements EdiRequest {

    private static final String PATH = "/api/v5/football/match/team_stats/detail";

    /**
     * 比赛id
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
