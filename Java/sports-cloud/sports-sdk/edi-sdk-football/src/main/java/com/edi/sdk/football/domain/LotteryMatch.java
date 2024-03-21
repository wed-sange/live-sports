package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 体彩比赛关联项
 */
@Data
public class LotteryMatch {
    /**
     * 球类id，1-足球、2-篮球
     */
    @JsonProperty(value = "sport_id")
    private Integer sportId;
    /**
     * 客队名称
     */
    @JsonProperty(value = "away_name")
    private String awayName;
    /**
     * 彩票期号
     */
    private String issue;
    /**
     * 主队名称
     */
    @JsonProperty(value = "home_name")
    private String homeName;
    /**
     * 比赛id
     */
    @JsonProperty(value = "match_id")
    private Integer matchId;
    /**
     * 主客是否一致，1-一致、0-不一致（eg：不一致-官网A vs B、纳米B vs A）
     */
    @JsonProperty(value = "is_same")
    private Integer isSame;
    /**
     * 序号
     */
    @JsonProperty(value = "issue_num")
    private String issueNum;
    /**
     * 唯一，先判断lottery_type，然后根据此id与各彩种接口比赛关联
     */
    private Integer id;
    /**
     * 彩种类型
     */
    @JsonProperty(value = "lottery_type")
    private Integer lotteryType;
}