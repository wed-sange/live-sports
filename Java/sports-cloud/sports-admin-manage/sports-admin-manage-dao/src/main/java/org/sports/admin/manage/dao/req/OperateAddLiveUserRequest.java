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
import java.util.List;


/**

 * @描述: 运营新增直播用户信息
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 14:11
 */
@Schema(description = "后台管理 - 运营新增直播用户信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class OperateAddLiveUserRequest {

    @Schema(description = "助理id")
    @NotNull(message = "助理id不能为空")
    private Long id;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "身份：1：主播；2：助手；3：运营")
    private Integer identityType;

    @Schema(description = "主播id")
    private List<Long> liveUserId;


}
