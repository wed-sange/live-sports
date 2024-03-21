package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 直播地址
 */
@Data
public class MatchStreamUrlsFree {
    /**
     * 赛事
     */
    private String comp;
    /**
     * web直播地址，没有为空
     */
    @JsonProperty(value = "pc_link")
    private String pcLink;
    /**
     * 客队
     */
    private String away;
    /**
     * 比赛id
     */
    @JsonProperty(value = "match_id")
    private Integer matchId;
    /**
     * 比赛时间
     */
    @JsonProperty(value = "match_time")
    private Integer matchTime;
    /**
     * wap直播地址，没有为空
     */
    @JsonProperty(value = "mobile_link")
    private String mobileLink;
    /**
     * 主队
     */
    private String home;
}