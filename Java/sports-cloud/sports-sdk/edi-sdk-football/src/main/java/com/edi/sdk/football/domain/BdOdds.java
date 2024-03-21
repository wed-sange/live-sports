package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 指数
 */
@Data
public class BdOdds {
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
             * 3：1 sp
             */
            private String s31;
            /**
             * 3：0 sp
             */
            private String s30;
            /**
             * 1：1 sp
             */
            private String s11;
            /**
             * 3：3 sp
             */
            private String s33;
            /**
             * 1：0 sp
             */
            private String s10;
            /**
             * 3：2 sp
             */
            private String s32;
            /**
             * 1：3 sp
             */
            private String s13;
            /**
             * 1：2 sp
             */
            private String s12;
            /**
             * 1：4 sp
             */
            private String s14;
            /**
             * 负其他sp
             */
            private String sl;
            /**
             * 平其他sp
             */
            private String sp;
            /**
             * 胜其他sp
             */
            private String sw;
            /**
             * 4：0 sp
             */
            private String s40;
            /**
             * 2：0 sp
             */
            private String s20;
            /**
             * 4：2 sp
             */
            private String s42;
            /**
             * 4：1 sp
             */
            private String s41;
            /**
             * 0：0 sp
             */
            private String s00;
            /**
             * 2：2 sp
             */
            private String s22;
            /**
             * 2：1 sp
             */
            private String s21;
            /**
             * 0：2 sp
             */
            private String s02;
            /**
             * 2：4 sp
             */
            private String s24;
            /**
             * 0：1 sp
             */
            private String s01;
            /**
             * 2：3 sp
             */
            private String s23;
            /**
             * 0：4 sp
             */
            private String s04;
            /**
             * 0：3 sp
             */
            private String s03;
        }

        /**
         * 半全场
         */
        private Bqc bqc;

        @Data
        public static class Bqc {
            /**
             * 胜负sp
             */
            private String l30;
            /**
             * 平负sp
             */
            private String l10;
            /**
             * 胜平sp
             */
            private String l31;
            /**
             * 负平sp
             */
            private String l01;
            /**
             * 胜胜sp
             */
            private String l33;
            /**
             * 平平sp
             */
            private String l11;
            /**
             * 负负sp
             */
            private String l00;
            /**
             * 负胜sp
             */
            private String l03;
            /**
             * 平胜sp
             */
            private String l13;
        }

        /**
         * 进球
         */
        private Jq jq;

        @Data
        public static class Jq {
            /**
             * 零球sp
             */
            private String j0;
            /**
             * 一球sp
             */
            private String j1;
            /**
             * 二球sp
             */
            private String j2;
            /**
             * 三球sp
             */
            private String j3;
            /**
             * 四球sp
             */
            private String j4;
            /**
             * 五球sp
             */
            private String j5;
            /**
             * 六球sp
             */
            private String j6;
            /**
             * 七球+sp
             */
            private String j7;
        }

        /**
         * 胜平负
         */
        private Spf spf;

        @Data
        public static class Spf {
            /**
             * 主胜sp
             */
            private String sf3;
            /**
             * 让球
             */
            private String goal;
            /**
             * 客胜sp
             */
            private String sf0;
            /**
             * 平局sp
             */
            private String sf1;
        }

        /**
         * 上下单双
         */
        private Sxp sxp;

        @Data
        public static class Sxp {
            /**
             * 下单sp
             */
            private String sx01;
            /**
             * 上双sp
             */
            private String sx10;
            /**
             * 上单sp
             */
            private String sx11;
            /**
             * 下双sp
             */
            private String sx00;
        }
    }

    /**
     * 序号
     */
    @JsonProperty(value = "issue_num")
    private Integer issueNum;
    /**
     * 比赛时间
     */
    @JsonProperty(value = "match_time")
    private Integer matchTime;
    /**
     * 唯一，用于和“体彩关联接口”中的比赛关联
     */
    private Integer id;
    /**
     * 销售状态码<br/>顺序：胜平负,总进球,半全场,上下单双盘,比分<br/>状态码：0-未开售、1-销售中、2-未知状态、3-已停售、4-已开奖
     */
    @JsonProperty(value = "sell_status")
    private String sellStatus;
    /**
     * 主队名称
     */
    private String home;
}