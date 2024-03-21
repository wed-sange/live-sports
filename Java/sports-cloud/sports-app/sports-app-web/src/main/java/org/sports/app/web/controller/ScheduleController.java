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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.service.vo.LiveVo;
import org.sports.admin.manage.service.vo.MatchCountVO;
import org.sports.app.service.constant.DefaultConstant;
import org.sports.app.service.req.*;
import org.sports.app.service.service.IScheduleService;
import org.sports.app.service.vo.HotMatchVo;
import org.sports.app.service.vo.MatchVO;
import org.sports.app.service.vo.live.LiveUserVO;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

import static org.sports.app.service.constant.DefaultConstant.HOT_MATCH_DAYS;


/**
 * @描述: 赛程模块相关控制器
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/21
 * @创建时间: 09:37
 */
@Tag(name =  "app - 赛程模块")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleController {


    @Resource
    private IScheduleService scheduleService;

    @PostMapping("/getHotMatchList")
    @Operation(summary = "/热门赛事列表查询")
    @MLog
    public Result<List<HotMatchVo>> getHotMatchList(@Validated @RequestBody HotMatchRequest req) {
        LocalDate startTime;
        LocalDate endTime;

        //如果是已完赛的比赛 这里获取今天过去三天的比赛
        if (DefaultConstant.QUERY_FINISH_MATCH.equals(req.getStatus())) {
            startTime = LocalDate.now(ZoneOffset.UTC).plusDays(-HOT_MATCH_DAYS);
            endTime = LocalDate.now(ZoneOffset.UTC);
            return Results.success( scheduleService.getHotMatchList(startTime,endTime,req.getMatchType(),req.getStatus()));
        } else {
            startTime = LocalDate.now(ZoneOffset.UTC);
            endTime = LocalDate.now(ZoneOffset.UTC).plusDays(HOT_MATCH_DAYS);
            return Results.success( scheduleService.getHotMatchList(startTime,endTime,req.getMatchType(),req.getStatus()));
        }

    }

    @PostMapping("/page")
    @Operation(summary = "/比赛分页查询")
    @MLog
    public Result<PageResponse<MatchVO>> getMatchPage(@Validated @RequestBody MatchPageRequest req) {
        return Results.success( scheduleService.getMatchPage(SotokenUtil.getUserId(), req));
    }

    @PostMapping("/latestMatch")
    @Operation(summary = "/最新热门比赛列表（进行中的和未开始的）")
    @MLog
    public Result<List<MatchVO>> getLatestMatch(@Validated @RequestBody MatchRequest req) {
        return Results.success(scheduleService.getLatestMatch(req));
    }
    @PostMapping("/ongoingMatch")
    @Operation(summary = "/直播中的的热门比赛")
    @MLog
    public Result<List<LiveVo>> getOngoingMatch(@Validated @RequestBody OngoingMatchRequest req) {
        return Results.success(scheduleService.getOngoingMatch(req));
    }
    @PostMapping("/match/{matchId}/{matchType}")
    @Operation(summary = "/查询比赛基本信息")
    @MLog
    public Result<MatchVO> getMatchInfoByMatchId(@PathVariable("matchId") Integer matchId, @PathVariable("matchType") Integer matchType) {
        MatchVO matchVO = scheduleService.getMatchInfoByMatchId(matchId, MatchType.getEnum(matchType));
        if(Objects.nonNull(matchVO)) {
            //增加纯净流，纯净流直播ID格式：比赛类型+"_"+比赛ID
            String liveId = matchType + "_" + matchId;
            matchVO.getAnchorList().add(LiveUserVO.builder().liveId(liveId).nickName("纯净流").isPureFlow(true).build());
        }
        return Results.success(matchVO);
    }
    @PostMapping("/match/anchorList/{matchId}/{matchType}")
    @Operation(summary = "/查询比赛正在直播的主播列表")
    @MLog
    public Result<List<LiveUserVO>> getAnchorListByMatchId(@PathVariable("matchId") Integer matchId, @PathVariable("matchType") Integer matchType) {
        List<LiveUserVO> liveUserVOList = scheduleService.getAnchorListByMatchId(matchId, MatchType.getEnum(matchType));
        String liveId = matchType + "_" + matchId;
        liveUserVOList.add(LiveUserVO.builder().liveId(liveId).nickName("纯净流").isPureFlow(true).build());
        return Results.success(liveUserVOList);
    }
    @PostMapping("/getMatchTimeCount")
    @Operation(summary = "/查询日历比赛数量")
    @MLog
    public Result<List<MatchCountVO>> getMatchTimeCount(@Validated @RequestBody MatchCountRequest request) {
        return Results.success(scheduleService.getMatchTimeCount(request));
    }
}
