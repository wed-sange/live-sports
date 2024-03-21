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

package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @描述: 短信DAO
 */
@Data
@Schema(description = "短信配置信息")
@TableName("t_admin_sms")
public class SmsDO implements Serializable {


    /**
     * id
     */
    @Schema(description = "ID 新增不传， 修改时必传")
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

    /**
     * 您的accessKey
     */
    @NotBlank(message = "accessKey不能为空")
    @Schema(description = "accessKey", required = true)
    private String accessKeyId;

    /**
     * 您的accessKeySecret
     */
    @NotBlank(message = "accessKeySecret不能为空")
    @Schema(description = "accessKeySecret", required = true)
    private String accessKeySecret;

    /**
     * 短信签名
     */
    @NotBlank(message = "短信签名不能为空")
    @Schema(description = "短信签名", required = true)
    private String signature;

    /**
     * 短信模板ID
     */
    @NotBlank(message = "短信模板ID不能为空")
    @Schema(description = "短信模板ID", required = true)
    private String templateId;

    /**
     * 您的sdkAppId
     */
    @NotBlank(message = "appId/senderId(阿里云/腾讯云专属)不能为空")
    @Schema(description = "appId/senderId(阿里云/腾讯云专属)", required = true)
    private String sdkAppId;

    /**
     * 启用状态
     */
    @NotBlank(message = "启用状态不能为空")
    @Schema(description = "启用状态 false关闭 true开启", required = true)
    private Boolean openStatus;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;


}
