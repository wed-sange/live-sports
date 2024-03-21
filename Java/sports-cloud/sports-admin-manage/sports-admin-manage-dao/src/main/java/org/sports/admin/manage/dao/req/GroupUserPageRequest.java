package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.springboot.starter.convention.page.PageRequest;

/**
 * 用户分页请求
 */
@Data
@Schema(description = "用户分页请求")
public class GroupUserPageRequest extends PageRequest {

    @Schema(description = "直播间ID")
    private Long groupId;

    @Schema(description = "关键词搜索")
    private String keywords;

    @Schema(description = "是否禁言")
    private Boolean isMute;
}
