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
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.service.service.ILiveUserService;
import org.sports.admin.manage.service.vo.LiveUserOpenVO;
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
 * 直播详情
 *

 */
@Tag(name = "直播信息- 直播详情")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/live/detail")
public class CommonLiveDetailController {
    @Resource
    private ILiveUserService liveUserService;


    @Operation(summary = "查询直播主播信息")
    @GetMapping("/live/user/info/{anchorId}")
    @MLog
    public Result<LiveUserOpenVO> getTextLive(@PathVariable("anchorId") @Schema(description = "主播ID") Long anchorId) {
        return Results.success(liveUserService.getLiveUserOpenInfo(SotokenUtil.getUserId(), anchorId));
    }

}
