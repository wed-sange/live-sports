package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 球队阵容列表
 */
@Data
public class TeamSquad {

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;
    /**
     * 球队id
     */
    private Integer id;

    /**
     * 球队阵容列表
     */
    private List<Squad> squad;

    @Data
    public static class Squad {
        /**
         * 是否有球衣号，1-是、0-否
         */
        @JsonProperty(value = "has_shirt_number")
        @Schema(description = "是否有球衣号，1-是、0-否")
        private Integer hasShirtNumber;
        /**
         * 球员id
         */
        @JsonProperty(value = "player_id")
        @Schema(description = "球员id")
        private Integer playerId;
        /**
         * 球员位置，F-前锋、M-中场、D-后卫、G-守门员、其他为未知
         */
        private String position;
        /**
         * 球衣号
         */
        @JsonProperty(value = "shirt_number")
        @Schema(description = "球衣号")
        private Integer shirtNumber;
    }
}