package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 获取赛季轮次最佳阵容列表
 */
@Data
public class SeasonBestLineup {

    /**
     * 最佳阵容id
     */
    private Integer id;
    /**
     * 阵型
     */
    private String formation;
    /**
     * 英文名称
     */
    @JsonProperty(value = "name_en")
    @Schema(description = "英文名称")
    private String nameEn;
    /**
     * 中文名称
     */
    @JsonProperty(value = "name_zh")
    @Schema(description = "中文名称")
    private String nameZh;
    /**
     * 数据发布时间
     */
    @JsonProperty(value = "update_time")
    @Schema(description = "数据发布时间")
    private Integer updateTime;
    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;
    /**
     * 赛事id
     */
    @JsonProperty(value = "competition_id")
    @Schema(description = "赛事id")
    private Integer competitionId;
    /**
     * 阶段id
     */
    @JsonProperty(value = "stage_id")
    @Schema(description = "阶段id")
    private Integer stageId;
    /**
     * 赛季id
     */
    @JsonProperty(value = "season_id")
    @Schema(description = "赛季id")
    private Integer seasonId;
    /**
     * 阵容球员列表
     */
    private List<Details> details;

    @Data
    public static class Details {
        /**
         * 球员id
         */
        @JsonProperty(value = "player_id")
        @Schema(description = "球员id")
        private Integer playerId;
        /**
         * x坐标，总共100
         */
        @JsonProperty(value = "location_x")
        @Schema(description = "x坐标，总共100")
        private Integer locationX;
        /**
         * 评分，10为满分，为了避免浮点数影响，x100倍存储为整数；eg：计算评分为(760/100=7.60)
         */
        private Integer rating;
        /**
         * y坐标，总共100
         */
        @JsonProperty(value = "location_y")
        @Schema(description = "y坐标，总共100")
        private Integer locationY;
        /**
         * 球队id
         */
        @JsonProperty(value = "team_id")
        @Schema(description = "球队id")
        private Integer teamId;
        /**
         * 位置，F-前锋、M-中场、D-后卫、G-守门员、其他为未知
         */
        private String position;
    }
}