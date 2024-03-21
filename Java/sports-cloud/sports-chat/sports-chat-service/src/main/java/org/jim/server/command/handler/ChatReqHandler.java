package org.jim.server.command.handler;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jodd.util.StringUtil;
import org.apache.logging.log4j.util.Strings;
import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImStatus;
import org.jim.core.exception.ImException;
import org.jim.core.packets.ChatBody;
import org.jim.core.packets.ChatType;
import org.jim.core.packets.Command;
import org.jim.core.packets.RespBody;
import org.jim.server.ImServerChannelContext;
import org.jim.server.JimServerAPI;
import org.jim.server.command.AbstractCmdHandler;
import org.jim.server.protocol.ProtocolManager;
import org.jim.server.queue.MsgQueueRunnable;
import org.jim.server.util.ChatKit;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.chat.service.constant.WsResponseStatus;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;

import java.util.Objects;

/**
 * 版本: [1.0]
 * 功能说明: 聊天请求cmd消息命令处理器
 */
public class ChatReqHandler extends AbstractCmdHandler {

    private RedissonClient redissonClient = SpringUtil.getBean(RedissonClient.class);

    @Override
    public ImPacket handler(ImPacket packet, ImChannelContext channelContext) throws ImException {
        ImServerChannelContext imServerChannelContext = (ImServerChannelContext) channelContext;
        if (packet.getBody() == null) {
            throw new ImException("body is null");
        }
        ChatBody chatBody = ChatKit.toChatBody(packet.getBody(), channelContext);
        //聊天数据格式不正确
        if (chatBody == null || ChatType.forNumber(chatBody.getChatType()) == null || StringUtils.isBlank(chatBody.getFromNickName())) {
            return ProtocolManager.Packet.dataInCorrect(channelContext, packet.getSendId());
        }
        // 私聊情况下，消息格式不正确
        if (chatBody.getChatType() == ChatType.CHAT_TYPE_PRIVATE.getNumber()
                && (StringUtil.isBlank(chatBody.getTo()) || StringUtil.isBlank(chatBody.getAnchorId()) || ObjectUtil.isNull(chatBody.getIdentityType()))) {
            return ProtocolManager.Packet.dataInCorrect(channelContext, packet.getSendId());
        }

        packet.setBody(chatBody.toByte());
        String fromId = chatBody.getFrom();
        //游客不允许发送消息
        if ("undefined".equals(fromId) || StringUtil.isBlank(fromId)) {
            return ProtocolManager.Packet.anonymousSend(channelContext, packet.getSendId());
        }
        //检查发送者是否被系统禁言，如果被禁言则不允许发送所有类型消息
        if (redissonClient.getSet(RedisCacheConstant.APP_USER_FORBIDDEN).contains(fromId) || redissonClient.getSet(RedisCacheConstant.LIVE_USER_FORBIDDEN).contains(fromId)) {
            return ProtocolManager.Packet.banUserSend(channelContext, packet.getSendId());
        }
        //检查用户是否被当前主播禁言，如果被禁言此主播所有直播间不允许发言
        if (chatBody.getChatType() == ChatType.CHAT_TYPE_PUBLIC.getNumber()
        ){
            //通过直播间ID查询主播ID
            String anchorId = (String) redissonClient.getMap(RedisCacheConstant.LIVEID_ANCHORID_MAP).get(chatBody.getGroupId());
            if(Strings.isNotBlank(anchorId) && redissonClient.getSet(CacheUtil.buildKey(RedisCacheConstant.APP_ANCHOR_USER_FORBIDDEN, anchorId)).contains(fromId)) {
                return ProtocolManager.Packet.banUserSend(channelContext, packet.getSendId());
            }
        }
        // 先生成ID，好做已读未读管理
        chatBody.setId(SnowflakeIdUtil.nextIdStr());
        //异步调用业务处理消息接口
        MsgQueueRunnable msgQueueRunnable = getMsgQueueRunnable(imServerChannelContext);
        msgQueueRunnable.addMsg(chatBody);
        msgQueueRunnable.executor.execute(msgQueueRunnable);
        RespBody respBody = new RespBody(Command.COMMAND_CHAT_REQ, chatBody);
        respBody.setCode(ImStatus.C10000.getCode());
        respBody.setSuccess(WsResponseStatus.TRUE);
        ImPacket chatPacket = new ImPacket(Command.COMMAND_CHAT_REQ, respBody.toByte());
        //设置同步序列号;
        chatPacket.setSynSeq(packet.getSynSeq());
//        ImServerConfig imServerConfig = ImConfig.Global.get();
//		boolean isStore = ImServerConfig.ON.equals(imServerConfig.getIsStore());
        //私聊(带主播工作组概念的私聊，这种私聊不判断在线与不在线了)
        if (ChatType.CHAT_TYPE_PRIVATE.getNumber() == chatBody.getChatType()) {
//			if(ChatKit.isOnline(toId, isStore)){
            JimServerAPI.sendToClass(chatBody, chatPacket, channelContext);
            //发送成功响应包
            return ProtocolManager.Packet.success(channelContext, packet.getSendId());
//			}else{
            //用户不在线响应包
//				return ProtocolManager.Packet.offline(channelContext);
//			}
            //群聊
        } else if (ChatType.CHAT_TYPE_PUBLIC.getNumber() == chatBody.getChatType()) {
            String groupId = chatBody.getGroupId();
            //如果不在群组中，需要加入到群组中
            if(!JimServerAPI.isInGroup(groupId, channelContext)){
                JimServerAPI.bindGroup(channelContext, groupId);
            }
            JimServerAPI.sendToGroup(groupId, chatPacket);
            //发送成功响应包
            return ProtocolManager.Packet.success(channelContext, packet.getSendId());
        }
        return null;
    }


    @Override
    public Command command() {
        return Command.COMMAND_CHAT_REQ;
    }

    /**
     * 获取聊天业务处理异步消息队列
     *
     * @param imServerChannelContext IM通道上下文
     * @return
     */
    private MsgQueueRunnable getMsgQueueRunnable(ImServerChannelContext imServerChannelContext) {
        MsgQueueRunnable msgQueueRunnable = (MsgQueueRunnable) imServerChannelContext.getMsgQue();
        if (Objects.nonNull(msgQueueRunnable.getProtocolCmdProcessor())) {
            return msgQueueRunnable;
        }
        synchronized (MsgQueueRunnable.class) {
            msgQueueRunnable.setProtocolCmdProcessor(this.getSingleProcessor());
        }
        return msgQueueRunnable;
    }
}
