package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 直播间热度管理
 * </p>
 *

 * @since 2023-07-26
 */
@Data
@TableName("t_live_heat_config")
@Schema(description = "直播间热度管理")
public class LiveHeatConfigDO implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "基础热度值（非热门）")
    @TableField("base_heat")
    private Integer baseHeat;

    @Schema(description = "基础热度值（热门）")
    @TableField("base_heat_hot")
    private Integer baseHeatHot;

//    @Schema(description = "30分钟增加值上限")
//    @TableField("thirty_add_upper")
//    private Integer thirtyAddUpper;
//
//    @Schema(description = "30分钟增加值下限")
//    @TableField("thirty_add_lower")
//    private Integer thirtyAddLower;

    @Schema(description = "人数系数值")
    @TableField("people_number_ratio")
    private Integer peopleNumberRatio;

    @Schema(description = "直播间消息发送系数")
    @TableField("msg_send_ratio")
    private Integer msgSendRatio;

    @Schema(description = "直播间分享系数")
    @TableField("share_ratio")
    private Integer shareRatio;

    @Schema(description = "推流地址")
    @TableField("streaming_address")
    private String streamingAddress;

    @Schema(description = "修改人")
    @TableField("update_by")
    private Long updateBy;

    @Schema(description = "修改时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
