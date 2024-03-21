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

package org.sports.live.manage.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.service.enums.BasketballStatusType;
import org.sports.admin.manage.service.enums.FootballStatusType;
import org.sports.admin.manage.service.vo.LinesVO;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @描述: 比赛视图层对象
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/20
 * @创建时间: 11:37
 */
@Schema(description = "APP - 比赛信息 Response VO")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude
public class MatchLiveVO implements Serializable {

    @Schema(description = "赛事ID")
    private Integer competitionId;

    @Schema(description = "赛事ID")
    private String competitionName;

    @Schema(description = "比赛ID", required = true)
    private Integer matchId;

    @Schema(description = "比赛类型：1：足球；2：篮球")
    private MatchType matchType;

    @Schema(description = "比赛时间")
    private LocalDateTime matchTime;

    @Schema(description = "比赛进行时间（分钟）进行中足球比赛有此信息")
    private Integer runTime;


    @Schema(description = "主队名称")
    private String homeName;

    @Schema(description = "主队Logo")
    private String homeLogo;

    @Schema(description = "主队比分")
    private Integer homeScore;

    @Schema(description = "主队半场比分")
    private Integer homeHalfScore;

    @Schema(description = "客队名称")
    private String awayName;

    @Schema(description = "客队Logo")
    private String awayLogo;

    @Schema(description = "客队比分")
    private Integer awayScore;

    @Schema(description = "客队半场比分")
    private Integer awayHalfScore;

    /**
     * @see FootballStatusType
     * @see BasketballStatusType
     */
    @Schema(description = "篮球状态:" +
            "0\t比赛异常，说明：暂未判断具体原因的异常比赛，可能但不限于：腰斩、取消等等，建议隐藏处理;" +
            "1\t未开赛;" +
            "2\t第一节;" +
            "3\t第一节完;" +
            "4\t第二节;" +
            "5\t第二节完;" +
            "6\t第三节;" +
            "7\t第三节完;" +
            "8\t第四节;" +
            "9\t加时;" +
            "10\t完场;" +
            "11\t中断;" +
            "12\t取消;" +
            "13\t延期;" +
            "14\t腰斩;" +
            "15\t待定;"+
            "足球状态码:" +
            "0\t比赛异常，说明：暂未判断具体原因的异常比赛，可能但不限于：腰斩、取消等等，建议隐藏处理;" +
            "1\t未开赛;" +
            "2\t上半场;" +
            "3\t中场;" +
            "4\t下半场;" +
            "5\t加时赛;" +
            "6\t加时赛(弃用);" +
            "7\t点球决战;" +
            "8\t完场;" +
            "9\t推迟;" +
            "10\t中断;" +
            "11\t腰斩;" +
            "12\t取消;" +
            "13\t待定"
    )
    private Integer status;
    /**
     * 线路
     */
    @Schema(description = "比赛对应的线路")
    private LinesVO lines;


}
