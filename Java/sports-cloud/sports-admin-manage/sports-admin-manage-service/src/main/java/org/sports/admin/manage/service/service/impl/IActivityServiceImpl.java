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
import org.sports.admin.manage.dao.entity.ActivityDO;
import org.sports.admin.manage.dao.repository.IActivityDao;
import org.sports.admin.manage.dao.req.ActivityPageRequest;
import org.sports.admin.manage.service.converter.ActivityConvert;
import org.sports.admin.manage.service.service.IActivityService;
import org.sports.admin.manage.service.vo.ActivityModifyVO;
import org.sports.admin.manage.service.vo.ActivityVO;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
@Slf4j
public class IActivityServiceImpl implements IActivityService {

    @Resource
    private IActivityDao activityDao;


    @Override
    public PageResponse<ActivityVO> getActivityPage(ActivityPageRequest pageRequest) {
        PageResponse<ActivityDO> activityPage = activityDao.getActivityPage(pageRequest);
        return activityPage.convert(each -> BeanUtil.convert(each, ActivityVO.class));
    }

    @Override
    public ActivityVO getActivityInfo(Long id) {
        return ActivityConvert.INSTANCE.convertToVo(activityDao.getActivityInfo(id));
    }

    @Override
    public void createActivity(ActivityVO bean) {
        activityDao.createActivity(ActivityConvert.INSTANCE.convertToDo(bean));
    }

    @Override
    public void updateActivity(ActivityModifyVO bean) {
        activityDao.updateActivity(ActivityConvert.INSTANCE.convertToDo(bean));
    }

    @Override
    public void deleteActivity(Long id) {
        activityDao.deleteActivity(id);
    }
}
