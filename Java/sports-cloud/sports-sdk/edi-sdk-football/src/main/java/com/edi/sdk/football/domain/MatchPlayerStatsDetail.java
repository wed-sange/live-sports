package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 比赛球员统计数据
 */
@Data
public class MatchPlayerStatsDetail {
    /**
     * 拳击球
     */
    private Integer punches;
    /**
     * 犯规
     */
    private Integer fouls;
    /**
     * 守门员出击成功
     */
    @JsonProperty(value = "runs_out_succ")
    private Integer runsOutSucc;
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
     * 评分，10为满分，为了避免浮点数影响，x100倍存储为整数，eg：计算评分为(760/100=7.60)
     */
    private Integer rating;
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
     * 球员id
     */
    @JsonProperty(value = "player_id")
    private Integer playerId;
    /**
     * 传球
     */
    private Integer passes;
    /**
     * 扑救
     */
    private Integer saves;
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
     * 出场时间(分钟)
     */
    @JsonProperty(value = "minutes_played")
    private Integer minutesPlayed;
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
     * 守门员出击
     */
    @JsonProperty(value = "runs_out")
    private Integer runsOut;
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
     * 传球被断
     */
    private Integer dispossessed;
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
     * 高空出击
     */
    @JsonProperty(value = "good_high_claim")
    private Integer goodHighClaim;
    /**
     * 两黄变红
     */
    @JsonProperty(value = "yellow2red_cards")
    private Integer yellow2redCards;
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
    /**
     * 首发
     */
    private Integer first;
}