package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * APP用户成长值信息
 * </p>
 *

 * @since 2023-07-26
 */
@Data
@Schema(description = "APP用户成长值信息")
public class AppUserGrowthVO implements Serializable{

    private static final long serialVersionUID = 1L;

    @Schema(description = "获取成长值类型(1直播聊天互动 2观看直播)")
    private Integer type;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "触发次数")
    private Integer eventNumber;

    @Schema(description = "统计开始时间")
    private LocalDateTime beginTime;

    @Schema(description = "统计截止时间")
    private LocalDateTime endTime;

}
