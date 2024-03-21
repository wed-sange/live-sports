package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 消息查询接口
 */
@Data
@Schema(description = "消息查询接口")
public class MessageQueryRequest2 {

    @Schema(description = "主播ID")
    private Long anchorId;

    @Schema(description = "用户ID")
    private Long userId;

}
