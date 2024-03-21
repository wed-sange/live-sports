package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 统计数据
 */
@Data
public class CompetitionStatsDetail {
    /**
     * 球队数据字段说明
     */
    @JsonProperty(value = "teams_stats")
    private List<TeamsStats> teamsStats;

    @Data
    public static class TeamsStats {
        /**
         * 篮板数
         */
        private Integer rebounds;
        /**
         * 罚球命中数
         */
        @JsonProperty(value = "free_throws_scored")
        private Integer freeThrowsScored;
        /**
         * 犯规
         */
        @JsonProperty(value = "total_fouls")
        private Integer totalFouls;
        /**
         * 两分球总数
         */
        @JsonProperty(value = "two_points_total")
        private Integer twoPointsTotal;
        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        private Integer teamId;
        /**
         * 抢断
         */
        private Integer steals;
        /**
         * 得分
         */
        private Integer points;
        /**
         * 罚球命中率
         */
        @JsonProperty(value = "free_throws_accuracy")
        private String freeThrowsAccuracy;
        /**
         * 两分球命中数
         */
        @JsonProperty(value = "two_points_scored")
        private Integer twoPointsScored;
        /**
         * 助攻
         */
        private Integer assists;
        /**
         * 统计范围，1-赛季、2-预选赛、3-小组赛、4-季前赛、5-常规赛、6-淘汰赛(季后赛)、7-附加赛
         */
        private Integer scope;
        /**
         * 投篮命中率
         */
        @JsonProperty(value = "field_goals_accuracy")
        private String fieldGoalsAccuracy;
        /**
         * 防守篮板数
         */
        @JsonProperty(value = "defensive_rebounds")
        private Integer defensiveRebounds;
        /**
         * 进攻篮板数
         */
        @JsonProperty(value = "offensive_rebounds")
        private Integer offensiveRebounds;
        /**
         * 两分球命中率
         */
        @JsonProperty(value = "two_points_accuracy")
        private String twoPointsAccuracy;
        /**
         * 罚球总数
         */
        @JsonProperty(value = "free_throws_total")
        private Integer freeThrowsTotal;
        /**
         * 三分球命中率
         */
        @JsonProperty(value = "three_points_accuracy")
        private String threePointsAccuracy;
        /**
         * 盖帽
         */
        private Integer blocks;
        /**
         * 投篮总数
         */
        @JsonProperty(value = "field_goals_total")
        private Integer fieldGoalsTotal;
        /**
         * 投篮命中数
         */
        @JsonProperty(value = "field_goals_scored")
        private Integer fieldGoalsScored;
        /**
         * 比赛场次
         */
        private Integer matches;
        /**
         * 失分
         */
        @JsonProperty(value = "points_against")
        private Integer pointsAgainst;
        /**
         * 三分球命中数
         */
        @JsonProperty(value = "three_points_scored")
        private Integer threePointsScored;
        /**
         * 失误
         */
        private Integer turnovers;
        /**
         * 三分球总数
         */
        @JsonProperty(value = "three_points_total")
        private Integer threePointsTotal;
    }

    /**
     * 赛季id
     */
    @JsonProperty(value = "season_id")
    private Integer seasonId;
    /**
     * 球员数据字段说明
     */
    @JsonProperty(value = "players_stats")
    private List<PlayersStats> playersStats;

    @Data
    public static class PlayersStats {
        /**
         * 篮板数
         */
        private Integer rebounds;
        /**
         * 罚球命中数
         */
        @JsonProperty(value = "free_throws_scored")
        private Integer freeThrowsScored;
        /**
         * 两分球总数
         */
        @JsonProperty(value = "two_points_total")
        private Integer twoPointsTotal;
        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        private Integer teamId;
        /**
         * 抢断
         */
        private Integer steals;
        /**
         * 得分
         */
        private Integer points;
        /**
         * 罚球命中率
         */
        @JsonProperty(value = "free_throws_accuracy")
        private String freeThrowsAccuracy;
        /**
         * 两分球命中数
         */
        @JsonProperty(value = "two_points_scored")
        private Integer twoPointsScored;
        /**
         * 球员id
         */
        @JsonProperty(value = "player_id")
        private Integer playerId;
        /**
         * 助攻
         */
        private Integer assists;
        /**
         * 统计范围，1-赛季、2-预选赛、3-小组赛、4-季前赛、5-常规赛、6-淘汰赛(季后赛)、7-附加赛
         */
        private Integer scope;
        /**
         * 上场时间
         */
        @JsonProperty(value = "minutes_played")
        private Integer minutesPlayed;
        /**
         * 投篮命中率
         */
        @JsonProperty(value = "field_goals_accuracy")
        private String fieldGoalsAccuracy;
        /**
         * 防守篮板数
         */
        @JsonProperty(value = "defensive_rebounds")
        private Integer defensiveRebounds;
        /**
         * 进攻篮板数
         */
        @JsonProperty(value = "offensive_rebounds")
        private Integer offensiveRebounds;
        /**
         * 两分球命中率
         */
        @JsonProperty(value = "two_points_accuracy")
        private String twoPointsAccuracy;
        /**
         * 罚球总数
         */
        @JsonProperty(value = "free_throws_total")
        private Integer freeThrowsTotal;
        /**
         * 三分球命中率
         */
        @JsonProperty(value = "three_points_accuracy")
        private String threePointsAccuracy;
        /**
         * 盖帽
         */
        private Integer blocks;
        /**
         * 个人犯规
         */
        @JsonProperty(value = "personal_fouls")
        private Integer personalFouls;
        /**
         * 投篮总数
         */
        @JsonProperty(value = "field_goals_total")
        private Integer fieldGoalsTotal;
        /**
         * 投篮命中数
         */
        @JsonProperty(value = "field_goals_scored")
        private Integer fieldGoalsScored;
        /**
         * 上场场次
         */
        private Integer court;
        /**
         * 比赛场次
         */
        private Integer matches;
        /**
         * 三分球命中数
         */
        @JsonProperty(value = "three_points_scored")
        private Integer threePointsScored;
        /**
         * 失误
         */
        private Integer turnovers;
        /**
         * 三分球总数
         */
        @JsonProperty(value = "three_points_total")
        private Integer threePointsTotal;
        /**
         * 首发
         */
        private Integer first;
    }
}