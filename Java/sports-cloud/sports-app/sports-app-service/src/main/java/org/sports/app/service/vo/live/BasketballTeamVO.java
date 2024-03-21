package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 篮球球队信息VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketballTeamVO implements Serializable {

    @Schema(description = "球队ID")
    private Integer id;

    @Schema(description = "球队ID")
    private String name;

    @Schema(description = "球队logo")
    private String logo;
}