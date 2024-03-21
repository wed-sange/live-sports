package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 统计数据列表
 */
@Data
public class Compensation {
    /**
     * 历史同赔
     */
    private Similar similar;

    @Data
    public static class Similar {
        /**
         * 指数公司项
         */
        private List<Companies> companies;

        @Data
        public static class Companies {
            /**
             * 名称
             */
            private String name;
            /**
             * 公司id
             */
            private Integer id;
        }

        /**
         * 球队项
         */
        private List<Teams> teams;

        @Data
        public static class Teams {
            /**
             * 名称
             */
            private String name;
            /**
             * 球队id
             */
            private Integer id;
        }

        /**
         * 赛事项
         */
        private List<Competitions> competitions;

        @Data
        public static class Competitions {
            /**
             * 名称
             */
            private String name;
            /**
             * 赛事id
             */
            private Integer id;
        }

        /**
         * 胜平负概率
         */
        private List<Analysis> analysis;

        @Data
        public static class Analysis {
            /**
             * 平率
             */
            @JsonProperty(value = "draw_rate")
            private Float drawRate;
            /**
             * 负率
             */
            @JsonProperty(value = "loss_rate")
            private Float lossRate;
            /**
             * 指数公司id
             */
            private Integer id;
            /**
             * 胜率
             */
            @JsonProperty(value = "win_rate")
            private Float winRate;
        }

        /**
         * 同赔赛程(欧赔)
         */
        private List<Europe> europe;

        @Data
        public static class Europe {
            /**
             * 负场数
             */
            private Integer loss;
            /**
             * 总场数
             */
            private Integer total;
            /**
             * 胜场数
             */
            private Integer won;
            /**
             * ["主胜 - float","平   - float","客胜 - float"]欧赔初盘指数<br/>example：[2.1, 3.4, 3.1]
             */
            @Schema(description = "欧赔 初盘指数<br/>example：[2.1, 3.4, 3.1]",
                    type = "array",
                    allowableValues = {
                            "主胜 - float",
                            "平   - float",
                            "客胜 - float"
                    }
            )
            private List<Object> odds;
            /**
             * 指数公司id
             */
            private Integer id;
            /**
             * 平场数
             */
            private Integer draw;
            /**
             * 赛程项
             */
            private List<Matches> matches;

            @Data
            public static class Matches {
                /**
                 * 主队id
                 */
                @JsonProperty(value = "home_team_id")
                private Integer homeTeamId;
                /**
                 * ["主胜 - float","平   - float","客胜 - float"]欧赔 初盘指数<br/>example：[2.1, 3.4, 3.1]
                 */
                @JsonProperty(value = "begin_odds")
                @Schema(description = "欧赔 初盘指数<br/>example：[2.1, 3.4, 3.1]",
                        type = "array",
                        allowableValues = {
                                "主胜 - float",
                                "平   - float",
                                "客胜 - float"
                        }
                )
                private List<Object> beginOdds;
                /**
                 * 主队进球
                 */
                @JsonProperty(value = "home_score")
                @Schema(description = "主队比分")
                private Integer homeScore;
                /**
                 * 赛事id
                 */
                @JsonProperty(value = "competition_id")
                private Integer competitionId;
                /**
                 * 客队进球
                 */
                @JsonProperty(value = "away_score")
                @Schema(description = "客队进球")
                private Integer awayScore;
                /**
                 * 比赛时间
                 */
                @JsonProperty(value = "match_time")
                private Integer matchTime;
                /**
                 * 比赛id
                 */
                private Integer id;
                /**
                 * 客队id
                 */
                @JsonProperty(value = "away_team_id")
                private Integer awayTeamId;
                /**
                 * ["主胜 - float","平   - float","客胜 - float"]欧赔 最后的即时盘指数<br/>example：[2.1, 3.4, 3.1]
                 */
                @JsonProperty(value = "immediate_odds")
                @Schema(description = "欧赔 初盘指数<br/>example：[2.1, 3.4, 3.1]",
                        type = "array",
                        allowableValues = {
                                "主胜 - float",
                                "平   - float",
                                "客胜 - float"
                        }
                )
                private List<Object> immediateOdds;
            }
        }
    }

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    private Integer updatedAt;
    /**
     * 比赛id
     */
    private Integer id;
    /**
     * 历史交锋
     */
    private History history;

    @Data
    public static class History {
        /**
         * 客队
         */
        private Away away;

        @Data
        public static class Away {
            /**
             * 负场数
             */
            @JsonProperty(value = "lost_count")
            private Integer lostCount;
            /**
             * 胜场数
             */
            @JsonProperty(value = "won_count")
            private Integer wonCount;
            /**
             * 胜率
             */
            private Float rate;
            /**
             * 平场数
             */
            @JsonProperty(value = "drawn_count")
            private Integer drawnCount;
        }

        /**
         * 主队
         */
        private Home home;

        @Data
        public static class Home {
            /**
             * 负场数
             */
            @JsonProperty(value = "lost_count")
            private Integer lostCount;
            /**
             * 胜场数
             */
            @JsonProperty(value = "won_count")
            private Integer wonCount;
            /**
             * 胜率
             */
            private Float rate;
            /**
             * 平场数
             */
            @JsonProperty(value = "drawn_count")
            private Integer drawnCount;
        }
    }

    /**
     * 近期战绩
     */
    private Recent recent;

    @Data
    public static class Recent {
        /**
         * 客队
         */
        private Away away;

        @Data
        public static class Away {
            /**
             * 负场数
             */
            @JsonProperty(value = "lost_count")
            private Integer lostCount;
            /**
             * 胜场数
             */
            @JsonProperty(value = "won_count")
            private Integer wonCount;
            /**
             * 胜率
             */
            private Float rate;
            /**
             * 平场数
             */
            @JsonProperty(value = "drawn_count")
            private Integer drawnCount;
        }

        /**
         * 主队
         */
        private Home home;

        @Data
        public static class Home {
            /**
             * 负场数
             */
            @JsonProperty(value = "lost_count")
            private Integer lostCount;
            /**
             * 胜场数
             */
            @JsonProperty(value = "won_count")
            private Integer wonCount;
            /**
             * 胜率
             */
            private Float rate;
            /**
             * 平场数
             */
            @JsonProperty(value = "drawn_count")
            private Integer drawnCount;
        }
    }
}
