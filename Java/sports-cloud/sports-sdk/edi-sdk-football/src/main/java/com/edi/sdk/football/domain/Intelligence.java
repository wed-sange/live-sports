package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 情报
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
    @Schema(description = "情报字段说明<br/>example：[1, '']",
            type = "array",
            allowableValues = {
                    "情报重要程度(1-5)，数值越大越重要 - int",
                    "情报内容 - string"
            }
    )
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
        @Schema(description = "情报字段说明<br/>example：[1, '']",
                type = "array",
                allowableValues = {
                        "情报重要程度(1-5)，数值越大越重要 - int",
                        "情报内容 - string"
                }
        )
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
        @Schema(description = "情报字段说明<br/>example：[1, '']",
                type = "array",
                allowableValues = {
                        "情报重要程度(1-5)，数值越大越重要 - int",
                        "情报内容 - string"
                }
        )
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
        @Schema(description = "情报字段说明<br/>example：[1, '']",
                type = "array",
                allowableValues = {
                        "情报重要程度(1-5)，数值越大越重要 - int",
                        "情报内容 - string"
                }
        )
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
        @Schema(description = "情报字段说明<br/>example：[1, '']",
                type = "array",
                allowableValues = {
                        "情报重要程度(1-5)，数值越大越重要 - int",
                        "情报内容 - string"
                }
        )
        private List<Object> away;

    }
}
