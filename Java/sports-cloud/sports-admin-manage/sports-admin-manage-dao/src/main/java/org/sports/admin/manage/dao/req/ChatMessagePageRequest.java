package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.springboot.starter.convention.page.PageRequest;

/**
 * 私聊分页请求
 */
@Data
@Schema(description = "私聊分页请求")
public class ChatMessagePageRequest extends PageRequest {

    @Schema(description = "昵称")
    private String nick;

    @Schema(description = "发送者身份身份(0：普通用户，1主播 2助手 3运营)")
    private Integer identityType;

    @Schema(description = "消息内容")
    private String content;

}
