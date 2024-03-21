package org.sports.app.service.vo.live;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LiveUserVO implements Serializable {
    @Schema(description = "直播间ID")
    private String liveId;
    @Schema(description = "开播时间")
    private LocalDateTime openTime;
    @Schema(description = "热度")
    private Long hotValue;

    @Schema(description = "主播ID")
    private Long userId;

    @Schema(description = "主播昵称")
    private String nickName;

    @Schema(description = "主播头像")
    private String userLogo;

    @Schema(description = "播放地址")
    private String playUrl;

    @Schema(description = "是否纯净流")
    private boolean isPureFlow;

}