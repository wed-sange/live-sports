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
import org.sports.admin.manage.service.service.IVersionService;
import org.sports.admin.manage.service.vo.VersionVO;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @描述: 版本管理相关控制器
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/27
 * @创建时间: 15:37
 */
@Tag(name =  "后台管理 - 版本管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/version")
public class VersionController {


    @Resource
    private IVersionService versionService;

    @PostMapping("/versionList")
    @Operation(summary = "/版本列表查询")
    @SaCheckPermission("app:version:query")
    @MLog
    public Result<List<VersionVO>> getActivityPage(@Validated @RequestBody VersionVO vo) {
        return Results.success(versionService.getVersionList(vo.getChannel()));
    }
   
    @PostMapping("/createVersion")
    @Operation(summary = "/新增版本")
    @SaCheckPermission("app:version:add")
    @MLog
    public Result createVersion(@Validated @RequestBody VersionVO vo) {
        versionService.createVersion(vo);
        return Results.success();
    }

    @PostMapping("/deleteVersion/{versionId}")
    @Operation(summary = "/删除版本")
    @SaCheckPermission("app:version:delete")
    @MLog
    public Result deleteVersion(@PathVariable("versionId") Long versionId) {
        versionService.deleteVersion(versionId);
        return Results.success();
    }

}
