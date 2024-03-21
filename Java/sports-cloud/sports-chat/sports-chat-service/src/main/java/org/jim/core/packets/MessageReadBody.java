package org.jim.core.packets;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageReadBody {

    private String messageId;

    /**
     * 0-未读 1-已读
     */
    private Integer read;

    /**
     * 当前用户ID
     */
    private String currentId;

    /**
     * 什么平台发送的 2-LIVE 3-APP
     */
    private Integer channelType;

    /**
     * 发送给谁的
     */
    private String toId;
}
