package org.sports.app.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class EmailRequest {

    @Schema(description = "邮箱")
    @Email(message = "邮箱格式错误")
    @Size(min = 0, max = 50, message = "邮箱长度不能超过50个字符")
    private String email;

    @Schema(description = "语言类型：1：中文；2：英文",defaultValue = "1")
    @NotNull(message = "语言类型不能为空")
    private Integer lang;
}
