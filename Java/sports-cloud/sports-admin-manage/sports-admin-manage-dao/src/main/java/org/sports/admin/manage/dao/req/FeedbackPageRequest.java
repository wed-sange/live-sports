package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.FeedbackStatus;
import org.sports.springboot.starter.convention.page.PageRequest;

/**
 * 意见反馈 分页请求对象
 */
@Data
public class FeedbackPageRequest extends PageRequest {

    @Schema(description = "状态 1:未处理,2:已处理")
    private FeedbackStatus feedbackStatus;
}
