package org.sports.admin.manage.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

@Data
public class LiveOperateDTO implements Serializable {

    @Schema(description = "直播间ID")
    private String id;

    @Schema(description = "主播ID")
    private String anchorId;
    /**
     * 封面图
     */
    private String coverImg;
    /**
     * 昵称
     */
    private String nickName;

    /**
     * 头像
     */
    private String userLogo;

    @Schema(description = "直播状态,1:未开始，2：直播中，3：已关播")
    private Integer liveStatus;
    /**
     * 比赛ID
     */
    private Integer matchId;
    /**
     * 比赛类型
     */
    private Integer matchType;
    /**
     * 赛事名称
     */
    private String competitionName;
    /**
     * 主队
     */
    private String homeTeamName;

    /**
     * 客队
     */
    private String awayTeamName;
    /**
     * 播放地址
     */
    private String playUrl;
    /**
     * 总热度
     */
    private Long hotValue;
    /**
     * 排名
     */
    private Integer rank;
}
