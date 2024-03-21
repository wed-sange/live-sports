package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 *  禁言用户对象
 */
@Data
@Schema(description = "禁言用户对象")
public class MuteUserRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主播ID")
    @NotNull(message = "主播ID不能为空")
    private Long anchorId;

    @Schema(description = "用户ID")
    @NotNull(message = "用户ID不能为空")
    private Long userId;

}
