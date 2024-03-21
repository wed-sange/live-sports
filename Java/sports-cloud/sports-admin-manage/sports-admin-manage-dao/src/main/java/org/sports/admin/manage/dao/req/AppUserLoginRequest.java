package org.sports.admin.manage.dao.req;

import com.anji.captcha.model.vo.CaptchaVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Schema(description = "APP用户登录信息")
public class AppUserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "账号")
    @NotBlank(message = "{question.notBlank}")
    private String account;

    @Schema(description = "密码")
    private String passwd;

    @Schema(description = "验证码")
    private String code;

    @Schema(description = "登录方式 1手机号 2邮箱")
    @NotNull(message = "{question.notBlank}")
    private Integer type;

    @Schema(description = "注册地址")
    private String registerAddr;

    @Schema(description = "来源 1IOS 2android 3H5 4小程序")
    private String source;

    @Schema(description = "区号[手机号登陆时候不能为空]")
    private String areaCode;

    @Schema(description = "行为验证码")
    private CaptchaVO captchaVO;
}
