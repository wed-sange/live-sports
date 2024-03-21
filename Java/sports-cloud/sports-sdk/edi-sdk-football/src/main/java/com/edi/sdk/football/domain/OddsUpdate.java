package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class OddsUpdate {

    /**
     * 比赛id
     */
    private Integer id;
    /**
     * 指数公司id，详见状态码->指数公司ID
     */
    @JsonProperty(value = "company_id")
    @Schema(description = "指数公司id，详见状态码->指数公司ID")
    private Integer companyId;

    /**
     * 更新修复时间
     */
    @JsonProperty(value = "update_time")
    @Schema(description = "更新修复时间")
    private Integer updateTime;

}