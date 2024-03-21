package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.springboot.starter.convention.page.PageRequest;

import java.util.List;

@Data
public class LiveSearchRequest extends PageRequest {
    @Schema(description = "比赛类型 1足球，2篮球")
    private MatchType matchType;

    @Schema(description = "名称  (主播名称、赛事名称、比赛)")
    private String name;

    @Schema(description = "排除的直播间ID")
    private Long exceptId;

    @Schema(description = "主播ID")
    private List<Long> anchorIds;

    @Schema(description = "比赛ID")
    private Integer matchId;
}
