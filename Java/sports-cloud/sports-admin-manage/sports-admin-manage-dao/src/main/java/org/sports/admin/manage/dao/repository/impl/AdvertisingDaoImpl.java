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
import org.sports.admin.manage.dao.entity.AdvertisingDO;
import org.sports.admin.manage.dao.enums.AdvertisingStatus;
import org.sports.admin.manage.dao.enums.AdvertisingType;
import org.sports.admin.manage.dao.mapper.AdvertisingMapper;
import org.sports.admin.manage.dao.repository.IAdvertisingDao;
import org.sports.admin.manage.dao.req.AdvertisingPageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @描述: 广告管理实现类
 * @版权: Copyright (c) 2023

 * @作者: xrc
 * @版本: 1.0
 * @创建日期: 2023/7/18
 * @创建时间: 14:37
 */
@Slf4j
@Service
@AllArgsConstructor
public class AdvertisingDaoImpl implements IAdvertisingDao {
    @Resource
    private AdvertisingMapper advertisingMapper;


    @Override
    public PageResponse<AdvertisingDO> getAdvertisingPage(AdvertisingPageRequest pageRequest) {
        LambdaQueryWrapper<AdvertisingDO> queryWrapper = Wrappers
                .lambdaQuery(AdvertisingDO.class)
                .eq(Objects.nonNull(pageRequest.getChannel()), AdvertisingDO::getChannel, pageRequest.getChannel())
                .eq(Objects.nonNull(pageRequest.getStatus()), AdvertisingDO::getStatus, pageRequest.getStatus());
        // 排序：根据创建时间倒序排列
        queryWrapper.orderByDesc(AdvertisingDO::getCreateTime);
        Page<AdvertisingDO> advertisingDOPage = advertisingMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        return PageUtil.convert(advertisingDOPage, AdvertisingDO.class);
    }

    @Override
    public AdvertisingDO getAdvertisingInfo(Long id) {
        return advertisingMapper.selectById(id);
    }

    @Override
    public boolean createAdvertising(AdvertisingDO bean) {
        bean.setId(SnowflakeIdUtil.nextId());
        return advertisingMapper.insert(bean) > 0;
    }

    @Override
    public boolean updateAdvertising(AdvertisingDO bean) {
        return advertisingMapper.updateById(bean) > 0;
    }

    @Override
    public boolean deleteAdvertisingById(Long id) {
        return advertisingMapper.deleteById(id) > 0;
    }

    @Override
    public List<AdvertisingDO> getAdvertisingList(Integer limit, AdvertisingType advertisingType) {
        return advertisingMapper.selectList(Wrappers
                .lambdaQuery(AdvertisingDO.class)
                .eq(Objects.nonNull(advertisingType), AdvertisingDO::getChannel, advertisingType)
                .eq(AdvertisingDO::getStatus, AdvertisingStatus.PUBLISHED)
                .orderByDesc(AdvertisingDO::getCreateTime)
                .last("limit " + limit)
        );
    }

    @Override
    public AdvertisingDO getOneAdvertising(AdvertisingType advertisingType) {
        List<AdvertisingDO> list = advertisingMapper.selectList(Wrappers
                .lambdaQuery(AdvertisingDO.class)
                .eq(Objects.nonNull(advertisingType), AdvertisingDO::getChannel, advertisingType)
                .eq(AdvertisingDO::getStatus, AdvertisingStatus.PUBLISHED)
                .last("ORDER BY RAND() LIMIT 1")
        );
        return list.size() > 0 ? list.get(0) : null;
    }
}
