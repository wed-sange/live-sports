package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 俱乐部排名
 */
@Data
public class RankingClub {
    /**
     * 排名变化
     */
    @JsonProperty(value = "position_changed")
    @Schema(description = "排名变化")
    private Integer positionChanged;
    /**
     * 排名
     */
    private Integer ranking;
    /**
     * 球队id
     */
    @JsonProperty(value = "team_id")
    @Schema(description = "球队id")
    private Integer teamId;
    /**
     * 上次积分
     */
    @JsonProperty(value = "previous_points")
    @Schema(description = "上次积分")
    private Integer previousPoints;
    /**
     * 积分
     */
    private Integer points;
}