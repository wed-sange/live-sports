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

package org.sports.app.service.vo.live;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sports.admin.manage.dao.enums.MatchType;


/**
 * @描述: 我关注主播视图层对象
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 14:11
 */
@Schema(description = "app - 我关注的主播信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class MyFollowAnchorVO {

    @Schema(description = "主播ID", required = true)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long anchorId;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "头像")
    private String head;

    @Schema(description = "粉丝数")
    private Long fans;

    @Schema(description = "正在直播ID")
    private Long liveId;

    @Schema(description = "正在直播比赛ID")
    private Integer matchId;

    @Schema(description = "比赛类型 1足球，2篮球")
    private MatchType matchType;

    /**
     * 主播昵称首字母
     */
    private String shortName;
}
