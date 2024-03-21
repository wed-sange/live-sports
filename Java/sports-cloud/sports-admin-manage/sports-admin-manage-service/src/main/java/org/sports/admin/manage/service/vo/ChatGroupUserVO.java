package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>
 * 群组用户信息
 * </p>
 *

 * @since 2023-07-27
 */
@Getter
@Setter
@Schema(description = "群组用户信息")
public class ChatGroupUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "群组ID")
    private String groupId;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "用户昵称")
    private String nick;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "是否在线：0：在线；1：离线")
    private Integer status;

    @Schema(description = "是否禁言")
    private boolean isMute;

    @Schema(description = "级别")
    private Integer level;


}
