package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 比赛数据存在信息
 */
@Data
public class MatchDataStataVO implements Serializable {

    @Schema(description = "是否有赛况")
    private Boolean hasStata;
    @Schema(description = "是否有阵容")
    private Boolean hasLineup;
    @Schema(description = "是否有指数")
    private Boolean hasOdds;


}