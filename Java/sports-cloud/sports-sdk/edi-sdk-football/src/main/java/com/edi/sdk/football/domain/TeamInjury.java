package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 球队伤停列表
 */
@Data
public class TeamInjury {
    /**
     * 球队id
     */
    private Integer id;
    /**
     * 球队伤停列表
     */
    private List<Injury> injury;

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;

    @Data
    public static class Injury {
        /**
         * 伤停说明
         */
        private String reason;
        /**
         * 开始时间
         */
        @JsonProperty(value = "start_time")
        @Schema(description = "开始时间")
        private Integer startTime;
        /**
         * 球员id
         */
        @JsonProperty(value = "player_id")
        @Schema(description = "球员id")
        private Integer playerId;
        /**
         * 影响赛事id
         */
        @JsonProperty(value = "competition_id")
        @Schema(description = "影响赛事id")
        private Integer competitionId;
        /**
         * 缺失比赛场次
         */
        @JsonProperty(value = "missed_matches")
        @Schema(description = "缺失比赛场次")
        private Integer missedMatches;
        /**
         * 结束时间
         */
        @JsonProperty(value = "end_time")
        @Schema(description = "结束时间")
        private Integer endTime;
        /**
         * 类型，1-受伤、2-停赛、0-未知
         */
        private Integer type;
    }
}