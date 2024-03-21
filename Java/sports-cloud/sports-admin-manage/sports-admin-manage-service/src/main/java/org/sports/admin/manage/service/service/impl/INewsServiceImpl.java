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

package org.sports.admin.manage.service.service.impl;


import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.repository.INewsDao;
import org.sports.admin.manage.service.converter.NewsConvert;
import org.sports.admin.manage.service.service.INewsService;
import org.sports.admin.manage.service.vo.NewsVO;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class INewsServiceImpl implements INewsService {

    @Resource
    private INewsDao newsDao;

    @Override
    public PageResponse<NewsVO> getLiveUserPage(PageRequest pageRequest) {
        return  newsDao.getLiveUserPage(pageRequest).convert(each -> BeanUtil.convert(each, NewsVO.class));
    }

    @Override
    public NewsVO getNewsInfo(Long id) {
        return NewsConvert.INSTANCE.convertToVo(newsDao.getNewsInfo(id));
    }

    @Override
    public void insertNews(NewsVO bean) {
         newsDao.insertNews(NewsConvert.INSTANCE.convertToDo(bean));
    }

    @Override
    public long getNewsCountByUrl(String newsUrl) {
        return newsDao.getNewsCountByUrl(newsUrl);
    }
}
