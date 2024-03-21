package org.sports.admin.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.req.HotMatchAddRequest;
import org.sports.admin.manage.dao.req.HotMatchListRequest;
import org.sports.admin.manage.dao.req.MatchSearchVoRequest;
import org.sports.admin.manage.service.service.IHotMatchService;
import org.sports.admin.manage.service.vo.HotMatchListVo;
import org.sports.admin.manage.service.vo.MatchSearchVo;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "热门比赛管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("match/hot")
@MLog
public class HotMatchController {

    private final IHotMatchService service;

    @Operation(summary = "搜索联赛")
    @PostMapping("search")
    @MLog
    public Result<List<MatchSearchVo>> search(@RequestBody @Validated MatchSearchVoRequest request) {
        return Results.success(service.search(request));
    }

    @Operation(summary = "热门比赛列表")
    @PostMapping("list")
    @SaCheckPermission("match:hot:query")
    @MLog
    public Result<List<HotMatchListVo>> list(@RequestBody @Validated HotMatchListRequest request) {
        return Results.success(service.list(request));
    }

    @Operation(summary = "添加")
    @PostMapping("add")
    @SaCheckPermission("match:hot:add")
    @MLog
    public Result<?> add(@RequestBody @Validated HotMatchAddRequest request) {
        service.add(request);
        return Results.success();
    }

    @Operation(summary = "移除")
    @PostMapping("remove/{id}")
    @SaCheckPermission("match:hot:remove")
    @MLog
    public Result<?> remove(@PathVariable("id") Long id) {
        service.remove(id);
        return Results.success();
    }
}
