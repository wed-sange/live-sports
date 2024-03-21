package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 获取球队阵容列表
 * 返回全量球队球员阵容数据，可根据时间戳增量获取新增或变动的数据
 *

 * 2023年5月31日 下午11:19:40
 */
@Data
public class TeamSquad {
    /**
     * 球队id
     */
    private Integer id;

    /**
     * 球队阵容
     */
    private List<Squad> squad;

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;

    /**
     * 球队阵容
     */
    @Data
    public static class Squad {

        /**
         * 球队id
         */
        @JsonProperty(value = "player_id")
        @Schema(description = "球队id")
        private Integer playerId;

        /**
         * 球员位置，C-中锋、SF-小前锋、PF-大前锋、SG-得分后卫、PG-组织后卫、F-前锋、G-后卫，其它都为未知
         */
        @JsonProperty(value = "position")
        private String position;
        /**
         * 球衣号
         */
        @JsonProperty(value = "shirt_number")
        @Schema(description = "球衣号")
        private String shirtNumber;



    }

}
