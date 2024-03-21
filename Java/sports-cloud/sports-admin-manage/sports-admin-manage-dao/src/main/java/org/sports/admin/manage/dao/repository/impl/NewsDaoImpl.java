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
import org.sports.admin.manage.dao.entity.NewsDO;
import org.sports.admin.manage.dao.mapper.NewsMapper;
import org.sports.admin.manage.dao.repository.INewsDao;
import org.sports.springboot.starter.common.enums.DelEnum;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.database.mybatisplus.PageUtil;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @描述: 新闻信息查询实现类
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 17:11
 */
@Slf4j
@Service
@AllArgsConstructor
public class NewsDaoImpl implements INewsDao {
    @Resource
    private NewsMapper newsMapper;


    @Override
    public PageResponse<NewsDO> getLiveUserPage(PageRequest pageRequest) {
        LambdaQueryWrapper<NewsDO> queryWrapper = Wrappers.lambdaQuery(NewsDO.class)
                .eq(NewsDO::getDelFlag , DelEnum.NORMAL.code()).orderByDesc(NewsDO::getPublishTime);
        Page<NewsDO> newsDOPage = newsMapper.selectPage(new Page<>(pageRequest.getCurrent(), pageRequest.getSize()), queryWrapper);
        //新闻列表不返回具体内容
        if(Objects.nonNull(newsDOPage)&& !CollectionUtils.isEmpty(newsDOPage.getRecords())){
            newsDOPage.getRecords().forEach(item -> item.setContent(null));
        }
        return PageUtil.convert(newsDOPage, NewsDO.class);
    }

    @Override
    public NewsDO getNewsInfo(Long id) {
        return newsMapper.selectById(id);
    }

    @Override
    public void insertNews(NewsDO bean) {
        bean.setId(SnowflakeIdUtil.nextId());
        newsMapper.insert(bean);
    }

    @Override
    public long getNewsCountByUrl(String newsUrl) {
        return newsMapper.selectCount(Wrappers.lambdaQuery(NewsDO.class).eq(NewsDO::getSourceUrl , newsUrl));
    }
}
