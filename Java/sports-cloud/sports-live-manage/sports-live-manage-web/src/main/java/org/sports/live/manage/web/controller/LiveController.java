package org.sports.live.manage.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.entity.LiveDO;
import org.sports.admin.manage.dao.req.LiveOpenRequest;
import org.sports.admin.manage.dao.req.LiveUpdateAddressRequest;
import org.sports.admin.manage.service.constant.PageConstant;
import org.sports.admin.manage.service.service.ILiveHeatConfigService;
import org.sports.admin.manage.service.service.ILiveService;
import org.sports.admin.manage.service.vo.HomeLivingInfoVo;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Tag(name = "直播接口")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("live")
public class LiveController {

    @Resource
    private  ILiveService service;

    @Resource
    private ILiveHeatConfigService liveHeatConfigService;

    @Operation(summary = "开启直播")
    @PostMapping("open")
    @MLog
    public Result<LiveDO> liveOpen(@Validated @RequestBody LiveOpenRequest request) {
        return Results.success(service.liveOpen(request));
    }

    @Operation(summary = "关播")
    @PostMapping("close")
    @MLog
    public Result<?> liveClose() {
        service.liveClose(SotokenUtil.getCheckUserId());
        return Results.success();
    }

    @Operation(summary = "更新直播拉流地址")
    @PostMapping("update/address")
    @MLog
    public Result<?> updateAddress(@Validated @RequestBody LiveUpdateAddressRequest request) {
        service.updateAddress(SotokenUtil.getCheckUserId(), request);
        return Results.success();
    }
    @Operation(summary = "根据直播ID查询直播详情", tags = {PageConstant.APP_LIVE_INFO})
    @PostMapping("living/info")
    public Result<HomeLivingInfoVo> livingInfo(@RequestParam Long id) {
        return Results.success(service.livingInfo(id));
    }
    @Operation(summary = "获取CDN推流地址")
    @PostMapping("getCDNStreamingAddress")
    @MLog
    public Result<String> updateAddress() {
        return Results.success(liveHeatConfigService.getLiveHeatConfig().getStreamingAddress());
    }

}
