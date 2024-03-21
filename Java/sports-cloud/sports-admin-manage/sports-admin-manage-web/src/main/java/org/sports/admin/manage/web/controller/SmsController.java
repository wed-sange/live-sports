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
import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.service.service.ISmsRuleService;
import org.sports.admin.manage.service.service.ISmsService;
import org.sports.admin.manage.service.vo.SmsRuleVO;
import org.sports.admin.manage.service.vo.SmsVO;
import org.sports.admin.manage.service.vo.SmsVaVO;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


/**
 * @描述: 短信管理相关控制器
 */
@Tag(name =  "后台管理 - 短信管理")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/sms")
public class SmsController {

    @Resource
    private ISmsService smsService;

    @Resource
    private ISmsRuleService smsRuleService;

    @PostMapping("/smsAllList")
    @Operation(summary = "/短信配置查询")
    @SaCheckPermission("system:sms:query")
    @MLog
    public Result<SmsVO> smsAllList() {
        return Results.success(smsService.getSmsAllList(null));
    }
   
    @PostMapping("/createSms")
    @Operation(summary = "/保存短信配置")
    @SaCheckPermission("system:sms:add")
    @MLog
    public Result createSms(@Validated @RequestBody SmsVO vo) {
        smsService.createSms(vo);
        return Results.success();
    }

    @PostMapping("/smsVaList")
    @Operation(summary = "/短信验证方式")
    @SaCheckPermission("system:sms:query")
    @MLog
    public Result<List<SmsVaVO>> smsVaList() {
        return Results.success(smsService.getsmsVaList());
    }


    @PostMapping("/smsRuleInfo")
    @Operation(summary = "/短信功能配置查询")
    @SaCheckPermission("system:smsRule:query")
    @MLog
    public Result<SmsRuleVO> smsRuleInfo() {
        return Results.success(smsRuleService.getSmsRuleInfo());
    }

    @PostMapping("/createSmsRule")
    @Operation(summary = "/保存功能配置")
    @SaCheckPermission("system:smsRule:add")
    @MLog
    public Result createSmsRule(@Validated @RequestBody SmsRuleVO vo) {
        smsRuleService.createSmsRule(vo);
        return Results.success();
    }

    @PostMapping("/smsErrorOff")
    @Operation(summary = "/短信异常处理通知解除")
    @SaCheckPermission("system:smsRule:smsErrorOff")
    @MLog
    public Result smsErrorOff() {
        smsService.smsErrorOff();
        return Results.success();
    }


}
