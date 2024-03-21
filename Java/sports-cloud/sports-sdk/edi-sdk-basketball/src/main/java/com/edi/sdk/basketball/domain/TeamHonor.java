package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 获取球队荣誉列表
 * 返回全量球队荣誉数据（冠军），可根据时间戳增量获取新增或变动的数据
 *

 * 2023年5月31日 下午11:19:40
 */
@Data
public class TeamHonor {
    /**
     * 球队id
     */
    private Integer id;

    /**
     * 球队荣誉列表
     */
    private List<Honors> honors;

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;


    /**
     * 球队荣誉列表
     */
    @Data
    public static class Honors {

        /**
         * 赛季年份
         */
        @JsonProperty(value = "season")
        private String season;

        /**
         * 赛事id
         */
        @JsonProperty(value = "competition_id")
        @Schema(description = "赛事id")
        private Integer competitionId;

        /**
         * 赛季id
         */
        @JsonProperty(value = "season_id")
        @Schema(description = "赛季id")
        private Integer seasonId;


    }

}
