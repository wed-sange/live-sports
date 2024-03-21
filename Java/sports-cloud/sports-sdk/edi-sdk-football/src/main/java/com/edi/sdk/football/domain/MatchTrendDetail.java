package com.edi.sdk.football.domain;

import java.util.List;

import lombok.Data;

/**
 * null
 */
@Data
public class MatchTrendDetail {
    /**
     * [["上半场，趋势变化数据 - int"],["下半场，趋势变化数据 - int"]]趋势变化，按分钟数变化<br/>example：[[16, 0, -2], [-16, 0, 1]]
     */
    private List<Object> data;
    /**
     * 半场数
     */
    private Integer count;
    /**
     * 事件列表
     */
    private List<Incidents> incidents;

    @Data
    public static class Incidents {
        /**
         * 时间(分钟)
         */
        private String time;
        /**
         * 事件发生方，1-主队、2-客队
         */
        private Integer position;
        /**
         * 类型，详见状态码->技术统计
         */
        private Integer type;
    }

    /**
     * 半场时长
     */
    private Integer per;
}