package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
@Schema(description = "后台用户登录信息")
public class UserBackgroundLoginVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "账号")
    @NotBlank(message = "{question.notBlank}")
    private String account;

    @Schema(description = "密码")
    @NotBlank(message = "{question.notBlank}")
    private String passwd;

    @Schema(description = "验证码")
    private Long code;

    @Schema(description = "私钥")
    private String secret;

}
