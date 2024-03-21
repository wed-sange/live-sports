package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.sports.admin.manage.dao.enums.FeedbackStatus;
import org.sports.admin.manage.dao.enums.FeedbackType;
import org.sports.springboot.starter.convention.page.PageRequest;

@EqualsAndHashCode(callSuper = true)
@Data
public class FeedbackListByAdminPageRequest extends PageRequest {
    @Schema(description = "状态 1:未处理,2:已处理")
    private FeedbackStatus feedbackStatus;
    @Schema(description = "反馈类型：1：更新问题，2：直播相关，3：产品体验，4：聊天相关，5：比赛相关，6：其他")
    private FeedbackType feedbackType;
    @Schema(description = "反馈用户ID")
    private Long feedbackUserId;
    @Schema(description = "反馈用户名")
    private String feedbackUserName;
}
