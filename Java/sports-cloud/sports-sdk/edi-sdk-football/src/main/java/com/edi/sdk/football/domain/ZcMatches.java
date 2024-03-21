package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * 传统足彩项
 */
@Data
public class ZcMatches {
    /**
     * 彩票开售时间
     */
    @JsonProperty(value = "start_time")
    private Integer startTime;
    /**
     * 彩票期号
     */
    private Integer issue;
    /**
     * 彩票开奖时间
     */
    @JsonProperty(value = "draw_time")
    private Integer drawTime;
    /**
     * 彩票停售时间
     */
    @JsonProperty(value = "end_time")
    private Integer endTime;
    /**
     * 赛程列表
     */
    private List<Matches> matches;

    @Data
    public static class Matches {
        /**
         * 比赛结果<br/>sfc：3-胜、1-平、0-负<br/>bqc：半场结果,全场结果<br/>jqc：全场比分，默认："-"
         */
        private String result;
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
         * 比赛时间
         */
        @JsonProperty(value = "match_time")
        private Integer matchTime;
        /**
         * 唯一id
         */
        private Integer id;
        /**
         * 主队名称
         */
        private String home;
    }
}