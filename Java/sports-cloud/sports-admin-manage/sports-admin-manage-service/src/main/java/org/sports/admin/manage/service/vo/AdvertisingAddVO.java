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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sports.admin.manage.dao.enums.AdvertisingStatus;
import org.sports.admin.manage.dao.enums.AdvertisingType;

/**
 * 广告位配置视图层对象
 */
@Schema(description = "运营管理 - 广告位配置信息 新增VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class AdvertisingAddVO {
    /**
     * 名称
     */
    @Schema(description = "广告名称")
    private String name;

    /**
     * 渠道类型
     *
     */
    @Schema(description = "渠道: 1:直播间滚动条;2:直播间展示位;3:个人中心;4:banner")
    private AdvertisingType channel;


    @Schema(description = "状态:   1:上架中; 2:下架中")
    private AdvertisingStatus status;

    /**
     * 文字
     */
    @Schema(description = "文字")
    private String text;

    /**
     * 图片展示地址
     */
    @Schema(description = "图片展示地址")
    private String imgUrl;

    /**
     * 跳转地址
     */
    @Schema(description = "跳转地址")
    private String targetUrl;

    /**
     * 备注
     */
    @Schema(description = "备注")
    private String remark;
}
