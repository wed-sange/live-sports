package org.sports.app.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.service.service.ISmsService;
import org.sports.app.service.req.SmsRequest;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @描述: 短信管理相关控制器
 */
@Tag(name = "短信发送")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("sms")
@MLog
public class SmsController {
    @Resource
    private ISmsService smsService;

    @SaIgnore
    @PostMapping("send")
    @Operation(summary = "短信发送")
    @MLog
    public Result<Void> send(@Validated @RequestBody SmsRequest smsRequest) {
        if(!smsRequest.getAreaCode().contains("+")){
            smsRequest.setAreaCode("+"+smsRequest.getAreaCode());
        }
        smsService.sendSmsMsg(smsRequest.getAreaCode()+smsRequest.getAccount());
        return Results.success();
    }

}
