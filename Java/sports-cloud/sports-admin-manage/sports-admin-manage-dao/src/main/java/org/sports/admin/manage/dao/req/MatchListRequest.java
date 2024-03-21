package org.sports.admin.manage.dao.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchStatus;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.springboot.starter.convention.page.PageRequest;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class MatchListRequest extends PageRequest {
    @Schema(description = "比赛日期时间：yyyyMMdd", example = "20230808")
    @NotNull
    @JsonFormat(pattern = "yyyyMMdd")
    private LocalDate matchDate;
    @Schema(description = "赛事ID", example = "[]")
    private List<Integer> competitionIds;
    @Schema(description = "比赛状态")
    private MatchStatus matchStatus;
    @NotNull
    @Schema(description = "比赛类型 1足球，2篮球", example = "1")
    private MatchType matchType;
}
