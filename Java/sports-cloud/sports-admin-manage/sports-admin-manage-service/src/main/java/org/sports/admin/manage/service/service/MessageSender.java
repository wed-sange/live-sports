package org.sports.admin.manage.service.service;

import org.sports.admin.manage.dao.enums.MessageType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface MessageSender {

    /**
     * 发送消息，通知用户刷新页面
     *
     * @param messageType 消息类型
     * @param body        消息体
     */
    void send(@Nonnull MessageType messageType, @Nullable Object body);
}
