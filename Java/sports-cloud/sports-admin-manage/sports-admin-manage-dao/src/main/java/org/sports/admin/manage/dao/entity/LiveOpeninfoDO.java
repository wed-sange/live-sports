package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 直播开播信息
 *

 * @since 2023-07-28
 */
@Data
@TableName("t_live_openinfo")
@Schema(description = "直播开播信息")
public class LiveOpeninfoDO implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "id")
    @Schema(description = "id")
    private Long id;

    @Schema(description = "主播ID")
    private Long anchorId;

    @Schema(description = "直播封面")
    @TableField("title_page")
    private String titlePage;

    @Schema(description = "直播公告")
    @TableField("notice")
    private String notice;

    @Schema(description = "开播首条聊天")
    @TableField("first_message")
    private String firstMessage;

    @Schema(description = "是否使用中")
    @TableField("used")
    private Integer used;

    @Schema(description = "创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    @Schema(description = "备注名称")
    private String remark;
}
