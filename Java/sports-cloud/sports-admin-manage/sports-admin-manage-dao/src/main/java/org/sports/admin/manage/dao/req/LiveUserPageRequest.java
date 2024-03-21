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
import org.sports.springboot.starter.convention.page.PageRequest;

import javax.validation.constraints.NotNull;

/**
 * @描述: 主播列表分页请求对象
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 14:37
 */
@Data
public class LiveUserPageRequest extends PageRequest {

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "身份 1主播 2助手 3运营")
    private Integer identityType;

    @Schema(description = "是否封禁 1是 0否")
    @NotNull(message = "{question.notBlank}")
    private Integer ynForbidden;
}
