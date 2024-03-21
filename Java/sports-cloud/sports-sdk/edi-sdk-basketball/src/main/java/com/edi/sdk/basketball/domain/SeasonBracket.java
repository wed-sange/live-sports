package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 淘汰阶段对阵图数据
 */
@Data
public class SeasonBracket {
    /**
     * 对阵
     */
    @JsonProperty(value = "match_ups")
    private List<MatchUps> matchUps;

    @Data
    public static class MatchUps {
        /**
         * ["比赛id - int"]对阵关联的比赛id (不存在为空)<br/>example：[3557996, 3557997]
         */
        @JsonProperty(value = "match_ids")
        private List<Object> matchIds;
        /**
         * 主队id
         */
        @JsonProperty(value = "home_team_id")
        private Integer homeTeamId;
        /**
         * 主队比分（多回合为胜场次数）
         */
        @JsonProperty(value = "home_score")
        @Schema(description = "主队比分（多回合为胜场次数）")
        private Integer homeScore;
        /**
         * 赛制说明（其它为未知）：<br/>1-单场决胜制<br/>8-3局2胜制<br/>9-5局3胜制<br/>10-7局4胜制
         */
        @JsonProperty(value = "type_id")
        private Integer typeId;
        /**
         * 客队比分（多回合为胜场次数）
         */
        @JsonProperty(value = "away_score")
        @Schema(description = "客队比分（多回合为胜场次数）")
        private Integer awayScore;
        /**
         * ["对阵id - int"]连线下一对阵id (不存在为空)<br/>example：[10013]
         */
        @JsonProperty(value = "parent_ids")
        private List<Object> parentIds;
        /**
         * 对阵阶段id
         */
        @JsonProperty(value = "round_id")
        private Integer roundId;
        /**
         * 排序（上半区在前半，下半区在后半）（NBA：东部=上半区，西部=下半区）
         */
        private Integer number;
        /**
         * ["对阵id - int","对阵id - int"]连线上一对阵id (不存在为空)<br/>example：[10006, 10007]
         */
        @JsonProperty(value = "children_ids")
        private List<Object> childrenIds;
        /**
         * 胜利队id
         */
        @JsonProperty(value = "winner_team_id")
        private Integer winnerTeamId;
        /**
         * 对阵id
         */
        private Integer id;
        /**
         * 状态说明（其它为未知）：<br/>1-未开赛<br/>2-等待对手<br/>6-进行中<br/>7-主场胜<br/>8-客场胜<br/>9-取消<br/>10-轮空
         */
        @JsonProperty(value = "state_id")
        private Integer stateId;
        /**
         * 客队id
         */
        @JsonProperty(value = "away_team_id")
        private Integer awayTeamId;
    }

    /**
     * 对阵图分组
     */
    private List<Groups> groups;

    @Data
    public static class Groups {
        /**
         * 首场比赛时间
         */
        @JsonProperty(value = "start_time")
        private Integer startTime;
        /**
         * 分组顺序
         */
        private Integer number;
        /**
         * 类型，1-胜者区、2-第3名、0-未知
         */
        @JsonProperty(value = "type_id")
        private Integer typeId;
        /**
         * 终场比赛时间
         */
        @JsonProperty(value = "end_time")
        private Integer endTime;
        /**
         * 对阵图分组id
         */
        private Integer id;
        /**
         * 对阵图id
         */
        @JsonProperty(value = "bracket_id")
        private Integer bracketId;
    }

    /**
     * 对阵阶段
     */
    private List<Rounds> rounds;

    @Data
    public static class Rounds {
        /**
         * 中文名称
         */
        @JsonProperty(value = "name_zh")
        private String nameZh;
        /**
         * 首场比赛时间
         */
        @JsonProperty(value = "start_time")
        private Integer startTime;
        /**
         * 轮次顺序
         */
        private Integer number;
        /**
         * 对阵图分组id
         */
        @JsonProperty(value = "group_id")
        private Integer groupId;
        /**
         * 终场比赛时间
         */
        @JsonProperty(value = "end_time")
        private Integer endTime;
        /**
         * 对阵阶段id
         */
        private Integer id;
        /**
         * 对阵图id
         */
        @JsonProperty(value = "bracket_id")
        private Integer bracketId;
        /**
         * 英文名称
         */
        @JsonProperty(value = "name_en")
        private String nameEn;
    }

    /**
     * 对阵图
     */
    private List<Brackets> brackets;

    @Data
    public static class Brackets {
        /**
         * 中文名称
         */
        @JsonProperty(value = "name_zh")
        private String nameZh;
        /**
         * 开始时间
         */
        @JsonProperty(value = "start_time")
        private Integer startTime;
        /**
         * 赛事id
         */
        @JsonProperty(value = "competition_id")
        private Integer competitionId;
        /**
         * 结束时间
         */
        @JsonProperty(value = "end_time")
        private Integer endTime;
        /**
         * 对阵图id
         */
        private Integer id;
        /**
         * 英文名称
         */
        @JsonProperty(value = "name_en")
        private String nameEn;
    }
}
