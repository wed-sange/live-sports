package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 正在直播的用户信息
 */
@Data
@Schema(description = "群组用户信息")
public class LivingUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "直播间id")
    private Long id;

    @Schema(description = "主播ID")
    private String anchorId;

    @Schema(description = "主播头像")
    private String avatar;

    @Schema(description = "主播昵称")
    private String nick;

    @Schema(description = "封面")
    private String titlePage;

    @Schema(description = "开播时间")
    private long createTime;

    @Schema(description = "未读消息数量")
    private Integer noReadSum;

    /**
     * 赛事名称
     */
    private String competitionName;
    /**
     * 比赛ID
     */
    private Integer matchId;
    /**
     * 比赛类型
     */
    private MatchType matchType;
    /**
     * 比赛时间
     */
    private Integer matchTime;
    /**
     * 主队
     */
    private String homeTeamName;
    /**
     * 主队LOGO
     */
    private String homeTeamLogo;
    /**
     * 客队
     */
    private String awayTeamName;
    /**
     * 客队LOGO
     */
    private String awayTeamLogo;

    /**
     * 播放地址
     */
    private String playUrl;

    /**
     * 开播时间
     */
    private LocalDateTime openTime;

    /**
     * 直播状态
     */
    private LiveStatus liveStatus;

    /**
     * 总热度
     */
    private Long hotValue;

}
