package org.sports.admin.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.dao.req.ChatMessagePageRequest;
import org.sports.admin.manage.service.service.IChatMessageService;
import org.sports.admin.manage.service.vo.ChatMessageVO2;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 聊天信息管理入口
 *

 * @since 2023-08-02
 */
@RestController
@RequestMapping("/chat/message")
@Tag(name =  "聊天信息管理")
public class ChatMessageController {

    @Autowired
    private IChatMessageService chatMessageService;

    @PostMapping("/page")
    @Operation(summary = "聊天私聊信息分页查询")
    @SaCheckPermission("chat:message:record")
    @MLog
    public Result<PageResponse<ChatMessageVO2>> getPrivateMsgPage(@Validated @RequestBody ChatMessagePageRequest req) {
        return Results.success(chatMessageService.getPrivateMsgPage(req));
    }

}
