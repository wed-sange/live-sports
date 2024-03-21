package com.edi.sdk.football.request;

import com.edi.sdk.core.EdiRequest;
import lombok.Data;

/**
 * 获取比赛历史同赔统计列表
 * 该接口返回30天内未开始比赛的统计数据（历史交锋、近期战绩、历史同赔），可根据时间戳增量获取新增或变动的数据
 * <p>
 * 1、首次全量更新，根据参数id获取全量数据
 * 2、后续增量更新，根据参数time增量获取变动数据（建议请求频次：1min/次）
 */
@Data
public class CompensationListRequest implements EdiRequest {

    private static final String PATH = "/api/v5/football/compensation/list";

    /**
     * 查询大于等于id的记录，根据id排序
     */
    private Integer id;
    /**
     * 查询大于等于更新时间的记录(时间戳)，根据更新时间排序
     */
    private Integer time;
    /**
     * 返回数据最大数，默认为100，最大为100
     */
    private Integer limit = 100;

    @Override
    public String getPath() {
        return PATH;
    }
}
