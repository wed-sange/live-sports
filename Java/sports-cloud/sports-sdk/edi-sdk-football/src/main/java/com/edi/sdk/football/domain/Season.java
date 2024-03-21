package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 赛季列表
 */
@Data
public class Season {
    /**
     * 赛事id
     */
    @JsonProperty(value = "competition_id")
    @Schema(description = "赛事id")
    private Integer competitionId;
    /**
     * 结束时间
     */
    @JsonProperty(value = "end_time")
    @Schema(description = "结束时间")
    private Integer endTime;
    /**
     * 是否有球员统计，1-是、0-否
     */
    @JsonProperty(value = "has_player_stats")
    @Schema(description = "是否有球员统计，1-是、0-否")
    private Integer hasPlayerStats;
    /**
     * 是否有积分榜，1-是、0-否
     */
    @JsonProperty(value = "has_table")
    @Schema(description = "是否有积分榜，1-是、0-否")
    private Integer hasTable;
    /**
     * 是否有球队统计，1-是、0-否
     */
    @JsonProperty(value = "has_team_stats")
    @Schema(description = "是否有球队统计，1-是、0-否")
    private Integer hasTeamStats;
    /**
     * 赛季id
     */
    private Integer id;
    /**
     * 是否最新赛季，1-是、0-否
     */
    @JsonProperty(value = "is_current")
    @Schema(description = "是否最新赛季，1-是、0-否")
    private Integer isCurrent;
    /**
     * 开始时间
     */
    @JsonProperty(value = "start_time")
    @Schema(description = "开始时间")
    private Integer startTime;
    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;
    /**
     * 赛季年份
     */
    private String year;
}