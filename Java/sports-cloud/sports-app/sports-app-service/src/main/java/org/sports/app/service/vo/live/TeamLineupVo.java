package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 球队阵容

 */

@Data
public class TeamLineupVo implements Serializable {
    @Schema(description = "球员logo(国家队)")
    private String nationalLogo;

    @Schema(description = "评分，10为满分")
    private String rating;

    @Schema(description = "球队id")

    private Integer teamId;
    @Schema(description = "是否队长，1-是、0-否")

    private Integer captain;

    @Schema(description = "球衣号")
    private Integer shirtNumber;

    @Schema(description = "球员名称")
    private String name;

    @Schema(description = "阵容x坐标，总共100")
    private Integer x;

    @Schema(description = "球员logo")
    private String logo;

    @Schema(description = "阵容y坐标，总共100")
    private Integer y;

    @Schema(description = "球员id")
    private Integer id;

    @Schema(description = "球员位置，F前锋、M中场、D后卫、G守门员、其他为未知")
    private String position;

    @Schema(description = "是否首发，1-是、0-否")
    private Integer first;
}