package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 篮球得分数据VO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketballScoreVO implements Serializable {

    @Schema(description = "比赛状态，状态码：<br/>0-比赛异常，说明：暂未判断具体原因的异常比赛，可能但不限于：腰斩、取消等等，建议隐藏处理<br/>1-未开赛<br/>2-第一节<br/>3-第一节完<br/>4-第二节<br/>5-第二节完<br/>6-第三节<br/>7-第三节完<br/>8-第四节<br/>9-加时<br/>10-完场<br/>11-中断<br/>12-取消<br/>13-延期<br/>14-腰斩<br/>15-待定")
    private Integer Status;

    @Schema(description = "小节剩余时间(秒)")
    private Integer timeRemaining;

    @Schema(description = "主队每节得分集合，<br/>0:主队第1节分数<br/>1:主队第2节分数<br/>2:主队第3节分数<br/>3:主队第4节分数<br/>4:主队加时分数")
    private List<Integer> homeScoreList;

    @Schema(description = "客队每节得分集合，<br/>0:客队第1节分数<br/>1:客队第2节分数<br/>2:客队第3节分数<br/>3:客队第4节分数<br/>4:客队加时分数")
    private List<Integer> awayScoreList;

    @Schema(description = "主队加时每节得分集合，0:主队第1节加时分数<br/> 1:主队第2节加时分数<br/> 2:主队第n节加时分数")
    private List<Integer> homeOverTimeScoresList;

    @Schema(description = "客队加时每节得分集合，0:客队第1节加时分数<br/> 1:客队第2节加时分数<br/> 2:客队第n节加时分数")
    private List<Integer> awayOverTimeScoresList;


}