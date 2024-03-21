package org.sports.live.manage.web.controller;


import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.admin.manage.service.converter.LiveUserConvert;
import org.sports.admin.manage.service.service.IAppPinnedUserService;
import org.sports.admin.manage.service.service.IChatGroupUserService;
import org.sports.admin.manage.service.service.ILiveUserService;
import org.sports.admin.manage.service.vo.ChatGroupUserVO;
import org.sports.admin.manage.service.vo.LiveUserVO;
import org.sports.springboot.starter.base.exception.enums.GlobalErrorCodeConstants;
import org.sports.springboot.starter.base.exception.enums.ServiceErrorCodeRange;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * live用户管理入口
 *

 * @since 2023-07-20
 */
@RestController
@RequestMapping("/user")
@Tag(name = "live用户管理入口")
public class LiveUserController {

    @Autowired
    private ILiveUserService liveUserService;
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private IChatGroupUserService chatGroupUserService;

    @Resource
    private IAppPinnedUserService iAppPinnedUserService;

    @GetMapping("/getUserInfo")
    @Operation(summary = "获取当前直播用户信息")
    @MLog
    public Result<LiveUserVO> getUserInfo() {
        Long userId = SotokenUtil.getCheckUserId();
        LiveUserVO liveUserVO = liveUserService.getLiveUserInfo(userId);
        return Results.success(liveUserVO);
    }

    @PutMapping("/updateInfo")
    @Operation(summary = "修改当前直播用户信息")
    @MLog
    public Result updateInfo(@Validated @RequestBody UserUpdateRequest userUpdateRequest) {
        Long userId = SotokenUtil.getCheckUserId();
        if (Strings.isEmpty(userUpdateRequest.getHead()) && Strings.isEmpty(userUpdateRequest.getName())) {
            return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
        }
        liveUserService.update(new LambdaUpdateWrapper<LiveUserDO>()
                .eq(LiveUserDO::getId, userId)
                .set(Strings.isNotEmpty(userUpdateRequest.getHead()), LiveUserDO::getHead, userUpdateRequest.getHead())
                .set(Strings.isNotEmpty(userUpdateRequest.getName()), LiveUserDO::getNickName, userUpdateRequest.getName())
                .set(LiveUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
        //主播缓存刷新
        LiveUserVO liveUserVO = LiveUserConvert.INSTANCE.convertToVo(liveUserService.getById(userId));
        if (IdentityType.ANCHOR.getCode().equals(liveUserVO.getIdentityType())) {
            redissonClient.getMap(RedisCacheConstant.LIVE_ANCHOR_INFO_CACHE).put(userId, JSONObject.toJSONString(liveUserVO));
        }
        return Results.success();
    }


    @PutMapping("/updatePasswd")
    @Operation(summary = "修改当前直播用户密码")
    @MLog
    public Result updatePasswd(@Validated @RequestBody UserPasswdUpdateRequest passwdUpdateRequest) {
        Long userId = SotokenUtil.getCheckUserId();
        if (!passwdUpdateRequest.getNewPasswd().equals(passwdUpdateRequest.getRePasswd())) {
            return Results.failure(GlobalErrorCodeConstants.BAD_REQUEST);
        }
        LiveUserDO liveUserDO = liveUserService.getById(userId);
        if (!liveUserDO.getPasswd().equals(DigestUtil.md5Hex(passwdUpdateRequest.getOldPasswd()))) {
            return Results.failure(ServiceErrorCodeRange.PASSWD_FAIL);
        }
        liveUserService.update(new LambdaUpdateWrapper<LiveUserDO>()
                .eq(LiveUserDO::getId, userId)
                .set(LiveUserDO::getPasswd, DigestUtil.md5Hex(passwdUpdateRequest.getNewPasswd()))
                .set(LiveUserDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
        SotokenUtil.logout(userId, UserChannelEnum.LIVE, null);
        return Results.success();
    }

    @DeleteMapping("/logout")
    @Operation(summary = "当前直播用户退出")
    @MLog
    public Result logout() {
        SotokenUtil.logout();
        return Results.success();
    }

    @PutMapping("/kickOut")
    @Operation(summary = "踢出直播间用户")
    @MLog
    public Result kickOut(@Validated @RequestBody KickOutUserRequest kick) {
        //提出用户目前逻辑是15分钟不允许再进入此主播直播间，和主播挂钩，不和直播间挂钩，防redis中，30分钟过期
         liveUserService.kickOutUser(kick);
        return Results.success();
    }

    @PutMapping("/forbidden")
    @Operation(summary = "禁言用户")
    @MLog
    public Result forbidden(@Validated @RequestBody MuteUserRequest muteUserRequest) {
        liveUserService.forbiddenAppUser(muteUserRequest);
        return Results.success();
    }

    @PutMapping("/unforbidden")
    @Operation(summary = "用户解除禁言")
    @MLog
    public Result unforbidden(@Validated @RequestBody MuteUserRequest muteUserRequest) {
        liveUserService.unforbiddenAppUser(muteUserRequest);
        return Results.success();
    }
    @PostMapping("/queryGroupUserPage")
    @Operation(summary = "查询直播间用户列表")
    @MLog
    public Result<PageResponse<ChatGroupUserVO>> queryGroupUserPage(@Validated @RequestBody GroupUserPageRequest req) {
        return Results.success(chatGroupUserService.getGroupUser(req));
    }

    @PutMapping("/setTop")
    @Operation(summary = "置顶")
    @MLog
    public Result setTop(@Validated @RequestBody TopUserRequest topUserRequest) {
        liveUserService.setTop(topUserRequest);
        return Results.success();
    }

    @PutMapping("/untop")
    @Operation(summary = "取消置顶")
    @MLog
    public Result untop(@Validated @RequestBody TopUserRequest topUserRequest) {
        liveUserService.untop(topUserRequest);
        return Results.success();
    }

}
