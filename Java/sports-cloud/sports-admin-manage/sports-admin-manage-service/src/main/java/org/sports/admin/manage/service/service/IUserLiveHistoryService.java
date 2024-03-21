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

package org.sports.admin.manage.service.service;

import org.sports.admin.manage.service.vo.LiveVo;
import org.sports.admin.manage.service.vo.UserLiveHistoryAddVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

/**
 * 用户观看历史查询
 */

public interface IUserLiveHistoryService {
    /**
     * 分页查询
     */
    PageResponse<LiveVo> getPage(PageRequest pageRequest, Long userId);

    /**
     * 插入新纪录
     */
    boolean insert(UserLiveHistoryAddVO addVO);
}
