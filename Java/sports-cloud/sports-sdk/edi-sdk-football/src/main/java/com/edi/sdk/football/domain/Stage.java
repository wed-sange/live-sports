package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 阶段列表
 */
@Data
public class Stage {
    /**
     * 总组数
     */
    @JsonProperty(value = "group_count")
    @Schema(description = "总组数")
    private Integer groupCount;
    /**
     * 阶段id
     */
    private Integer id;
    /**
     * 比赛模式，1-积分赛、2-淘汰赛
     */
    private Integer mode;
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
     * 粤语名称
     */
    @JsonProperty(value = "name_zht")
    @Schema(description = "粤语名称")
    private String nameZht;
    /**
     * 排序，阶段的先后顺序
     */
    private Integer order;
    /**
     * 总轮数
     */
    @JsonProperty(value = "round_count")
    @Schema(description = "总轮数")
    private Integer roundCount;
    /**
     * 赛季id
     */
    @JsonProperty(value = "season_id")
    @Schema(description = "赛季id")
    private Integer seasonId;
    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;
}