package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 群组用户信息
 * </p>
 *

 * @since 2023-08-02
 */
@Data
@Schema(description = "群组用户信息")
public class ChatMessageVO2 implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "发送用户ID")
    private String fromId;

    @Schema(description = "发送者头像")
    private String avatar;

    @Schema(description = "发送者昵称")
    private String nick;

    @Schema(description = "发送者身份身份(0：普通用户，1主播 2助手 3运营)")
    private Integer identityType;

    @Schema(description = "目标用户ID")
    private String toId;

    @Schema(description = "目标用户昵称")
    private String toName;

    @Schema(description = " 消息类型;(如：0:text、1:image、2:voice、3:video、4:music、5:news)")
    private Integer msgType;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = "创建时间")
    private long createTime;


}
