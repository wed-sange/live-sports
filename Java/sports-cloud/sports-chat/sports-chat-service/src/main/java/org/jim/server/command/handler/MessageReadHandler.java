package org.jim.server.command.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.exception.ImException;
import org.jim.core.packets.Command;
import org.jim.core.packets.MessageReadBody;
import org.jim.core.packets.RespBody;
import org.jim.core.utils.JsonKit;
import org.jim.server.JimServerAPI;
import org.jim.server.command.AbstractCmdHandler;
import org.redisson.api.RLock;
import org.redisson.api.RReadWriteLock;
import org.redisson.api.RedissonClient;
import org.sports.chat.service.constant.CacheConstant;
import org.sports.chat.service.util.PushMessageUtil;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 版本: [1.0]
 * 功能说明: 消息已读处理器
 */
@Slf4j
public class MessageReadHandler extends AbstractCmdHandler {


    private RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
    private PushMessageUtil pushMessageUtil = SpringUtil.getBean(PushMessageUtil.class);

    @Override
    public ImPacket handler(ImPacket packet, ImChannelContext channelContext) throws ImException {
        if (packet.getBody() == null) {
            throw new ImException("body is null");
        }
        MessageReadBody messageBody = JsonKit.toBean(packet.getBody(), MessageReadBody.class);
        // 对发给主播组的消息要进行已读同步 APP端才是用户发给主播组的消息
        if (UserChannelEnum.APP.getFlag().equals(String.valueOf(messageBody.getChannelType()))) {
            ImPacket imPacket = new ImPacket(Command.COMMAND_CHAT_REQ, new RespBody(Command.COMMAND_MESSAGE_READ_REQ, messageBody).toByte());
            RReadWriteLock readWriteLock = redissonClient.getReadWriteLock(CacheConstant.CHAT_LIVE_GROUP_USER_SESSION);
            RLock rLock = readWriteLock.readLock();
            rLock.lock(10, TimeUnit.SECONDS);
            try {
                // 发送给所有在线主播组成员， 不发给当前已读用户
                Object anchorSendIds = redissonClient.getBucket(CacheConstant.CHAT_LIVE_GROUP_USER_SESSION + CacheConstant.ANCHOR + messageBody.getToId()).get();
                if (ObjectUtil.isNotNull(anchorSendIds)) {
                    for (String id : (List<String>) anchorSendIds) {
                        if (ObjectUtils.notEqual(id, messageBody.getCurrentId())) {
                            for (ImChannelContext imChannelContext : JimServerAPI.getByUserId(id)) {
                                if (Objects.nonNull(imChannelContext)) {
                                    JimServerAPI.send(imChannelContext, imPacket);
                                }
                            }
                        }
                    }
                }
            } finally {
                rLock.unlock();
            }
        }
        //修改消息状态和推送已读消息（由于这里ws推送消息快于数据库操作，可能数据库还没有保存好这条消息）
        pushMessageUtil.updateMessageStatus(messageBody);
        return null;
    }

    @Override
    public Command command() {
        return Command.COMMAND_MESSAGE_READ_REQ;
    }

}
