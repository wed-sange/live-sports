package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 获取国家队FIBA男子排名
 * 返回国家队FIBA男子排名数据，可根据pub_time更新时间查询历史排名数据
 *

 * 2023年5月31日 下午11:19:40
 */
@Data
public class RankingFibaMen {
    /**
     * 排名发布时间列表
     */
    @JsonProperty(value = "pub_times")
    @Schema(description = "排名发布时间列表")
    private List<Integer> pubTimes;

    /**
     * 当前查询的排名发布时间
     */
    @JsonProperty(value = "pub_time")
    @Schema(description = "当前查询的排名发布时间")
    private Integer pubTime;

    /**
     * 球员职业生涯项
     */
    private List<Items> items;


    /**
     * 排名列表
     * fiba排名
     */
    @Data
    public static class Items {

        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        @Schema(description = "球队id")
        private Integer teamId;

        /**
         * 世界排名
         */
        private Integer ranking;

        /**
         * 区域id，2-欧洲、3-美洲、4-亚洲、6-非洲
         */
        @JsonProperty(value = "region_id")
        @Schema(description = "区域id，2-欧洲、3-美洲、4-亚洲、6-非洲")
        private Integer regionId;

        /**
         * 区域排名
         */
        @JsonProperty(value = "region_ranking")
        @Schema(description = "区域排名")
        private Integer regionRanking;

        /**
         * 积分
         */
        @JsonProperty(value = "points")
        private float points;

        /**
         * 世界排名变化
         */
        @JsonProperty(value = "position_changed")
        @Schema(description = "世界排名变化")
        private Integer positionChanged;

    }

}
