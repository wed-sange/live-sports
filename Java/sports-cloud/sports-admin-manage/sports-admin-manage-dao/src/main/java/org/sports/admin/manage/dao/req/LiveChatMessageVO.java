package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.LiveStatus;

import java.time.LocalDateTime;

/**
 * 直播端消息信息
 */
@Data
@Schema(description = "直播端消息信息")
public class LiveChatMessageVO extends ChatMessageVO {

    /**
     * 当前主播直播状态
     */
    private LiveStatus liveStatus;
    /**
     * 是否置顶
     */
    private boolean setTop;
    /**
     * 置顶时间
     */
    private LocalDateTime setTopTime;

}
