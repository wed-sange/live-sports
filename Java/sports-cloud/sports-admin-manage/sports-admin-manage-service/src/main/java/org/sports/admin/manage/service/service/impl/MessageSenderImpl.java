package org.sports.admin.manage.service.service.impl;

import com.alibaba.fastjson2.JSON;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.enums.MessageType;
import org.sports.admin.manage.dao.req.PushToAllWsDto;
import org.sports.admin.manage.service.config.SportsMessageProperties;
import org.sports.admin.manage.service.service.MessageSender;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

@EnableConfigurationProperties(SportsMessageProperties.class)
@RequiredArgsConstructor
@Service
@Slf4j
public class MessageSenderImpl implements MessageSender {
    private final SportsMessageProperties properties;
    private final RedissonClient redissonClient;

    @Override
    public void send(@Nonnull MessageType messageType, @Nullable Object body) {
        Assert.notNull(messageType, "消息类型不能为空");
        RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue(properties.getQueue());
        PushToAllWsDto dto = new PushToAllWsDto();
        dto.setCommand(messageType.getCommand());
        dto.setData(body);
        //就这样吧
        log.info("发送消息到队列：{}",JSON.toJSONString(dto));
        blockingQueue.putAsync(JSON.toJSONString(dto));
    }



}
