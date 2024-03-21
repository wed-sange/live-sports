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

package org.sports.admin.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.constant.UserConstant;
import org.sports.admin.manage.service.service.ILiveService;
import org.sports.admin.manage.service.service.ILiveUserService;
import org.sports.admin.manage.service.vo.LiveUserVO;
import org.sports.springboot.starter.base.utils.RandomUtils;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @描述: 直播用户相关控制器
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 11:53
 */
@Tag(name =  "LIVE用户管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/live/user")
public class LiveUserController {

    @Resource
    private ILiveUserService liveUserService;
    @Resource
    private  ILiveService liveService;

    @PostMapping("/page")
    @Operation(summary = "/主播、运营账号信息分页查询")
    @SaCheckPermission("anchor:account:query")
    @MLog
    public Result<PageResponse<LiveUserVO>> getLiveOperateUserPage(@Validated @RequestBody LiveUserPageRequest req) {
        return Results.success(liveUserService.getLiveOperateUserPage(req));
    }

    @PostMapping("/queryHelpersByLiveId/{liveId}")
    @Operation(summary = "/根据主播ID 查询所有助手")
    @SaCheckPermission("anchor:helper:query")
    @MLog
    public Result<List<LiveUserVO>> queryHelpersByLiveId(@PathVariable("liveId") Long liveId) {
        return Results.success(liveUserService.queryHelpersByLiveId(liveId));
    }
   
    @PostMapping("/createLiveUser")
    @Operation(summary = "/新增直播用户 新增助手时候必传所属主播")
    @SaCheckPermission("anchor:account:update")
    @MLog
    public Result createActivity(@Validated @RequestBody LiveUserAddRequest request) {
        if(IdentityType.ASSISTANT.getCode() == request.getIdentityType().intValue() && request.getBelongLive() == null){
            return Results.failure("所属主播不能为空");
        }
        liveUserService.createLiveUser(request);
        return Results.success();
    }
    @PostMapping("/updateLiveUser")
    @Operation(summary = "/修改直播用户【目前只支持备注修改】")
    @SaCheckPermission("anchor:account:update")
    @MLog
    public Result updateActivity(@Validated @RequestBody LiveUserUpdateRequest request) {
        liveUserService.updateLiveUser(request);
        return Results.success();
    }
    @GetMapping("/getLiveUserInfo/{id}")
    @Operation(summary = "/查询直播用户明细")
    @SaCheckPermission("anchor:account:query")
    @MLog
    public Result<LiveUserVO> getLiveUserInfo(@PathVariable("id") Long id) {
        return Results.success(liveUserService.getLiveUserInfo(id));
    }
    @PostMapping("/cancelLiveUser/{id}")
    @Operation(summary = "/注销用户")
    @SaCheckPermission("anchor:account:cancel")
    @MLog
    public Result cancelLiveUser(@PathVariable("id") Long id) {
        liveUserService.cancelLiveUser(id);
        //封禁直播用户要进行关播
        liveService.liveClose(id);
        return Results.success();
    }
    @PostMapping("/forbiddenLiveUser")
    @Operation(summary = "/封禁直播用户")
    @SaCheckPermission("anchor:account:forbidden")
    @MLog
    public Result forbiddenLiveUser(@Validated @RequestBody UserForbiddenRequest forbiddenRequest) {
        if(forbiddenRequest.getYnForbidden() < 1 || forbiddenRequest.getYnForbidden() > 2){
            return Results.failure("是否禁言状态有误");
        }
        if(forbiddenRequest.getYnForbidden() == 1 && forbiddenRequest.getForbiddenDay() == null){
            return Results.failure("非永久禁言-禁言天数不能为空");
        }
        liveUserService.forbiddenLiveUser(forbiddenRequest);
        //封禁直播用户要进行关播
        liveService.liveClose(forbiddenRequest.getId());
        return Results.success();
    }
    @PostMapping("/unForbiddenLiveUser/{id}")
    @Operation(summary = "/解禁直播用户")
    @SaCheckPermission("anchor:account:forbidden")
    @MLog
    public Result unForbiddenLiveUser(@PathVariable("id") Long id) {
        liveUserService.unForbiddenLiveUser(id);
        return Results.success();
    }

    @PutMapping("/resetPasswd/{id}")
    @Operation(summary = "重置用户密码")
    @SaCheckPermission("anchor:account:resetPasswd")
    @MLog
    public Result<String> resetPasswd(@PathVariable("id") Long id) {
        liveUserService.resetPasswd(id);
        return Results.success(UserConstant.INIT_PASSWD);
    }

    @GetMapping("/getDefaultPasswd")
    @Operation(summary = "获取默认密码")
    @MLog
    public Result<String> getDefaultPasswd() {
        return Results.success(RandomUtils.buildPasswd());
    }

    @PostMapping("/addLiveUser")
    @Operation(summary = "/助理更新绑定直播用户 ")
    @MLog
    public Result operateAddLiveUser(@Validated @RequestBody OperateAddLiveUserRequest request) {
        liveUserService.operateBindingLiveUser(request);
        return Results.success();
    }

    @PostMapping("/query/{id}")
    @Operation(summary = "/根据助理id查询所有主播信息")
    @MLog
    public Result operateAddLiveUser(@PathVariable("id") Long id) {
        return Results.success( liveUserService.queryLiveUserByOperate(id));
    }

    @PostMapping("/create/operate/liveUser")
    @Operation(summary = "/助理新增直播用户 新增主播时候必传所属助理id")
    // @SaCheckPermission("anchor:account:liveUser")
    @MLog
    public Result createOperateAddLiveUser(@Validated @RequestBody OpLiveUserAddRequest request) {
        liveUserService.createOperateUser(request);
        return Results.success();
    }

    @PostMapping("/obtain/liveUsers")
    @Operation(summary = "/获取所有未注销的主播列表")
    @MLog
    public Result obtainLiveUsers() {
        return Results.success( liveUserService.queryLiveUsers());
    }



}
