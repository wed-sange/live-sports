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

package org.sports.admin.manage.service.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.vo.LiveUserOpenVO;
import org.sports.admin.manage.service.vo.LiveUserVO;
import org.sports.springboot.starter.base.exception.ErrorCode;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.util.List;


public interface ILiveUserService extends IService<LiveUserDO> {

    /**
     * 分页查询主播、运营用户
     *
     * @param pageRequest
     * @return
     */
    PageResponse<LiveUserVO> getLiveOperateUserPage(LiveUserPageRequest pageRequest);


    /**
     * 获取所有未注销的主播列表
     * @param
     * @return
     */
    List<LiveUserVO> queryLiveUsers();

    /**
     * 根据主播ID 查询所有助手
     * @param liveId
     * @return
     */
    List<LiveUserVO> queryHelpersByLiveId(Long liveId);

    /**
     * 根据ID批量查询主播信息
     *
     * @param ids
     * @return
     */
    List<LiveUserVO> getLiveUserList(List<Long> ids);

    /**
     * 根据ID查询主播信息
     *
     * @param id
     * @return
     */
    LiveUserVO getLiveUserInfo(Long id);

    /**
     * 新增主播用户
     *
     * @param request
     */
    void createLiveUser(LiveUserAddRequest request);

    /**
     * 助理绑定主播
     *
     * @param request
     */
    void operateBindingLiveUser(OperateAddLiveUserRequest request);

    /**
     * 根据超级助理ID查询所有主播
     *
     * @param id
     */
    List<LiveUserVO> queryLiveUserByOperate(Long id);

    /**
     * 更新主播用户表信息
     *
     * @param request
     */
    void updateLiveUser(LiveUserUpdateRequest request);

    /**
     * 注销用户
     *
     * @param id
     */
    void cancelLiveUser(Long id);

    /**
     * 封禁用户
     *
     * @param forbiddenRequest
     */
    void forbiddenLiveUser(UserForbiddenRequest forbiddenRequest);

    /**
     * 解禁用户
     *
     * @param id
     */
    void unForbiddenLiveUser(Long id);

    /**
     * 重置用户密码
     * @param id
     */
    void resetPasswd(Long id);

    /**
     * 根据账号获取直播用户信息
     *
     * @param account
     * @return
     */
    LiveUserDO getByAccount(String account);

    /**
     * 根据ID获取主播公开信息
     */
    LiveUserOpenVO getLiveUserOpenInfo(Long loginUserId, Long anchorId);

    /**
     * 根据主播昵称模糊搜索主播列表
     * @param nickName
     * @return
     */
    List<LiveUserVO> getLiveUserListByNickName(String nickName);

    /**
     * 校验直播用户是否有效 有效返回null 无效返回对应错误吗
     * @param liveUser
     * @return
     */
    ErrorCode checkLiveUser(LiveUserDO liveUser);

    void setOpenInfoFlag(Long userId);

    void kickOutUser(KickOutUserRequest kick);

    void forbiddenAppUser(MuteUserRequest muteUserRequest);

    void unforbiddenAppUser(MuteUserRequest muteUserRequest);

    /**
     * 新增助理用户
     *
     * @param request
     */
    void createOperateUser(OpLiveUserAddRequest request);

    /**
     * 置顶用户
     * @param topUserRequest
     */
    void setTop(TopUserRequest topUserRequest);

    /**
     * 取消置顶用户
     * @param topUserRequest
     */
    void untop(TopUserRequest topUserRequest);
}
