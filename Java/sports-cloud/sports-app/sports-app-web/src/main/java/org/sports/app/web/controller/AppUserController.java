package org.sports.app.web.controller;


import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.AppUserDO;
import org.sports.admin.manage.dao.req.UserUpdateRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.IAppUserService;
import org.sports.admin.manage.service.vo.AnchorControlUserVO;
import org.sports.admin.manage.service.vo.AppUserVO;
import org.sports.springboot.starter.base.exception.enums.GlobalErrorCodeConstants;
import org.sports.springboot.starter.base.exception.enums.ServiceErrorCodeRange;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * APP用户管理入口
 *

 * @since 2023-07-20
 */
@RestController
@RequestMapping("/user")
@Tag(name = "APP用户管理入口")
@SaCheckLogin
public class AppUserController {

    @Autowired
    private IAppUserService appUserService;

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private DistributedCache distributedCache;

    @GetMapping("/getUserInfo")
    @Operation(summary = "获取当前APP用户信息")
    @MLog
    public Result<AppUserVO> getUserInfo() {
        Long userId = SotokenUtil.getCheckUserId();
        AppUserVO appUserVO = appUserService.getCacheAppUserById(userId);
        return Results.success(appUserVO);
    }

    @PutMapping("/updateInfo")
    @Operation(summary = "修改当前用户信息")
    @MLog
    public Result updateInfo(@Validated @RequestBody UserUpdateRequest userUpdateRequest) {
        Long userId = SotokenUtil.getCheckUserId();
        if (Strings.isEmpty(userUpdateRequest.getHead()) && Strings.isEmpty(userUpdateRequest.getName())) {
            return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
        }
        if (Strings.isNotBlank(userUpdateRequest.getName()) && userUpdateRequest.getName().length() > 8) {
            return Results.failure("昵称长度8个字符以内");
        }
        LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
        if (Strings.isNotEmpty(userUpdateRequest.getName())) {
            if (userUpdateRequest.getName().length() > 8) {
                return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
            }
            List<AppUserDO> byNickName = appUserService.getByNickName(userUpdateRequest.getName());
            if (CollectionUtil.isNotEmpty(byNickName) && byNickName.stream().filter(item -> !item.getId().equals(userId)).collect(Collectors.toList()).size() > 0) {
                return Results.failure(ServiceErrorCodeRange.HAS_SAME_NICKNAME);
            }
        }
        appUserService.update(new LambdaUpdateWrapper<AppUserDO>().eq(AppUserDO::getId, userId)
                .set(Strings.isNotEmpty(userUpdateRequest.getHead()), AppUserDO::getHead, userUpdateRequest.getHead())
                .set(Strings.isNotEmpty(userUpdateRequest.getName()), AppUserDO::getName, userUpdateRequest.getName())
                .set(Strings.isNotEmpty(userUpdateRequest.getName()), AppUserDO::getNameLastTime, nowTime)
                .set(AppUserDO::getUpdateTime, nowTime));
        appUserService.refreshCacheAppUser(userId);
        return Results.success();
    }

    @DeleteMapping("/logout")
    @Operation(summary = "当前APP用户退出")
    @MLog
    public Result logout() {
        SotokenUtil.logout();
        return Results.success();
    }

    @DeleteMapping("/cancel")
    @Operation(summary = "当前APP用户注销")
    @MLog
    public Result cancel() {
        Long userId = SotokenUtil.getCheckUserId();
        SotokenUtil.kickout();
        appUserService.update(new LambdaUpdateWrapper<AppUserDO>().eq(AppUserDO::getId, userId)
                .set(AppUserDO::getYnCancel, YnEnum.ONE.getValue())
                .set(AppUserDO::getNameLastTime, LocalDateTime.now(ZoneOffset.UTC)));
        appUserService.refreshCacheAppUser(userId);
        return Results.success();
    }

    @GetMapping("/getAnchorControlUserInfo/{anchorId}")
    @Operation(summary = "查询主播对当前用户禁言和踢出直播间信息")
    @MLog
    public Result<AnchorControlUserVO> getAnchorControlUserInfo(@PathVariable Long anchorId) {
        Long userId = SotokenUtil.getCheckUserId();
        AnchorControlUserVO anchorControlUserVO = new AnchorControlUserVO();
        anchorControlUserVO.setAnchorId(anchorId);
        anchorControlUserVO.setUserId(userId);
        anchorControlUserVO.setMute(redissonClient.getSet(CacheUtil.buildKey(RedisCacheConstant.APP_ANCHOR_USER_FORBIDDEN, String.valueOf(anchorId))).contains(String.valueOf(userId)));
        String cacheKey = CacheUtil.buildKey(CacheConstant.KICK_OUT_USER_KEY, String.valueOf(userId), String.valueOf(anchorId));
        String value = distributedCache.get(cacheKey);
        if (Objects.isNull(value)) {
            anchorControlUserVO.setTickOut(false);
        } else {
            anchorControlUserVO.setTickOut(true);
            anchorControlUserVO.setTickOutLeftTime(distributedCache.getExpirationTime(cacheKey, TimeUnit.MINUTES));
        }
        return Results.success(anchorControlUserVO);
    }
}
