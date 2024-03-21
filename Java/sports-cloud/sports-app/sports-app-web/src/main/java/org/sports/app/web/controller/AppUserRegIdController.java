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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.service.service.IAppUserRegIdService;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @描述: 极光注册ID和用户关联控制器
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/12/08
 * @创建时间: 17:37
 */
@Tag(name =  "app - 极光推送注册ID和用户绑定")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/jpush")
public class AppUserRegIdController {


    @Resource
    private IAppUserRegIdService appUserRegIdService;

    @GetMapping("/regId/{regId}")
    @Operation(summary = "/极光推送绑定用户")
    @MLog
    @SaCheckLogin
    public Result<Boolean> bindUser(@PathVariable("regId") String regId) {
        return Results.success(appUserRegIdService.bindRegId(SotokenUtil.getCheckUserId(), regId));
    }
    @GetMapping("/unbind/regId/{regId}")
    @Operation(summary = "/极光推送解绑设备")
    @MLog
    @SaCheckLogin
    public Result<Boolean> unbindUser(@PathVariable("regId") String regId) {
        return Results.success(appUserRegIdService.unbindRegId(SotokenUtil.getCheckUserId(), regId));
    }
}
