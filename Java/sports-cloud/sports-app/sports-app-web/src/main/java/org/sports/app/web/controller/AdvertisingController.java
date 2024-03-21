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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.service.service.IAdvertisingService;
import org.sports.admin.manage.service.vo.AdvertisingAppVO;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;


/**
 * 广告信息
 *

 */
@Tag(name = "广告信息")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/advertising")
public class AdvertisingController {
    @Resource
    private IAdvertisingService advertisingService;


    @Operation(summary = "查询banner列表")
    @GetMapping("/banner/list")
    @MLog
    public Result<List<AdvertisingAppVO>> getTextLive() {
        return Results.success(advertisingService.getBannerList());
    }

    @Operation(summary = "查询滚动文字列表")
    @GetMapping("/text/scroll/list")
    @MLog
    public Result<List<AdvertisingAppVO>> getTextScrollList() {
        return Results.success(advertisingService.getTextScrollList());
    }

    @Operation(summary = "查询个人中心广告")
    @GetMapping("/personal/center")
    @MLog
    public Result<AdvertisingAppVO> getPersonalCenter() {
        return Results.success(advertisingService.getPersonalCenter());
    }

    @Operation(summary = "查询直播间广告")
    @GetMapping("/live/show")
    @MLog
    public Result<AdvertisingAppVO> getLiveShow() {
        return Results.success(advertisingService.getLiveShow());
    }

}
