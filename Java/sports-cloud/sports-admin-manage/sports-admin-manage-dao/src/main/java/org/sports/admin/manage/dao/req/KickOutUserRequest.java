package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 *  踢出用户对象
 */
@Data
@Schema(description = "踢出用户对象")
public class KickOutUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "直播间ID")
    @NotNull(message = "直播间ID不能为空")
    private Long liveId;

    @Schema(description = "主播ID")
    @NotNull(message = "主播ID不能为空")
    private Long anchorId;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

}
