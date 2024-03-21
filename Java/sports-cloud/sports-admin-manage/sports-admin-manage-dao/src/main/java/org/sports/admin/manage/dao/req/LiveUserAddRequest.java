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

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


/**
 * @描述: 直播用户新增信息
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 14:11
 */
@Schema(description = "后台管理 - 直播用户信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class LiveUserAddRequest {

    @Schema(description = "账号")
    @NotBlank(message = "账号不能为空")
    private String account;

    @Schema(description = "密码")
    @NotBlank(message = "密码不能为空")
    private String passwd;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "身份：1：主播；2：助手；3：运营")
    @NotNull(message = "身份不能为空")
    private Integer identityType;

    @Schema(description = "所属主播【身份为助手时候必填】")
    private Long belongLive;

}
