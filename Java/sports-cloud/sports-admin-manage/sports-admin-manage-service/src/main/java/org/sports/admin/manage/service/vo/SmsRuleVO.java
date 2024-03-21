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

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sports.admin.manage.dao.entity.SmsDO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @描述: 短信功能配置视图层对象
 */
@Schema(description = "后台管理 - 短信功能信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsRuleVO implements Serializable {

    /**
     * 验证方式json[1,2,3,4]
     */
    @NotNull(message = "验证方式不能为空")
    @Schema(description = "验证方式id数组 json[1,2,3,4]")
    private List<Long> vaTypeList;
    /**
     * 使用方式： 1随机使用 2轮替使用
     */
    @NotNull(message = "使用方式不能为空")
    @Schema(description = "使用方式： 1随机使用 2轮替使用")
    private Integer useType;
    /**
     * 轮替次数
     */
    @NotNull(message = "轮替次数不能为空")
    @Schema(description = "轮替次数")
    private Integer ratateNum;
    /**
     * 异常次数
     */
    @NotNull(message = "异常次数不能为空")
    @Schema(description = "异常次数")
    private Integer errorNum;
    /**
     * 系统通知联系人
     */
    @NotBlank(message = "系统通知联系人不能为空")
    @Schema(description = "系统通知联系人")
    private String contactInfo;
}
