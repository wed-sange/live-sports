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


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.sports.admin.manage.dao.entity.AppAnchorMuteUserDO;
import org.sports.admin.manage.dao.mapper.AppAnchorMuteUserMapper;
import org.sports.admin.manage.service.service.IAppAnchorMuteUserService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class IAppAnchorMuteUserServiceImpl extends ServiceImpl<AppAnchorMuteUserMapper, AppAnchorMuteUserDO> implements IAppAnchorMuteUserService {

    @Override
    public List<AppAnchorMuteUserDO> queryMuteUsersByAnchorId(Long anchorId) {
        LambdaQueryWrapper<AppAnchorMuteUserDO> queryWrapper = Wrappers.lambdaQuery(AppAnchorMuteUserDO.class);
        queryWrapper.eq(AppAnchorMuteUserDO::getAnchorId, anchorId);
        return this.list(queryWrapper);
    }
}
