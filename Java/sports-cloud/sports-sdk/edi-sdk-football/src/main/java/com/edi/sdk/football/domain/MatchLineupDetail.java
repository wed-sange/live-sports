package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * null
 */
@Data
public class MatchLineupDetail {
    /**
     * 主队阵型
     */
    @JsonProperty(value = "home_formation")
    private String homeFormation;
    /**
     * 客队阵型球员列表
     */
    private List<Away> away;

    @Data
    public static class Away {
        /**
         * 球员logo(国家队)
         */
        @JsonProperty(value = "national_logo")
        private String nationalLogo;
        /**
         * 评分，10为满分
         */
        private String rating;
        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        private Integer teamId;
        /**
         * 是否队长，1-是、0-否
         */
        private Integer captain;
        /**
         * 球衣号
         */
        @JsonProperty(value = "shirt_number")
        private Integer shirtNumber;
        /**
         * 球员名称
         */
        private String name;
        /**
         * 阵容x坐标，总共100
         */
        private Integer x;
        /**
         * 球员事件列表，有事件存在，默认不存在
         */
        private List<Incidents> incidents;

        @Data
        public static class Incidents {
            /**
             * 发生方，0-中立、1-主队、2-客队
             */
            private Integer belong;
            /**
             * 主队比分
             */
            @JsonProperty(value = "home_score")
            @Schema(description = "主队比分")
            private Integer homeScore;
            /**
             * 红黄牌、换人事件原因，详见状态码->事件原因（红黄牌、换人事件存在）
             */
            @JsonProperty(value = "reason_type")
            private Integer reasonType;
            /**
             * 客队比分
             */
            @JsonProperty(value = "away_score")
            @Schema(description = "客队比分")
            private Integer awayScore;
            /**
             * 事件发生时间（含加时时间，'A+B':A-比赛时间,B-加时时间）
             */
            private String time;
            /**
             * 事件类型，详见状态码->技术统计
             */
            private Integer type;
            /**
             * 球员信息<br/>player-相关球员<br/>assist1-助攻球员1<br/>assist2-助攻球员2<br/>in_player-换上球员<br/>out_player-换下球员
             */
            private Player player;

            @Data
            public static class Player {
                /**
                 * 中文名称
                 */
                private String name;
                /**
                 * 球员id
                 */
                private Integer id;
            }
        }

        /**
         * 球员logo
         */
        private String logo;
        /**
         * 阵容y坐标，总共100
         */
        private Integer y;
        /**
         * 球员id
         */
        private Integer id;
        /**
         * 球员位置，F前锋、M中场、D后卫、G守门员、其他为未知
         */
        private String position;
        /**
         * 是否首发，1-是、0-否
         */
        private Integer first;
    }

    /**
     * 客队阵型
     */
    @JsonProperty(value = "away_formation")
    private String awayFormation;
    /**
     * 主队球衣颜色
     */
    @JsonProperty(value = "home_color")
    private String homeColor;
    /**
     * 客队球衣颜色
     */
    @JsonProperty(value = "away_color")
    private String awayColor;
    /**
     * 正式阵容，1-是、0-不是
     */
    private Integer confirmed;
    /**
     * 主队阵型球员列表
     */
    private List<Home> home;

    @Data
    public static class Home {
        /**
         * 球员logo(国家队)
         */
        @JsonProperty(value = "national_logo")
        private String nationalLogo;
        /**
         * 评分，10为满分
         */
        private String rating;
        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        private Integer teamId;
        /**
         * 是否队长，1-是、0-否
         */
        private Integer captain;
        /**
         * 球衣号
         */
        @JsonProperty(value = "shirt_number")
        private Integer shirtNumber;
        /**
         * 球员名称
         */
        private String name;
        /**
         * 阵容x坐标，总共100
         */
        private Integer x;
        /**
         * 球员事件列表，有事件存在，默认不存在
         */
        private List<Incidents> incidents;

        @Data
        public static class Incidents {
            /**
             * 发生方，0-中立、1-主队、2-客队
             */
            private Integer belong;
            /**
             * 主队比分
             */
            @JsonProperty(value = "home_score")
            @Schema(description = "主队比分")
            private Integer homeScore;
            /**
             * 红黄牌、换人事件原因，详见状态码->事件原因（红黄牌、换人事件存在）
             */
            @JsonProperty(value = "reason_type")
            private Integer reasonType;
            /**
             * 客队比分
             */
            @JsonProperty(value = "away_score")
            @Schema(description = "客队比分")
            private Integer awayScore;
            /**
             * 事件发生时间（含加时时间，'A+B':A-比赛时间,B-加时时间）
             */
            private String time;
            /**
             * 事件类型，详见状态码->技术统计
             */
            private Integer type;
            /**
             * 球员信息<br/>player-相关球员<br/>assist1-助攻球员1<br/>assist2-助攻球员2<br/>in_player-换上球员<br/>out_player-换下球员
             */
            private Player player;

            @Data
            public static class Player {
                /**
                 * 中文名称
                 */
                private String name;
                /**
                 * 球员id
                 */
                private Integer id;
            }
        }

        /**
         * 球员logo
         */
        private String logo;
        /**
         * 阵容y坐标，总共100
         */
        private Integer y;
        /**
         * 球员id
         */
        private Integer id;
        /**
         * 球员位置，F前锋、M中场、D后卫、G守门员、其他为未知
         */
        private String position;
        /**
         * 是否首发，1-是、0-否
         */
        private Integer first;
    }
}
