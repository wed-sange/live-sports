package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * 主播控制用户信息
 */
@Data
@Schema(description = "主播控制用户信息")
public class AnchorControlUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主播ID")
    private Long anchorId;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "是否禁言")
    private boolean isMute;

    @Schema(description = "是否踢出房间")
    private boolean isTickOut;

    @Schema(description = "踢出房间剩余时间(分钟)")
    private long tickOutLeftTime;

}
