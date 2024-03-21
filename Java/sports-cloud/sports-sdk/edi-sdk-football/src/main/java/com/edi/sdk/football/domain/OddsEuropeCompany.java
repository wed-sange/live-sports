package com.edi.sdk.football.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 百欧公司
 */
@Data
public class OddsEuropeCompany {
    /**
     * 中文名称
     */
    @JsonProperty(value = "name_zh")
    @Schema(description = "中文名称")
    private String nameZh;
    /**
     * 公司id
     */
    private Integer id;
    /**
     * 英文名称
     */
    @JsonProperty(value = "name_en")
    @Schema(description = "英文名称")
    private String nameEn;
}