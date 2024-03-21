package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.FeedbackStatus;
import org.sports.admin.manage.dao.enums.FeedbackType;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FeedbackDetailByAdminVO extends AdminVo {
    @Schema(description = "ID")
    private Long id;

    @Schema(description = "反馈用户ID")
    private Long feedbackUserId;

    @Schema(description = "反馈用户名")
    private String feedbackUserName;

    @Schema(description = "反馈时间")
    private LocalDateTime feedbackTime;

    @Schema(description = "处理时间")
    private LocalDateTime replyTime;

    @Schema(description = "反馈类型：1：更新问题，2：直播相关，3：产品体验，4：聊天相关，5：比赛相关，6：其他")
    private FeedbackType feedbackType;

    @Schema(description = "反馈内容")
    private String feedbackContent;

    @Schema(description = "反馈结果")
    private String feedbackResult;

    @Schema(description = "是否忽略")
    private Boolean ignoreFlag;

    @Schema(description = "状态 1:未处理,2:已处理")
    private FeedbackStatus feedbackStatus;

    @Schema(description = "是否已读")
    private Boolean readFlag;

    @Schema(description = "反馈图片")
    private List<String> feedbackImage;
}
