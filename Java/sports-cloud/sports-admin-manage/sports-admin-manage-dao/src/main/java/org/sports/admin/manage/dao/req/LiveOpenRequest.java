package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LiveOpenRequest {
    @NotNull
    @Schema(description = "比赛类型 1足球，2篮球")
    private MatchType matchType;
    @Schema(description = "比赛ID")
    @NotNull
    private Integer matchId;
    @Schema(description = "播放地址")
    @NotEmpty
    private String playUrl;

    @Schema(description = "开播信息配置ID")
    private Long openInfoId;
}
