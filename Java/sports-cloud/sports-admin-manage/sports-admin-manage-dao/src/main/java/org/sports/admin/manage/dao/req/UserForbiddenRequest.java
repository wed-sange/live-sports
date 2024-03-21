package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * APP用户禁言/live用户封禁请求
 *

 * @since 2023-07-24
 */
@Data
@Schema(description = "用户禁言/封禁请求")
public class UserForbiddenRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    @NotNull(message = "{question.notBlank}")
    private Long id;

    @Schema(description = "是否禁言/封禁 1普通禁言/封禁 2永久禁言/封禁")
    @NotNull(message = "{question.notBlank}")
    private Integer ynForbidden;

    @Schema(description = "禁言/封禁天数")
    private Integer forbiddenDay;

    @Schema(description = "禁言/封禁原因")
    @NotBlank(message = "{question.notBlank}")
    private String forbiddenDescp;

}
