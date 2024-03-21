package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchType;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MatchSearchVo implements Serializable {
    @Schema(description = "赛事ID")
    private Integer competitionId;
    @Schema(description = "名称")
    private String competitionName;
    @Schema(description = "全称")
    private String fullCompetitionName;
    @Schema(description = "比赛类型 1足球，2篮球")
    private MatchType matchType;
}
