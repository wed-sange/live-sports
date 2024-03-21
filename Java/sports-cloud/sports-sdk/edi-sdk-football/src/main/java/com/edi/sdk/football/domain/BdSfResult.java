package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.List;

/**
 * 结果
 */
@Data
public class BdSfResult extends HashMap<String, List<BdSfResult.Num>> {

    @Data
    public static class Num {
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
             * 胜负，3-主胜、0-客胜
             */
            private Sf sf;

            @Data
            public static class Sf {
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
}
