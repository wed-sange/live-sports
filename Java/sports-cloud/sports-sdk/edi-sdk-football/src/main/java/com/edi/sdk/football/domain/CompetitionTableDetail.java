package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 积分榜
 */
@Data
public class CompetitionTableDetail {
    /**
     * 升降级列表
     */
    private List<Promotions> promotions;

    @Data
    public static class Promotions {
        /**
         * 中文名称
         */
        @JsonProperty(value = "name_zh")
        @Schema(description = "中文名称")
        private String nameZh;
        /**
         * 颜色值
         */
        private String color;
        /**
         * 粤语名称
         */
        @JsonProperty(value = "name_zht")
        @Schema(description = "粤语名称")
        private String nameZht;
        /**
         * 升降级id
         */
        private Integer id;
        /**
         * 英文名称
         */
        @JsonProperty(value = "name_en")
        @Schema(description = "英文名称")
        private String nameEn;
    }

    /**
     * 积分项
     */
    private List<Tables> tables;

    @Data
    public static class Tables {
        /**
         * 分区信息（极少部分赛事才有，比如美职联）
         */
        private String conference;
        /**
         * 所属阶段id
         */
        @JsonProperty(value = "stage_id")
        @Schema(description = "所属阶段id")
        private Integer stageId;
        /**
         * 积分榜表id
         */
        private Integer id;
        /**
         * 球队积分项
         */
        private List<Rows> rows;

        @Data
        public static class Rows {
            /**
             * 说明
             */
            private String note;
            /**
             * 客场比赛场次
             */
            @JsonProperty(value = "away_total")
            @Schema(description = "客场比赛场次")
            private Integer awayTotal;
            /**
             * 升降级id
             */
            @JsonProperty(value = "promotion_id")
            @Schema(description = "升降级id")
            private Integer promotionId;
            /**
             * 球队id
             */
            @JsonProperty(value = "team_id")
            @Schema(description = "球队id")
            private Integer teamId;
            /**
             * 客场失球
             */
            @JsonProperty(value = "away_goals_against")
            @Schema(description = "客场失球")
            private Integer awayGoalsAgainst;
            /**
             * 主场比赛场次
             */
            @JsonProperty(value = "home_total")
            @Schema(description = "主场比赛场次")
            private Integer homeTotal;
            /**
             * 积分
             */
            private Integer points;
            /**
             * 客场平的场次
             */
            @JsonProperty(value = "away_draw")
            @Schema(description = "客场平的场次")
            private Integer awayDraw;
            /**
             * 负的场次
             */
            private Integer loss;
            /**
             * 比赛场次
             */
            private Integer total;
            /**
             * 主场进球
             */
            @JsonProperty(value = "home_goals")
            @Schema(description = "主场进球")
            private Integer homeGoals;
            /**
             * 主场积分
             */
            @JsonProperty(value = "home_points")
            @Schema(description = "主场积分")
            private Integer homePoints;
            /**
             * 客场排名
             */
            @JsonProperty(value = "away_position")
            @Schema(description = "客场排名")
            private Integer awayPosition;
            /**
             * 胜的场次
             */
            private Integer won;
            /**
             * 客场积分
             */
            @JsonProperty(value = "away_points")
            @Schema(description = "客场积分")
            private Integer awayPoints;
            /**
             * 客场净胜球
             */
            @JsonProperty(value = "away_goal_diff")
            @Schema(description = "客场净胜球")
            private Integer awayGoalDiff;
            /**
             * 扣除积分
             */
            @JsonProperty(value = "deduct_points")
            @Schema(description = "扣除积分")
            private Integer deductPoints;
            /**
             * 主场排名
             */
            @JsonProperty(value = "home_position")
            @Schema(description = "主场排名")
            private Integer homePosition;
            /**
             * 主场平的场次
             */
            @JsonProperty(value = "home_draw")
            @Schema(description = "主场平的场次")
            private Integer homeDraw;
            /**
             * 进球
             */
            private Integer goals;
            /**
             * 客场负的场次
             */
            @JsonProperty(value = "away_loss")
            @Schema(description = "客场负的场次")
            private Integer awayLoss;
            /**
             * 平的场次
             */
            private Integer draw;
            /**
             * 客场进球
             */
            @JsonProperty(value = "away_goals")
            @Schema(description = "客场进球")
            private Integer awayGoals;
            /**
             * 净胜球
             */
            @JsonProperty(value = "goal_diff")
            @Schema(description = "净胜球")
            private Integer goalDiff;
            /**
             * 主场净胜球
             */
            @JsonProperty(value = "home_goal_diff")
            @Schema(description = "主场净胜球")
            private Integer homeGoalDiff;
            /**
             * 失球
             */
            @JsonProperty(value = "goals_against")
            @Schema(description = "失球")
            private Integer goalsAgainst;
            /**
             * 客场胜的场次
             */
            @JsonProperty(value = "away_won")
            @Schema(description = "客场胜的场次")
            private Integer awayWon;
            /**
             * 排名
             */
            private Integer position;
            /**
             * 主场胜的场次
             */
            @JsonProperty(value = "home_won")
            @Schema(description = "主场胜的场次")
            private Integer homeWon;
            /**
             * 主场负的场次
             */
            @JsonProperty(value = "home_loss")
            @Schema(description = "主场负的场次")
            private Integer homeLoss;
            /**
             * 主场失球
             */
            @JsonProperty(value = "home_goals_against")
            @Schema(description = "主场失球")
            private Integer homeGoalsAgainst;
        }

        /**
         * 不为0表示分组赛的第几组，1-A、2-B以此类推
         */
        private Integer group;
    }

    /**
     * 赛季id
     */
    @JsonProperty(value = "season_id")
    private Integer seasonId;
}
