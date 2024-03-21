package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class FeedbackReplyRequest {
    @Schema(description = "ID")
    @NotNull
    private Long id;

    @Schema(description = "反馈结果")
    @NotBlank
    private String feedbackResult;



}
