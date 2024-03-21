package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


/**
 * A用户信息修改
 *

 * @since 2023-07-20
 */
@Data
@Schema(description = "用户信息")
public class UserUpdateRequest implements Serializable {
    /**
     * 昵称
     */
    @Schema(description = "昵称")
    //@Size(max = 8, min = 1, message = "[昵称]长度8个字符以内")
    private String name;

    @Schema(description = "头像")
    private String head;
}
