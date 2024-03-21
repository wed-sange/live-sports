package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 全量历史比赛的打包数据地址（比赛详情、指数）
 *

 */
@Data
public class Archive {
    /**
     * 比赛详情地址，数据格式请参考‘获取历史比赛统计数据’接口
     */
    @JsonProperty(value = "match_detail") 
    @Schema(description = "比赛详情地址，数据格式请参考‘获取历史比赛统计数据’接口")
    private String matchDetail;
    /**
     * 比赛指数地址，数据格式请参考‘获取单场比赛指数数据’接口
     */
    @JsonProperty(value = "odds_history")
    @Schema(description = "比赛指数地址，数据格式请参考‘获取单场比赛指数数据’接口")
    private String oddsHistory;
}
