package org.sports.admin.manage.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LiveDTO {
    @Schema(description = "直播ID")
    private Long id;
    @Schema(description = "主播ID")
    private Long userId;
    @Schema(description = "主播昵称")
    private String nickName;
    @Schema(description = "主播头像")
    private String userLogo;
    @Schema(description = "比赛ID")
    private Integer matchId;
    @Schema(description = "比赛类型 1足球，2篮球")
    private Integer matchType;
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
}
