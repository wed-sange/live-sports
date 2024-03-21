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

package org.sports.app.service.service;

import org.sports.admin.manage.dao.entity.MyFollowDO;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.FriendsPageRequest;
import org.sports.app.service.vo.live.MyFollowAnchorVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.util.List;


public interface IMyFollowService {
    /**
     * 我关注的主播分页查询
     * @param userId
     * @param pageRequest
     * @return
     */
    PageResponse<MyFollowAnchorVO> getMyFollowAnchorPage(Long userId, PageRequest pageRequest);

    /**
     * 取消关注主播
     * @param user
     * @param anchorId
     */
    void unfollowAnchor(Long user,Long anchorId);

    Long getFansCount(Long anchorId);

    /**
     * 关注主播
     * @param user
     * @param anchorId
     */
    void followAnchor(Long user,Long anchorId);

    /**
     * 取消关注比赛
     * @param user
     * @param matchId
     */
    void unfollowMatch(Long user,Long matchId, MatchType matchType);

    /**
     * 关注比赛
     * @param user
     * @param matchId
     */
    void followMatch(Long user,Long matchId, MatchType matchType);

    /**
     * 是否关注比赛
     * @param matchId
     * @param matchType
     * @return
     */
    boolean isFocusMatch(Long userId, Integer matchId, MatchType matchType);

    boolean isFocusAnchor(Long userId, Long anchorId);

    List<MyFollowDO> getMyFollowMatchList(Long userId);

    /**
     * 取消好友
     * @param userId
     * @param anchorId
     */
    void unfriendAnchor(Long userId, Long anchorId);

    /**
     * 我的好友分页查询
     * @param userId
     * @param req
     * @return
     */
    PageResponse<MyFollowAnchorVO> getMyFriendsPage(Long userId, FriendsPageRequest req);
}
