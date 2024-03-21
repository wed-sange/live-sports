package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 比赛球队统计数据
 */
@Data
public class MatchTeamStats {
    /**
     * 球队统计数据字段说明
     */
    private List<Stats> stats;

    @Data
    public static class Stats {
        /**
         * 犯规
         */
        private Integer fouls;
        /**
         * 角球
         */
        @JsonProperty(value = "corner_kicks")
        private Integer cornerKicks;
        /**
         * 点球
         */
        private Integer penalty;
        /**
         * 传球成功
         */
        @JsonProperty(value = "passes_accuracy")
        private Integer passesAccuracy;
        /**
         * 红牌
         */
        @JsonProperty(value = "red_cards")
        private Integer redCards;
        /**
         * 有效阻挡
         */
        @JsonProperty(value = "blocked_shots")
        private Integer blockedShots;
        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        private Integer teamId;
        /**
         * 1对1拼抢
         */
        private Integer duels;
        /**
         * 丢失球权
         */
        @JsonProperty(value = "poss_losts")
        private Integer possLosts;
        /**
         * 传中球
         */
        private Integer crosses;
        /**
         * 任意球
         */
        private Integer freekicks;
        /**
         * 传球
         */
        private Integer passes;
        /**
         * 助攻
         */
        private Integer assists;
        /**
         * 1对1拼抢成功
         */
        @JsonProperty(value = "duels_won")
        private Integer duelsWon;
        /**
         * 抢断
         */
        private Integer tackles;
        /**
         * 进球
         */
        private Integer goals;
        /**
         * 被侵犯
         */
        @JsonProperty(value = "was_fouled")
        private Integer wasFouled;
        /**
         * 越位
         */
        private Integer offsides;
        /**
         * 过人成功
         */
        @JsonProperty(value = "dribble_succ")
        private Integer dribbleSucc;
        /**
         * 快攻射门
         */
        @JsonProperty(value = "fastbreak_shots")
        private Integer fastbreakShots;
        /**
         * 关键传球
         */
        @JsonProperty(value = "key_passes")
        private Integer keyPasses;
        /**
         * 任意球得分
         */
        @JsonProperty(value = "freekick_goals")
        private Integer freekickGoals;
        /**
         * 控球率
         */
        @JsonProperty(value = "ball_possession")
        private Integer ballPossession;
        /**
         * 黄牌
         */
        @JsonProperty(value = "yellow_cards")
        private Integer yellowCards;
        /**
         * 传中球成功
         */
        @JsonProperty(value = "crosses_accuracy")
        private Integer crossesAccuracy;
        /**
         * 长传
         */
        @JsonProperty(value = "long_balls")
        private Integer longBalls;
        /**
         * 快攻
         */
        private Integer fastbreaks;
        /**
         * 击中门框
         */
        @JsonProperty(value = "hit_woodwork")
        private Integer hitWoodwork;
        /**
         * 拦截
         */
        private Integer interceptions;
        /**
         * 成功长传
         */
        @JsonProperty(value = "long_balls_accuracy")
        private Integer longBallsAccuracy;
        /**
         * 两黄变红
         */
        @JsonProperty(value = "yellow2red_cards")
        private Integer yellow2redCards;
        /**
         * 失球
         */
        @JsonProperty(value = "goals_against")
        private Integer goalsAgainst;
        /**
         * 射正
         */
        @JsonProperty(value = "shots_on_target")
        private Integer shotsOnTarget;
        /**
         * 过人
         */
        private Integer dribble;
        /**
         * 射门
         */
        private Integer shots;
        /**
         * 解围
         */
        private Integer clearances;
        /**
         * 快攻进球
         */
        @JsonProperty(value = "fastbreak_goals")
        private Integer fastbreakGoals;
    }

    /**
     * 比赛id
     */
    private Integer id;
}