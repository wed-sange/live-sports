package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * null
 */
@Data
public class JcOdds {
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
         * 比分赔率，顺序：1:0,2:0,2:1,3:0,3:1,3:2,4:0,4:1,4:2,5:0,5:1,5:2,胜其他,0:0,1:1,2:2,3:3,平其他,0:1,0:2,1:2,0:3,1:3,2:3,0:4,1:4,2:4,0:5,1:5,2:5,负其他
         */
        private String bf;
        /**
         * 进球总数赔率，顺序：0球,1球,2球,3球,4球,5球,6球,7+球
         */
        private String jq;
        /**
         * 序号
         */
        @JsonProperty(value = "issue_num")
        private String issueNum;
        /**
         * 胜平负赔率，顺序：胜,平,负
         */
        private String spf;
        /**
         * 比赛时间
         */
        @JsonProperty(value = "match_time")
        private Integer matchTime;
        /**
         * 销售状态码<br/>顺序：胜平负,让球,比分,进 球数,半全场<br/>状态码：0-未开售、1-仅过关、2-单关和过关、3-无/停售
         */
        @JsonProperty(value = "sell_status")
        private String sellStatus;
        /**
         * 主队名称
         */
        private String home;
        /**
         * 主队简称
         */
        @JsonProperty(value = "short_home")
        private String shortHome;
        /**
         * 半全场胜平负赔率，顺序：胜胜,胜平,胜负,平胜,平平,平负,负胜,负平,负负
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
        private String shortComp;
        /**
         * 客队简称
         */
        @JsonProperty(value = "short_away")
        private String shortAway;
        /**
         * 让球胜平负赔率，顺序：让球数,胜,平,负
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
         * 销售状态码<br/>顺序：胜负,让分胜负,大小分,胜分差<br/>状态码：0-未开售、1-仅过关、2-单关和过关、3-无/停售
         */
        @JsonProperty(value = "sell_status")
        private String sellStatus;
        /**
         * 主队名称
         */
        private String home;
        /**
         * 胜分差赔率，顺序：主胜1-5,客胜1-5,主胜6-10,客胜6-10,主胜11-15,客胜11-15,主胜16-20,客胜16-20,主胜21-25,客胜21-25,主胜26+,客胜26+
         */
        private String sfc;
        /**
         * 主队简称
         */
        @JsonProperty(value = "short_home")
        private String shortHome;
        /**
         * 胜负赔率，顺序：胜,负
         */
        private String sf;
        /**
         * 大小分赔率，顺序：预设分,大,小
         */
        private String dxf;
        /**
         * 让分胜负赔率，顺序：让分数,胜,负
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
        private String shortComp;
        /**
         * 客队简称
         */
        @JsonProperty(value = "short_away")
        private String shortAway;
    }
}