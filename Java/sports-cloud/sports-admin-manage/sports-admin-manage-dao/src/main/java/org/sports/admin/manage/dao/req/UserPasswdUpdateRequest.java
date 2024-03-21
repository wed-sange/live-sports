package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;


/**
 * 用户密码修改
 *

 * @since 2023-07-20
 */
@Data
@Schema(description = "用户密码信息")
public class UserPasswdUpdateRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "旧密码")
    @NotBlank(message = "{question.notBlank}")
    private String oldPasswd;

    @Schema(description = "新密码")
    @NotBlank(message = "{question.notBlank}")
    private String newPasswd;

    @Schema(description = "确认密码")
    @NotBlank(message = "{question.notBlank}")
    private String rePasswd;

}
