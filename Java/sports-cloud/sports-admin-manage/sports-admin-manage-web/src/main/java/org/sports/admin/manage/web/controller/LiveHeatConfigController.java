package org.sports.admin.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.service.service.ILiveHeatConfigService;
import org.sports.admin.manage.service.vo.LiveHeatConfigVO;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 直播间热度管理 前端控制器
 * </p>
 *

 * @since 2023-07-26
 */
@RestController
@RequestMapping("/live/heat/config")
@Tag(name =  "直播间热度配置管理")
public class LiveHeatConfigController {

    @Autowired
    private ILiveHeatConfigService liveHeatConfigService;

    @GetMapping("/getInfo")
    @Operation(summary = "获取直播间热度配置信息")
    @SaCheckPermission("live:heat:query")
    @MLog
    public Result<LiveHeatConfigVO> getInfo() {
        LiveHeatConfigVO liveHeatConfigVO = liveHeatConfigService.getLiveHeatConfig();
        return Results.success(liveHeatConfigVO);
    }

    @PutMapping("/update")
    @Operation(summary = "修改直播间热度配置信息")
    @SaCheckPermission("live:heat:update")
    @MLog
    public Result update(@Validated @RequestBody LiveHeatConfigVO liveHeatConfigVO) {
        liveHeatConfigService.updateLiveHeatConfig(liveHeatConfigVO);
        return Results.success();
    }


}
