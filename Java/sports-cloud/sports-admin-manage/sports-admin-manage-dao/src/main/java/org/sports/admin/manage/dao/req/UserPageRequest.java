package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.springboot.starter.convention.page.PageRequest;

/**
 * 用户分页请求
 */
@Data
@Schema(description = "用户分页请求")
public class UserPageRequest extends PageRequest {

    @Schema(description = "用户ID【app专用】")
    private Long id;

    @Schema(description = "账号【admin专用】")
    private String account;

    @Schema(description = "昵称")
    private String name;

    @Schema(description = "是否禁言 0否 1是【app专用】")
    private Integer ynForbidden;

}
