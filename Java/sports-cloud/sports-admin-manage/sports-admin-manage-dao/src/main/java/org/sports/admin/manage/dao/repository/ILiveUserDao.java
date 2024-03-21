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
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.req.LiveUserPageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.util.List;


public interface ILiveUserDao {

    /**
     * 分页查询主播用户表
     * @param pageRequest
     * @return
     */
    PageResponse<LiveUserDO> getLiveUserPage(LiveUserPageRequest pageRequest);

    /**
     * 根据主播ID批量查询主播
     * @param ids
     * @return
     */
    List<LiveUserDO> getLiveUserList(List<Long> ids);
    /**
     * 根据ID查询主播信息
     * @param id
     * @return
     */
    LiveUserDO getLiveUserInfo(Long id);

    /**
     * 新增主播用户
     * @param bean
     */
    void createLiveUser(LiveUserDO bean);

    /**
     * 更新主播用户表信息
     * @param bean
     */
    void updateLiveUser(LiveUserDO bean);

    /**
     * 根据主播ID查询助理列表
     * @param liveUserIds
     * @return
     */
    List<LiveUserDO> getLiveUserListByBelongId(List<Long> liveUserIds);

    /**
     * 根据账号获取直播用户信息
     * @param account
     * @return
     */
    LiveUserDO getByAccount(String account);

    /**
     * 根据条件过滤模糊查询相关用户IDS
     * @param liveUserDO
     * @return
     */
    List<Long> getLiveUserFilter(LiveUserDO liveUserDO);

}
