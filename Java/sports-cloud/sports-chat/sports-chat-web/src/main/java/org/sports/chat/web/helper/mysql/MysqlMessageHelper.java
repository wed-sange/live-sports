//package org.sports.chat.web.helper.mysql;
//
//import cn.hutool.extra.spring.SpringUtil;
//import com.google.common.collect.Lists;
//import org.apache.commons.collections4.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.jim.core.cache.redis.RedisCacheManager;
//import org.jim.core.config.ImConfig;
//import org.jim.core.listener.ImStoreBindListener;
//import org.jim.core.message.AbstractMessageHelper;
//import org.jim.core.packets.*;
//import org.jim.server.helper.redis.RedisMessageHelper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.sports.chat.service.entity.ChatMessageDO;
//import org.sports.chat.service.entity.ChatUserDO;
//import org.sports.chat.service.entity.GroupUserDO;
//import org.sports.chat.service.enums.SentType;
//import org.sports.chat.service.service.IChatService;
//import org.springframework.stereotype.Component;
//
//import java.time.ZoneOffset;
//import java.util.*;
//import java.util.stream.Collectors;
//
//
///**
// * Mysql获取持久化+同步消息助手;
// *
//
// * @date 2018年4月10日 下午4:06:26
// */
//@Component
//public class MysqlMessageHelper extends AbstractMessageHelper {
//
//    private Logger log = LoggerFactory.getLogger(RedisMessageHelper.class);
//
//    private static final String SUFFIX = ":";
//
//    private IChatService chatService = SpringUtil.getBean(IChatService.class);
//
//
//    public MysqlMessageHelper() {
//        this.imConfig = ImConfig.Global.get();
//    }
//
//    @Override
//    public ImStoreBindListener getBindListener() {
//
//        return new MysqlImStoreBindListener(imConfig, this);
//    }
//
//    @Override
//    public boolean isOnline(String userId) {
//        ChatUserDO userDO = chatService.getUserInfoByUserID(userId);
//        if (Objects.isNull(userDO)) {
//            return false;
//        }
//        if (UserStatusType.ONLINE.getNumber() == userDO.getStatus()) {
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public List<String> getGroupUsers(String groupId) {
//        return null;
//    }
//
//
//    @Override
//    public void writeMessage(String timelineTable, String timelineId, ChatBody chatBody) {
//        ChatMessageDO messageDO = new ChatMessageDO();
//        messageDO.setFromId(chatBody.getFrom());
//        messageDO.setToId(chatBody.getTo());
//        messageDO.setCmd(chatBody.getCmd());
//        messageDO.setMsgType(chatBody.getMsgType());
//        messageDO.setChatType(chatBody.getChatType());
//        messageDO.setAvatar(null);
//        messageDO.setNick(null);
//        messageDO.setContent(chatBody.getContent());
//        messageDO.setIdentityType(null);
//        messageDO.setLevel(null);
//        messageDO.setGroupId(chatBody.getGroupId());
//        if (STORE.equals(timelineTable)) {//已推送
//            messageDO.setSent(SentType.YES.getCode());
//        } else { //待推送，离线消息
//            messageDO.setSent(SentType.NO.getCode());
//        }
//        chatService.saveChatMessage(messageDO);
//    }
//
//
//    @Override
//    public void addGroupUser(String userId, String groupId) {
//        chatService.addGroupUser(userId, groupId);
//        // 存入redis保证查询
//        List<String> users = RedisCacheManager.getCache(GROUP).listGetAll(groupId);
//        if(!users.contains(userId)){
//            RedisCacheManager.getCache(GROUP).listPushTail(groupId, userId);
//        }
//    }
//
//    @Override
//    public void removeGroupUser(String userId, String groupId) {
//        chatService.removeGroupUser(groupId, userId);
//        RedisCacheManager.getCache(GROUP).listRemove(groupId, userId);
//    }
//
//    @Override
//    public UserMessageData getFriendsOfflineMessage(String userId, String fromUserId) {
//        List<ChatMessageDO> chatMessageDOS = chatService.getNotSendMessage(userId, fromUserId);
//        List<ChatBody> messageDataList = transChatMessage(chatMessageDOS);
//        return putFriendsMessage(new UserMessageData(userId), messageDataList, null);
//    }
//
//    private List<ChatBody> transChatMessage(List<ChatMessageDO> chatMessageDOS) {
//        List<ChatBody> returnList = Lists.newArrayList();
//        chatMessageDOS.forEach(item -> {
//            ChatBody chatBody = ChatBody.newBuilder()
//                    .chatType(item.getChatType())
//                    .content(item.getContent())
//                    .from(item.getFromId())
//                    .to(item.getToId())
//                    .msgType(item.getMsgType())
//                    .setCmd(item.getCmd())
//                    .setCreateTime(item.getCreateTime().toInstant(ZoneOffset.UTC).toEpochMilli())
//                    .setId(String.valueOf(item.getId()))
//                    .groupId(item.getGroupId()).build();
//            returnList.add(chatBody);
//        });
//        return returnList;
//    }
//
//    @Override
//    public UserMessageData getFriendsOfflineMessage(String userId) {
//        UserMessageData messageData = new UserMessageData(userId);
//        try {
//            List<ChatMessageDO> chatMessageDOS = chatService.getNotSendMessage(userId, null);
//            List<ChatBody> messageList = transChatMessage(chatMessageDOS);
//            putFriendsMessage(messageData, messageList, null);
//        } catch (Exception e) {
//            log.error(e.toString(), e);
//        }
//        return messageData;
//    }
//
//    @Override
//    public UserMessageData getGroupOfflineMessage(String userId, String groupId) {
//        return null;
//    }
//
//    @Override
//    public UserMessageData getFriendHistoryMessage(String userId, String fromUserId, Double beginTime, Double endTime, Integer offset, Integer count) {
//
//        return null;
//    }
//
//    @Override
//    public UserMessageData getGroupHistoryMessage(String userId, String groupId, Double beginTime, Double endTime, Integer offset, Integer count) {
//        return null;
//    }
//
//
//    /**
//     * 放入用户群组消息;
//     *
//     * @param userMessage
//     * @param messages
//     */
//    public UserMessageData putGroupMessage(UserMessageData userMessage, List<ChatBody> messages) {
//        if (Objects.isNull(userMessage) || CollectionUtils.isEmpty(messages)) {
//            return userMessage;
//        }
//        messages.forEach(chatBody -> {
//            String groupId = chatBody.getGroupId();
//            if (StringUtils.isEmpty(groupId)) {
//                return;
//            }
//            List<ChatBody> groupMessages = userMessage.getGroups().get(groupId);
//            if (CollectionUtils.isEmpty(groupMessages)) {
//                groupMessages = new ArrayList();
//                userMessage.getGroups().put(groupId, groupMessages);
//            }
//            groupMessages.add(chatBody);
//        });
//        return userMessage;
//    }
//
//    /**
//     * 组装放入用户好友消息;
//     *
//     * @param userMessage
//     * @param messages
//     */
//    public UserMessageData putFriendsMessage(UserMessageData userMessage, List<ChatBody> messages, String friendId) {
//        if (Objects.isNull(userMessage) || CollectionUtils.isEmpty(messages)) {
//            return userMessage;
//        }
//        messages.forEach(chatBody -> {
//            String fromId = chatBody.getFrom();
//            if (StringUtils.isEmpty(fromId)) {
//                return;
//            }
//            String targetFriendId = friendId;
//            if (StringUtils.isEmpty(targetFriendId)) {
//                targetFriendId = fromId;
//            }
//            List<ChatBody> friendMessages = userMessage.getFriends().get(targetFriendId);
//            if (CollectionUtils.isEmpty(friendMessages)) {
//                friendMessages = new ArrayList();
//                userMessage.getFriends().put(targetFriendId, friendMessages);
//            }
//            friendMessages.add(chatBody);
//        });
//        return userMessage;
//    }
//
//    /**
//     * 获取群组所有成员信息
//     *
//     * @param groupId                               群组ID
//     * @param type(0:所有在线用户,1:所有离线用户,2:所有用户[在线+离线])
//     * @return
//     */
//    @Override
//    public Group getGroupUsers(String groupId, Integer type) {
//        if (Objects.isNull(groupId) || Objects.isNull(type)) {
//            log.warn("group:{} or type:{} is null", groupId, type);
//            return null;
//        }
//        List<GroupUserDO> groupUsers = chatService.getGroupUsers(groupId, type);
//        Group group = Group.newBuilder().groupId(groupId).build();
//        if (Objects.isNull(group)) {
//            return null;
//        }
//        List<String> userIds = this.getGroupUsers(groupId);
//        if (CollectionUtils.isEmpty(userIds)) {
//            return null;
//        }
//        List<User> users = new ArrayList<User>();
//        userIds.forEach(userId -> {
//            User user = getUserByType(userId, type);
//            if (Objects.isNull(user)) return;
//            validateStatusByType(type, users, user);
//        });
//        group.setUsers(users);
//        return group;
//    }
//
//    /**
//     * 根据获取type校验是否组装User
//     *
//     * @param type
//     * @param users
//     * @param user
//     */
//    private void validateStatusByType(Integer type, List<User> users, User user) {
//        String status = user.getStatus();
//        if (UserStatusType.ONLINE.getNumber() == type && UserStatusType.ONLINE.getStatus().equals(status)) {
//            users.add(user);
//        } else if (UserStatusType.OFFLINE.getNumber() == type && UserStatusType.OFFLINE.getStatus().equals(status)) {
//            users.add(user);
//        } else if (UserStatusType.ALL.getNumber() == type) {
//            users.add(user);
//        }
//    }
//
//    @Override
//    public User getUserByType(String userId, Integer type) {
//        ChatUserDO userDO = chatService.getUserInfoByUserID(userId);
//        if (Objects.isNull(userDO)) {
//            return null;
//        }
//        User user = User.newBuilder().userId(userDO.getUserId()).nick(userDO.getNick())
//                .avatar(userDO.getAvatar()).terminal(userDO.getTerminal()).sign(userDO.getSign()).build();
//        boolean isOnline = this.isOnline(userId);
//        String status = isOnline ? UserStatusType.ONLINE.getStatus() : UserStatusType.OFFLINE.getStatus();
//        if (UserStatusType.ONLINE.getNumber() == type && isOnline) {
//            user.setStatus(status);
//            return user;
//        } else if (UserStatusType.OFFLINE.getNumber() == type && !isOnline) {
//            user.setStatus(status);
//            return user;
//        } else if (type == UserStatusType.ALL.getNumber()) {
//            user.setStatus(status);
//            return user;
//        }
//        return null;
//    }
//
//    @Override
//    public Group getFriendUsers(String userId, String friendGroupId, Integer type) {
//
//        return null;
//    }
//
//
//    /**
//     * 获取好友分组所有成员信息
//     *
//     * @param userId                                用户ID
//     * @param type(0:所有在线用户,1:所有离线用户,2:所有用户[在线+离线])
//     * @return
//     */
//    @Override
//    public List<Group> getAllFriendUsers(String userId, Integer type) {
//        return null;
//    }
//
//    @Override
//    public List<Group> getAllGroupUsers(String userId, Integer type) {
//        if (Objects.isNull(userId)) {
//            return null;
//        }
//        List<String> groupIds = Lists.newArrayList();
//        if (CollectionUtils.isEmpty(groupIds)) {
//            return null;
//        }
//        GroupUserDO userOnlineGroup = chatService.getUserOnlineGroup(userId);
//        if (Objects.isNull(userOnlineGroup)) {
//            return null;
//        }
//        List<Group> groups = Lists.newArrayList();
//        Group group = getGroupUsers(userOnlineGroup.getGroupId(), type);
//        if (Objects.isNull(group)) {
//            return null;
//        }
//        groups.add(group);
//        return groups;
//    }
//
//    /**
//     * 更新用户终端协议类型及在线状态;
//     *
//     * @param user 用户信息
//     */
//    @Override
//    public boolean updateUserTerminal(User user) {
//        String userId = user.getUserId();
//        String terminal = user.getTerminal();
//        String status = user.getStatus();
//        if (StringUtils.isEmpty(userId) || StringUtils.isEmpty(terminal) || StringUtils.isEmpty(status)) {
//            log.error("userId:{},terminal:{},status:{} must not null", userId, terminal, status);
//            return false;
//        }
//        ChatUserDO chatUserDO = new ChatUserDO();
//        chatUserDO.setUserId(user.getUserId());
//        chatUserDO.setNick(user.getNick());
//        chatUserDO.setAvatar(user.getAvatar());
//        if (UserStatusType.ONLINE.getStatus().equals(user.getStatus())) {
//            chatUserDO.setStatus(UserStatusType.ONLINE.getNumber());
//        } else {
//            chatUserDO.setStatus(UserStatusType.OFFLINE.getNumber());
//        }
//        chatUserDO.setSign(null);
//        chatUserDO.setTerminal(user.getTerminal());
//        // 同时存入数据库和redis(保证查询)。
//        chatService.saveChatUser(chatUserDO);
//        RedisCacheManager.getCache(USER).put(userId+SUFFIX+TERMINAL+SUFFIX+terminal, user.getStatus());
//        return true;
//    }
//
//    /**
//     * 获取用户拥有的群组;
//     *
//     * @param userId
//     * @return
//     */
//    @Override
//    public List<String> getGroups(String userId) {
//        List<String> groups = Lists.newArrayList();
//        return groups;
//    }
//
//
//}
