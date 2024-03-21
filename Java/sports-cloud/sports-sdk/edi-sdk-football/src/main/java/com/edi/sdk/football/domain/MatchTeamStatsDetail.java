package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 比赛球队统计数据
 */
@Data
public class MatchTeamStatsDetail {
    /**
     * 犯规
     */
    private Integer fouls;
    /**
     * 角球
     */
    @JsonProperty(value = "corner_kicks")
    @Schema(description = "角球")
    private Integer cornerKicks;
    /**
     * 点球
     */
    private Integer penalty;
    /**
     * 传球成功
     */
    @JsonProperty(value = "passes_accuracy")
    @Schema(description = "传球成功")
    private Integer passesAccuracy;
    /**
     * 红牌
     */
    @JsonProperty(value = "red_cards")
    @Schema(description = "红牌")
    private Integer redCards;
    /**
     * 有效阻挡
     */
    @JsonProperty(value = "blocked_shots")
    @Schema(description = "有效阻挡")
    private Integer blockedShots;
    /**
     * 球队id
     */
    @JsonProperty(value = "team_id")
    @Schema(description = "球队id")
    private Integer teamId;
    /**
     * 1对1拼抢
     */
    private Integer duels;
    /**
     * 丢失球权
     */
    @JsonProperty(value = "poss_losts")
    @Schema(description = "丢失球权")
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
    @Schema(description = "1对1拼抢成功")
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
    @Schema(description = "被侵犯")
    private Integer wasFouled;
    /**
     * 越位
     */
    private Integer offsides;
    /**
     * 过人成功
     */
    @JsonProperty(value = "dribble_succ")
    @Schema(description = "过人成功")
    private Integer dribbleSucc;
    /**
     * 快攻射门
     */
    @JsonProperty(value = "fastbreak_shots")
    @Schema(description = "快攻射门")
    private Integer fastbreakShots;
    /**
     * 关键传球
     */
    @JsonProperty(value = "key_passes")
    @Schema(description = "关键传球")
    private Integer keyPasses;
    /**
     * 任意球得分
     */
    @JsonProperty(value = "freekick_goals")
    @Schema(description = "任意球得分")
    private Integer freekickGoals;
    /**
     * 控球率
     */
    @JsonProperty(value = "ball_possession")
    @Schema(description = "控球率")
    private Integer ballPossession;
    /**
     * 黄牌
     */
    @JsonProperty(value = "yellow_cards")
    @Schema(description = "黄牌")
    private Integer yellowCards;
    /**
     * 传中球成功
     */
    @JsonProperty(value = "crosses_accuracy")
    @Schema(description = "传中球成功")
    private Integer crossesAccuracy;
    /**
     * 长传
     */
    @JsonProperty(value = "long_balls")
    @Schema(description = "长传")
    private Integer longBalls;
    /**
     * 快攻
     */
    private Integer fastbreaks;
    /**
     * 击中门框
     */
    @JsonProperty(value = "hit_woodwork")
    @Schema(description = "击中门框")
    private Integer hitWoodwork;
    /**
     * 拦截
     */
    private Integer interceptions;
    /**
     * 成功长传
     */
    @JsonProperty(value = "long_balls_accuracy")
    @Schema(description = "成功长传")
    private Integer longBallsAccuracy;
    /**
     * 两黄变红
     */
    @JsonProperty(value = "yellow2red_cards")
    @Schema(description = "两黄变红")
    private Integer yellow2redCards;
    /**
     * 失球
     */
    @JsonProperty(value = "goals_against")
    @Schema(description = "失球")
    private Integer goalsAgainst;
    /**
     * 射正
     */
    @JsonProperty(value = "shots_on_target")
    @Schema(description = "射正")
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
    @Schema(description = "快攻进球")
    private Integer fastbreakGoals;
}
