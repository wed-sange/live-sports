package org.sports.app.service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sports.admin.manage.dao.enums.MatchType;

import java.io.Serializable;

@Schema(description = "APP - 热门赛事 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class HotMatchVo implements Serializable {


    @Schema(description = "热门赛事id")
    private Integer competitionId;

    @Schema(description = "热门赛事名称")
    private String competitionName;

    @Schema(description = "赛事类型：1：足球；2：篮球")
    private MatchType matchType;

    @Schema(description = "比赛数量")
    private Integer matchCount;
}
