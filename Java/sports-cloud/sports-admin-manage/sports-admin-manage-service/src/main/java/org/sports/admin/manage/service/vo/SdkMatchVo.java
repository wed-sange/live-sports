package org.sports.admin.manage.service.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchStatus;
import org.sports.admin.manage.dao.enums.MatchType;

import java.io.Serializable;
import java.util.List;

/**
 * 比赛列表
 */
@Data
public class SdkMatchVo implements Serializable {
    @Schema(description = "动画、情报、阵容 字段")
    private Coverage coverage;

    @Data
    public static class Coverage implements Serializable {
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

    @Schema(description = "备注")
    private String note;
    @JsonProperty(value = "home_team_id")
    @Schema(description = "主队id")
    private Integer homeTeamId;
    @Schema(description = "是否中立场，1-是、0-否")
    private Integer neutral;
    @JsonProperty(value = "season_id")
    @Schema(description = "赛季id")
    private Integer seasonId;
    @JsonProperty(value = "match_time")
    @Schema(description = "比赛时间")
    private Integer matchTime;
    @JsonProperty(value = "home_scores")
    @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]", allowableValues = {"比分(常规时间) - int",
            "半场比分 - int", "红牌 - int", "黄牌 - int", "角球，-1表示没有角球数据 - int", "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"})
    private List<Object> homeScores;
    @JsonProperty(value = "away_scores")
    @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]", allowableValues = {"比分(常规时间) - int",
            "半场比分 - int", "红牌 - int", "黄牌 - int", "角球，-1表示没有角球数据 - int", "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"})
    private List<Object> awayScores;
    @Schema(description = "双回合中另一回合比赛id")
    @JsonProperty(value = "related_id")
    private Integer relatedId;
    @Schema(description = "裁判id")
    @JsonProperty(value = "referee_id")
    private Integer refereeId;
    @Schema(description = "比赛环境,有数据才有此字段")
    private Environment environment;

    @Data
    public static class Environment implements Serializable {
        /**
         * 天气id<br/>
         * 1:局部有云<br/>
         * 2:多云<br/>
         * 3:局部有云/雨<br/>
         * 4:雪<br/>
         * 5:晴<br/>
         * 6:阴有雨/局部有雷暴<br/>
         * 7:阴<br/>
         * 8:薄雾<br/>
         * 9:阴有雨<br/>
         * 10:多云有雨<br/>
         * 11:多云有雨/局部有雷暴<br/>
         * 12:局部有云/雨和雷暴<br/>
         * 13:雾
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

    @JsonProperty(value = "status_id")
    @Schema(description = "比赛状态，详见状态码->比赛状态")
    private Integer statusId;
    @Schema(description = "关联信息")
    private Round round;

    @Data
    public static class Round implements Serializable {
        @JsonProperty(value = "stage_id")
        @Schema(description = "阶段id")
        private Integer stageId;
        @JsonProperty(value = "group_num")
        @Schema(description = "第几组，1-A、2-B以此类推")
        private Integer groupNum;
        /**
         * 第几轮
         */
        @JsonProperty(value = "round_num")
        @Schema(description = "第几轮")
        private Integer roundNum;
    }

    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;
    @JsonProperty(value = "away_position")
    @Schema(description = "客队排名")
    private String awayPosition;
    @JsonProperty(value = "competition_id")
    @Schema(description = "赛事id")
    private Integer competitionId;
    @Schema(description = "比赛id")
    private Integer id;
    @JsonProperty(value = "home_position")
    @Schema(description = "主队排名")
    private String homePosition;
    @JsonProperty(value = "away_team_id")
    @Schema(description = "客队id")
    private Integer awayTeamId;
    @JsonProperty(value = "agg_score")
    @Schema(description = "[\"主队比分 - int\",\"客队比分 - int\"]双回合常规时间(包括加时时间)总比分 字段说明<br/>example：[3, 6]")
    private List<Object> aggScore;
    @JsonProperty(value = "venue_id")
    @Schema(description = "场馆id")
    private Integer venueId;

    @Schema(description = "类型id，1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无")
    private Integer kind;
    @Schema(description = "比赛总节数(不包含加时)")
    @JsonProperty(value = "period_count")
    private Integer periodCount;
    @Schema(description = "加时赛比分字段说明（大于1个加时才有该字段，每一位为1节加时比分）<br />* example：[[8, 9, 6], [8, 9, 8]]<br/>*Enum:<br/>Array[2]<br/>0:Array[3]<br/>0:\"主队第1节加时分数 - int\" < br / >1:\"主队第2节加时分数 - int\" < br / >2:\"主队第n节加时分数 - int\" < br / >1:Array[3]<br/>0:\"客队第1节加时分数 - int\" < br / >1:\"客队第2节加时分数 - int\" < br / >2:\"客队第n节加时分数 - int\" < br / > ")
    @JsonProperty(value = "over_time_scores")
    private List<List<Integer>> overTimeScores;

    @Schema(description = "彩票期号、序号（lottery 体彩查询存在）")
    private Lottery lottery;

    @Data
    public static class Lottery implements Serializable {
        /**
         * 彩票期号（竞彩无期号，为空）
         */
        private String issue;
        /**
         * 序号
         */
        @JsonProperty(value = "issue_num")
        private String issueNum;
    }

    @Schema(description = "比赛类型：1：足球；2：篮球")
    private MatchType matchType;

    @Schema(description = "比赛状态 1：比赛中，2：未开赛, 3:已完成")
    private MatchStatus matchStatus;
    @Schema(description = "比赛进行时间（分钟）进行中足球比赛有此信息")
    private Integer runTime;
}