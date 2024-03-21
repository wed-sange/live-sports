package org.sports.admin.manage.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 登录信息
 */
@Data
@Schema(description = "登录")
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户本次登录token")
    private String token;

    @Schema(description = "是否第一次登录")
    private Boolean isFirstLogin;


}
