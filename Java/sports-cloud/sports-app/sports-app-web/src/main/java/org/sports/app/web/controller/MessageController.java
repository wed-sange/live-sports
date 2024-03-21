package org.sports.app.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.ChatMessageDO;
import org.sports.admin.manage.dao.entity.MessageSlidePageReqDto;
import org.sports.admin.manage.dao.enums.ChatType;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.req.AppUserMsgPageRequest;
import org.sports.admin.manage.dao.req.CancelUnreadMsgRequest;
import org.sports.admin.manage.dao.req.ChatMessageVO;
import org.sports.admin.manage.service.service.IChatMessageService;
import org.sports.admin.manage.service.service.IFeedbackService;
import org.sports.admin.manage.service.service.IPushMessageService;
import org.sports.app.service.service.IMessageService;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户消息列表入口
 *

 * @since 2023-07-31
 */
@RestController
@RequestMapping("/msg")
@Tag(name = "用户消息列表入口")
public class MessageController {

    @Autowired
    private IMessageService messageService;
    @Autowired
    private IFeedbackService feedbackService;
    @Autowired
    private IChatMessageService chatMessageService;
    @Resource
    private IPushMessageService pushMessageService;

    @PostMapping("/history")
    @Operation(summary = "历史消息查询")
    @MLog
    public Result<List<ChatMessageDO>> getHistoryMessage(@Validated @RequestBody MessageSlidePageReqDto messageSlidePageReqDto){
        List<ChatMessageDO> result = chatMessageService.getHistoryMessage(messageSlidePageReqDto, Boolean.TRUE);
        if(Strings.isNotBlank(messageSlidePageReqDto.getChatType()) &&ChatType.CHAT_TYPE_PRIVATE.getNumber() == Integer.valueOf(messageSlidePageReqDto.getChatType())){
            pushUserUnreadMsgCount(SotokenUtil.getCheckUserId());
        }
        return Results.success(result);
    }
    @PostMapping("/msgPage")
    @Operation(summary = "消息列表页面：消息分页查询，返回对象fromId=0代表这条数据为反馈通知")
    @MLog
    @SaCheckLogin
    public Result<PageResponse<ChatMessageVO>> queryMsgByPage(@Validated @RequestBody AppUserMsgPageRequest pageRequest){
        Long userId = SotokenUtil.getCheckUserId();
        return Results.success(messageService.queryMsgByPage(pageRequest, userId));
    }

    @PutMapping("/delRecordsByAnchorId")
    @Operation(summary = "根据主播ID，删除聊天记录[假删除]")
    @MLog
    @SaCheckLogin
    public Result delRecords(@Validated @RequestBody CancelUnreadMsgRequest msgRequest){
        Long userId = SotokenUtil.getCheckUserId();
        messageService.delRecordsByAnchorId(msgRequest.getAnchorId(), userId);
        pushUserUnreadMsgCount(userId);
        return Results.success();
    }

    @PutMapping("/cancelUnread")
    @Operation(summary = "根据主播ID，消除对应主播红点提示")
    @MLog
    @SaCheckLogin
    public Result cancelUnread(@Validated @RequestBody CancelUnreadMsgRequest msgRequest){
        Long userId = SotokenUtil.getCheckUserId();
        messageService.cancelUnread(msgRequest.getAnchorId(), userId);
        pushUserUnreadMsgCount(userId);
        return Results.success();
    }

    @PutMapping("/cancelAllUnread")
    @Operation(summary = "消除所有红点提示（包含当前用户对应的主播和反馈通知）")
    @MLog
    @SaCheckLogin
    public Result cancelAllUnread(){
        Long userId = SotokenUtil.getCheckUserId();
        messageService.cancelAllUnread(userId);
        pushUserUnreadMsgCount(userId);
        return Results.success();
    }

    /**
     * 推送用户未读消息总数
     * @param userId
     */
    private void pushUserUnreadMsgCount(Long userId){
        pushMessageService.pushUnreadMessageTotalCount(null, String.valueOf(userId), IdentityType.APP_USER.getCode());
    }


}
