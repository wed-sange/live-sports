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

package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 广告状态
 */
@Getter
@AllArgsConstructor
@Schema(description = "广告状态,1:直播间滚动条,2:直播间展示位,3:个人中心,4:banner", example = "1")
public enum AdvertisingType {
    /**
     * 直播间滚动条
     */
    LIVE_SCROLL_BAR(1, "直播间滚动条"),
    /**
     * 直播间展示位
     */
    LIVE_SHOW_POSITION(2, "直播间展示位"),
    /**
     * 个人中心
     */
    PERSONAL_CENTER(3, "个人中心"),
    /**
     * banner
     */
    BANNER(4, "banner"),
    ;
    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
}
