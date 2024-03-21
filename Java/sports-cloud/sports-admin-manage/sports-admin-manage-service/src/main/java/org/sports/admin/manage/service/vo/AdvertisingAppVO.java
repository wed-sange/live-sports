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


@Schema(description = "Banner Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class AdvertisingAppVO {
    /**
     * id
     */
    @Schema(description = "广告ID")
    private Long id;
    /**
     * 名称
     */
    @Schema(description = "广告名称")
    private String name;

    /**
     * 文字
     */
    @Schema(description = "文字")
    private String text;

    /**
     * 图片展示地址
     */
    @Schema(description = "图片地址")
    private String imgUrl;

    /**
     * 跳转地址
     */
    @Schema(description = "跳转地址")
    private String targetUrl;

}
