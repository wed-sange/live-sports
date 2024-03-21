package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 篮球球员统计数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketballPlayerStatsVO implements Serializable {

    @Schema(description = "主队球员id")
    private Integer id;

    @Schema(description = "名称")
    private String name;

    @Schema(description = "球员logo")
    private String logo;

    @Schema(description = "球衣号")
    private String number;

    @Schema(description = "球员数据")
    private BasketballPlayerDataStatsVO data;


}