package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 传统足彩开奖结果项
 */
@Data
public class ZcResult {
    /**
     * 开奖结果<br/>3-主胜、1-平、0-客胜；*-异常比赛（延期、腰斩、取消等未能完场）
     */
    private String result;
    /**
     * 二等奖奖金 (胜负彩存在)
     */
    @JsonProperty(value = "second_prize")
    @Schema(description = "二等奖奖金 (胜负彩存在)")
    private Integer secondPrize;
    /**
     * 一等奖注数
     */
    @JsonProperty(value = "first_pot_count")
    @Schema(description = "一等奖注数")
    private Integer firstPotCount;
    /**
     * 彩票期号
     */
    private String issue;
    /**
     * 奖池奖金
     */
    private Integer jackpot;
    /**
     * 二等奖注数 (胜负彩存在)
     */
    @JsonProperty(value = "second_pot_count")
    @Schema(description = "二等奖注数 (胜负彩存在)")
    private Integer secondPotCount;
    /**
     * 一等奖奖金
     */
    @JsonProperty(value = "first_prize")
    @Schema(description = "一等奖奖金")
    private Integer firstPrize;
    /**
     * 销售额
     */
    private Integer sales;
}