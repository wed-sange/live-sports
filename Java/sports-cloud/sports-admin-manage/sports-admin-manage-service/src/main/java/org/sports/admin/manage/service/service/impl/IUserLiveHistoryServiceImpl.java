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


import cn.hutool.core.collection.CollUtil;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.entity.UserLiveHistoryDo;
import org.sports.admin.manage.dao.repository.IUserLiveHistoryDao;
import org.sports.admin.manage.service.service.ILiveService;
import org.sports.admin.manage.service.service.IUserLiveHistoryService;
import org.sports.admin.manage.service.vo.LiveVo;
import org.sports.admin.manage.service.vo.UserLiveHistoryAddVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Service
@Slf4j
public class IUserLiveHistoryServiceImpl implements IUserLiveHistoryService {

    @Resource
    private IUserLiveHistoryDao userLiveHistoryDao;
    @Resource
    private ILiveService liveService;

    @Override
    public PageResponse<LiveVo> getPage(PageRequest pageRequest, Long userId) {
        PageResponse<UserLiveHistoryDo> page = userLiveHistoryDao.getPage(pageRequest, userId);
        List<UserLiveHistoryDo> recordList = page.getRecords();
        List<LiveVo> liveVoList = new ArrayList<>();
        if (CollUtil.isNotEmpty(recordList)) {
            List<Long> liveUserIdList = recordList.stream().map(UserLiveHistoryDo::getLiveUserId).distinct().collect(Collectors.toList());
            // 查询出直播间信息组装返回
            liveVoList = liveService.findLatestByUserIds(liveUserIdList);
            // 按照之前的查询出的ID排序
            Map<Long, Integer> liveIdOrder = IntStream.range(0, liveUserIdList.size()).boxed().collect(Collectors.toMap(liveUserIdList::get, index -> index));
            liveVoList = liveVoList.stream()
                    .sorted(Comparator.comparing(liveVo -> liveIdOrder.get(liveVo.getUserId())))
                    .collect(Collectors.toList());
        }
        return PageResponse.<LiveVo>builder()
                .current(page.getCurrent())
                .size(page.getSize())
                .total(page.getTotal())
                .records(liveVoList).build();
    }

    @Override
    public boolean insert(UserLiveHistoryAddVO addVO) {
        // 查询出直播间主播ID
        LiveDO liveInfo = liveService.getById(addVO.getLiveId());
        if (Objects.nonNull(liveInfo)) {
            UserLiveHistoryDo historyDo = new UserLiveHistoryDo();
            historyDo.setUserId(addVO.getUserId());
            historyDo.setLiveUserId(liveInfo.getUserId());
            return userLiveHistoryDao.insert(historyDo);
        }
        return false;
    }
}
