package org.sports.chat.service.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.jim.core.packets.Command;
import org.jim.server.JimServerAPI;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.enums.MessageType;
import org.sports.chat.service.entity.PushToAllWsDto;
import org.sports.chat.service.service.IChatService;
import org.sports.chat.service.service.ISystemMessageService;

@Slf4j
public class PushMessageListener implements Runnable {


    private ISystemMessageService iSystemMessageService = SpringUtil.getBean(ISystemMessageService.class);

    private IChatService chatService = SpringUtil.getBean(IChatService.class);

    private RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);
    private LiveGroupCache liveGroupCache = SpringUtil.getBean(LiveGroupCache.class);
    private PushMessageUtil pushMessageUtil = SpringUtil.getBean(PushMessageUtil.class);
    private String consumerName;

    private static Boolean running = true;

    public PushMessageListener(String consumerName) {
        this.consumerName = consumerName;
    }

    @Override
    public void run() {
        while (running) {
            try {
                RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue("sports:message:queue");
                if (!blockingQueue.isEmpty()) {
                    String message = blockingQueue.poll();
                    PushToAllWsDto dto = JSONObject.parseObject(message, PushToAllWsDto.class);
                    if (ObjectUtil.isNull(dto) || ObjectUtil.isNull(dto.getCommand())) {
                        continue;
                    }
                    // 直播用户缓存刷新
                    if (MessageType.LIVE_USER_CACHE_REFRESH.getCommand().equals(dto.getCommand())) {
                        liveGroupCache.refreshCache();
                        continue;
                    }
                    if (ObjectUtil.isNull(dto.getData())) {
                        continue;
                    }
                    log.info("收到队列消息：{}", JSON.toJSONString(dto));
                    if (MessageType.FEEDBACK_REPLY.getCommand().equals(dto.getCommand())) {
                        JSONObject jsonObject = JSONObject.parse(dto.getData().toString());
                        String userId = jsonObject.getString("userId");
                        pushMessageUtil.pushAppUserFeedback(userId, dto.getData());
                        pushMessageUtil.pushUnReadMsgCount(userId, null, IdentityType.APP_USER.getCode());
                        continue;
                    }
                    if (MessageType.USER_UNREAD_MSG_COUNT.getCommand().equals(dto.getCommand())) {
                        //推送用户未读数量
                        JSONObject jsonObject = JSONObject.parse(dto.getData().toString());
                        String userId = jsonObject.getString("userId");
                        String anchorId = jsonObject.getString("anchorId");
                        Integer identity = jsonObject.getInteger("identity");
                        pushMessageUtil.pushUnReadMsgCount(userId, anchorId, identity);
                        continue;
                    }
                    if (MessageType.FORBIDDEN_APP_USER.getCommand().equals(dto.getCommand())) {
                        //推送用户禁言通知
                        JSONObject jsonObject = JSONObject.parse(dto.getData().toString());
                        String userId = jsonObject.getString("userId");
                        pushMessageUtil.pushAppUserForbidden(userId, dto.getData());
                        pushMessageUtil.pushUnReadMsgCount(userId, null, IdentityType.APP_USER.getCode());
                        continue;
                    }
                    if (MessageType.UNFORBIDDEN_APP_USER.getCommand().equals(dto.getCommand())) {
                        //推送用户解禁通知
                        JSONObject jsonObject = JSONObject.parse(dto.getData().toString());
                        String userId = jsonObject.getString("userId");
                        pushMessageUtil.pushAppUserUnForbidden(userId, dto.getData());
                        pushMessageUtil.pushUnReadMsgCount(userId, null, IdentityType.APP_USER.getCode());
                        continue;
                    }

                    //极光推送开赛和完赛通知
                    if (MessageType.START_MATCH.getCommand().equals(dto.getCommand()) || MessageType.FINISHED_MATCH.getCommand().equals(dto.getCommand())) {
                        pushMessageUtil.jPushMatch(dto.getData());
                        continue;
                    }
                    // 比赛分数变动
                    if (MessageType.MATCH_SCORE.getCommand().equals(dto.getCommand())) {
                        iSystemMessageService.sendMessageToAllUser(Command.MATCH_SCORE_PUSH, message);
                        continue;
                    }
                    // 新闻有更新
                    if (MessageType.NEWS_REFRESH.getCommand().equals(dto.getCommand())) {
                        iSystemMessageService.sendMessageToAllUser(Command.NEWS_REFRESH, message);
                        continue;
                    }
                    if (MessageType.KICK_OUT_USER.getCommand().equals(dto.getCommand())) {
                        //推送用户被踢出直播间通知
                        JSONObject jsonObject = JSONObject.parse(dto.getData().toString());
                        String userId = jsonObject.getString("userId");
                        String groupId = jsonObject.getString("bizId");
                        //踢出房间群组
                        JimServerAPI.unbindGroup(groupId, userId);
                        pushMessageUtil.pushAppUserKickOut(userId, dto.getData());//通知用户
                        pushMessageUtil.pushUnReadMsgCount(userId, null, IdentityType.APP_USER.getCode());//通知用户
                        pushMessageUtil.pushGroupUserKickOut(groupId, userId);//通知直播间其他人
                        continue;
                    }
                    JSONObject jsonObject = JSONObject.parse(dto.getData().toString());
                    String groupId = jsonObject.getString("id");
                    String anchorId = jsonObject.getString("anchorId");
                    // 开播只需要调用开播方法即可
                    if (MessageType.ANCHOR_OPEN.getCommand().equals(dto.getCommand())) {
                        iSystemMessageService.sendMessageToAllUser(Command.COMMAND_LIVE_OPEN, message);
                        //通知关注此主播的用户主播开播
                        pushMessageUtil.pushAppUserLiving(groupId, anchorId);
                        chatService.openLiveByAnchor(groupId, jsonObject.getString("nickName"), anchorId);
                        continue;
                    }
                    // 关播需要调用关播方法，同时通知组内用户端关闭直播流
                    if (MessageType.ANCHOR_CLOSE.getCommand().equals(dto.getCommand())) {
                        //iSystemMessageService.sendMessageToGroupUser(Command.COMMAND_LIVE_CLOSE, groupId, message);
                        iSystemMessageService.sendMessageToAllUser(Command.COMMAND_LIVE_CLOSE, message);
                        chatService.closeLiveByAnchor(groupId, anchorId);
                        continue;
                    }
                    // 更新直播流只需要通知组内用户更新直播流即可
                    if (MessageType.ANCHOR_UPDATE_PLAY_URL.getCommand().equals(dto.getCommand())) {
                        iSystemMessageService.sendMessageToGroupUser(Command.COMMAND_LIVE_UPDATE_URL, groupId, message);
                        continue;
                    }

                }
            } catch (Exception e) {
                log.error("消费redis队列失败", e);
            }
        }
    }


    // 预留的关闭推送功能接口
    public static void stop() {
        running = false;
    }

    // 预留的开启推送功能接口
    public static void start() {
        running = true;
    }
}
