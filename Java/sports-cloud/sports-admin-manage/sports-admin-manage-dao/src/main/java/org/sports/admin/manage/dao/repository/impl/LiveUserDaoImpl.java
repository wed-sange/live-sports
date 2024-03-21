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

package org.sports.admin.manage.dao.repository.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.enums.CancelType;
import org.sports.admin.manage.dao.mapper.LiveUserMapper;
import org.sports.admin.manage.dao.repository.ILiveUserDao;
import org.sports.admin.manage.dao.req.LiveUserPageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @描述: 主播用户管理实现类
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 11:11
 */
@Slf4j
@Service
@AllArgsConstructor
public class LiveUserDaoImpl implements ILiveUserDao {
    @Resource
    private LiveUserMapper liveUserMapper;


    @Override
    public PageResponse<LiveUserDO> getLiveUserPage(LiveUserPageRequest pageRequest) {
        LambdaQueryWrapper<LiveUserDO> queryWrapper = Wrappers.lambdaQuery(LiveUserDO.class).eq(LiveUserDO::getYnCancel ,CancelType.UN_CANCELLED.getCode());
        if(Strings.isNotBlank(pageRequest.getNickName())) {
            queryWrapper.like(LiveUserDO::getNickName, pageRequest.getNickName());
        }
        if(Objects.nonNull(pageRequest.getIdentityType())) {
            queryWrapper.eq(LiveUserDO::getIdentityType, pageRequest.getIdentityType());
        }
        if(Objects.nonNull(pageRequest.getYnForbidden())) {
            queryWrapper.eq(LiveUserDO::getYnForbidden, pageRequest.getYnForbidden());
        }
        queryWrapper.orderByDesc(LiveUserDO::getCreateTime, LiveUserDO::getId);
        Page<LiveUserDO> liveUserDOPage = liveUserMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(liveUserDOPage, LiveUserDO.class);
    }

    @Override
    public List<LiveUserDO> getLiveUserList(List<Long> ids) {
        return liveUserMapper.selectBatchIds(ids);
    }

    @Override
    public LiveUserDO getLiveUserInfo(Long id) {
        return liveUserMapper.selectById(id);
    }

    @Override
    public void createLiveUser(LiveUserDO bean) {
        bean.setId(SnowflakeIdUtil.nextId());
        bean.setPasswd(DigestUtil.md5Hex(bean.getPasswd()));
        liveUserMapper.insert(bean);

    }

    @Override
    public void updateLiveUser(LiveUserDO bean) {
        liveUserMapper.updateById(bean);
    }

    @Override
    public List<LiveUserDO> getLiveUserListByBelongId(List<Long> liveUserIds) {
        return liveUserMapper.selectList(Wrappers.lambdaQuery(LiveUserDO.class).in(LiveUserDO::getBelongLive , liveUserIds));
    }

    @Override
    public LiveUserDO getByAccount(String account){
        return liveUserMapper.selectOne(new LambdaQueryWrapper<LiveUserDO>().eq(LiveUserDO::getAccount, account));
    }

    @Override
    public List<Long> getLiveUserFilter(LiveUserDO liveUserDO) {
        LambdaQueryWrapper<LiveUserDO> queryWrapper = Wrappers.lambdaQuery(LiveUserDO.class)
                .select(LiveUserDO::getId)
                .like(Strings.isNotBlank(liveUserDO.getNickName()), LiveUserDO::getNickName, liveUserDO.getNickName())
                .like(Strings.isNotBlank(liveUserDO.getAccount()), LiveUserDO::getAccount, liveUserDO.getAccount())
                .like(Objects.nonNull(liveUserDO.getIdentityType()), LiveUserDO::getIdentityType, liveUserDO.getIdentityType())
                .like(Objects.nonNull(liveUserDO.getYnForbidden()), LiveUserDO::getYnForbidden, liveUserDO.getYnForbidden());
        List<LiveUserDO> userList = liveUserMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(userList)){
            return null;
        }
        return userList.stream().map(LiveUserDO::getId).collect(Collectors.toList());
    }

}
