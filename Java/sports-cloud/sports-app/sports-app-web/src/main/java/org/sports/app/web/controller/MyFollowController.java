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

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.entity.AppUserLiveShareDO;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.FriendsPageRequest;
import org.sports.admin.manage.service.constant.CacheConstant;
import org.sports.admin.manage.service.service.IAppUserLiveShareService;
import org.sports.app.service.service.IMyFollowService;
import org.sports.app.service.service.IScheduleService;
import org.sports.app.service.vo.MatchVO;
import org.sports.app.service.vo.live.MyFollowAnchorVO;
import org.sports.springboot.starter.cache.DistributedCache;
import org.sports.springboot.starter.cache.toolkit.CacheUtil;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


/**
 * @描述: 我的关注和订阅模块相关控制器
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/24
 * @创建时间: 15:37
 */
@Tag(name = "app - 我的关注和订阅模块")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/follow")
public class MyFollowController {


    @Resource
    private IMyFollowService myFollowService;

    @Resource
    private IScheduleService scheduleService;

    @Resource
    private DistributedCache distributedCache;
    @Resource
    private IAppUserLiveShareService appUserLiveShareService;

    @PostMapping("/matchPage")
    @Operation(summary = "/我关注的比赛分页查询")
    @MLog
    @SaCheckLogin
    public Result<PageResponse<MatchVO>> getMyFollowMatchList(@Validated @RequestBody PageRequest req) {
        return Results.success(scheduleService.getMyFollowMatchList(SotokenUtil.getCheckUserId(), req));
    }

    @PostMapping("/anchorPage")
    @Operation(summary = "/我关注的主播分页查询")
    @MLog
    @SaCheckLogin
    public Result<PageResponse<MyFollowAnchorVO>> getMyFollowAnchorPage(@Validated @RequestBody PageRequest req) {
        return Results.success(myFollowService.getMyFollowAnchorPage(SotokenUtil.getCheckUserId(), req));
    }

    @PostMapping("/myFriendsPage")
    @Operation(summary = "/我的好友分页查询")
    @MLog
    @SaCheckLogin
    public Result<PageResponse<MyFollowAnchorVO>> getMyFriendsPage(@Validated @RequestBody FriendsPageRequest req) {
        return Results.success(myFollowService.getMyFriendsPage(SotokenUtil.getCheckUserId(), req));
    }

    @PostMapping("/anchor/follow/{anchorId}")
    @Operation(summary = "/关注主播")
    @MLog
    @SaCheckLogin
    public Result followAnchor(@PathVariable("anchorId") Long anchorId) {

        Long userId = SotokenUtil.getCheckUserId();
        String cacheKey = CacheUtil.buildKey(CacheConstant.USER_FOLLOW_ANCHOR,userId.toString(),anchorId.toString());
        if(Objects.nonNull(distributedCache.get(cacheKey))){
            return Results.failure("您的操作过于频繁，请稍后再试");
        }
        boolean focusAnchor = myFollowService.isFocusAnchor(userId, anchorId);
        if(!focusAnchor) {
            myFollowService.followAnchor(SotokenUtil.getCheckUserId(), anchorId);
        }
        distributedCache.put(cacheKey,true,2, TimeUnit.SECONDS);
        return Results.success();
    }

    @PostMapping("/anchor/unfriend/{anchorId}")
    @Operation(summary = "/取消好友")
    @MLog
    @SaCheckLogin
    public Result unfriendAnchor(@PathVariable("anchorId") Long anchorId) {
        myFollowService.unfriendAnchor(SotokenUtil.getCheckUserId(), anchorId);
        return Results.success();
    }

    @PostMapping("/anchor/unfollow/{anchorId}")
    @Operation(summary = "/取消关注主播")
    @MLog
    @SaCheckLogin
    public Result unfollowAnchor(@PathVariable("anchorId") Long anchorId) {
        myFollowService.unfollowAnchor(SotokenUtil.getCheckUserId(), anchorId);
        return Results.success();
    }

    @PostMapping("/match/follow/{matchId}/{matchType}")
    @Operation(summary = "/关注比赛")
    @MLog
    @SaCheckLogin
    public Result followMatch(@PathVariable("matchId") Long matchId, @PathVariable("matchType") Integer matchType) {
        Long userId = SotokenUtil.getCheckUserId();
        String cacheKey = CacheUtil.buildKey(CacheConstant.USER_FOLLOW_MATCH,userId.toString(),matchId.toString(),matchType.toString());
        if(Objects.nonNull(distributedCache.get(cacheKey))){
            return Results.failure("您的操作过于频繁，请稍后再试");
        }
        boolean focusAnchor = myFollowService.isFocusMatch(userId, matchId.intValue(),MatchType.getEnum(matchType));
        if(!focusAnchor) {
            myFollowService.followMatch(SotokenUtil.getCheckUserId(), matchId, MatchType.getEnum(matchType));
        }
        distributedCache.put(cacheKey,true,2, TimeUnit.SECONDS);
        return Results.success();
    }

    @PostMapping("/match/unfollow/{matchId}/{matchType}")
    @Operation(summary = "/取消关注比赛")
    @MLog
    @SaCheckLogin
    public Result unfollowMatch(@PathVariable("matchId") Long matchId, @PathVariable("matchType") Integer matchType) {
        myFollowService.unfollowMatch(SotokenUtil.getCheckUserId(), matchId, MatchType.getEnum(matchType));
        return Results.success();
    }


    @PostMapping("/live/share/{liveId}")
    @Operation(summary = "/直播分享")
    @MLog
    public Result liveShare(@PathVariable("liveId") Long liveId) {
        Long userId = SotokenUtil.getUserId();
        if(userId == null){
            return Results.success();
        }
        if(appUserLiveShareService.count(new LambdaQueryWrapper<AppUserLiveShareDO>()
                .eq(AppUserLiveShareDO::getUserId, userId)
                .eq(AppUserLiveShareDO::getLiveId, liveId)) > 0){
            return Results.success();
        }
        AppUserLiveShareDO appUserLiveShare = new AppUserLiveShareDO();
        appUserLiveShare.setUserId(userId);
        appUserLiveShare.setLiveId(liveId);
        appUserLiveShare.setCreateTime(LocalDateTime.now(ZoneOffset.UTC));
        if(appUserLiveShareService.save(appUserLiveShare)){
            return Results.success();
        }
        return Results.failure("直播分享失败");
    }
}
