package com.edi.sdk.basketball.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 比赛分析数据
 */
@Data
public class MatchAnalysis {
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
             * 主队id
             */
            @JsonProperty(value = "home_team_id")
            private Integer homeTeamId;
            /**
             * 类型id，1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无
             */
            private Integer kind;
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
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "home_scores")
            private List<Object> homeScores;
            /**
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "away_scores")
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","兼容忽略"]BET365初盘<br/>example：["0.83,3.5,0.83,0","1.55,0.0,2.3,0","0.83,157.5,0.83,0",""]
             */
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
            /**
             * 比赛总节数(不包含加时)
             */
            @JsonProperty(value = "period_count")
            private Integer periodCount;
        }

        /**
         * 交锋赛程（格式同 info比赛信息字段）
         */
        private List<Vs> vs;

        @Data
        public static class Vs {
            /**
             * 主队id
             */
            @JsonProperty(value = "home_team_id")
            private Integer homeTeamId;
            /**
             * 类型id，1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无
             */
            private Integer kind;
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
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "home_scores")
            private List<Object> homeScores;
            /**
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "away_scores")
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","兼容忽略"]BET365初盘<br/>example：["0.83,3.5,0.83,0","1.55,0.0,2.3,0","0.83,157.5,0.83,0",""]
             */
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
            /**
             * 比赛总节数(不包含加时)
             */
            @JsonProperty(value = "period_count")
            private Integer periodCount;
        }

        /**
         * 主队近期赛程（格式同 info比赛信息字段）
         */
        private List<Home> home;

        @Data
        public static class Home {
            /**
             * 主队id
             */
            @JsonProperty(value = "home_team_id")
            private Integer homeTeamId;
            /**
             * 类型id，1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无
             */
            private Integer kind;
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
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "home_scores")
            private List<Object> homeScores;
            /**
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "away_scores")
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","兼容忽略"]BET365初盘<br/>example：["0.83,3.5,0.83,0","1.55,0.0,2.3,0","0.83,157.5,0.83,0",""]
             */
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
            /**
             * 比赛总节数(不包含加时)
             */
            @JsonProperty(value = "period_count")
            private Integer periodCount;
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
             * 主队id
             */
            @JsonProperty(value = "home_team_id")
            private Integer homeTeamId;
            /**
             * 类型id，1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无
             */
            private Integer kind;
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
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "home_scores")
            private List<Object> homeScores;
            /**
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "away_scores")
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","兼容忽略"]BET365初盘<br/>example：["0.83,3.5,0.83,0","1.55,0.0,2.3,0","0.83,157.5,0.83,0",""]
             */
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
            /**
             * 比赛总节数(不包含加时)
             */
            @JsonProperty(value = "period_count")
            private Integer periodCount;
        }

        /**
         * 历史交锋（格式同 info比赛信息字段）
         */
        private List<Vs> vs;

        @Data
        public static class Vs {
            /**
             * 主队id
             */
            @JsonProperty(value = "home_team_id")
            private Integer homeTeamId;
            /**
             * 类型id，1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无
             */
            private Integer kind;
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
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "home_scores")
            private List<Object> homeScores;
            /**
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "away_scores")
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","兼容忽略"]BET365初盘<br/>example：["0.83,3.5,0.83,0","1.55,0.0,2.3,0","0.83,157.5,0.83,0",""]
             */
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
            /**
             * 比赛总节数(不包含加时)
             */
            @JsonProperty(value = "period_count")
            private Integer periodCount;
        }

        /**
         * 主队近期战绩（格式同 info比赛信息字段）
         */
        private List<Home> home;

        @Data
        public static class Home {
            /**
             * 主队id
             */
            @JsonProperty(value = "home_team_id")
            private Integer homeTeamId;
            /**
             * 类型id，1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无
             */
            private Integer kind;
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
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "home_scores")
            private List<Object> homeScores;
            /**
             * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
             */
            @JsonProperty(value = "away_scores")
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
             * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","兼容忽略"]BET365初盘<br/>example：["0.83,3.5,0.83,0","1.55,0.0,2.3,0","0.83,157.5,0.83,0",""]
             */
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
            /**
             * 比赛总节数(不包含加时)
             */
            @JsonProperty(value = "period_count")
            private Integer periodCount;
        }
    }

    /**
     *
     */
    private Info info;

    @Data
    public static class Info {
        /**
         * 主队id
         */
        @JsonProperty(value = "home_team_id")
        private Integer homeTeamId;
        /**
         * 类型id，1-常规赛、2-季后赛、3-季前赛、4-全明星、5-杯赛、6-附加赛、0-无
         */
        private Integer kind;
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
         * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
         */
        @JsonProperty(value = "home_scores")
        private List<Object> homeScores;
        /**
         * ["第1节分数 - int","第2节分数 - int","第3节分数 - int","第4节分数 - int","加时分数 - int"]比分字段说明<br/>example：[0, 0, 0, 0, 0]
         */
        @JsonProperty(value = "away_scores")
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
         * ["亚盘   主,盘口,客,是否封盘 - string","欧盘   胜,平,负,是否封盘   - string","大小盘 大,盘口,小,是否封盘 - string","兼容忽略"]BET365初盘<br/>example：["0.83,3.5,0.83,0","1.55,0.0,2.3,0","0.83,157.5,0.83,0",""]
         */
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
        /**
         * 比赛总节数(不包含加时)
         */
        @JsonProperty(value = "period_count")
        private Integer periodCount;
    }
}