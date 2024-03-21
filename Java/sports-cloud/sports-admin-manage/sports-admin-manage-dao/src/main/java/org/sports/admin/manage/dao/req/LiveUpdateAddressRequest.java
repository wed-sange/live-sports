package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LiveUpdateAddressRequest {
    @Schema(description = "播放地址")
    @NotNull
    private String playUrl;
}
