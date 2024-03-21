package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 获取历史比赛统计数据
 * 返回已完结历史比赛（限制：30天内）的统计数据（技术统计、阵容、文字直播）
 * 请求次数：120次/min
 * <p>
 * 说明：
 * 实时比赛统计数据都在实时统计数据接口获取，实时统计数据接口返回的是4h内的全量比赛的实时统计数据，需本地记录
 * 若比赛统计数据获取有缺失或未获取到，再通过历史比赛统计数据接口进行查缺补漏
 */
@Data
public class MatchLiveHistoryRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/match/live/history";

    /**
     * 查询大于等于id的记录，根据id排序
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
