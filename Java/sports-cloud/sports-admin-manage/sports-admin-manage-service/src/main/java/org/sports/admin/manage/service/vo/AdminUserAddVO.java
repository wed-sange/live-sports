package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 后台管理人员信息新增/修改
 *

 * @since 2023-07-20
 */
@Data
@Schema(description = "后台管理人员信息")
public class AdminUserAddVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "账号")
    @NotBlank(message = "{question.notBlank}")
    private String account;

    @Schema(description = "昵称")
    private String name;

    @Schema(description = "密码")
    private String passwd;

}
