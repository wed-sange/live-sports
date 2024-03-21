//package org.sports.admin.manage.dao.entity;
//
//import com.baomidou.mybatisplus.annotation.*;
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.Data;
//
//import java.io.Serializable;
//import java.time.LocalDateTime;
//
///**
// * <p>
// * 后台管理人员信息
// * </p>
// *
//
// * @since 2023-07-20
// */
//@Data
//@TableName("t_admin_user")
//@Schema(description = "后台管理人员信息")
//public class AdminUserDO implements Serializable {
//
//    private static final long serialVersionUID = 1L;
//
//    @TableId(value = "id",type = IdType.ASSIGN_ID)
//    @Schema(description = "主键id")
//    private Long id;
//
//    @Schema(description = "账号")
//    @TableField("account")
//    private String account;
//
//    @Schema(description = "昵称")
//    @TableField("name")
//    private String name;
//
//    @Schema(description = "密码")
//    @TableField("passwd")
//    private String passwd;
//
//    @Schema(description = "是否有效 1是 0否")
//    @TableField("yn_valid")
//    private Integer ynValid;
//
//    @Schema(description = "创建时间")
//    @TableField(value = "create_time", fill = FieldFill.INSERT)
//    private LocalDateTime createTime;
//
//    @Schema(description = "修改时间")
//    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
//    private LocalDateTime updateTime;
//
//}
