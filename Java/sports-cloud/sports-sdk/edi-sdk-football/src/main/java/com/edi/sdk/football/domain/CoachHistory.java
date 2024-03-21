package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 教练执教履历列表
 */
@Data
public class CoachHistory {

    /**
     * 教练id
     */
    private Integer id;
    /**
     * 执教履历列表
     */
    private List<History> history;

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;

    @Data
    public static class History {
        /**
         * 合同到期时间
         */
        @JsonProperty(value = "contract_until")
        @Schema(description = "合同到期时间")
        private Integer contractUntil;
        /**
         * 加盟时间
         */
        private Integer joined;
        /**
         * 球员任用数
         */
        @JsonProperty(value = "players_used")
        @Schema(description = "球员任用数")
        private Integer playersUsed;
        /**
         * 负场次
         */
        private Integer lose;
        /**
         * 场均得失球（场均得球:场均失球）
         */
        @JsonProperty(value = "goal_ppt")
        @Schema(description = "场均得失球（场均得球:场均失球）")
        private String goalPpt;
        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        @Schema(description = "球队id")
        private Integer teamId;
        /**
         * 任职，1-主教练、2-助理教练、0-未知
         */
        private Integer position;
        /**
         * 平场次
         */
        private Integer draw;
        /**
         * 场均得分
         */
        private String ppm;
        /**
         * 执教场次
         */
        private Integer matches;
        /**
         * 胜场次
         */
        private Integer win;
    }
}