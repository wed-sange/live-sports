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
import org.sports.admin.manage.dao.entity.AdvertisingDO;
import org.sports.admin.manage.dao.enums.AdvertisingType;
import org.sports.admin.manage.dao.repository.IAdvertisingDao;
import org.sports.admin.manage.dao.req.AdvertisingPageRequest;
import org.sports.admin.manage.service.converter.AdvertisingConvert;
import org.sports.admin.manage.service.service.IAdvertisingService;
import org.sports.admin.manage.service.vo.*;
import org.sports.springboot.starter.common.toolkit.BeanUtil;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class IAdvertisingServiceImpl implements IAdvertisingService {

    @Resource
    private IAdvertisingDao activityDao;


    @Override
    public PageResponse<AdvertisingVO> getAdvertisingPage(AdvertisingPageRequest pageRequest) {
        PageResponse<AdvertisingDO> activityPage = activityDao.getAdvertisingPage(pageRequest);
        return activityPage.convert(each -> BeanUtil.convert(each, AdvertisingVO.class));
    }

    @Override
    public AdvertisingVO getAdvertisingInfo(Long id) {
        return AdvertisingConvert.INSTANCE.convertToVo(activityDao.getAdvertisingInfo(id));
    }

    @Override
    public boolean createAdvertising(AdvertisingAddVO bean) {
        return activityDao.createAdvertising(AdvertisingConvert.INSTANCE.convertToDo(bean));
    }

    @Override
    public boolean updateAdvertising(AdvertisingUpdateVO bean) {
        return activityDao.updateAdvertising(AdvertisingConvert.INSTANCE.convertToDo(bean));
    }

    @Override
    public boolean deleteAdvertisingById(Long id) {
        return activityDao.deleteAdvertisingById(id);
    }

    @Override
    public List<AdvertisingAppVO> getBannerList() {
        //最多显示5个banner广告，超过随机处理，根据创建时间倒叙排列
        List<AdvertisingDO> bannerList = activityDao.getAdvertisingList(20, AdvertisingType.BANNER);
        Collections.shuffle(bannerList);
        return bannerList.stream()
                .limit(5).sorted(Comparator.comparing(AdvertisingDO::getCreateTime).reversed())
                .map(each -> BeanUtil.convert(each, AdvertisingAppVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<AdvertisingAppVO> getTextScrollList() {
        //最多显示5个banner广告，超过随机处理，根据创建时间倒叙排列
        List<AdvertisingDO> bannerList = activityDao.getAdvertisingList(20, AdvertisingType.LIVE_SCROLL_BAR);
        Collections.shuffle(bannerList);
        return bannerList.stream()
                .limit(5)
                .map(each -> BeanUtil.convert(each, AdvertisingAppVO.class))
                .collect(Collectors.toList());
    }

    @Override
    public AdvertisingAppVO getLiveShow() {
        return BeanUtil.convert(activityDao.getOneAdvertising(AdvertisingType.LIVE_SHOW_POSITION), AdvertisingAppVO.class);
    }

    @Override
    public AdvertisingAppVO getPersonalCenter() {
        return BeanUtil.convert(activityDao.getOneAdvertising(AdvertisingType.PERSONAL_CENTER), AdvertisingAppVO.class);
    }
}
