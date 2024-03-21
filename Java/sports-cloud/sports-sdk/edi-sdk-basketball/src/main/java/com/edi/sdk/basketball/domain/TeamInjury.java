package com.edi.sdk.basketball.domain;


import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 获取球队伤停列表
 * 返回全量球队的球员伤停数据，可根据时间戳增量获取新增或变动的数据
 *

 * 2023年5月31日 下午11:19:40
 */
@Data
public class TeamInjury {
    /**
     * 球队id
     */
    private Integer id;

    /**
     * 球队伤停列表
     */
    private List<Injury> injury;

    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;

    /**
     * 球队伤停列表
     */
    @Data
    public static class Injury {

        /**
         * 球员id
         */
        @JsonProperty(value = "player_id")
        @Schema(description = "球员id")
        private Integer playerId;

        /**
         * 影响赛事id
         */
        @JsonProperty(value = "competition_id")
        @Schema(description = "影响赛事id")
        private Integer competitionId;

        /**
         * 类型，1.受伤、2.停赛、3.缺席、0-未知
         */
        @JsonProperty(value = "type")
        private Integer type;

        /**
         * 状态，1.无法出场、2.赛季报销、3.每日观察、0-未知
         */
        @JsonProperty(value = "status")
        private Integer status;

        /**
         * 受伤原因
         */
        @JsonProperty(value = "reason")
        private String reason;

        /**
         * 开始时间
         */
        @JsonProperty(value = "start_time")
        @Schema(description = "开始时间")
        private Integer startTime;

        /**
         * 结束时间
         */
        @JsonProperty(value = "end_time")
        @Schema(description = "结束时间")
        private Integer endTime;

        /**
         * 缺失比赛场次
         */
        @JsonProperty(value = "missed_matches")
        @Schema(description = "缺失比赛场次")
        private Integer missedMatches;

    }

}
