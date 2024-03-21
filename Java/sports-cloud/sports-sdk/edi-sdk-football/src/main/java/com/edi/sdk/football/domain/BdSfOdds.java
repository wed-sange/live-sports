package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 指数
 */
@Data
public class BdSfOdds {
    /**
     * 球类id，1-足球、2-篮球
     */
    @JsonProperty(value = "sport_id")
    private Integer sportId;
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
         * 胜负
         */
        private Sf sf;

        @Data
        public static class Sf {
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
     * 销售状态码，0-未开售、1-销售中、2-未知状态、3-已停售、4-已开奖
     */
    @JsonProperty(value = "sell_status")
    private String sellStatus;
    /**
     * 主队名称
     */
    private String home;
}