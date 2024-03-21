package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 结果
 */
@Data
public class BdResult {
    /**
     * 赛事名称
     */
    private String comp;
    /**
     * 客队名称
     */
    private String away;
    /**
     * 期号
     */
    private Integer issue;
    /**
     * 主队比分
     */
    @JsonProperty(value = "home_score")
    @Schema(description = "主队比分")
    private Integer homeScore;
    /**
     * 客队比分
     */
    @JsonProperty(value = "away_score")
    @Schema(description = "客队比分")
    private Integer awayScore;
    /**
     * 指数项
     */
    private Odds odds;

    @Data
    public static class Odds {
        /**
         * 比分
         */
        private Bf bf;

        @Data
        public static class Bf {
            /**
             * 全场比分，*表示所有选项
             */
            private String rb1;
            /**
             * sp
             */
            private String sp;
        }

        /**
         * 半全场：3-主胜、1-平局、0-客胜
         */
        private Bqc bqc;

        @Data
        public static class Bqc {
            /**
             * 半场结果，*表示所有选项
             */
            private String rb1;
            /**
             * 全场结果，*表示所有选项
             */
            private String rb2;
            /**
             * sp
             */
            private String sp;
        }

        /**
         * 进球
         */
        private Jq jq;

        @Data
        public static class Jq {
            /**
             * 进球数，*表示所有选项
             */
            private String rb1;
            /**
             * sp
             */
            private String sp;
        }

        /**
         * 胜平负：3-主胜、1-平局、0-客胜
         */
        private Spf spf;

        @Data
        public static class Spf {
            /**
             * 让球
             */
            private String rb1;
            /**
             * 结果，*表示所有选项
             */
            private String rb2;
            /**
             * sp
             */
            private String sp;
        }

        /**
         * 上下单双：4-上、5-下、6-单、7-双
         */
        private Sxp sxp;

        @Data
        public static class Sxp {
            /**
             * 上下，*表示所有选项
             */
            private String rb1;
            /**
             * 单双，*表示所有选项
             */
            private String rb2;
            /**
             * sp
             */
            private String sp;
        }
    }

    /**
     * 序号
     */
    @JsonProperty(value = "issue_num")
    @Schema(description = "序号")
    private Integer issueNum;
    /**
     * 比赛时间
     */
    @JsonProperty(value = "match_time")
    @Schema(description = "比赛时间")
    private Integer matchTime;
    /**
     * 唯一，用于和“体彩关联接口”中的比赛关联
     */
    private Integer id;
    /**
     * 销售状态码<br/>顺序：胜平负,总进球,半全场,上下单双盘,比分<br/>状态码：0-未开售、1-销售中、2-未知状态、3-已停售、4-已开奖
     */
    @JsonProperty(value = "sell_status")
    @Schema(description = "销售状态码<br/>顺序：胜平负,总进球,半全场,上下单双盘,比分<br/>状态码：0-未开售、1-销售中、2-未知状态、3-已停售、4-已开奖")
    private String sellStatus;
    /**
     * 主队名称
     */
    private String home;
}
