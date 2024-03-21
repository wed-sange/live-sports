package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class IncidentsVO implements Serializable {
    @Schema(description = "事件相关球员id，可能不存在")
    private Integer playerId;

    @Schema(description = "事件相关球员名称，可能不存在")
    private String playerName;

    @Schema(description = "助攻球员1ID")
    private Integer assist1Id;

    @Schema(description = "助攻球员1名称")
    private String assist1Name;

    @Schema(description = "助攻球员2ID")
    private Integer assist2Id;

    @Schema(description = "助攻球员2名称")
    private String assist2Name;

    @Schema(description = "换上球员ID")
    private Integer inPlayerId;

    @Schema(description = "换上球员名称")
    private String inPlayerName;

    @Schema(description = "换下球员ID")
    private Integer outPlayerId;

    @Schema(description = "换下球员名称")
    private String outPlayerName;

    @Schema(description = "主队比分（进球、未进球 事件存在）")
    private Integer homeScore;

    @Schema(description = "红黄牌、换人事件原因<br/>1-犯规<br/>2-个人犯规<br/>3-侵犯对手/受伤换人<br/>4-战术犯规/战术换人<br/>5-进攻犯规" + "6-无球犯规<br/>7-持续犯规<br/>8-持续侵犯<br/>9-暴力行为<br/>10-危险动作<br/>11-手球犯规<br/>12-严重犯规<br/>13-故意犯规（防守球员为最后一名防守人时）<br/>14-阻挡进球机会<br/>15-拖延时间<br/>16-视频回看裁定<br/>17-判罚取消<br/>18-争论<br/>19-对判罚表达异议<br/>20-犯规和攻击言语<br/>21-过度庆祝<br/>22-没有回退到要求的距离<br/>23-打架<br/>24-辅助判罚<br/>25-替补席<br/>26-赛后行为<br/>27-其他原因<br/>28-未被允许进入场地<br/>29-进入比赛场地<br/>30-离开比赛赛场<br/>31-非体育道德行为<br/>32-非主观意愿的恶意犯规<br/>33-冒充或顶替（指球员与球衣不是同一人上场比赛）<br/>34-干预var复审<br/>35-进入裁判评审区<br/>36-吐口水（向球员或裁判）<br/>37-病毒<br/>0-未知")
    private Integer reasonType;

    @Schema(description = "客队比分（进球、未进球 事件存在）")
    private Integer awayScore;

    @Schema(description = "事件发生方，0-中立、1-主队、2-客队")
    private Integer position;

    @Schema(description = "时间(分钟)")
    private Integer time;

    @Schema(description = "技术统计类型：<br/>1-进球<br/>2-角球<br/>3-黄牌<br/>4-红牌<br/>5-越位<br/>6-任意球<br/>7-球门球<br/>8-点球<br/>9-换人<br/>10-比赛开始<br/>11-中场<br/>12-结束<br/>13-半场比分<br/>15-两黄变红<br/>16-点球未进<br/>17-乌龙球<br/>18-助攻<br/>19-伤停补时<br/>21-射正<br/>22-射偏<br/>23-进攻<br/>24-危险进攻<br/>25-控球率<br/>26-加时赛结束<br/>27-点球大战结束<br/>28-VAR(视频助理裁判)<br/>29-点球(点球大战)<br/>30-点球未进(点球大战)")
    private Integer type;

    @Schema(description = "VAR结果（VAR事件存在）<br/>1-进球有效<br/>2-进球无效<br/>3-点球有效<br/>4-点球取消<br/>5-红牌有效<br/>6-红牌取消<br/>7-出牌处罚核实<br/>8-出牌处罚更改<br/>9-维持原判<br/>10-判罚更改<br/>0-未知")
    private Integer varResult;

    @Schema(description = "时间")
    private Integer second;

    @Schema(description = "VAR原因（VAR事件存在）<br/>1-进球判定<br/>2-进球未判定<br/>3-点球判定<br/>4-点球未判定<br/>5-红牌判定<br/>6-出牌处罚判定<br/>7-错认身份<br/>0-其他")
    private Integer varReason;
}