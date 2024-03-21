package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchType;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class HomeLivingVo implements Serializable {
    @Schema(description = "直播间ID")
    private Long id;
    @Schema(description = "主播ID")
    private Long userId;
    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "头像")
    private String userLogo;
    @Schema(description = "封面")
    private String titlePage;
    @Schema(description = "公告")
    private String notice;
    @Schema(description = "首条消息")
    private String firstMessage;
    @Schema(description = "赛事ID")
    private Integer competitionId;
    @Schema(description = "赛事名称")
    private String competitionName;
    @Schema(description = "比赛ID")
    private Integer matchId;
    @Schema(description = "比赛类型 1足球，2篮球")
    private MatchType matchType;
    @Schema(description = "比赛时间")
    private Integer matchTime;
    @Schema(description = "主队")
    private String homeTeamName;
    @Schema(description = "主队LOGO")
    private String homeTeamLogo;
    @Schema(description = "客队")
    private String awayTeamName;
    @Schema(description = "客队LOGO")
    private String awayTeamLogo;
    @Schema(description = "来源地址")
    private String sourceUrl;
    @Schema(description = "播放地址")
    private String playUrl;
    @Schema(description = "开播时间")
    private LocalDateTime openTime;
    @Schema(description = "关播时间")
    private LocalDateTime closeTime;
    @Schema(description = "直播状态")
    private LiveStatus liveStatus;
    @Schema(description = "总热度")
    private Long hotValue;
}
