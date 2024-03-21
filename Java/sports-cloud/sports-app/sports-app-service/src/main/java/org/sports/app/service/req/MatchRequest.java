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

package org.sports.app.service.req;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.springboot.starter.convention.page.PageRequest;

import javax.validation.constraints.NotNull;

import static org.sports.app.service.constant.DefaultConstant.LATEST_MATCH_COUNT;

/**
 * @描述: 比赛请求对象
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/21
 * @创建时间: 14:37
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MatchRequest  {

    /**
     * @see org.sports.admin.manage.dao.enums.MatchType
     */
    @Schema(description = "比赛类型：1：足球；2：篮球")
    private MatchType matchType;

    @Schema(description = "查询已完成比赛因为篮球和足球状态不一致，这里统一使用99代表查询已完赛比赛")
    private Integer status;

    @Schema(description = "返回结果数量")
    @NotNull
    private Integer top = LATEST_MATCH_COUNT;
}
