package org.sports.app.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.req.LiveSearchRequest;
import org.sports.admin.manage.service.constant.PageConstant;
import org.sports.admin.manage.service.vo.HomeLivingInfoVo;
import org.sports.admin.manage.service.vo.HomeLivingVo;
import org.sports.app.service.service.HomeService;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "首页")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("home")
@MLog
public class HomeController {
    private final HomeService service;

    @Operation(summary = "正在直播", tags = {PageConstant.APP_HOME_PAGE})
    @PostMapping("living/page")
    public Result<PageResponse<HomeLivingVo>> living(@RequestBody @Validated LiveSearchRequest request) {
        return Results.success(service.living(request));
    }

    @Operation(summary = "根据直播ID查询直播详情", tags = {PageConstant.APP_LIVE_INFO})
    @PostMapping("living/info")
    public Result<HomeLivingInfoVo> livingInfo(@RequestParam Long id) {
        return Results.success(service.livingInfo(id));
    }
}
