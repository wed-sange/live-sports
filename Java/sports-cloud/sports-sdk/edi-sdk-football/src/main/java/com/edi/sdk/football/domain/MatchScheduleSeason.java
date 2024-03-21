package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 比赛列表
 */
@Data
public class MatchScheduleSeason {
    /**
     * 动画、情报、阵容 字段
     */
    private Coverage coverage;

    @Data
    public static class Coverage {
        /**
         * 是否有阵容，1-是、0-否
         */
        private Integer lineup;
        /**
         * 是否有动画，1-是、0-否
         */
        private Integer mlive;
        /**
         * 是否有情报，1-是、0-否
         */
        private Integer intelligence;
    }

    /**
     * 备注
     */
    private String note;
    /**
     * 主队id
     */
    @JsonProperty(value = "home_team_id")
    private Integer homeTeamId;
    /**
     * 是否中立场，1-是、0-否
     */
    private Integer neutral;
    /**
     * 赛季id
     */
    @JsonProperty(value = "season_id")
    private Integer seasonId;
    /**
     * 比赛时间
     */
    @JsonProperty(value = "match_time")
    private Integer matchTime;
    /**
     * ["比分(常规时间) - int","半场比分 - int","红牌 - int","黄牌 - int","角球，-1表示没有角球数据 - int","加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"]比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]
     */
    @JsonProperty(value = "home_scores")
    private List<Object> homeScores;
    /**
     * ["比分(常规时间) - int","半场比分 - int","红牌 - int","黄牌 - int","角球，-1表示没有角球数据 - int","加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"]比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]
     */
    @JsonProperty(value = "away_scores")
    private List<Object> awayScores;
    /**
     * 双回合中另一回合比赛id
     */
    @JsonProperty(value = "related_id")
    private Integer relatedId;
    /**
     * 裁判id
     */
    @JsonProperty(value = "referee_id")
    private Integer refereeId;
    /**
     * 比赛环境,有数据才有此字段
     */
    private Environment environment;

    @Data
    public static class Environment {
        /**
         * 天气id<br/>1:局部有云<br/>2:多云<br/>3:局部有云/雨<br/>4:雪<br/>5:晴<br/>6:阴有雨/局部有雷暴<br/>7:阴<br/>8:薄雾<br/>9:阴有雨<br/>10:多云有雨<br/>11:多云有雨/局部有雷暴<br/>12:局部有云/雨和雷暴<br/>13:雾
         */
        private Integer weather;
        /**
         * 温度
         */
        private String temperature;
        /**
         * 湿度
         */
        private String humidity;
        /**
         * 气压
         */
        private String pressure;
        /**
         * 风速
         */
        private String wind;
    }

    /**
     * 比赛状态，详见状态码->比赛状态
     */
    @JsonProperty(value = "status_id")
    private Integer statusId;
    /**
     * 关联信息
     */
    private Round round;

    @Data
    public static class Round {
        /**
         * 阶段id
         */
        @JsonProperty(value = "stage_id")
        private Integer stageId;
        /**
         * 第几组，1-A、2-B以此类推
         */
        @JsonProperty(value = "group_num")
        private Integer groupNum;
        /**
         * 第几轮
         */
        @JsonProperty(value = "round_num")
        private Integer roundNum;
    }

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    private Integer updatedAt;
    /**
     * 客队排名
     */
    @JsonProperty(value = "away_position")
    private String awayPosition;
    /**
     * 赛事id
     */
    @JsonProperty(value = "competition_id")
    private Integer competitionId;
    /**
     * 比赛id
     */
    private Integer id;
    /**
     * 主队排名
     */
    @JsonProperty(value = "home_position")
    private String homePosition;
    /**
     * 客队id
     */
    @JsonProperty(value = "away_team_id")
    private Integer awayTeamId;
    /**
     * ["主队比分 - int","客队比分 - int"]双回合常规时间(包括加时时间)总比分 字段说明<br/>example：[3, 6]
     */
    @JsonProperty(value = "agg_score")
    private List<Object> aggScore;
    /**
     * 场馆id
     */
    @JsonProperty(value = "venue_id")
    private Integer venueId;
}