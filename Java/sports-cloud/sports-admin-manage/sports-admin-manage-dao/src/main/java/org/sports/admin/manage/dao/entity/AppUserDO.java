package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * <p>
 * APP用户信息
 * </p>
 *

 * @since 2023-07-20
 */
@Data
@TableName("t_app_user")
@Schema(description = "APP用户信息")
public class AppUserDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "手机号区号")
    @TableField("area_code")
    private String areaCode;

    @Schema(description = "手机号")
    @TableField("tel")
    private String tel;

    @Schema(description = "邮箱")
    @TableField("email")
    private String email;

    @Schema(description = "昵称")
    @TableField("name")
    private String name;

    @Schema(description = "头像")
    @TableField("head")
    private String head;

    @Schema(description = "密码")
    @TableField("passwd")
    private String passwd;

    @Schema(description = "等级")
    @TableField("lv_num")
    private Integer lvNum;

    @Schema(description = "等级名称")
    @TableField("lv_name")
    private String lvName;

    @Schema(description = "成长值")
    @TableField("growth_value")
    private Integer growthValue;

    @Schema(description = "下一阶段成长值")
    @TableField("growth_value_next")
    private Integer growthValueNext;

    @Schema(description = "注册地址")
    @TableField("register_addr")
    private String registerAddr;

    @Schema(description = "昵称上次修改时间")
    @TableField("name_last_time")
    private LocalDateTime nameLastTime;

    @Schema(description = "是否注销 1是 0否")
    @TableField("yn_cancel")
    private Integer ynCancel;

    @Schema(description = "是否禁言 0否 1普通禁言 2永久禁言")
    @TableField("yn_forbidden")
    private Integer ynForbidden;

    @Schema(description = "禁言天数")
    @TableField("forbidden_day")
    private Integer forbiddenDay;

    @Schema(description = "禁言期限")
    @TableField("forbidden_time")
    private LocalDateTime forbiddenTime;

    @Schema(description = "禁言原因")
    @TableField("forbidden_descp")
    private String forbiddenDescp;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
