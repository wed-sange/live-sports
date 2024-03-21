/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sports.chat.service.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.jim.core.ImChannelContext;
import org.jim.core.packets.ChatType;
import org.jim.core.packets.UserStatusType;
import org.jim.server.JimServerAPI;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.*;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.mapper.AppUserMapper;
import org.sports.admin.manage.dao.mapper.ChatGroupUserMapper;
import org.sports.admin.manage.dao.mapper.ChatMessageMapper;
import org.sports.admin.manage.dao.mapper.LiveUserMapper;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.AppUserNoticeService;
import org.sports.chat.service.constant.CacheConstant;
import org.sports.chat.service.entity.ChatUserDO;
import org.sports.chat.service.entity.GroupDO;
import org.sports.chat.service.enums.SentType;
import org.sports.chat.service.mapper.ChatGroupMapper;
import org.sports.chat.service.mapper.ChatUserMapper;
import org.sports.chat.service.service.IChatService;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@Slf4j
public class IChatServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessageDO> implements IChatService {

    @Resource
    private ChatMessageMapper chatMessageMapper;
    @Resource
    private ChatUserMapper chatUserMapper;
    @Resource
    private AppUserNoticeService appUserNoticeService;
    @Resource
    private ChatGroupUserMapper chatGroupUserMapper;

    @Resource
    private ChatGroupMapper chatGroupMapper;
    @Resource
    private LiveUserMapper liveUserMapper;

    @Resource
    private AppUserMapper appUserMapper;

    @Autowired
    private RedissonClient redissonClient;

    @Override
    public void removeGroupUser(String groupId, String userId) {
        LambdaQueryWrapper<ChatGroupUserDO> queryWrapper = Wrappers.lambdaQuery(ChatGroupUserDO.class).eq(ChatGroupUserDO::getGroupId, groupId).eq(ChatGroupUserDO::getUserId, userId);
        ChatGroupUserDO groupUserDO = chatGroupUserMapper.selectOne(queryWrapper);
        if (Objects.isNull(groupUserDO)) {
            return;
        }
        groupUserDO.setStatus(UserStatusType.OFFLINE.getNumber());
        groupUserDO.setLeaveStatus(YnEnum.ONE.getValue());
        chatGroupUserMapper.updateById(groupUserDO);
    }

    @Override
    public long userTotalCountNoReadMsg(String userId) {
        if (Strings.isBlank(userId)) {
            return 0;
        }
        return this.count(Wrappers.lambdaQuery(ChatMessageDO.class)
                .eq(ChatMessageDO::getToId, userId)
                .eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PRIVATE.getNumber())
                .eq(ChatMessageDO::getReadable, 0)
                .ne(ChatMessageDO::getIdentityType, 0)
                .eq(ChatMessageDO::getUserDel, 0)) + appUserNoticeService.countNoRead(Long.valueOf(userId));
    }

    @Override
    public AppUserDO getUserInfoByUserID(String userId) {
        return appUserMapper.selectById(Long.valueOf(userId));

    }

    @Override
    public LiveUserDO getAnchorInfoByAnchorId(String anchorId) {
        return liveUserMapper.selectById(Long.valueOf(anchorId));
    }

    @Override
    @Transactional
    public void addGroupUser(String userId, String groupId) {
        LambdaQueryWrapper<ChatGroupUserDO> queryWrapper = Wrappers.lambdaQuery(ChatGroupUserDO.class).eq(ChatGroupUserDO::getGroupId, groupId).eq(ChatGroupUserDO::getUserId, userId);
        ChatGroupUserDO groupUserDO = chatGroupUserMapper.selectOne(queryWrapper);
        if (Objects.isNull(groupUserDO)) {
            ChatUserDO chatUserDO = chatUserMapper.selectOne(Wrappers.lambdaQuery(ChatUserDO.class).eq(ChatUserDO::getUserId, userId).ne(ChatUserDO::getDelFlag, 1));
            // 这里获取头像和昵称数据来源于不更新的t_chat_user表， 所以头像和昵称都是不变的，但这样不影响目前业务也简化了流程。
            ChatGroupUserDO userDO = new ChatGroupUserDO();
            if (Objects.nonNull(chatUserDO)) {
                userDO.setAvatar(chatUserDO.getAvatar());
                userDO.setNick(chatUserDO.getNick());
                userDO.setStatus(UserStatusType.ONLINE.getNumber());
                userDO.setGroupId(groupId);
                userDO.setUserId(userId);
            }
            chatGroupUserMapper.insert(userDO);
        } else if (UserStatusType.ONLINE.getNumber() != groupUserDO.getStatus()) {
            groupUserDO.setStatus(UserStatusType.ONLINE.getNumber());
            groupUserDO.setLeaveStatus(YnEnum.ZERO.getValue());
            chatGroupUserMapper.updateById(groupUserDO);
        }


    }


    @Override
    public void saveChatMessage(ChatMessageDO messageDO) {
        chatMessageMapper.insert(messageDO);
    }

    @Override
    public void saveChatUser(ChatUserDO chatUserDO) {
        LambdaQueryWrapper<ChatUserDO> queryWrapper = Wrappers.lambdaQuery(ChatUserDO.class).eq(ChatUserDO::getUserId, chatUserDO.getUserId());
        ChatUserDO userDO = chatUserMapper.selectOne(queryWrapper);
        if (Objects.nonNull(userDO)) {
            Integer status = chatUserDO.getStatus();
            userDO.setStatus(status);
            chatUserMapper.updateById(userDO);
        } else {
            chatUserDO.setId(SnowflakeIdUtil.nextId());
            chatUserMapper.insert(chatUserDO);
        }
    }

    @Override
    public List<ChatMessageDO> getNotSendMessage(String userId, String fromUserId) {
        LambdaQueryWrapper<ChatMessageDO> queryWrapper = Wrappers.lambdaQuery(ChatMessageDO.class).eq(ChatMessageDO::getToId, userId).eq(ChatMessageDO::getSent, SentType.NO.getCode());
        if (Strings.isNotBlank(fromUserId)) {
            queryWrapper.eq(ChatMessageDO::getFromId, fromUserId);
        }
        return chatMessageMapper.selectList(queryWrapper);
    }

    @Override
    public ChatGroupUserDO getUserOnlineGroup(String userId) {
        LambdaQueryWrapper<ChatGroupUserDO> queryWrapper = Wrappers.lambdaQuery(ChatGroupUserDO.class).eq(ChatGroupUserDO::getUserId, userId).eq(ChatGroupUserDO::getStatus, UserStatusType.ONLINE.getNumber()).orderByDesc(ChatGroupUserDO::getId);
        return chatGroupUserMapper.selectOne(queryWrapper);
    }


    @Transactional
    @Override
    public void openLiveByAnchor(String groupId, String groupName, String userId) {
        if (StringUtils.isBlank(groupId)) {
            return;
        }

        GroupDO groupDO = chatGroupMapper.selectOne(Wrappers.lambdaQuery(GroupDO.class)
                .eq(GroupDO::getGroupId, groupId));
        if (Objects.isNull(groupDO)) {
            groupDO = getGroupDO(groupId, groupName, userId);
            chatGroupMapper.insert(groupDO);
        }
        // 将所有的groupId缓存至redis中（所有的，并不是启用的）
        RSet<String> set = redissonClient.getSet(CacheConstant.CHAT_GROUPIDS);
        set.add(groupId);
        // 绑定主播到聊天室
        if (StringUtils.isNotBlank(userId)) {
            for (ImChannelContext context : JimServerAPI.getByUserId(userId)) {
                JimServerAPI.bindGroup(context, groupId);
            }
        }
    }

    @Override
    public void closeLiveByAnchor(String groupId, String userId) {
        if (StringUtils.isBlank(groupId) || StringUtils.isBlank(userId)) {
            return;
        }
        JimServerAPI.unbindGroup(userId, groupId);
    }

    @Override
    public long liveTotalCountNoReadMsg(String anchorId) {
        return this.count(Wrappers.lambdaQuery(ChatMessageDO.class)
                .eq(ChatMessageDO::getToId, anchorId)
                .eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PRIVATE.getNumber())
                .eq(ChatMessageDO::getReadable, 0)
                .eq(ChatMessageDO::getIdentityType, 0)
                .eq(ChatMessageDO::getAnchorDel, 0));
    }

    @Override
    public long operatorTotalCountNoReadMsg(List<String> anchorIds) {
        return this.count(Wrappers.lambdaQuery(ChatMessageDO.class)
                .eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PRIVATE.getNumber())
                .in(ChatMessageDO::getAnchorId, anchorIds)
                .eq(ChatMessageDO::getReadable, 0)
                .eq(ChatMessageDO::getIdentityType, 0)
                .eq(ChatMessageDO::getAnchorDel, 0));
    }

    @Override
    public long getUnreadMsgCount(String userId, Integer identity) {
        long totalCount = 0;
        if (IdentityType.ANCHOR.getCode().equals(identity)) { //主播
            totalCount = liveTotalCountNoReadMsg(userId);
        } else if (IdentityType.ASSISTANT.getCode().equals(identity)) {//助手
            //查询所属主播ID
            LambdaQueryWrapper<LiveUserDO> select = Wrappers.lambdaQuery(LiveUserDO.class).eq(LiveUserDO::getId, userId);
            LiveUserDO userDO = liveUserMapper.selectOne(select);
            if (Objects.isNull(userDO)) {
                return totalCount;
            }
            totalCount = liveTotalCountNoReadMsg(String.valueOf(userDO.getBelongLive()));
        } else if (IdentityType.OPERATOR.getCode().equals(identity)) { //运营
            //查询运营账号
            LiveUserDO liveUserDO = liveUserMapper.selectById(Long.valueOf(userId));
            //数据库历史数据，运营账号下面没有分配主播
            if (Strings.isBlank(liveUserDO.getPossessLive())) {
                return totalCount;
            }
            // 将逗号分隔的数字列表转换为Long类型的列表
            List<String> possessLiveList = Arrays.stream(liveUserDO.getPossessLive().split(","))
                    .collect(Collectors.toList());
            totalCount = operatorTotalCountNoReadMsg(possessLiveList);
        } else {
            totalCount = userTotalCountNoReadMsg(userId);
        }
        return totalCount;
    }

    @Override
    public long userToLiveTotalCountNoReadMsg(String anchorId, String userId) {
        return this.count(Wrappers.lambdaQuery(ChatMessageDO.class)
                .eq(ChatMessageDO::getToId, anchorId)
                .eq(ChatMessageDO::getFromId, userId)
                .eq(ChatMessageDO::getChatType, ChatType.CHAT_TYPE_PRIVATE.getNumber())
                .eq(ChatMessageDO::getReadable, 0)
                .eq(ChatMessageDO::getIdentityType, 0)
                .eq(ChatMessageDO::getDelFlag, 0));
    }

    @Override
    public ChatMessageDO anchorUserLastMessage(String anchorId, String userId) {
        LambdaQueryWrapper<ChatMessageDO> wrapperQuery = Wrappers.lambdaQuery(ChatMessageDO.class).eq(ChatMessageDO::getAnchorId, anchorId)
                .and(wrapper -> wrapper.eq(ChatMessageDO::getFromId, userId).or().eq(ChatMessageDO::getToId, userId)).orderByDesc(ChatMessageDO::getId).last("LIMIT 1 ");
        ChatMessageDO messageDOS = chatMessageMapper.selectOne(wrapperQuery);
        return messageDOS;
    }

    private static GroupDO getGroupDO(String groupId, String groupName, String userId) {
        GroupDO groupDO = new GroupDO();
        groupDO.setName(groupName);
        groupDO.setGroupId(groupId);
        groupDO.setCreator(userId);
        return groupDO;
    }
}
