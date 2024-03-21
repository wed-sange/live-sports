package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**

 */
@Data
public class OddsInfoVO implements Serializable {

    @Schema(description = "公司ID")
    private String companyId;

    @Schema(description = "公司名称")
    private String companyName;

    @Schema(description = "初盘-主胜")
    private String firstHomeWin;

    @Schema(description = "初盘-客胜")
    private String firstAwayWin;

    @Schema(description = "初盘-平/盘口")
    private String firstDraw;


    @Schema(description = "初盘-赔付率")
    private String firstLossRatio;

    @Schema(description = "即盘-主胜")
    private String currentHomeWin;

    @Schema(description = "即盘-客胜")
    private String currentAwayWin;

    @Schema(description = "即盘-平/盘口")
    private String currentDraw;

    @Schema(description = "即盘-赔付率")
    private String currentLossRatio;

    @Schema(description = "比赛状态：1-未开赛")
    private Integer status;

    @Schema(description = "是否封盘：0-否，1-是")
    private Integer close;
}