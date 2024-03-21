package org.sports.chat.service.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.jim.core.ImChannelContext;
import org.jim.core.ImPacket;
import org.jim.core.ImStatus;
import org.jim.core.packets.*;
import org.jim.core.ws.Opcode;
import org.jim.core.ws.WsResponsePacket;
import org.jim.server.JimServerAPI;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.*;
import org.sports.admin.manage.dao.entity.base.AppUserRegIdDO;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.enums.LiveStatus;
import org.sports.admin.manage.dao.enums.PushType;
import org.sports.admin.manage.dao.mapper.ChatMessageMapper;
import org.sports.admin.manage.dao.mapper.LiveUserMapper;
import org.sports.admin.manage.dao.repository.IMyFollowDao;
import org.sports.admin.manage.service.dto.LiveDTO;
import org.sports.admin.manage.service.dto.MatchPushDTO;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.AppNoticeConfigService;
import org.sports.admin.manage.service.service.IAppUserRegIdService;
import org.sports.admin.manage.service.service.ILiveService;
import org.sports.admin.manage.service.vo.LiveVo;
import org.sports.chat.service.component.JpushClient;
import org.sports.chat.service.constant.CacheConstant;
import org.sports.chat.service.constant.WsResponseStatus;
import org.sports.chat.service.service.IChatService;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PushMessageUtil {
    @Resource
    private IChatService chatService;
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ChatMessageMapper chatMessageMapper;

    @Resource
    private IMyFollowDao myFollowDao;

    @Resource
    private ILiveService liveService;

    @Resource
    private LiveUserMapper liveUserMapper;
    @Resource
    private DistributedCache distributedCache;
    @Resource
    private JpushClient jpushClient;

    @Resource
    private IAppUserRegIdService appUserRegIdService;
    @Resource
    private AppNoticeConfigService appNoticeConfigService;


    public void pushUnReadMsgCount(String userId, String anchorId, Integer identity) {
        UserUnReadMsgCount userUnReadMsgCount = new UserUnReadMsgCount();
        RespBody unreadBody = new RespBody(Command.USER_TOTAL_UNREAD_MESSAGE, userUnReadMsgCount);
        unreadBody.setCode(ImStatus.C10000.getCode());
        unreadBody.setSuccess(WsResponseStatus.TRUE);
        if (IdentityType.ANCHOR.getCode().equals(identity)) { //用户发送给主播需要通知主播，助理
            Object o = redissonClient.getBucket(CacheConstant.CHAT_LIVE_GROUP_USER_SESSION + CacheConstant.ANCHOR + anchorId).get();
            List<String> operateList = (List<String>) o;
            RespBody userMessage = null;
            if (!CollectionUtils.isEmpty(operateList)) {
                userMessage = getAnchorReadUserMessage(userId, anchorId);
                long totalCount = chatService.getUnreadMsgCount(anchorId, IdentityType.ANCHOR.getCode());
                userUnReadMsgCount.setTotalCount(totalCount);
                JimServerAPI.sendToUser(anchorId, new ImPacket(Command.USER_TOTAL_UNREAD_MESSAGE, unreadBody.toByte()));
                for (String operate : operateList) {
                    totalCount = chatService.getUnreadMsgCount(operate, IdentityType.OPERATOR.getCode());
                    userUnReadMsgCount.setTotalCount(totalCount);
                    JimServerAPI.sendToUser(operate, new ImPacket(Command.USER_TOTAL_UNREAD_MESSAGE, unreadBody.toByte()));
                    if (Objects.nonNull(userMessage)) {
                        JimServerAPI.sendToUser(operate, new ImPacket(Command.ANCHOR_READ_USER_MESSAGE, userMessage.toByte()));
                    }
                }
            }
        } else { //更新用户的未读数量
            //如果是主播发送给用户的需要推送用户未读消息总数
            long totalCount = chatService.getUnreadMsgCount(userId, IdentityType.APP_USER.getCode());
            userUnReadMsgCount.setTotalCount(totalCount);
            JimServerAPI.sendToUser(userId, new ImPacket(Command.USER_TOTAL_UNREAD_MESSAGE, unreadBody.toByte()));
        }

    }

    private RespBody getAnchorReadUserMessage(String userId, String anchorId) {
        AnchorReadUserMessage userMessage = new AnchorReadUserMessage();
        LiveUserDO anchorInfoByAnchorId = chatService.getAnchorInfoByAnchorId(anchorId);
        if (Objects.nonNull(anchorInfoByAnchorId)) {
            userMessage.setAnchorId(anchorId);
            userMessage.setAvatar(anchorInfoByAnchorId.getHead());
            userMessage.setNick(anchorInfoByAnchorId.getNickName());
            userMessage.setNoReadSum(chatService.liveTotalCountNoReadMsg(anchorId));
            AppUserDO userInfoByUserID = chatService.getUserInfoByUserID(userId);
            if (Objects.nonNull(userInfoByUserID)) {
                ChatMessageDO chatMessageDO = chatService.anchorUserLastMessage(anchorId, userId);
                ChatUserInfo chatUserInfo = new ChatUserInfo();
                chatUserInfo.setUserId(userId);
                chatUserInfo.setAvatar(userInfoByUserID.getHead());
                chatUserInfo.setNick(userInfoByUserID.getName());
                chatUserInfo.setNoReadSum(chatService.userToLiveTotalCountNoReadMsg(anchorId, userId));
                if (Objects.nonNull(chatMessageDO)) {
                    chatUserInfo.setId(chatMessageDO.getId());
                    chatUserInfo.setCreateTime(chatMessageDO.getCreateTime().getTime());
                    chatUserInfo.setMsgType(chatMessageDO.getMsgType());
                    chatUserInfo.setContent(chatMessageDO.getContent());
                    chatUserInfo.setIdentityType(chatMessageDO.getIdentityType());
                }
                userMessage.setChatUserInfo(chatUserInfo);
                RespBody unreadBody = new RespBody(Command.ANCHOR_READ_USER_MESSAGE, userMessage);
                unreadBody.setCode(ImStatus.C10000.getCode());
                unreadBody.setSuccess(WsResponseStatus.TRUE);
                return unreadBody;
            }
        }
        return null;
    }


    public void updateMessageStatus(MessageReadBody messageBody) {
        // 1 表示已读，如果消息已读则进行修改
        if (!YnEnum.ONE.getValue().equals(messageBody.getRead())) {
            return;
        }
        ChatMessageDO chatMessageDO = chatMessageMapper.selectById(Long.valueOf(messageBody.getMessageId()));
        if (Objects.isNull(chatMessageDO)) {
            log.info("消息未入库这里存入redis");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            chatMessageDO = chatMessageMapper.selectById(Long.valueOf(messageBody.getMessageId()));
        }
        if (UserChannelEnum.APP.getFlag().equals(String.valueOf(messageBody.getChannelType()))) { //app发送消息修改已读必须消息是主播端发的
            if (0 != chatMessageDO.getIdentityType().intValue()) {
                log.info("主播消息修改成已读状态");
                chatMessageDO.setReadable(1);
                chatMessageMapper.updateById(chatMessageDO);
                pushUnReadMsgCount(chatMessageDO.getToId(), chatMessageDO.getAnchorId(), IdentityType.APP_USER.getCode());
            }
        } else if (UserChannelEnum.LIVE.getFlag().equals(String.valueOf(messageBody.getChannelType()))) {
            if (0 == chatMessageDO.getIdentityType().intValue()) {
                log.info("用户消息修改成已读状态");
                chatMessageDO.setReadable(1);
                chatMessageMapper.updateById(chatMessageDO);
                pushUnReadMsgCount(chatMessageDO.getFromId(), chatMessageDO.getAnchorId(), IdentityType.ANCHOR.getCode());
            }
        }
    }

    public void pushUnReadMsgCount(ChatBody chatBody) {
        UserUnReadMsgCount userUnReadMsgCount = new UserUnReadMsgCount();
        RespBody unreadBody = new RespBody(Command.USER_TOTAL_UNREAD_MESSAGE, userUnReadMsgCount);
        unreadBody.setCode(ImStatus.C10000.getCode());
        unreadBody.setSuccess(WsResponseStatus.TRUE);
        if (chatBody.getIdentityType().intValue() == IdentityType.APP_USER.getCode()) { //用户发送给主播需要通知主播，助理，运营
            long totalCount = chatService.getUnreadMsgCount(chatBody.getTo(), IdentityType.ANCHOR.getCode());
            userUnReadMsgCount.setTotalCount(totalCount);
            Object o = redissonClient.getBucket(CacheConstant.CHAT_LIVE_GROUP_USER_SESSION + CacheConstant.ANCHOR + chatBody.getAnchorId()).get();
            List<String> archors = (List<String>) o;
            if (!CollectionUtils.isEmpty(archors)) {
                archors.forEach(archor -> {
                    long cont = chatService.getUnreadMsgCount(archor, IdentityType.OPERATOR.getCode());
                    userUnReadMsgCount.setTotalCount(cont);
                    JimServerAPI.sendToUser(archor, new ImPacket(Command.USER_TOTAL_UNREAD_MESSAGE, unreadBody.toByte()));
                });
            }
        } else { //主播发送用户
            //如果是主播发送给用户的需要推送用户未读消息总数
            long totalCount = chatService.getUnreadMsgCount(chatBody.getTo(), IdentityType.APP_USER.getCode());
            userUnReadMsgCount.setTotalCount(totalCount);
            JimServerAPI.sendToUser(chatBody.getTo(), new ImPacket(Command.USER_TOTAL_UNREAD_MESSAGE, unreadBody.toByte()));
        }
    }

    public void pushAppUserForbidden(String userId, Object data) {
        RespBody body = new RespBody(Command.FORBIDDEN_APP_USER_MESSAGE, data);
        body.setCode(ImStatus.C10000.getCode());
        body.setSuccess(WsResponseStatus.TRUE);
        JimServerAPI.sendToUser(userId, new ImPacket(Command.FORBIDDEN_APP_USER_MESSAGE, body.toByte()));
    }

    public void pushAppUserUnForbidden(String userId, Object data) {
        RespBody body = new RespBody(Command.UNFORBIDDEN_APP_USER_MESSAGE, data);
        body.setCode(ImStatus.C10000.getCode());
        body.setSuccess(WsResponseStatus.TRUE);
        JimServerAPI.sendToUser(userId, new ImPacket(Command.UNFORBIDDEN_APP_USER_MESSAGE, body.toByte()));
    }

    public void pushAppUserFeedback(String userId, Object data) {
        RespBody body = new RespBody(Command.COMMAND_SYSTEM_FEEDBACK, data);
        body.setCode(ImStatus.C10000.getCode());
        body.setSuccess(WsResponseStatus.TRUE);
        JimServerAPI.sendToUser(userId, new ImPacket(Command.COMMAND_SYSTEM_FEEDBACK, body.toByte()));
    }

    public void pushUnReadMsgCount(User user) {
        long totalCount = chatService.getUnreadMsgCount(user.getUserId(), user.getIdentity());
        UserUnReadMsgCount userUnReadMsgCount = new UserUnReadMsgCount();
        userUnReadMsgCount.setTotalCount(totalCount);
        RespBody respBody = new RespBody(Command.USER_TOTAL_UNREAD_MESSAGE, userUnReadMsgCount);
        respBody.setCode(ImStatus.C10000.getCode());
        respBody.setSuccess(WsResponseStatus.TRUE);
        JimServerAPI.sendToUser(user.getUserId(), new ImPacket(Command.USER_TOTAL_UNREAD_MESSAGE, respBody.toByte()));

    }

    /**
     * 推送最热主播
     *
     * @param userId
     */
    public void pushAppUserMaxHotLive(String userId) {
        //30秒推送一次
        String userPushKey = CacheUtil.buildKey(CacheConstant.PUSH_USER_FOLLOW_ANCHOR_MAX_HOT, userId);//同一个用户30秒推一次
        if (Objects.nonNull(distributedCache.get(userPushKey))) {
            return;
        }
        List<MyFollowDO> myFollowAnchorList = myFollowDao.getMyFollowAnchorList(Long.valueOf(userId));
        if (CollectionUtils.isEmpty(myFollowAnchorList)) {
            return;
        }
        List<Long> anchorIds = myFollowAnchorList.stream().map(MyFollowDO::getBizId).collect(Collectors.toList());
        //查询主播中热度最高的直播信息
        List<LiveVo> livingList = liveService.getLivingByUserIds(anchorIds);
        if (CollectionUtils.isEmpty(livingList)) {
            return;
        }
        LiveVo max = livingList.stream().max(Comparator.comparing(LiveVo::getHotValue)).orElse(null);
        if (Objects.isNull(max)) {
            return;
        }
        LiveUserDO liveUserDO = liveUserMapper.selectById(max.getUserId());
        LiveDTO liveDTO = new LiveDTO();
        BeanUtil.convert(max, liveDTO);
        liveDTO.setNickName(liveUserDO.getNickName());
        liveDTO.setUserLogo(liveUserDO.getHead());
        liveDTO.setMatchType(max.getMatchType().getCode());
        String pushLiveKey = CacheUtil.buildKey(CacheConstant.PUSH_USER_FOLLOW_ANCHOR_MAX_HOT, userId, liveDTO.getId().toString());//同一个直播只推一次
        if (Objects.isNull(distributedCache.get(pushLiveKey))) {
            RespBody respBody = new RespBody(Command.FOLLOW_ANCHOR_OPEN_LIVE, liveDTO);
            respBody.setCode(ImStatus.C10000.getCode());
            respBody.setSuccess(WsResponseStatus.TRUE);
            JimServerAPI.sendToUser(userId, new ImPacket(Command.FOLLOW_ANCHOR_OPEN_LIVE, respBody.toByte()));
            distributedCache.put(pushLiveKey, liveDTO, 1, TimeUnit.DAYS);
            distributedCache.put(userPushKey, liveDTO, 30, TimeUnit.SECONDS);
        }
    }

    /**
     * 有关注此主播的用户需要推送直播信息
     *
     * @param livingId
     * @param anchorId
     */
    public void pushAppUserLiving(String livingId, String anchorId) {

        //主播粉丝数量
        List<MyFollowDO> followAnchorList = myFollowDao.getFansList(Long.valueOf(anchorId));
        if (CollectionUtils.isEmpty(followAnchorList)) {
            return;
        }
        List<Long> userIds = followAnchorList.stream().map(MyFollowDO::getUserId).collect(Collectors.toList());
        //查询主播中热度最高的直播信息
        LiveDO liveDO = liveService.getById(Long.valueOf(livingId));
        if (Objects.nonNull(liveDO) && !LiveStatus.LIVING.equals(liveDO.getLiveStatus())) {
            return;
        }
        LiveUserDO liveUserDO = liveUserMapper.selectById(liveDO.getUserId());
        LiveDTO liveDTO = new LiveDTO();
        BeanUtil.convert(liveDO, liveDTO);
        liveDTO.setNickName(liveUserDO.getNickName());
        liveDTO.setUserLogo(liveUserDO.getHead());
        liveDTO.setMatchType(liveDO.getMatchType().getCode());
        RespBody respBody = new RespBody(Command.FOLLOW_ANCHOR_OPEN_LIVE, liveDTO);
        respBody.setCode(ImStatus.C10000.getCode());
        respBody.setSuccess(WsResponseStatus.TRUE);
        userIds.forEach(userId -> {
            String userIdStr = String.valueOf(userId);
            //30秒推送一次app内部
            String pushKey = CacheUtil.buildKey(CacheConstant.PUSH_USER_FOLLOW_ANCHOR_MAX_HOT, userIdStr);
            if (Objects.isNull(distributedCache.get(pushKey))) {
                JimServerAPI.sendToUser(userIdStr, new ImPacket(Command.FOLLOW_ANCHOR_OPEN_LIVE, respBody.toByte()));
                distributedCache.put(pushKey, liveDTO, 30, TimeUnit.SECONDS);
            }
        });
        jPushUserLiving(userIds, liveDTO);

    }

    /**
     * 推送用户直播中
     *
     * @param userIds
     * @param liveDTO
     */
    private void jPushUserLiving(List<Long> userIds, LiveDTO liveDTO) {
        //5分钟推送一次
        List<String> pushRegIds = getPushRegIds(userIds);
        if (CollectionUtils.isEmpty(pushRegIds)) {
            return;
        }
        Map<String, Object> extras = Maps.newHashMap();
        if (Objects.nonNull(liveDTO.getId())) {
            extras.put("liveId", String.valueOf(liveDTO.getId()));
        }
        extras.put("isPureFlow", false);
        if (Objects.nonNull(liveDTO.getUserId())) {
            extras.put("anchorId", String.valueOf(liveDTO.getUserId()));
        }
        if (Objects.nonNull(liveDTO.getMatchId())) {
            extras.put("matchId", liveDTO.getMatchId());
        }
        if (Objects.nonNull(liveDTO.getMatchType())) {
            extras.put("matchType", liveDTO.getMatchType());
        }
        extras.put("pushType", PushType.ANCHOR_OPEN_LIVE.getCode());
        String alert = MessageFormat.format("{0} 正在直播 {1} VS {2}", liveDTO.getNickName(), liveDTO.getHomeTeamName(), liveDTO.getAwayTeamName());

        while (pushRegIds.size() > 0) {
            Map<String, List<String>> to = Maps.newHashMap();
            List<String> subList = Lists.newArrayList();
            if (pushRegIds.size() > 1000) {
                subList.addAll(pushRegIds.subList(0, 1000));
            } else {
                subList.addAll(pushRegIds.subList(0, pushRegIds.size()));
            }
            to.put("registration_id", subList);
            jpushClient.sendPush(jpushClient.buildPushRequestBody(to, Arrays.asList("android", "ios"), "主播通知", alert, extras));
            pushRegIds.removeAll(subList);
        }
    }

    public static void main(String[] args) {
        String str = "{\n" +
                "        \"awayTeamLogo\":\"https://cdn.sportnanoapi.com/football/team/d5ebaa82eb19a655107b0e4ebe65f193.png\",\n" +
                "        \"awayTeamName\":\"葡萄牙U18\",\n" +
                "        \"homeTeamLogo\":\"https://cdn.sportnanoapi.com/football/team/6bec992038b411f2966fab9932d3d7a5.png\",\n" +
                "        \"homeTeamName\":\"西班牙U18\",\n" +
                "        \"id\":1714100716767805440,\n" +
                "        \"matchId\":4026449,\n" +
                "        \"matchTime\":1697472000,\n" +
                "        \"matchType\":1,\n" +
                "        \"nickName\":\"cQ521510\",\n" +
                "        \"userId\":1713826210274799616\n" +
                "    }";
        LiveDTO liveDTO = JSON.toJavaObject(str, LiveDTO.class);
        String alert = MessageFormat.format("{0} 正在直播 {1} VS {2}", liveDTO.getNickName(), liveDTO.getHomeTeamName(), liveDTO.getAwayTeamName());
        System.out.println("args = " + alert);
    }

    private List<String> getPushRegIds(List<Long> userIds) {
        List<AppNoticeConfigDO> noticeConfig = appNoticeConfigService.getNoticeConfig(userIds);
        //查询出不开启通知的用户
        List<Long> notOpenUserIds = noticeConfig.stream().filter(item -> YnEnum.ZERO.getValue().equals(item.getYnFollowMatch())).map(item -> item.getUserId()).collect(Collectors.toList());
        userIds.removeAll(notOpenUserIds);
        //是否绑定极光推送regId
        List<AppUserRegIdDO> regIdList = appUserRegIdService.getUserRegIds(userIds);
        if (CollectionUtils.isEmpty(regIdList)) {
            return null;
        }
        List<String> userRegIds = regIdList.stream().map(AppUserRegIdDO::getRegId).collect(Collectors.toList());
        List<String> filterRegIds = userRegIds.stream().filter(item -> Objects.isNull(distributedCache.get(CacheUtil.buildKey(CacheConstant.JPUSH_ANCHOR_LIVING, item)))).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(filterRegIds)) {
            return null;
        }
        //todo 测试修改推送时间间隔，正式需要修改回来
        filterRegIds.forEach(item -> distributedCache.put(CacheUtil.buildKey(CacheConstant.JPUSH_ANCHOR_LIVING, item), true, 1, TimeUnit.MINUTES));
        return filterRegIds;
    }

    /**
     * 推送比赛开始和结束
     *
     * @param data
     */
    public void jPushMatch(Object data) {
        log.info("开赛和完赛推送消息：{}", data.toString());
        List<MatchPushDTO> pushList = JSON.parseArray(data.toString(), MatchPushDTO.class);
        pushList.forEach(item -> {
            if (!CollectionUtils.isEmpty(item.getRegIds())) {
                while (item.getRegIds().size() > 0) {
                    Map<String, List<String>> to = Maps.newHashMap();
                    Map<String, Object> extras = Maps.newHashMap();
                    List<String> subList = Lists.newArrayList();
                    if (item.getRegIds().size() > 1000) {
                        subList.addAll(item.getRegIds().subList(0, 1000));
                    } else {
                        subList.addAll(item.getRegIds().subList(0, item.getRegIds().size()));
                    }
                    to.put("registration_id", subList);
                    if (Objects.nonNull(item.getLiveId())) {
                        extras.put("liveId", item.getLiveId());
                    }
                    if (Objects.nonNull(item.isPureFlow())) {
                        extras.put("isPureFlow", item.isPureFlow());
                    }
                    if (Objects.nonNull(item.getAnchorId())) {
                        extras.put("anchorId", item.getAnchorId());
                    }
                    if (Objects.nonNull(item.getMatchId())) {
                        extras.put("matchId", item.getMatchId());
                    }
                    if (Objects.nonNull(item.getMatchType())) {
                        extras.put("matchType", item.getMatchType());
                    }
                    if (Objects.nonNull(item.getPushType())) {
                        extras.put("pushType", item.getPushType());
                    }
                    jpushClient.sendPush(jpushClient.buildPushRequestBody(to, Arrays.asList("android", "ios"), item.getTitle(), item.getAlert(), extras));
                    item.getRegIds().removeAll(subList);
                }
            }
        });
    }

    public void pushAppUserKickOut(String userId, Object data) {
        RespBody body = new RespBody(Command.KICK_OUT_USER, data);
        body.setCode(ImStatus.C10000.getCode());
        body.setSuccess(WsResponseStatus.TRUE);
        JimServerAPI.sendToUser(userId, new ImPacket(Command.KICK_OUT_USER, body.toByte()));
    }

    /**
     * 用户退出 房间通知房间内人员
     * @param groupId
     * @param userId
     */
    public void pushGroupUserKickOut(String groupId, String userId) {
        List<ImChannelContext> byUserId = JimServerAPI.getByUserId(userId);
        if(CollectionUtil.isEmpty(byUserId)){
            return;
        }
        User clientUser = null;
        for(ImChannelContext imChannelContext: byUserId){
            User user = imChannelContext.getSessionContext().getImClientNode().getUser();
            if(Objects.nonNull(user)){
                clientUser = user;
                break;
            }
        }
        if(Objects.isNull(clientUser)){
            return;
        }
        User notifyUser = User.builder().userId(clientUser.getUserId()).lvNum(clientUser.getLvNum()).nick(clientUser.getNick()).status(UserStatusType.ONLINE.getStatus()).build();
        OutGroupNotifyRespBody outGroupNotifyRespBody = OutGroupNotifyRespBody.success();
        outGroupNotifyRespBody.setGroup(groupId).setUser(notifyUser);
        //发退出房间通知  COMMAND_EXIT_GROUP_NOTIFY_RESP
        WsResponsePacket wsResponsePacket = new WsResponsePacket();
        wsResponsePacket.setCommand(Command.COMMAND_EXIT_GROUP_NOTIFY_RESP);
        wsResponsePacket.setBody(outGroupNotifyRespBody.toByte());
        wsResponsePacket.setWsOpcode(Opcode.TEXT);
        JimServerAPI.sendToGroup(groupId, wsResponsePacket);
    }
}
