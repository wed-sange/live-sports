package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * <p>
 * 群组用户信息
 * </p>
 *

 * @since 2023-07-26
 */
@Data
@TableName("t_chat_message")
@Schema(description = "群组用户信息")
public class ChatMessageDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "发送用户ID")
    @TableField("from_id")
    private String fromId;

    @Schema(description = "目标用户ID")
    @TableField("to_id")
    private String toId;

    @Schema(description = "主播ID")
    @TableField("anchor_id")
    private String anchorId;

    @Schema(description = "消息命令码")
    @TableField("cmd")
    private Integer cmd;

    @Schema(description = " 消息类型;(如：0:text、1:image、2:voice、3:video、4:music、5:news,6:system)")
    @TableField("msg_type")
    private Integer msgType;

    @Schema(description = "聊天类型;(如1:公聊、2私聊)")
    @TableField("chat_type")
    private Integer chatType;

    @Schema(description = "发送者头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "发送者昵称")
    @TableField("nick")
    private String nick;

    @Schema(description = "发送者身份身份(0：普通用户，1主播 2助手 3运营)")
    @TableField("identity_type")
    private Integer identityType;

    @Schema(description = "级别")
    @TableField("level")
    private Integer level;

    @Schema(description = "消息内容")
    @TableField("content")
    private String content;

    @Schema(description = "群组ID")
    @TableField("group_id")
    private String groupId;


    @Schema(description = "是否已发送: 0 未发送 1 已发送")
    @TableField("sent")
    private Integer sent;

    @Schema(description = "是否已读：0 未读 1 已读")
    @TableField("readable")
    private Integer readable;

    @Schema(description = "是否用户删除：0 未删 1 已删")
    private Integer userDel;

    @Schema(description = "是否主播删除：0 未删 1 已删")
    private Integer anchorDel;

    /**
     * 创建时间
     */
    private Timestamp createTime;

    /**
     * 修改时间
     */
    private Timestamp updateTime;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 更新者
     */
    private String updater;
    /**
     * 删除标志
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer delFlag;

}
