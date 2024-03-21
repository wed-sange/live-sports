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

package org.sports.admin.manage.service.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @描述: app用户推送消息对象
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/11/22
 * @创建时间: 11:37
 */
@Schema(description = "APP - 比赛信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class NoticeUserDTO implements Serializable {
    @Schema(description = "通知表主键", required = true)
    private Long noticeId;

    @Schema(description = "业务ID")
    private String bizId;

    @Schema(description = "用户ID", required = true)
    private Long userId;

    @Schema(description = "前端展示内容")
    private String title;

    @Schema(description = "原因或者备注")
    private String reason;

    @Schema(description = "创建时间")
    private Long createTime;
}
