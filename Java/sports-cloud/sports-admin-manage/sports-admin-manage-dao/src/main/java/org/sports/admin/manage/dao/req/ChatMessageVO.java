package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 消息信息
 *

 * @since 2023-07-31
 */
@Data
@Schema(description = "消息信息")
public class ChatMessageVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "发送用户ID")
    private String fromId;

    @Schema(description = "主播ID")
    private String anchorId;

    @Schema(description = "发送者头像")
    private String avatar;

    @Schema(description = "发送者昵称")
    private String nick;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "数据类型:1:普通消息 2：反馈通知")
    private Integer dataType = Integer.valueOf(1);

    @Schema(description = "创建时间")
    private long createTime;

    @Schema(description = "未读消息数量")
    private Integer noReadSum;

    @Schema(description = "消息类型(0:text、1:image、2:voice、3:video、4:music、5:news)")
    private Integer msgType;

}
