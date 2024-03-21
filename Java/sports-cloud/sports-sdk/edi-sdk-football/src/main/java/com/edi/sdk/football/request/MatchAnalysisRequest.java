package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 获取比赛分析数据
 * 返回比赛分析统计数据（历史交锋/近期战绩、未来赛程、进球分布）
 * <p>
 * 该接口用于请求未开赛的比赛的历史对阵等数据，多为历史数据，变化不频繁
 * 请求限制：前30天比赛
 * <p>
 * 请求次数：60次/min
 */
@Data
public class MatchAnalysisRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/analysis";
    /**
     * 比赛id
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
