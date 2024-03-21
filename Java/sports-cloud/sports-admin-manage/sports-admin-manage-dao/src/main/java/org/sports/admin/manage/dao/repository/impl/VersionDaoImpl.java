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
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.entity.VersionDO;
import org.sports.admin.manage.dao.mapper.VersionMapper;
import org.sports.admin.manage.dao.repository.IVersionDao;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @描述: 版本管理实现类
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/27
 * @创建时间: 14:37
 */
@Slf4j
@Service
@AllArgsConstructor
public class VersionDaoImpl implements IVersionDao {
    @Resource
    private VersionMapper versionMapper;

    @Override
    public List<VersionDO> getVersionList(Integer channel) {
        LambdaQueryWrapper<VersionDO> queryWrapper = Wrappers.lambdaQuery(VersionDO.class).eq(Objects.nonNull(channel), VersionDO::getChannel ,channel).orderByDesc(VersionDO::getVersion);
        return versionMapper.selectList(queryWrapper);
    }

    @Override
    public void deleteVersion(Long versionId) {
        versionMapper.deleteById(versionId);
    }

    @Override
    public void createVersion(VersionDO bean) {
        bean.setId(SnowflakeIdUtil.nextId());
        versionMapper.insert(bean);
    }

    @Override
    public VersionDO getLatestVersion(Long channel) {
        LambdaQueryWrapper<VersionDO> queryWrapper = Wrappers.lambdaQuery(VersionDO.class).eq(VersionDO::getChannel ,channel).orderByDesc(VersionDO::getVersion);
        List<VersionDO> versionDOS = versionMapper.selectList(queryWrapper);
        if(CollectionUtils.isEmpty(versionDOS)){
            return null;
        }
        return versionDOS.get(0);
    }
}
