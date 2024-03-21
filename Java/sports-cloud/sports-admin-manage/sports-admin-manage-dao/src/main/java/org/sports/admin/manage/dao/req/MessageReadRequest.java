package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 消息批量已读接口
 */
@Data
@Schema(description = "消息批量已读")
public class MessageReadRequest  {

    @Schema(description = "批量消息ID")
    private List<Long> messageIds;

    @Schema(description = "主播ID")
    private Long anchorId;

    @Schema(description = "用户ID")
    private Long userId;

}
