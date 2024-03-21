package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * APP用户系统通知

 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "APP用户系统通知")
public class AppUserNoticeVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主ID")
    private Long id;

    @Schema(description = "通知类型(0系统通知 1反馈结果 2禁言通知 3解禁通知)")
    private Integer type;

    @Schema(description = "通知标题")
    private String title;

    @Schema(description = "通知内容")
    private String notice;

    @Schema(description = "是否已读")
    private Boolean readFlag;

    @Schema(description = "通知时间")
    private LocalDateTime createTime;

    @Schema(description = "业务ID")
    private String bizId;

}
