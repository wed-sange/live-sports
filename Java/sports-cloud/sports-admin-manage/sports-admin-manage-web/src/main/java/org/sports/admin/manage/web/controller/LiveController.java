package org.sports.admin.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.req.LivePageRequest;
import org.sports.admin.manage.service.service.ILiveService;
import org.sports.admin.manage.service.vo.LiveStatisticsVO;
import org.sports.admin.manage.service.vo.LiveVo;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "直播接口")
@Validated
@RequiredArgsConstructor
@RestController
public class LiveController {
    private final ILiveService service;

    @Operation(summary = "关播-管理人员")
    @PostMapping("live/close/admin/{id}")
    @SaCheckPermission("anchor:manage:close")
    @MLog
    public Result<?> liveCloseByAdmin(@PathVariable Long id) {
        service.liveCloseByAdmin(id);
        return Results.success();
    }

    @PostMapping("/live/page")
    @Operation(summary = "/直播列表分页查询")
    @SaCheckPermission("anchor:manage:query")
    @MLog
    public Result<PageResponse<LiveVo>> getLivePage(@Validated @RequestBody LivePageRequest req) {
        return Results.success(service.getLivePage(req));
    }

    @PostMapping("/live/statistics/{userId}/{type}")
    @Operation(summary = "/直播统计（参数主播ID和统计类型【1：本月 2：近三月 3：全部】）")
    @SaCheckPermission("anchor:manage:info")
    @MLog
    public Result<LiveStatisticsVO> getLiveStatistics(@PathVariable("userId") Long userId, @PathVariable("type") Integer type) {
        return Results.success(service.getLiveStatistics(userId, type));
    }
}
