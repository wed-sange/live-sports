package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 历史比赛统计数据
 */
@Data
public class MatchLiveHistory {
    /**
     * ["纳米比赛id - int","比赛状态，详见状态码->比赛状态 - int",["主队比分(常规时间) - int","主队半场比分 - int","主队红牌 - int","主队黄牌 - int","主队角球，-1表示没有角球数据 - int","主队加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","主队点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"],["客队比分(常规时间) - int","客队半场比分 - int","客队红牌 - int","客队黄牌 - int","客队角球，-1表示没有角球数据 - int","客队加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","客队点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"],"开球时间戳，上/下半场开球时间(根据比赛状态判断) - int","备注信息，可忽略 - string"]比分字段说明<br/>example：[2783605,8,[1, 0, 0, 0, -1, 0, 0],[1, 0, 0, 0, -1, 0, 0],0,""]
     */
    @Schema(description = "比分字段说明<br/>example：[2783605,8,[1, 0, 0, 0, -1, 0, 0],[1, 0, 0, 0, -1, 0, 0],0,\"\"]",
            type = "array",
            allowableValues = {
                    "纳米比赛id - int",
                    "比赛状态，详见状态码->比赛状态 - int",
                    "$$[\"主队比分(常规时间) - int\",\"主队半场比分 - int\",\"主队红牌 - int\",\"主队黄牌 - int\",\"主队角球，-1表示没有角球数据 - int\",\"主队加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int\",\"主队点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int\"]$$",
                    "$$[\"客队比分(常规时间) - int\",\"客队半场比分 - int\",\"客队红牌 - int\",\"客队黄牌 - int\",\"客队角球，-1表示没有角球数据 - int\",\"客队加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int\",\"客队点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int\"]$$",
                    "开球时间戳，上/下半场开球时间(根据比赛状态判断) - int",
                    "备注信息，可忽略 - string"
            }
    )
    private List<Object> score;
    /**
     * 比赛统计字段说明，不存在为空
     */
    private List<Stats> stats;

    @Data
    public static class Stats {
        /**
         * 客队值
         */
        private Integer away;
        /**
         * 类型，详见状态码->技术统计
         */
        private Integer type;
        /**
         * 主队值
         */
        private Integer home;
    }

    /**
     * 文字直播字段说明，不存在为空
     */
    private List<Tlive> tlive;

    @Data
    public static class Tlive {
        /**
         * 事件内容
         */
        private String data;
        /**
         * 是否重要事件，1-是、0-否
         */
        private Integer main;
        /**
         * 事件发生方，0-中立、1-主队、2-客队
         */
        private Integer position;
        /**
         * 事件时间(分钟)
         */
        private String time;
        /**
         * 类型，详见状态码->技术统计
         */
        private Integer type;
    }

    /**
     * 比赛事件字段说明，不存在为空
     */
    private List<Incidents> incidents;

    @Data
    public static class Incidents {
        /**
         * 事件相关球员id，可能不存在<br/>
         * assist1-助攻球员1<br/>
         * assist2-助攻球员2<br/>
         * in_player-换上球员<br/>
         * out_player-换下球员
         */
        @JsonProperty(value = "player_id")
        @Schema(description = "事件相关球员id，可能不存在")
        private Integer playerId;

        @JsonProperty(value = "assist1_name")
        @Schema(description = "助攻球员1名称")
        private String assist1Name;

        @JsonProperty(value = "assist2_id")
        @Schema(description = "助攻球员2ID")
        private Integer assist2Id;

        @JsonProperty(value = "assist2_name")
        @Schema(description = "助攻球员2名称")
        private String assist2Name;

        @JsonProperty(value = "in_player_id")
        @Schema(description = "换上球员ID")
        private Integer inPlayerId;

        @JsonProperty(value = "in_player_name")
        @Schema(description = "换上球员名称")
        private String inPlayerName;

        @JsonProperty(value = "out_player_id")
        @Schema(description = "换下球员ID")
        private Integer outPlayerId;

        @JsonProperty(value = "out_player_name")
        @Schema(description = "换下球员名称")
        private String outPlayerName;

        /**
         * 主队比分（进球、未进球 事件存在）
         */
        @JsonProperty(value = "home_score")
        @Schema(description = "主队比分（进球、未进球 事件存在）")
        private Integer homeScore;
        /**
         * 红黄牌、换人事件原因，详见状态码->事件原因（红黄牌、换人事件存在）
         */
        @JsonProperty(value = "reason_type")
        @Schema(description = "红黄牌、换人事件原因，详见状态码->事件原因（红黄牌、换人事件存在）")
        private Integer reasonType;
        /**
         * 客队比分（进球、未进球 事件存在）
         */
        @JsonProperty(value = "away_score")
        @Schema(description = "客队比分（进球、未进球 事件存在）")
        private Integer awayScore;
        /**
         * 事件发生方，0-中立、1-主队、2-客队
         */
        private Integer position;
        /**
         * 时间(分钟)
         */
        private Integer time;
        /**
         * 事件相关球员名称，可能不存在
         */
        @JsonProperty(value = "player_name")
        @Schema(description = "事件相关球员名称，可能不存在")
        private String playerName;
        /**
         * 类型，详见状态码->技术统计
         */
        private Integer type;
        /**
         * VAR结果（VAR事件存在）<br/>
         * 1-进球有效<br/>
         * 2-进球无效<br/>
         * 3-点球有效<br/>
         * 4-点球取消<br/>
         * 5-红牌有效<br/>
         * 6-红牌取消<br/>
         * 7-出牌处罚核实<br/>
         * 8-出牌处罚更改<br/>
         * 9-维持原判<br/>
         * 10-判罚更改<br/>
         * 0-未知
         */
        @JsonProperty(value = "var_result")
        @Schema(description = "VAR结果（VAR事件存在）<br/>\n" + "1-进球有效<br/>\n" + "2-进球无效<br/>\n" + "3-点球有效<br/>\n"
                + "4-点球取消<br/>\n" + "5-红牌有效<br/>\n" + "6-红牌取消<br/>\n" + "7-出牌处罚核实<br/>\n" + "8-出牌处罚更改<br/>\n"
                + "9-维持原判<br/>\n" + "10-判罚更改<br/>\n" + "0-未知")
        private Integer varResult;
        /**
         * 时间
         */
        private Integer second;
        /**
         * VAR原因（VAR事件存在）<br/>
         * 1-进球判定<br/>
         * 2-进球未判定<br/>
         * 3-点球判定<br/>
         * 4-点球未判定<br/>
         * 5-红牌判定<br/>
         * 6-出牌处罚判定<br/>
         * 7-错认身份<br/>
         * 0-其他
         */
        @JsonProperty(value = "var_reason")
        @Schema(description = "VAR原因（VAR事件存在）<br/>\n"
                + "1-进球判定<br/>\n"
                + "2-进球未判定<br/>\n"
                + "3-点球判定<br/>\n"
                + "4-点球未判定<br/>\n"
                + "5-红牌判定<br/>\n"
                + "6-出牌处罚判定<br/>\n"
                + "7-错认身份<br/>\n"
                + "0-其他")
        private Integer varReason;
    }

    /**
     * 比赛id
     */
    private Integer id;
}
