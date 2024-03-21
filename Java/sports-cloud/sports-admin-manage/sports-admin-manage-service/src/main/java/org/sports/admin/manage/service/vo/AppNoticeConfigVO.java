package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serializable;

/**
 * APP用户通知设置实体类

 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "APP用户通知设置")
public class AppNoticeConfigVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "是否开启关注比赛通知 1是 0否")
    private Integer ynFollowMatch;

    @Schema(description = "是否开启主播开播通知 1是 0否")
    private Integer ynLiveOpen;

}
