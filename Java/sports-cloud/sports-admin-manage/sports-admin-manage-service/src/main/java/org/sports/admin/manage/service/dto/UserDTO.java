package org.sports.admin.manage.service.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户信息
 */
@Data
@Schema(description = "用户")
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    private String userId;

    @Schema(description = "主播ID")
    private String anchorId;

    @Schema(description = "用户身份：0-普通用户   1-主播   2-主播助理 3：运营")
    private Integer identity;
}
