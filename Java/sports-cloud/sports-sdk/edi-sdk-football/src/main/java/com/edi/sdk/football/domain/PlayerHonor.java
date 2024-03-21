package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 球员荣誉列表
 */
@Data
public class PlayerHonor {

    /**
     * 球员id
     */
    private Integer id;
    /**
     * 球员荣誉项
     */
    private List<Honors> honors;

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;

    @Data
    public static class Honors {
        /**
         * 荣誉id
         */
        @JsonProperty(value = "honor_id")
        @Schema(description = "荣誉id")
        private Integer honorId;
        /**
         * 赛事id
         */
        @JsonProperty(value = "competition_id")
        @Schema(description = "赛事id")
        private Integer competitionId;
        /**
         * 赛季年份
         */
        private String season;
        /**
         * 赛季id
         */
        @JsonProperty(value = "season_id")
        @Schema(description = "赛季id")
        private Integer seasonId;
        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        @Schema(description = "球队id")
        private Integer teamId;
        /**
         * 国家id（国家足球先生荣誉颁发的国家），有数据存在
         */
        @JsonProperty(value = "country_id")
        @Schema(description = "国家id（国家足球先生荣誉颁发的国家），有数据存在")
        private Integer countryId;
    }
}