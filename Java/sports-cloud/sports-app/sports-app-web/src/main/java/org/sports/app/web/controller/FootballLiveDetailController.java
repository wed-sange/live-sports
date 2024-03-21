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

package org.sports.app.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.app.service.service.IFootballLiveDetailService;
import org.sports.app.service.vo.live.*;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 直播详情
 *

 */
@Tag(name = "直播信息- 直播详情")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/live/detail/football")
public class FootballLiveDetailController {
    @Resource
    private IFootballLiveDetailService footballLiveDetailService;

    @Operation(summary = "查询文字直播信息-足球")
    @GetMapping("/text/live/{matchId}")
    @MLog
    public Result<List<TliveVO>> getTextLive(@PathVariable("matchId") @Schema(description = "比赛ID") String matchId) {
        return Results.success(footballLiveDetailService.getTextLive(matchId));
    }

    @Operation(summary = "查询比赛事件-足球")
    @GetMapping("/incidents/{matchId}")
    @MLog
    public Result<List<IncidentsVO>> getIncidents(@PathVariable("matchId") @Schema(description = "比赛ID") String matchId) {
        return Results.success(footballLiveDetailService.getIncidents(matchId));
    }

    @Operation(summary = "查询比赛技术统计-足球")
    @GetMapping("/stats/{matchId}")
    @MLog
    public Result<List<StatsVO>> getMatchStats(@PathVariable("matchId") @Schema(description = "比赛ID") String matchId) {
        return Results.success(footballLiveDetailService.getMatchStats(matchId));
    }

    @Operation(summary = "查询比赛阵容-足球")
    @GetMapping("/lineup/{matchId}")
    @MLog
    public Result<MatchLineupDetailVO> getMatchLineupDetail(@PathVariable("matchId") @Schema(description = "比赛ID") String matchId) {
        return Results.success(footballLiveDetailService.getMatchLineupDetail(matchId));
    }

    @Operation(summary = "查询指数数据-足球")
    @GetMapping("/odds/{matchId}")
    @MLog
    public Result<CompanyOdds> getMatchOdds(@PathVariable("matchId") @Schema(description = "比赛ID") String matchId) {
        return Results.success(footballLiveDetailService.getMatchOdds(matchId));
    }

}
