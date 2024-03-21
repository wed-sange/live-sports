package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * APP用户成长值信息
 * </p>
 *

 * @since 2023-07-26
 */
@Data
@TableName("t_app_user_growth")
@Schema(description = "APP用户成长值信息")
public class AppUserGrowthDO implements Serializable{

    private static final long serialVersionUID = 1L;
    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "获取成长值类型(1直播聊天互动 2观看直播)")
    @TableField("type")
    private Integer type;

    @Schema(description = "用户ID")
    @TableField("user_id")
    private Long userId;

    @Schema(description = "触发次数")
    @TableField("event_number")
    private Integer eventNumber;

    @Schema(description = "获得成长值")
    @TableField("event_growth")
    private Integer eventGrowth;

    @Schema(description = "统计开始时间")
    @TableField("begin_time")
    private LocalDateTime beginTime;

    @Schema(description = "统计截止时间")
    @TableField("end_time")
    private LocalDateTime endTime;

    @Schema(description = "创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;


}
