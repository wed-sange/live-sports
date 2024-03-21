package org.sports.app.web.controller;

import cn.dev33.satoken.annotation.SaIgnore;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.app.service.req.EmailRequest;
import org.sports.app.service.service.EmailService;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "邮件发送")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("email")
@MLog
public class EmailController {
    @Autowired
    private EmailService emailService;

    @SaIgnore
    @PostMapping("send")
    @Operation(summary = "邮件发送")
    @MLog
    public Result<Void> send(@Validated @RequestBody EmailRequest emailRequest) {
        emailService.sendMessageToEmail(emailRequest.getEmail(),emailRequest.getLang());
        return Results.success();
    }

}
