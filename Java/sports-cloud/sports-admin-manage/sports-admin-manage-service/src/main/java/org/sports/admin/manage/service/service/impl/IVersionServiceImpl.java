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
import org.sports.admin.manage.dao.repository.IVersionDao;
import org.sports.admin.manage.service.converter.VersionConvert;
import org.sports.admin.manage.service.service.IVersionService;
import org.sports.admin.manage.service.vo.LatestVersionVO;
import org.sports.admin.manage.service.vo.VersionVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service
@Slf4j
public class IVersionServiceImpl implements IVersionService {

    @Resource
    private IVersionDao versionDao;


    @Override
    public List<VersionVO> getVersionList(Integer channel) {
        return VersionConvert.INSTANCE.convertList(versionDao.getVersionList(channel));
    }

    @Override
    public void deleteVersion(Long id) {
       versionDao.deleteVersion(id);
    }

    @Override
    public void createVersion(VersionVO bean) {
        versionDao.createVersion(VersionConvert.INSTANCE.convertToDo(bean));
    }

    @Override
    public LatestVersionVO getLatestVersion(Long channel) {
        return VersionConvert.INSTANCE.convertToVo2(versionDao.getLatestVersion(channel));
    }


}
