package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 篮球统计数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketballStatsVO implements Serializable {

//    @Schema(description = "技术统计类型，类型码：<br/>1-3分球进球数<br/>2-2分球进球数<br/>3-罚球进球数<br/>4-剩余暂停数<br/>5-犯规数<br/>6-罚球命中率<br/>7-总暂停数")
//    private Integer type;

    @Schema(description = "主队数值")
    private BasketballTeamDataStatsVO home;

    @Schema(description = "客队数值")
    private BasketballTeamDataStatsVO away;
}