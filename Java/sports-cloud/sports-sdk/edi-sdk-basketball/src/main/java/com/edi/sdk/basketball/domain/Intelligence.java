package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 获取情报
 * 返回全量比赛情报数据，可根据时间戳增量获取新增或变动的数据
 * 2023年5月31日 下午11:19:40
 */
@Data
public class Intelligence {
    /**
     * 比赛id
     */
    private Integer id;
    /**
     * 有利情报
     */
    @JsonProperty(value = "good")
    private Good good;
    /**
     * 不利情报
     */
    @JsonProperty(value = "bad")
    private Bad bad;
    /**
     * 中立情报，不存在为空
     * 中立情报，不存在为空
     * 情报字段说明
     * example：[1, '']
     * Enum:
     * Array[2]
     * 0:"情报重要程度(1-5)，数值越大越重要 - int"
     * 1:"情报内容 - string"
     */
    @JsonProperty(value = "neutral")
    private List<List<Object>> neutral;
    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;

    /**
     * 有利情报
     */
    @Data
    public static class Good {

        /**
         * 主队有利情报，不存在为空
         * 情报字段说明
         * example：[1, '']
         * Enum:
         * Array[2]
         * 0:"情报重要程度(1-5)，数值越大越重要 - int"
         * 1:"情报内容 - string"
         */
        @JsonProperty(value = "home")
        private List<Object> home;

        /**
         * 客队有利情报，不存在为空
         * 情报字段说明
         * example：[1, '']
         * Enum:
         * Array[2]
         * 0:"情报重要程度(1-5)，数值越大越重要 - int"
         * 1:"情报内容 - string"
         */
        @JsonProperty(value = "away")
        private List<Object> away;

    }

    /**
     * 不利情报
     */
    @Data
    public static class Bad {

        /**
         * 主队不利情报，不存在为空
         * 情报字段说明
         * example：[1, '']
         * Enum:
         * Array[2]
         * 0:"情报重要程度(1-5)，数值越大越重要 - int"
         * 1:"情报内容 - string"
         */
        @JsonProperty(value = "home")
        private List<Object> home;

        /**
         * 客队不利情报，不存在为空
         * 情报字段说明
         * example：[1, '']
         * Enum:
         * Array[2]
         * 0:"情报重要程度(1-5)，数值越大越重要 - int"
         * 1:"情报内容 - string"
         */
        @JsonProperty(value = "away")
        private List<Object> away;

    }


}
