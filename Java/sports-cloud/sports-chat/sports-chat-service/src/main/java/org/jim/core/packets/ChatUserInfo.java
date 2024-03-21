package org.jim.core.packets;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;


@Data
@Schema(description = "用户消息")
public class ChatUserInfo implements Serializable {

    private static final long serialVersionUID = 1L;
    @Schema(description = "消息ID")
    private Long id;

    @Schema(description = "消息内容")
    private String content;

    @Schema(description = " 消息类型;(如：0:text、1:image、2:voice、3:video、4:music、5:news)")
    private Integer msgType;

    @Schema(description = "数据类型:1:普通消息 2：反馈通知")
    private Integer dataType = Integer.valueOf(1);

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "0：普通用户，1主播 2助手 3运营")
    private Integer identityType;

    @Schema(description = "发送者头像")
    private String avatar;

    @Schema(description = "发送者昵称")
    private String nick;

    @Schema(description = "未读消息数量")
    private long noReadSum;

    @Schema(description = "消息发送时间")
    private Long createTime;

}
