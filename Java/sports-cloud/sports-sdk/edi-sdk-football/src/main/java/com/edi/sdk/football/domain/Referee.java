package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 裁判列表
 */
@Data
public class Referee {
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 生日（0-未知）
     */
    private Integer birthday;
    /**
     * 裁判id
     */
    private Integer id;
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
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;
}