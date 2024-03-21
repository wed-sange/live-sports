package org.sports.admin.manage.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * json信息中
 */
@Data
@Schema(description = "国家")
public class CountryDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "中文")
    private String cn;

    @Schema(description = "英文")
    private String en;

    @Schema(description = "全称")
    private String full;
    @Schema(description = "简称")
    private String shortName;
    @Schema(description = "电话区号")
    private String dialingCode;

    /**
     * 图标
     */
    private String icon;

}
