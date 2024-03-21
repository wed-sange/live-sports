package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.springboot.starter.convention.page.PageRequest;

/**
 * APP用户消息分页请求
 */
@Data
@Schema(description = "APP用户消息分页请求")
public class AppUserMsgPageRequest extends PageRequest {

    @Schema(description = "用户昵称")
    private String userName;

}
