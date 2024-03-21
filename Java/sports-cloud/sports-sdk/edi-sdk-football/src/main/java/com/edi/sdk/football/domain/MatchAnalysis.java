package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 比赛分析数据
 */
@Data
public class MatchAnalysis {
    /**
     * 进球分布，可能不存在
     */
    @JsonProperty(value = "goal_distribution")
    private GoalDistribution goalDistribution;

    @Data
    public static class GoalDistribution {
        /**
         * 客队进球分布，没有数据为空
         */
        private Away away;

        @Data
        public static class Away {
            /**
             * 全部场次
             */
            private All all;

            @Data
            public static class All {
                /**
                 * 客队失球分布
                 */
                @Schema(description = "客队失球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> conceded;
                /**
                 * 客队进球分布
                 */
                @Schema(description = "客队进球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> scored;
                /**
                 * 比赛场次
                 */
                private Integer matches;
            }

            /**
             * 客场场次
             */
            private AwayAway away;

            @Data
            public static class AwayAway {
                /**
                 * 客队客场失球分布
                 */
                @Schema(description = "客队客场失球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> conceded;
                /**
                 * 客队客场进球分布
                 */
                @Schema(description = "客队客场进球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> scored;
                /**
                 * 比赛场次
                 */
                private Integer matches;
            }

            /**
             * 主场场次
             */
            private Home home;

            @Data
            public static class Home {
                /**
                 * 客队主场失球分布
                 */
                @Schema(description = "客队主场失球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> conceded;
                /**
                 * 客队主场进球分布
                 */
                @Schema(description = "客队主场进球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> scored;
                /**
                 * 比赛场次
                 */
                private Integer matches;
            }
        }

        /**
         * 主队进球分布，没有数据为空
         */
        private Home home;

        @Data
        public static class Home {
            /**
             * 全部场次
             */
            private All all;

            @Data
            public static class All {
                /**
                 * 主队失球分布
                 */
                @Schema(description = "主队失球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> conceded;
                /**
                 * 主队进球分布
                 */
                @Schema(description = "主队进球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> scored;
                /**
                 * 比赛场次
                 */
                private Integer matches;
            }

            /**
             * 客场场次
             */
            private Away away;

            @Data
            public static class Away {
                /**
                 * 主队客场失球分布
                 */
                @Schema(description = "主队客场失球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> conceded;
                /**
                 * 主队客场进球分布
                 */
                @Schema(description = "主队客场进球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> scored;
                /**
                 * 比赛场次
                 */
                private Integer matches;
            }

            /**
             * 主场场次
             */
            private HomeHome home;

            @Data
            public static class HomeHome {
                /**
                 * 主队主场失球分布
                 */
                @Schema(description = "主队主场失球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> conceded;
                /**
                 * 主队主场进球分布
                 */
                @Schema(description = "主队主场进球分布",
                        type = "array",
                        allowableValues = {
                                "个数 - int",
                                "百分比（%） - int",
                                "开始时间（分钟数） - int",
                                "结束时间（分钟数） - int"
                        }
                )
                private List<List<Integer>> scored;
                /**
                 * 比赛场次
                 */
                private Integer matches;
            }
        }
    }

    /**
     * 未来赛程
     */
    private Future future;

    @Data
    public static class Future {
        /**
         * 客队近期赛程（格式同 info比赛信息字段）
         */
        private List<Away> away;

        @Data
        public static class Away {
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
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> homeScores;
            /**
             * ["比分(常规时间) - int","半场比分 - int","红牌 - int","黄牌 - int","角球，-1表示没有角球数据 - int","加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"]比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]
             */
            @JsonProperty(value = "away_scores")
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> awayScores;
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","角球   大,盘口,小,是否封盘 - string"]BET365初盘<br/>example：["0.86,-0.5,1.04,0","3.8,3.6,2.05,0","0.9,2.75,1.0,0","0.9,9,0.9,0"]
             */
            @Schema(description = "BET365初盘<br/>example：[\"0.86,-0.5,1.04,0\",\"3.8,3.6,2.05,0\",\"0.9,2.75,1.0,0\",\"0.9,9,0.9,0\"]",
                    type = "array",
                    allowableValues = {
                            "亚盘   主,盘口,客,是否封盘 - string",
                            "欧盘   胜,平,负,是否封盘   - string",
                            "大小盘 大,盘口,小,是否封盘 - string",
                            "角球   大,盘口,小,是否封盘 - string"
                    }
            )
            private List<Object> odds;
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
        }

        /**
         * 交锋赛程（格式同 info比赛信息字段）
         */
        private List<Vs> vs;

        @Data
        public static class Vs {
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
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> homeScores;
            /**
             * ["比分(常规时间) - int","半场比分 - int","红牌 - int","黄牌 - int","角球，-1表示没有角球数据 - int","加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"]比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]
             */
            @JsonProperty(value = "away_scores")
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> awayScores;
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","角球   大,盘口,小,是否封盘 - string"]BET365初盘<br/>example：["0.86,-0.5,1.04,0","3.8,3.6,2.05,0","0.9,2.75,1.0,0","0.9,9,0.9,0"]
             */
            @Schema(description = "BET365初盘<br/>example：[\"0.86,-0.5,1.04,0\",\"3.8,3.6,2.05,0\",\"0.9,2.75,1.0,0\",\"0.9,9,0.9,0\"]",
                    type = "array",
                    allowableValues = {
                            "亚盘   主,盘口,客,是否封盘 - string",
                            "欧盘   胜,平,负,是否封盘   - string",
                            "大小盘 大,盘口,小,是否封盘 - string",
                            "角球   大,盘口,小,是否封盘 - string"
                    }
            )
            private List<Object> odds;
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
        }

        /**
         * 主队近期赛程（格式同 info比赛信息字段）
         */
        private List<Home> home;

        @Data
        public static class Home {
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
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> homeScores;
            /**
             * ["比分(常规时间) - int","半场比分 - int","红牌 - int","黄牌 - int","角球，-1表示没有角球数据 - int","加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"]比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]
             */
            @JsonProperty(value = "away_scores")
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> awayScores;
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","角球   大,盘口,小,是否封盘 - string"]BET365初盘<br/>example：["0.86,-0.5,1.04,0","3.8,3.6,2.05,0","0.9,2.75,1.0,0","0.9,9,0.9,0"]
             */
            @Schema(description = "BET365初盘<br/>example：[\"0.86,-0.5,1.04,0\",\"3.8,3.6,2.05,0\",\"0.9,2.75,1.0,0\",\"0.9,9,0.9,0\"]",
                    type = "array",
                    allowableValues = {
                            "亚盘   主,盘口,客,是否封盘 - string",
                            "欧盘   胜,平,负,是否封盘   - string",
                            "大小盘 大,盘口,小,是否封盘 - string",
                            "角球   大,盘口,小,是否封盘 - string"
                    }
            )
            private List<Object> odds;
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
        }
    }

    /**
     * 历史交锋/近期战绩
     */
    private History history;

    @Data
    public static class History {
        /**
         * 客队近期战绩（格式同 info比赛信息字段）
         */
        private List<Away> away;

        @Data
        public static class Away {
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
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> homeScores;
            /**
             * ["比分(常规时间) - int","半场比分 - int","红牌 - int","黄牌 - int","角球，-1表示没有角球数据 - int","加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"]比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]
             */
            @JsonProperty(value = "away_scores")
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> awayScores;
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","角球   大,盘口,小,是否封盘 - string"]BET365初盘<br/>example：["0.86,-0.5,1.04,0","3.8,3.6,2.05,0","0.9,2.75,1.0,0","0.9,9,0.9,0"]
             */
            @Schema(description = "BET365初盘<br/>example：[\"0.86,-0.5,1.04,0\",\"3.8,3.6,2.05,0\",\"0.9,2.75,1.0,0\",\"0.9,9,0.9,0\"]",
                    type = "array",
                    allowableValues = {
                            "亚盘   主,盘口,客,是否封盘 - string",
                            "欧盘   胜,平,负,是否封盘   - string",
                            "大小盘 大,盘口,小,是否封盘 - string",
                            "角球   大,盘口,小,是否封盘 - string"
                    }
            )
            private List<Object> odds;
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
        }

        /**
         * 历史交锋（格式同 info比赛信息字段）
         */
        private List<Vs> vs;

        @Data
        public static class Vs {
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
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> homeScores;
            /**
             * ["比分(常规时间) - int","半场比分 - int","红牌 - int","黄牌 - int","角球，-1表示没有角球数据 - int","加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"]比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]
             */
            @JsonProperty(value = "away_scores")
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> awayScores;
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","角球   大,盘口,小,是否封盘 - string"]BET365初盘<br/>example：["0.86,-0.5,1.04,0","3.8,3.6,2.05,0","0.9,2.75,1.0,0","0.9,9,0.9,0"]
             */
            @Schema(description = "BET365初盘<br/>example：[\"0.86,-0.5,1.04,0\",\"3.8,3.6,2.05,0\",\"0.9,2.75,1.0,0\",\"0.9,9,0.9,0\"]",
                    type = "array",
                    allowableValues = {
                            "亚盘   主,盘口,客,是否封盘 - string",
                            "欧盘   胜,平,负,是否封盘   - string",
                            "大小盘 大,盘口,小,是否封盘 - string",
                            "角球   大,盘口,小,是否封盘 - string"
                    }
            )
            private List<Object> odds;
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
        }

        /**
         * 主队近期战绩（格式同 info比赛信息字段）
         */
        private List<Home> home;

        @Data
        public static class Home {
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
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> homeScores;
            /**
             * ["比分(常规时间) - int","半场比分 - int","红牌 - int","黄牌 - int","角球，-1表示没有角球数据 - int","加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"]比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]
             */
            @JsonProperty(value = "away_scores")
            @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                    type = "array",
                    allowableValues = {
                            "比分(常规时间) - int",
                            "半场比分 - int",
                            "红牌 - int",
                            "黄牌 - int",
                            "角球，-1表示没有角球数据 - int",
                            "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                            "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                    }
            )
            private List<Object> awayScores;
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","角球   大,盘口,小,是否封盘 - string"]BET365初盘<br/>example：["0.86,-0.5,1.04,0","3.8,3.6,2.05,0","0.9,2.75,1.0,0","0.9,9,0.9,0"]
             */
            @Schema(description = "BET365初盘<br/>example：[\"0.86,-0.5,1.04,0\",\"3.8,3.6,2.05,0\",\"0.9,2.75,1.0,0\",\"0.9,9,0.9,0\"]",
                    type = "array",
                    allowableValues = {
                            "亚盘   主,盘口,客,是否封盘 - string",
                            "欧盘   胜,平,负,是否封盘   - string",
                            "大小盘 大,盘口,小,是否封盘 - string",
                            "角球   大,盘口,小,是否封盘 - string"
                    }
            )
            private List<Object> odds;
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
        }
    }

    /**
     *
     */
    private Info info;

    @Data
    public static class Info {
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
        @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                type = "array",
                allowableValues = {
                        "比分(常规时间) - int",
                        "半场比分 - int",
                        "红牌 - int",
                        "黄牌 - int",
                        "角球，-1表示没有角球数据 - int",
                        "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                        "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                }
        )
        private List<Object> homeScores;
        /**
         * ["比分(常规时间) - int","半场比分 - int","红牌 - int","黄牌 - int","角球，-1表示没有角球数据 - int","加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int","点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"]比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]
         */
        @JsonProperty(value = "away_scores")
        @Schema(description = "比分字段说明<br/>example：[1, 0, 0, 0, -1, 0, 0]",
                type = "array",
                allowableValues = {
                        "比分(常规时间) - int",
                        "半场比分 - int",
                        "红牌 - int",
                        "黄牌 - int",
                        "角球，-1表示没有角球数据 - int",
                        "加时比分(120分钟，即包括常规时间比分)，加时赛才有 - int",
                        "点球大战比分(不包含常规时间及加时赛比分)，点球大战才有 - int"
                }
        )
        private List<Object> awayScores;
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
         * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","角球   大,盘口,小,是否封盘 - string"]BET365初盘<br/>example：["0.86,-0.5,1.04,0","3.8,3.6,2.05,0","0.9,2.75,1.0,0","0.9,9,0.9,0"]
         */
        @Schema(description = "BET365初盘<br/>example：[\"0.86,-0.5,1.04,0\",\"3.8,3.6,2.05,0\",\"0.9,2.75,1.0,0\",\"0.9,9,0.9,0\"]",
                type = "array",
                allowableValues = {
                        "亚盘   主,盘口,客,是否封盘 - string",
                        "欧盘   胜,平,负,是否封盘   - string",
                        "大小盘 大,盘口,小,是否封盘 - string",
                        "角球   大,盘口,小,是否封盘 - string"
                }
        )
        private List<Object> odds;
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
    }
}
