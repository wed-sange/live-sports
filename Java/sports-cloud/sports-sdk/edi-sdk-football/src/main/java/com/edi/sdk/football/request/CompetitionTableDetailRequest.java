package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 获取赛事积分榜数据
 * 返回查询赛事最新赛季的积分榜数据详情
 * 请求次数：120次/min
 */
@Data
public class CompetitionTableDetailRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/competition/table/detail";

    /**
     * 查询大于等于id的记录，根据id排序
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
