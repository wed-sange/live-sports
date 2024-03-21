package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.FeedbackStatus;
import org.sports.admin.manage.dao.enums.FeedbackType;

import java.util.Date;
import java.util.List;

@Data
public class FeedbackSubmitRequest {
    @Schema(description = "反馈类型：1：更新问题，2：直播相关，3：产品体验，4：聊天相关，5：比赛相关，6：其他")
    private FeedbackType feedbackType;
    @Schema(description = "反馈内容")
    private String feedbackContent;
    @Schema(description = "反馈图片")
    private List<String> feedbackImage;
}
