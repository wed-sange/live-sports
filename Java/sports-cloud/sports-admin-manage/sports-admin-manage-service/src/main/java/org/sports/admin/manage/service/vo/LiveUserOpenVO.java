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

 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class LiveUserOpenVO {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "昵称")
    private String nickName;

    @Schema(description = "头像")
    private String head;

    @Schema(description = "公告")
    private String notice;

    @Schema(description = "直播封面")
    private String titlePage;

    @Schema(description = "开播首条聊天")
    private String firstMessage;

    @Schema(description = "粉丝数量")
    private Long fansCount;

    @Schema(description = "是否关注")
    private boolean focus;
}
