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
import java.util.List;

/**
 * @描述: 比赛得分推送对象
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
public class MatchPushDTO implements Serializable {

    @Schema(description = "标题")
    private String title;

    @Schema(description = "推送消息内容")
    private String alert;

    @Schema(description = "直播间ID")
    private String liveId;

    @Schema(description = "主播ID")
    private String anchorId;

    @Schema(description = "比赛ID")
    private Integer matchId;

    @Schema(description = "比赛类型")
    private Integer matchType;

    @Schema(description = "是否纯净流")
    private boolean isPureFlow;

    @Schema(description = "推送用户regId")
    private List<String> regIds;

    @Schema(description = "推送类型")
    private Integer pushType;

}