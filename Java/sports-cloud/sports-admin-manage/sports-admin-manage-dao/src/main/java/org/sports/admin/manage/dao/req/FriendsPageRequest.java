package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.springboot.starter.convention.page.PageRequest;

/**
 * 我的好友分页请求
 */
@Data
@Schema(description = "我的好友分页请求")
public class FriendsPageRequest extends PageRequest {

    @Schema(description = "用户昵称")
    private String userName;

}
