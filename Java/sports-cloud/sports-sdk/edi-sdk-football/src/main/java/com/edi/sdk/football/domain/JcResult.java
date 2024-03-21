package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * null
 */
@Data
public class JcResult {
    /**
     * 足球项
     */
    private List<Jczq> jczq;

    @Data
    public static class Jczq {
        /**
         * 赛事名称
         */
        private String comp;
        /**
         * 客队名称
         */
        private String away;
        /**
         * 比分，顺序：结果,赔率
         */
        private String bf;
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
         * 进球，顺序：结果,赔率
         */
        private String jq;
        /**
         * 序号
         */
        @JsonProperty(value = "issue_num")
        @Schema(description = "序号")
        private String issueNum;
        /**
         * 胜平负，顺序：结果,赔率；<br/>结果：3-主胜、1-平、0-客胜
         */
        private String spf;
        /**
         * 比赛时间
         */
        @JsonProperty(value = "match_time")
        @Schema(description = "比赛时间")
        private Integer matchTime;
        /**
         * 客队半场比分
         */
        @JsonProperty(value = "half_away_score")
        @Schema(description = "客队半场比分")
        private Integer halfAwayScore;
        /**
         * 主队半场比分
         */
        @JsonProperty(value = "half_home_score")
        @Schema(description = "主队半场比分")
        private Integer halfHomeScore;
        /**
         * 主队名称
         */
        private String home;
        /**
         * 主队简称
         */
        @JsonProperty(value = "short_home")
        @Schema(description = "主队简称")
        private String shortHome;
        /**
         * 半全场，顺序：半场结果,全场结果,赔率；<br/>结果：3-主胜、1-平、0-客胜
         */
        private String bqc;
        /**
         * 唯一，用于和“体彩关联接口”中的比赛关联
         */
        private Integer id;
        /**
         * 赛事简称
         */
        @JsonProperty(value = "short_comp")
        @Schema(description = "赛事简称")
        private String shortComp;
        /**
         * 客队简称
         */
        @JsonProperty(value = "short_away")
        @Schema(description = "客队简称")
        private String shortAway;
        /**
         * 让球胜平负，顺序：让球,结果,赔率；<br/>结果：3-主胜、1-平、0-客胜
         */
        private String rq;
    }

    /**
     * 篮球项
     */
    private List<Jclq> jclq;

    @Data
    public static class Jclq {
        /**
         * 赛事名称
         */
        private String comp;
        /**
         * 客队名称
         */
        private String away;
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
         * 序号
         */
        @JsonProperty(value = "issue_num")
        @Schema(description = "序号")
        private String issueNum;
        /**
         * 比赛时间
         */
        @JsonProperty(value = "match_time")
        @Schema(description = "比赛时间")
        private Integer matchTime;
        /**
         * 主队名称
         */
        private String home;
        /**
         * 胜分差，顺序：结果,赔率；<br/>结果1-6：客胜1-5、6-10、11-15、16-20、21-25、26+；<br/>结果7-12：主胜1-5、6-10、11-15、16-20、21-25、26+
         */
        private String sfc;
        /**
         * 主队简称
         */
        @JsonProperty(value = "short_home")
        @Schema(description = "主队简称")
        private String shortHome;
        /**
         * 胜负，顺序：结果,赔率；<br/>结果：3-主胜、0-客胜
         */
        private String sf;
        /**
         * 大小分，顺序：大小分,结果,赔率；<br/>结果：1-大分、0-小分
         */
        private String dxf;
        /**
         * 让分胜负，顺序：让分,结果,赔率；<br/>结果：3-主胜、0-客胜
         */
        private String rf;
        /**
         * 唯一，用于和“体彩关联接口”中的比赛关联
         */
        private Integer id;
        /**
         * 赛事简称
         */
        @JsonProperty(value = "short_comp")
        @Schema(description = "赛事简称")
        private String shortComp;
        /**
         * 客队简称
         */
        @JsonProperty(value = "short_away")
        @Schema(description = "客队简称")
        private String shortAway;
    }
}
