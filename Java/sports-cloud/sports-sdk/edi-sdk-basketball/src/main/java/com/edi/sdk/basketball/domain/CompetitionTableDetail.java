package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 积分榜
 */
@Data
public class CompetitionTableDetail {
    /**
     * 积分项
     */
    private List<Tables> tables;

    @Data
    public static class Tables {
        /**
         * 所属阶段id
         */
        @JsonProperty(value = "stage_id")
        private Integer stageId;
        /**
         * 统计范围，1-赛季、2-预选赛、3-小组赛、4-季前赛、5-常规赛、6-淘汰赛(季后赛)、0-无
         */
        private Integer scope;
        /**
         * 积分榜表名称
         */
        private String name;
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
             * 近期战绩（正连胜，负连败），可能不存在
             */
            private Integer streaks;
            /**
             * 客场：客场胜-客场负，可能不存在
             */
            private String away;
            /**
             * 东（西）部胜-负，该球队在东或西部的胜负数据，可能不存在
             */
            private String conference;
            /**
             * 场均净胜，可能不存在
             */
            @JsonProperty(value = "diff_avg")
            private Float diffAvg;
            /**
             * 总得分，杯赛存在
             */
            @JsonProperty(value = "points_for")
            private Integer pointsFor;
            /**
             * 球队id
             */
            @JsonProperty(value = "team_id")
            private Integer teamId;
            /**
             * 胜率
             */
            @JsonProperty(value = "won_rate")
            private Float wonRate;
            /**
             * 场均失分，可能不存在
             */
            @JsonProperty(value = "points_against_avg")
            private Float pointsAgainstAvg;
            /**
             * 主场：主场胜-主场负，可能不存在
             */
            private String home;
            /**
             * 积分，杯赛存在
             */
            private Integer points;
            /**
             * 胜场差，可能不存在
             */
            @JsonProperty(value = "game_back")
            private String gameBack;
            /**
             * 赛区胜-负，该球队在该赛区的胜负数据，可能不存在
             */
            private String division;
            /**
             * 负场=主场负+客场负
             */
            private Integer lost;
            /**
             * 胜场=主场胜+客场胜
             */
            private Integer won;
            /**
             * 近10场胜-负（在该赛季只打了5场比赛：4-1），可能不存在
             */
            @JsonProperty(value = "last_10")
            private String last10;
            /**
             * 排名
             */
            private Integer position;
            /**
             * 总失分，杯赛存在
             */
            @JsonProperty(value = "points_agt")
            private Integer pointsAgt;
            /**
             * 场均得分，可能不存在
             */
            @JsonProperty(value = "points_avg")
            private Float pointsAvg;
        }
    }

    /**
     * 赛季id
     */
    @JsonProperty(value = "season_id")
    private Integer seasonId;
}