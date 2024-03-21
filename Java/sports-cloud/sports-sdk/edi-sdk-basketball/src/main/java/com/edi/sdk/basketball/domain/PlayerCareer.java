package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 获取球员职业生涯列表
 * 返回全量球员职业生涯数据，可根据时间戳增量获取新增或变动的数据
 *

 * 2023年5月31日 下午11:19:40
 */
@Data
public class PlayerCareer {
    /**
     * 球员id
     */
    private Integer id;

    /**
     * 球员职业生涯项
     */
    private List<Career> career;

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;


    /**
     * 球员职业生涯项
     */
    @Data
    public static class Career {

        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        @Schema(description = "球队id")
        private Integer teamId;

        /**
         * 赛季年份
         */
        private String seasons;

        /**
         * 时间（年）
         */
        private Integer years;

    }

}
