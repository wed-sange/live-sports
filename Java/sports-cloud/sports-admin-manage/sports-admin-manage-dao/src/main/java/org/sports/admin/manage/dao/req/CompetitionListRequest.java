package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class CompetitionListRequest {
    @Schema(description = "时间")
    @NotNull
    private String matchTime;
    @NotNull
    @Schema(description = "比赛类型 1足球，2篮球")
    private MatchType matchType;
    @Schema(description = "赛事类型IDs")
    private List<Integer> competitionIds = new ArrayList<>();
}
