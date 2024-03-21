package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * APP用户通知实体类

 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_app_user_notice")
public class AppUserNoticeDO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主ID
     */
    private Long id;
    /**
     * 通知类型(0系统通知 1反馈结果 2禁言通知 3解禁通知)
     */
    private Integer type;
    /**
     * 通知用户ID
     */
    private Long userId;
    /**
     * 通知标题
     */
    private String title;
    /**
     * 通知内容
     */
    private String notice;
    /**
     * 是否已读
     */
    private Boolean readFlag;
    /**
     * 业务ID
     */
    private String bizId;
    /**
     * 通知时间
     */
    private LocalDateTime createTime;

    /**
     * 删除标志
     */
    @TableField(fill = FieldFill.INSERT)
    @TableLogic
    private Integer delFlag;

}
