package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 获取赛事统计详情
 * 返回查询赛事最新赛季的球队球员统计详情数据
 * 请求次数：120次/min
 * <p>
 * 说明：
 * 由于球员存在转会情况，球员数据中会存在一个球员有多条数据，所在球队不同的情况，分别表示该球员在不同球队的数据，可合并处理
 */
@Data
public class CompetitionStatsDetailRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/competition/stats/detail";

    /**
     * 查询大于等于id的记录，根据id排序
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
