package org.sports.app.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 指数历史查询 Request
 *

 */
@Data
public class OddsHistoryRequest {
    @Schema(description = "比赛ID")
    @NotBlank(message = "比赛ID不能为空")
    private String matchId;

    @Schema(description = "公司ID")
    @NotBlank(message = "公司ID不能为空")
    private String companyId;

    @Schema(description = "指数类型：胜平负：en；让球：asia；进球数：bs；角球：cr")
    @NotBlank(message = "指数类型不能为空")
    private String oddsType;
}
