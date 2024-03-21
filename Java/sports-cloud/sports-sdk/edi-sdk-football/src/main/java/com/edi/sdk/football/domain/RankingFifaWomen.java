package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * null
 */
@Data
public class RankingFifaWomen {
    /**
     * 当前查询的排名发布时间
     */
    @JsonProperty(value = "pub_time")
    @Schema(description = "排名发布时间列表<br/>example：[1582128000]",
            type = "array",
            allowableValues = {
                    "发布时间（时间戳格式） - int"
            }
    )
    private Integer pubTime;
    /**
     * ["发布时间（时间戳格式） - int"]排名发布时间列表<br/>example：[1582128000]
     */
    @JsonProperty(value = "pub_times")
    @Schema(description = "当前查询的排名发布时间")
    private List<Integer> pubTimes;
    /**
     * 排名列表
     */
    private List<Items> items;

    @Data
    public static class Items {
        /**
         * 排名变化
         */
        @JsonProperty(value = "position_changed")
        @Schema(description = "排名变化")
        private Integer positionChanged;
        /**
         * 区域id，1-欧洲足联、2-南美洲足联、3-中北美洲及加勒比海足协、4-非洲足联、5-亚洲足联、6-大洋洲足联
         */
        @JsonProperty(value = "region_id")
        @Schema(description = "区域id，1-欧洲足联、2-南美洲足联、3-中北美洲及加勒比海足协、4-非洲足联、5-亚洲足联、6-大洋洲足联")
        private Integer regionId;
        /**
         * 排名
         */
        private Integer ranking;
        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        @Schema(description = "球队id")
        private Integer teamId;
        /**
         * 上次积分
         */
        @JsonProperty(value = "previous_points")
        @Schema(description = "上次积分")
        private Integer previousPoints;
        /**
         * 积分
         */
        private Integer points;
    }
}
