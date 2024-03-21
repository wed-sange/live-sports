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
import org.sports.admin.manage.dao.req.ActivityPageRequest;
import org.sports.admin.manage.service.service.IActivityService;
import org.sports.admin.manage.service.vo.ActivityModifyVO;
import org.sports.admin.manage.service.vo.ActivityVO;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @描述: 活动列表查询相关控制器
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/5/26
 * @创建时间: 14:37
 */
@Tag(name =  "后台管理 - 活动管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/activity")
public class ActivityController {


    @Resource
    private IActivityService activityService;

    @PostMapping("/page")
    @Operation(summary = "/活动分页查询")
    @SaCheckPermission("operate:activity:query")
    @MLog
    public Result<PageResponse<ActivityVO>> getActivityPage(@Validated @RequestBody ActivityPageRequest req) {
        return Results.success(activityService.getActivityPage(req));
    }
   
    @PostMapping("/createActivity")
    @Operation(summary = "/新增活动")
    @SaCheckPermission("operate:activity:update")
    @MLog
    public Result createActivity(@Validated @RequestBody ActivityVO activity) {
        activityService.createActivity(activity);
        return Results.success();
    }
    @PostMapping("/updateActivity")
    @Operation(summary = "/修改活动")
    @SaCheckPermission("operate:activity:update")
    @MLog
    public Result updateActivity(@Validated @RequestBody ActivityModifyVO activity) {
        activityService.updateActivity(activity);
        return Results.success();
    }
    @PostMapping("/deleteActivity/{id}")
    @SaCheckPermission("operate:activity:delete")
    @Operation(summary = "/删除活动")
    @MLog
    public Result deleteActivity(@PathVariable("id") Long id) {
        activityService.deleteActivity(id);
        return Results.success();
    }
    @GetMapping("/getActivityInfo/{id}")
    @Operation(summary = "/查询活动明细")
    @SaCheckPermission("operate:activity:query")
    @MLog
    public Result<ActivityVO> getMatchCountryList(@PathVariable("id") Long id) {
        return Results.success(activityService.getActivityInfo(id));
    }
}
