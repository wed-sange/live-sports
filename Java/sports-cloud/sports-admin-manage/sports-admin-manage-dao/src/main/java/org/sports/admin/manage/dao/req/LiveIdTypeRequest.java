package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchType;

@Data
@Builder
public class LiveIdTypeRequest {
    @Schema(description = "比赛类型 1足球，2篮球")
    private MatchType matchType;

    @Schema(description = "排除ID")
    private Integer matchId;
}
