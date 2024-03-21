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

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.service.service.IVersionService;
import org.sports.admin.manage.service.vo.LatestVersionVO;
import org.sports.admin.manage.service.vo.VersionVO;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.sports.admin.manage.service.constant.CacheConstant.VERSION;


/**
 * @描述: 版本相关控制器
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/27
 * @创建时间: 15:37
 */
@Tag(name =  "app - 查询最新版本")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/version")
public class VersionController {


    @Resource
    private IVersionService versionService;

    @PostMapping("/getLatestVersion/{channel}")
    @Operation(summary = "/查询最新版本")
    @MLog
    @Cached(cacheType = CacheType.REMOTE, name = VERSION, key = "#channel", expire = 1, timeUnit = TimeUnit.MINUTES)
    public Result<LatestVersionVO> getLatestVersion(@PathVariable("channel") @Schema(description = "渠道：1：安卓 2：IOS") Long channel) {
        return Results.success(versionService.getLatestVersion(channel));
    }


}
