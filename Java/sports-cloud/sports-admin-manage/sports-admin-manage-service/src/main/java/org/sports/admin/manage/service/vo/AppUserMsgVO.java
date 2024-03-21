package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * app用户消息列表返回对象
 *

 * @since 2023-07-31
 */
@Data
@Schema(description = "app用户消息列表返回对象")
public class AppUserMsgVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "昵称")
    private String name;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

}
