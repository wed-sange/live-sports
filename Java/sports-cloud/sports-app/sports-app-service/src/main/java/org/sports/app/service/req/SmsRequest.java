package org.sports.app.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class SmsRequest {

    @Schema(description = "账号")
    @NotBlank(message = "{question.notBlank}")
    private String account;

    @Schema(description = "区号[手机号登陆时候不能为空]")
    private String areaCode;

}
