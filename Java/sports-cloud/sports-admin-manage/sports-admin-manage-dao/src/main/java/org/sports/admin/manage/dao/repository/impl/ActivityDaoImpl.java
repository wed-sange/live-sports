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

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.ActivityDO;
import org.sports.admin.manage.dao.mapper.ActivityMapper;
import org.sports.admin.manage.dao.repository.IActivityDao;
import org.sports.admin.manage.dao.req.ActivityPageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @描述: 活动管理实现类
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/18
 * @创建时间: 14:37
 */
@Slf4j
@Service
@AllArgsConstructor
public class ActivityDaoImpl implements IActivityDao {
    @Resource
    private   ActivityMapper activityMapper;


    @Override
    public PageResponse<ActivityDO> getActivityPage(ActivityPageRequest pageRequest) {
        LambdaQueryWrapper<ActivityDO> queryWrapper = Wrappers.lambdaQuery(ActivityDO.class).orderByDesc(ActivityDO::getSortOrder);
        if(Strings.isNotBlank(pageRequest.getTitle())) {
            queryWrapper.like(ActivityDO::getTitle, pageRequest.getTitle());
        }
        if(Objects.nonNull(pageRequest.getStatus())) {
            queryWrapper.eq(ActivityDO::getStatus, pageRequest.getStatus());
        }
        Page<ActivityDO> activityDOPage = activityMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(activityDOPage, ActivityDO.class);
    }

    @Override
    public ActivityDO getActivityInfo(Long id) {
        return activityMapper.selectById(id);
    }

    @Override
    public void createActivity(ActivityDO bean) {
        bean.setId(SnowflakeIdUtil.nextId());
        activityMapper.insert(bean);
    }

    @Override
    public void updateActivity(ActivityDO bean) {
        activityMapper.updateById(bean);
    }

    @Override
    public void deleteActivity(Long id) {
        activityMapper.deleteById(id);
    }
}
