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

package org.sports.chat.service.service;


import org.sports.admin.manage.dao.entity.AppUserDO;
import org.sports.admin.manage.dao.entity.ChatGroupUserDO;
import org.sports.admin.manage.dao.entity.ChatMessageDO;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.chat.service.entity.ChatUserDO;

import java.util.List;

public interface IChatService {

    /**
     * 移除群组中用户
     * @param groupId
     * @param userId
     */
    void removeGroupUser(String groupId, String userId);

    /**
     * 用户未读消息总和；
     * @param userId
     * @return
     */
    long userTotalCountNoReadMsg(String userId);

    /**
     * 查询用户信息
     * @param userId
     */
    AppUserDO getUserInfoByUserID (String  userId);

    /**
     * 查询主播信息
     * @param anchorId
     */
    LiveUserDO getAnchorInfoByAnchorId (String  anchorId);


    void addGroupUser(String userId, String groupId);

    void saveChatMessage(ChatMessageDO messageDO);

    void saveChatUser(ChatUserDO chatUserDO);

    List<ChatMessageDO> getNotSendMessage(String userId, String fromUserId);

    ChatGroupUserDO getUserOnlineGroup(String userId);

    /**
     * 开播接口
     * @param groupId 群聊ID
     * @param groupName 群聊名称-可沿用直播间名称
     * @param userId 主播的userId
     */
    void openLiveByAnchor(String groupId, String groupName, String userId);

    /**
     * 关播接口
     * @param groupId 群聊ID
     * @param userId 主播的userId
     */
    void closeLiveByAnchor(String groupId, String userId);

    long liveTotalCountNoReadMsg(String anchorId);

    long operatorTotalCountNoReadMsg(List<String> anchorIds);

    long getUnreadMsgCount(String userId, Integer identity);

    long userToLiveTotalCountNoReadMsg(String anchorId,String userId);

    ChatMessageDO anchorUserLastMessage(String anchorId, String userId);
}
