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

package org.sports.admin.manage.dao.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.sports.admin.manage.dao.enums.AdvertisingStatus;
import org.sports.admin.manage.dao.enums.AdvertisingType;
import org.sports.springboot.starter.convention.page.PageRequest;

/**
 * @描述: 广告分页请求对象
 * @版权: Copyright (c) 2023

 * @作者: xrc
 * @版本: 1.0
 * @创建日期: 2023/7/18
 * @创建时间: 14:37
 */
@Data
public class AdvertisingPageRequest extends PageRequest {

    @Schema(description = "渠道: 1:直播间滚动条;2:直播间展示位;3:个人中心;4:banner")
    private AdvertisingType channel;
    @Schema(description = "状态:   1:上架中; 2:下架中")
    private AdvertisingStatus status;
}
