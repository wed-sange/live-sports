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
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.service.service.IUserLiveHistoryService;
import org.sports.admin.manage.service.vo.LiveVo;
import org.sports.admin.manage.service.vo.UserLiveHistoryAddVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * 直播详情
 *

 */
@Tag(name = "用户-观看历史")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/user/live/history")
public class UserLiveHistoryController {
    @Resource
    private IUserLiveHistoryService userLiveHistoryService;


    @Operation(summary = "新增用户观看直播间记录")
    @GetMapping("/add/{liveId}")
    @MLog
    @SaCheckLogin
    public Result<Boolean> insert(@PathVariable("liveId") @Schema(description = "直播间ID") Long liveId) {
        UserLiveHistoryAddVO addVO = new UserLiveHistoryAddVO();
        addVO.setLiveId(liveId);
        addVO.setUserId(SotokenUtil.getUserId());
        return Results.success(userLiveHistoryService.insert(addVO));
    }

    @Operation(summary = "分页查询用户观看记录")
    @PostMapping("/page/list")
    @MLog
    @SaCheckLogin
    public Result<PageResponse<LiveVo>> getPage(@Validated @RequestBody PageRequest pageRequest) {
        return Results.success(userLiveHistoryService.getPage(pageRequest, SotokenUtil.getUserId()));
    }

}
