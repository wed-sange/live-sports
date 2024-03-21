package org.sports.live.manage.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.CompetitionListRequest;
import org.sports.admin.manage.dao.req.HotMatchListRequest;
import org.sports.admin.manage.dao.req.MatchListRequest;
import org.sports.admin.manage.service.service.IHotMatchService;
import org.sports.admin.manage.service.vo.HotMatchListVo;
import org.sports.live.manage.service.IMatchService;
import org.sports.live.manage.vo.CompetitionListVo;
import org.sports.live.manage.vo.MatchListVo;
import org.sports.live.manage.vo.MatchLiveVO;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "比赛信息")
@Validated
@RequiredArgsConstructor
@RestController
public class MatchController {
    private final IMatchService service;
    private final IHotMatchService hot;

    @Operation(summary = "比赛列表")
    @PostMapping("match/list")
    @MLog
    public Result<PageResponse<MatchListVo>> list(@Validated @RequestBody MatchListRequest request) {
        return Results.success(service.list(SotokenUtil.getCheckUserId(), request));
    }

    @Operation(summary = "赛事列表", description = "获取指定日期的赛事列表")
    @PostMapping("match/competition/list")
    @MLog
    public Result<List<CompetitionListVo>> competitionByTime(@Validated @RequestBody CompetitionListRequest request) {
        return Results.success(service.competitionByTime(request));
    }

    @Operation(summary = "热门比赛列表")
    @PostMapping("list")
    @MLog
    public Result<List<HotMatchListVo>> hotList(@RequestBody @Validated HotMatchListRequest request) {
        return Results.success(hot.list(request));
    }

    @PostMapping("/match/{matchId}/{matchType}")
    @Operation(summary = "/查询比赛基本信息")
    @MLog
    public Result<MatchLiveVO> getMatchInfoByMatchId(@PathVariable("matchId") Integer matchId, @PathVariable("matchType") Integer matchType) {
        return Results.success( service.getMatchInfoByMatchId(matchId, MatchType.getEnum(matchType)));
    }
}
