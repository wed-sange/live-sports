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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * @描述: 短信验证方式配置视图层对象
 */
@Schema(description = "后台管理 - 短信验证方式信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SmsVaVO implements Serializable {

    /**
     * id
     */
    @Schema(description = "ID")
    private Long id;

    /**
     * 标号
     */
    @Schema(description = "标号")
    private Integer smsNo;

    /**
     * 平台类型（阿里云，腾讯云）
     */
    @Min(value = 1, message = "最小是1")
    @Max(value = 2, message = "最大是2")
    @NotNull(message = "平台类型不能为空")
    @Schema(description = "平台类型（1阿里云，2腾讯云", required = true)
    private Integer supplier;

}
