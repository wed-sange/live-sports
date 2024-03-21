package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 篮球得分统计数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketballScoreStatsVO implements Serializable {

    @Schema(description = "主队球员数据")
    private List<BasketballPlayerStatsVO> home;

    @Schema(description = "客队球员数据")
    private List<BasketballPlayerStatsVO> away;


}