package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FeedbackIgnoreRequest {
    @Schema(description = "ID")
    private Long id;
}
