package org.sports.app.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 球队信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamBaseInfoVO implements Serializable {

    @Schema(description = "球队ID")
    private Integer id;

    @Schema(description = "球队名称")
    private String name;

    @Schema(description = "球队logo")
    private String logo;
}