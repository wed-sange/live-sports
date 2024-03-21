package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 消除红点提示请求
 */
@Data
@Schema(description = "消除红点提示请求")
public class CancelUnreadMsgRequest {

    @Schema(description = "直播用户ID [0:代表清除反馈通知]")
    @NotNull(message = "{question.notBlank}")
    private String anchorId;

}
