package org.sports.admin.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.AppUserDO;
import org.sports.admin.manage.dao.req.UserForbiddenRequest;
import org.sports.admin.manage.dao.req.UserPageRequest;
import org.sports.admin.manage.service.service.IAppUserService;
import org.sports.admin.manage.service.vo.AppUserVO;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * APP用户信息 前端控制器
 *

 * @since 2023-07-20
 */
@RestController
@RequestMapping("/app/user")
@Tag(name =  "APP用户管理")
public class AppUserController {

    @Autowired
    private IAppUserService appUserService;
    @Autowired
    private RedissonClient redissonClient;

    @PostMapping("/page")
    @Operation(summary = "APP用户分页查询")
    @SaCheckPermission("app:user:query")
    @MLog
    public Result<PageResponse<AppUserVO>> getAppUserPage(@Validated @RequestBody UserPageRequest req) {
        return Results.success(appUserService.getAppUserPage(req));
    }

    @GetMapping("/getUserInfo/{id}")
    @Operation(summary = "获取APP用户信息")
    @SaCheckPermission("app:user:query")
    @MLog
    public Result<AppUserVO> getAppUserInfo(@PathVariable("id") Long id) {
        AppUserDO appUserDO = appUserService.getById(id);
        AppUserVO appUserVO = new AppUserVO();
        BeanUtils.copyProperties(appUserDO, appUserVO);
        return Results.success(appUserVO);
    }

    @PutMapping("/forbidden")
    @Operation(summary = "禁言用户")
    @SaCheckPermission("app:user:forbidden")
    @MLog
    public Result forbidden(@Validated @RequestBody UserForbiddenRequest forbiddenRequest) {
        if(forbiddenRequest.getYnForbidden() < 1 || forbiddenRequest.getYnForbidden() > 2){
            return Results.failure("是否禁言状态有误");
        }
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        LocalDateTime forbiddenTime = null;
        if(forbiddenRequest.getYnForbidden() == 1){
            if(forbiddenRequest.getForbiddenDay() == null){
                return Results.failure("非永久禁言-禁言天数不能为空");
            }
            forbiddenTime = nowTime.plusDays(forbiddenRequest.getForbiddenDay());
        }
        AppUserDO appUserDO = new AppUserDO();
        BeanUtils.copyProperties(forbiddenRequest, appUserDO);
        appUserDO.setForbiddenTime(forbiddenTime);
        appUserService.forbiddenUser(appUserDO);
        return Results.success();
    }

    @PutMapping("/free/{id}")
    @Operation(summary = "用户解除禁言")
    @SaCheckPermission("app:user:forbidden")
    @MLog
    public Result free(@PathVariable("id") Long id) {
        AppUserDO appUserDO = appUserService.getById(id);
        AppUserVO appUserVO = new AppUserVO();
        BeanUtils.copyProperties(appUserDO, appUserVO);
        appUserService.unForbiddenUser(appUserVO);
        return Results.success();
    }

}
