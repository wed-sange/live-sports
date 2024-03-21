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

package org.sports.admin.manage.service.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;


/**
 * @描述: 直播用户视图层对象
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
public class LiveUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "账号")
    private String account;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "身份：1：主播；2：助手；3：运营")
    private Integer identityType;

    @Schema(description = "头像")
    private String head;

    @Schema(description = "公告")
    private String notice;

    @Schema(description = "备注")
    private String remarks;

    @Schema(description = "是否封禁 0否 1普通封禁 2永久封禁")
    private Integer ynForbidden;

    @Schema(description = "封禁原因")
    private String forbiddenDescp;

    @Schema(description = "封禁天数")
    private Integer forbiddenDay;

    @Schema(description = "封禁期限")
    private LocalDateTime forbiddenTime;

    @Schema(description = "助手数量")
    private Integer assistantCount;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "剩余时长")
    private Integer leaveDay;

    @Schema(description = "是否配置过直播信息")
    private Boolean setOpenInfo;

    @Schema(description = "关联主播")
    private List<LiveUserNameVO> relationLiveUsers;

    private String possessLive;

}