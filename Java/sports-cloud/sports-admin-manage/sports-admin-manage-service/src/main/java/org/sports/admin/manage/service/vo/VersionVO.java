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

import java.time.LocalDateTime;

/**
 * @描述: 版本视图层对象
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/18
 * @创建时间: 14:37
 */
@Schema(description = "后台管理 - 版本信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class VersionVO {

    @Schema(description = "版本ID", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @Schema(description = "渠道：1：安卓 2：IOS")
    private Integer channel;

    @Schema(description = "版本")
    private String version;

    @Schema(description = "版本说明")
    private String remarks;

    @Schema(description = "是否强制更新：0 ：不强制 1：强制")
    private Integer forcedUpdate;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "资源路径")
    private String sourceUrl;

}
