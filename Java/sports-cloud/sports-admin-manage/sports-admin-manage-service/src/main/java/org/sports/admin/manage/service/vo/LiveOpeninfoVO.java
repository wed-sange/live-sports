package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 直播开播信息
 *

 * @since 2023-07-28
 */
@Data
@Schema(description = "直播开播信息")
public class LiveOpeninfoVO implements Serializable {
    private static final long serialVersionUID = 1L;

    @Schema(description = "开播信息配置ID")
    private Long id;

    @Schema(description = "主播ID")
    private Long anchorId;

    @Schema(description = "直播封面")
    @NotBlank(message = "{question.notBlank}")
    @Size(min = 1, max = 256, message = "直播封面地址太长")
    private String titlePage;

    @Schema(description = "直播公告")
    @NotNull(message = "{question.notBlank}")
    @Size(min = 1, max = 1024, message = "直播公告内容太多，请缩减")
    private String notice;

    @Schema(description = "开播首条聊天")
    @Size(min = 1, max = 1024, message = "开播首条聊天内容太多，请缩减")
    private String firstMessage;

    @Schema(description = "备注名称")
    @NotNull(message = "备注名称不能为空")
    @Size(min = 1, max = 30, message = "备注名称长度1到30个字符")
    private String remark;

    @Schema(description = "是否使用中：1使用中 0未使用")
    private Integer used;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
