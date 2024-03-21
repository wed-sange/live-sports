package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 教练列表
 */
@Data
public class Coach {
    /**
     * 年龄
     */
    private Integer age;
    /**
     * 生日（0-未知）
     */
    private Integer birthday;
    /**
     * 合同到期时间
     */
    @JsonProperty(value = "contract_until")
    @Schema(description = "合同到期时间")
    private Integer contractUntil;
    /**
     * 国家id
     */
    @JsonProperty(value = "country_id")
    @Schema(description = "国家id")
    private Integer countryId;
    /**
     * 教练id
     */
    private Integer id;
    /**
     * 加盟时间
     */
    private Integer joined;
    /**
     * 教练logo
     */
    private String logo;
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
     * 国籍
     */
    private String nationality;
    /**
     * 习惯的阵型
     */
    @JsonProperty(value = "preferred_formation")
    @Schema(description = "习惯的阵型")
    private String preferredFormation;
    /**
     * 执教球队id
     */
    @JsonProperty(value = "team_id")
    @Schema(description = "执教球队id")
    private Integer teamId;
    /**
     * 更新时间
     */
    @JsonProperty(value = "updated_at")
    @Schema(description = "更新时间")
    private Integer updatedAt;
}