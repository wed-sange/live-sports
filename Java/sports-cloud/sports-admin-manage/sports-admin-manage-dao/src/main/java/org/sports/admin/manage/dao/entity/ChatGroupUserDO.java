package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;

import java.io.Serializable;

/**
 * <p>
 * 群组用户信息
 * </p>
 *

 * @since 2023-07-27
 */
@Getter
@Setter
@TableName("t_chat_group_user")
@Schema(description = "群组用户信息")
public class ChatGroupUserDO extends MysqlBaseDO  implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "群组ID")
    @TableField("group_id")
    private String groupId;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private String userId;

    @Schema(description = "用户昵称")
    @TableField("nick")
    private String nick;

    @Schema(description = "头像")
    @TableField("avatar")
    private String avatar;

    @Schema(description = "是否在线")
    @TableField("status")
    private Integer status;

    @Schema(description = "是否离开直播间：1：离开；0：未离开")
    @TableField("leave_status")
    private Integer leaveStatus;


}
