package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 获取球员荣誉列表
 * 返回全量球员荣誉数据，可根据时间戳增量获取新增或变动的数据
 *

 * 2023年5月31日 下午11:19:40
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


    /**
     * 球员荣誉
     */
    @Data
    public static class Honors {

        /**
         * 荣誉id
         */
        @JsonProperty(value = "honor_id")
        @Schema(description = "荣誉id")
        private Integer honorId;

        /**
         * 赛季年份
         */
        private String season;

        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        @Schema(description = "球队id")
        private Integer team_id;

        /**
         * 赛事id
         */
        @JsonProperty(value = "competition_id")
        @Schema(description = "赛事id")
        private Integer competitionId;
    }

}
