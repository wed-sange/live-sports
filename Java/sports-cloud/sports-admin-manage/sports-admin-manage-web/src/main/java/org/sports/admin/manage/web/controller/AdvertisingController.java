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
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.req.AdvertisingPageRequest;
import org.sports.admin.manage.service.service.IAdvertisingService;
import org.sports.admin.manage.service.vo.AdvertisingAddVO;
import org.sports.admin.manage.service.vo.AdvertisingUpdateVO;
import org.sports.admin.manage.service.vo.AdvertisingVO;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Tag(name = "运营管理 - 广告位配置")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/advertising")
public class AdvertisingController {


    @Resource
    private IAdvertisingService advertisingService;

    @PostMapping("/page")
    @Operation(summary = "广告位分页查询")
    @SaCheckPermission("operate:advertising:query")
    @MLog
    public Result<PageResponse<AdvertisingVO>> getAdvertisingPage(@Validated @RequestBody AdvertisingPageRequest req) {
        return Results.success(advertisingService.getAdvertisingPage(req));
    }

    @PostMapping("/createAdvertising")
    @Operation(summary = "新增广告位")
    @SaCheckPermission("operate:advertising:update")
    @MLog
    public Result<Boolean> createAdvertising(@Validated @RequestBody AdvertisingAddVO advertising) {
        return Results.success(advertisingService.createAdvertising(advertising));
    }

    @PostMapping("/updateAdvertising")
    @Operation(summary = "修改广告位")
    @SaCheckPermission("operate:advertising:update")
    @MLog
    public Result<Boolean> updateAdvertising(@Validated @RequestBody AdvertisingUpdateVO advertising) {
        return Results.success(advertisingService.updateAdvertising(advertising));
    }

    @PostMapping("/deleteAdvertising/{id}")
    @Operation(summary = "删除广告位")
    @SaCheckPermission("operate:advertising:delete")
    @MLog
    @Parameter(name = "id",description = "广告ID")
    public Result<Boolean> deleteAdvertising(@PathVariable("id") Long id) {
        return Results.success(advertisingService.deleteAdvertisingById(id));
    }

    @GetMapping("/getAdvertisingInfo/{id}")
    @Operation(summary = "/查询广告位明细")
    @SaCheckPermission("operate:advertising:query")
    @MLog
    @Parameter(name = "id",description = "广告ID")
    public Result<AdvertisingVO> getMatchCountryList(@PathVariable("id") Long id) {
        return Results.success(advertisingService.getAdvertisingInfo(id));
    }
}
