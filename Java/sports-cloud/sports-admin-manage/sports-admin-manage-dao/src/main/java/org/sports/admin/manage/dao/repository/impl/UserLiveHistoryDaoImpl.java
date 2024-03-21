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

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.entity.UserLiveHistoryDo;
import org.sports.admin.manage.dao.mapper.UserLiveHistoryMapper;
import org.sports.admin.manage.dao.repository.IUserLiveHistoryDao;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

/**
 * 用户观看历史DAO实现
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserLiveHistoryDaoImpl implements IUserLiveHistoryDao {
    @Resource
    private UserLiveHistoryMapper userLiveHistoryMapper;


    @Override
    public PageResponse<UserLiveHistoryDo> getPage(PageRequest pageRequest, Long userId) {
        LambdaQueryWrapper<UserLiveHistoryDo> queryWrapper = Wrappers.lambdaQuery(UserLiveHistoryDo.class)
                .eq(UserLiveHistoryDo::getUserId, userId)
                .orderByDesc(UserLiveHistoryDo::getInTime);
        Page<UserLiveHistoryDo> pageInfo = userLiveHistoryMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(pageInfo, UserLiveHistoryDo.class);
    }

    @Override
    @Transactional
    public boolean insert(UserLiveHistoryDo bean) {
        // 保证同一用户对同一直播间只有一条浏览记录
        List<UserLiveHistoryDo> historyDoList = userLiveHistoryMapper.selectList(Wrappers
                .lambdaQuery(UserLiveHistoryDo.class)
                .eq(UserLiveHistoryDo::getUserId, bean.getUserId())
                .eq(UserLiveHistoryDo::getLiveUserId, bean.getLiveUserId())
        );
        if (CollUtil.isNotEmpty(historyDoList)) {
            bean.setId(historyDoList.get(0).getId());
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            bean.setUpdateTime(now);
            bean.setInTime(now);
            return userLiveHistoryMapper.updateById(bean) > 0;
        } else {
            bean.setId(SnowflakeIdUtil.nextId());
            LocalDateTime now = LocalDateTime.now(ZoneOffset.UTC);
            bean.setCreateTime(now);
            bean.setUpdateTime(now);
            bean.setInTime(now);
            return userLiveHistoryMapper.insert(bean) > 0;
        }
    }
}
