package org.sports.live.manage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.MatchStatus;
import org.sports.admin.manage.dao.enums.MatchType;

@Data
public class MatchListVo implements Comparable<MatchListVo> {
    @Schema(description = "赛事ID")
    private Integer competitionId;
    @Schema(description = "赛事名称")
    private String competitionName;
    @Schema(description = "比赛ID")
    private Integer matchId;
    @Schema(description = "比赛类型")
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
    private String awayTeamLog;
    @Schema(description = "直播状态")
    private LiveStatus liveStatus;
    @Schema(description = "比赛状态")
    private MatchStatus matchStatus;
    @Schema(description = "当前主播针对此比赛正在直播的播放地址")
    private String playUrl;
    @Schema(description = "主队比分")
    private Integer homeScore;

    @Schema(description = "主队半场比分")
    private Integer homeHalfScore;

    @Schema(description = "客队比分")
    private Integer awayScore;

    @Schema(description = "客队半场比分")
    private Integer awayHalfScore;
    @Override
    public int compareTo(@NotNull MatchListVo o) {
        return matchTime.compareTo(o.matchTime);
    }
}
