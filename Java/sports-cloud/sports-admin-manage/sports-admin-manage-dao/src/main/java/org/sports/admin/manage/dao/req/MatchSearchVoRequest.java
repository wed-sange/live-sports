package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.MatchType;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class MatchSearchVoRequest implements Serializable {
    @Schema(description = "名称")
    @NotEmpty
    private String competitionName;

    @NotNull
    @Schema(description = "比赛类型 1足球，2篮球")
    private MatchType matchType;
}
