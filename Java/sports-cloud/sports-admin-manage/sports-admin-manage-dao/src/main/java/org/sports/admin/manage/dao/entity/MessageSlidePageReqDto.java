package org.sports.admin.manage.dao.entity;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.entity.base.SlidePageBase;

import javax.validation.constraints.NotNull;

@Data
public class MessageSlidePageReqDto extends SlidePageBase<Long> {

    /**
     * 查询用户的ID（APP为当前用户，live端为查看的用户ID）
     */
    @Schema(description = "to用户的ID（APP为当前用户，live端为查看的用户ID）")
    private String searchId;

    /**
     * 当前用户的ID（APP为当前用户，live端为查看的用户ID）
     */
    @Schema(description = "from用户的ID（APP为当前用户，live端为主播ID）")
    private String userId;
    /**
     * 消息类型，0-历史消息，1-离线消息
     */
    @Schema(description = "消息类型，0-历史消息，1-离线消息")
    @NotNull
    private String type;
    /**
     * 群聊Id
     */
    @Schema(description = "群聊Id")
    private String groupId;
    /**
     * 群聊还是私聊 1-群聊，2-私聊
     */
    @Schema(description = "群聊还是私聊 1-群聊，2-私聊")
    @NotNull
    private String chatType;
    /**
     * 查询类型（判断是用户还是直播端，普通用户查看不了被删除的消息）， live-2, APP-3
     */
    @Schema(description = "查询类型（判断是用户还是直播端，普通用户查看不了被删除的消息）， live-2, APP-3")
    @NotNull
    private String searchType;

    /**
     * 是否只查询主播组发送的消息
     */
    @Schema(description = "是否只查询主播组发送的消息")
    private boolean anchorSendOnly;

    @Schema(description = "查询方向：up| down")
    private String inquiryMode = "up";
}
