package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 比赛阵容VO
 *

 */
@Data
public class MatchLineupDetailVO implements Serializable {

    @Schema(description = "主队阵型")
    private String homeFormation;

    @Schema(description = "客队阵型")
    private String awayFormation;

    @Schema(description = "主队球衣颜色")
    private String homeColor;

    @Schema(description = "客队球衣颜色")
    private String awayColor;

    @Schema(description = "正式阵容，1-是、0-不是")
    private Integer confirmed;


    @Schema(description = "主队阵型球员列表")
    private List<TeamLineupVo> home;

    @Schema(description = "客队阵型球员列表")
    private List<TeamLineupVo> away;

    @Schema(description = "裁判名称")
    private String refereeName;

    @Schema(description = "主队logo")
    private String homeLogo;

    @Schema(description = "客队logo")
    private String awayLogo;

    @Schema(description = "主队市值")
    private Integer homeMarketValue;

    @Schema(description = "主队市值单位")
    private String homeMarketValueCurrency;

    @Schema(description = "主队市值")
    private Integer awayMarketValue;

    @Schema(description = "主队市值单位")
    private String awayMarketValueCurrency;
}