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

package org.sports.admin.manage.dao.repository;
import org.sports.admin.manage.dao.entity.MyFollowDO;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.util.List;


public interface IMyFollowDao {

    PageResponse<MyFollowDO> getMyFollowAnchorPage(Long userId, PageRequest pageRequest);

    /**
     * 我关注的比赛列表
     * @param userId
     * @return
     */
    List<MyFollowDO> getMyFollowMatchList(Long userId);

    /**
     * 根据比赛查询关注的用户
     * @param matchIds
     * @param matchType
     * @return
     */
    List<MyFollowDO> getFollowMatchList(List<Long> matchIds, MatchType matchType);

    /**
     * 我关注的主播列表
     * @param userId
     * @return
     */
    List<MyFollowDO> getMyFollowAnchorList(Long userId);

    /**
     * 取消关注主播
     * @param user
     * @param anchorId
     */
    void unfollowAnchor(Long user,Long anchorId);

    /**
     * 关注主播
     * @param user
     * @param anchorId
     */
    void followAnchor(Long user,Long anchorId);

    /**
     * 取消关注比赛
     *
     * @param user
     * @param matchId
     * @param matchType
     */
    void unfollowMatch(Long user, Long matchId, MatchType matchType);

    /**
     * 关注比赛
     *
     * @param user
     * @param matchId
     * @param matchType
     */
    void followMatch(Long user, Long matchId, MatchType matchType);

    /**
     * 查询主播粉丝数量
     * @param anchorId
     * @return
     */
    long getFansCount(Long anchorId);

    /**
     * 查询主播粉丝列表
     * @param anchorId
     * @return
     */
    List<MyFollowDO> getFansList(Long anchorId);
    boolean isFocusMatch(Long userId, Integer matchId, MatchType matchType);

    boolean isFocusAnchor(Long userId, Long anchorId);
    void unfriendAnchor(Long userId, Long anchorId);

    PageResponse<MyFollowDO> getMyFriendsPage(Long userId, PageRequest pageRequest);

    List<MyFollowDO> getMyFriendsList(Long userId, List<Long> liveUserIds);
}
