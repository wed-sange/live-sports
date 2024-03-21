package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 后台管理人员信息
 * </p>
 *

 * @since 2023-07-20
 */
@Data
@Schema(description = "后台管理人员信息")
public class AdminUserVO implements Serializable {

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
