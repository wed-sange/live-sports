package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class StatsVO implements Serializable {
    @Schema(description = "客队值")
    private Integer away;

    @Schema(description = "技术统计类型：<br/>1-进球<br/>2-角球<br/>3-黄牌<br/>4-红牌<br/>5-越位<br/>6-任意球<br/>7-球门球<br/>8-点球<br/>9-换人<br/>10-比赛开始<br/>11-中场<br/>12-结束<br/>13-半场比分<br/>15-两黄变红<br/>16-点球未进<br/>17-乌龙球<br/>18-助攻<br/>19-伤停补时<br/>21-射正<br/>22-射偏<br/>23-进攻<br/>24-危险进攻<br/>25-控球率<br/>26-加时赛结束<br/>27-点球大战结束<br/>28-VAR(视频助理裁判)<br/>29-点球(点球大战)<br/>30-点球未进(点球大战)")
    private Integer type;

    @Schema(description = "主队值")
    private Integer home;
}