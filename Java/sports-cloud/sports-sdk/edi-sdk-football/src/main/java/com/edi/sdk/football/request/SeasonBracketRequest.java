package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 获取淘汰阶段对阵图数据
 * 该接口返回同一赛季的赛事淘汰赛阶段对阵数据（如：nba/cba季后赛）
 * 请求次数：120次/min
 */
@Data
public class SeasonBracketRequest implements EdiRequest {
    private static final String PATH = "/api/v5/football/season/bracket";

    /**
     * 查询大于等于id的记录，根据id排序
     */
    private Integer id;

    @Override
    public String getPath() {
        return PATH;
    }
}
