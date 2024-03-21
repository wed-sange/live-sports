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

/**
 * @描述: 线路
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/18
 * @创建时间: 14:37
 */
@Schema(description = "后台管理 - 活动信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class LinesVO {

    @Schema(description = "普清")
    private PushUrl ld;

    @Schema(description = "中文高清")
    private PushUrl chineseHd;

    @Schema(description = "英文高清")
    private PushUrl englishHd;

    @Schema(description = "其他高清")
    private PushUrl otherHd;


    @Data
    @Builder
    public static class PushUrl {
        /**
         * RTMP
         */
        private String rtmpUrl;
        /**
         * M3U8
         */
        private String m3u8Url;
        /**
         * flv
         */
        private String flvUrl;
    }
}
