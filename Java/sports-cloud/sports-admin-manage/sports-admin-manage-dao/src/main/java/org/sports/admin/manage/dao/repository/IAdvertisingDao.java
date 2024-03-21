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

package org.sports.admin.manage.dao.repository;


import org.sports.admin.manage.dao.entity.AdvertisingDO;
import org.sports.admin.manage.dao.enums.AdvertisingType;
import org.sports.admin.manage.dao.req.AdvertisingPageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.util.List;


public interface IAdvertisingDao {
    PageResponse<AdvertisingDO> getAdvertisingPage(AdvertisingPageRequest pageRequest);

    AdvertisingDO getAdvertisingInfo(Long id);


    boolean createAdvertising(AdvertisingDO bean);


    boolean updateAdvertising(AdvertisingDO bean);

    boolean deleteAdvertisingById(Long id);

    /**
     * 获取广告列表
     *
     * @param limit 限制数量
     */
    List<AdvertisingDO> getAdvertisingList(Integer limit, AdvertisingType advertisingType);

    /**
     * 随机获取一条广告
     *
     * @param advertisingType
     * @return
     */
    AdvertisingDO getOneAdvertising(AdvertisingType advertisingType);
}
