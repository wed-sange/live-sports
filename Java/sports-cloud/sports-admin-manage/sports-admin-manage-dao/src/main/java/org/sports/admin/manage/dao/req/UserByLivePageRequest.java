package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.springboot.starter.convention.page.PageRequest;

/**
 * 通过主播查询用户分页请求
 */
@Data
@Schema(description = "通过主播查询用户分页请求")
public class UserByLivePageRequest extends PageRequest {

    @Schema(description = "主播ID，可为空，为空时候查询当前所属主播用户")
    private Long anchorId;

}
