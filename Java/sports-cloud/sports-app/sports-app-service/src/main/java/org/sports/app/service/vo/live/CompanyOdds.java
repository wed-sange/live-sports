package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**

 */
@Data
public class CompanyOdds implements Serializable {

    @Schema(description = "胜平负")
    private List<OddsInfoVO> euInfo;

    @Schema(description = "让球")
    private List<OddsInfoVO> asiaInfo;

    @Schema(description = "进球数")
    private List<OddsInfoVO> bsInfo;

    @Schema(description = "角球")
    private List<OddsInfoVO> crInfo;

}