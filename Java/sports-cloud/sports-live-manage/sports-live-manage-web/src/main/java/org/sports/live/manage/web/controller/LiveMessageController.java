package org.sports.live.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.util.Strings;
import org.sports.admin.manage.dao.entity.ChatMessageDO;
import org.sports.admin.manage.dao.entity.MessageSlidePageReqDto;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.req.*;
import org.sports.admin.manage.service.service.IChatMessageService;
import org.sports.admin.manage.service.service.IPushMessageService;
import org.sports.admin.manage.service.vo.LivingUserVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.Vo.SotokenBGUserVo;
import org.sports.springboot.starter.satoken.enums.LiveIdentityTypeEnum;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 直播消息列表入口
 *

 * @since 2023-07-31
 */
@RestController
@RequestMapping("/msg")
@Tag(name = "直播消息入口")
@SaCheckLogin
public class LiveMessageController {

    @Resource
    private IChatMessageService chatMessageService;

    @Resource
    private IPushMessageService pushMessageService;

    @PostMapping("/privateLivePage")
    @Operation(summary = "消息-私聊回复：主播列表查询")
    @MLog
    public Result<PageResponse<LiveChatMessageVO>> queryPrivateLivePage(@Validated @RequestBody PageRequest pageRequest) {
        SotokenBGUserVo userVo = SotokenUtil.getBGTokenUserInfo();
        if (Objects.equals(LiveIdentityTypeEnum.OPERATE.getValue(), userVo.getIdentityType())) {
            return Results.success(chatMessageService.queryPrivateLivePage(pageRequest));
        }
        Long liveId = userVo.getId();
        if (Objects.equals(LiveIdentityTypeEnum.HELPER.getValue(), userVo.getIdentityType())) {
            liveId = userVo.getBelongLive();
        }
        return Results.success(chatMessageService.queryLivePrivateById(liveId));
    }

    @PostMapping("/publicLivePage")
    @Operation(summary = "消息-直播间消息：在线主播列表查询，返回对象主ID为群聊房间ID")
    @MLog
    public Result<List<LivingUserVO>> queryPublicLivePage() {
        SotokenBGUserVo userVo = SotokenUtil.getBGTokenUserInfo();
        if (Objects.equals(LiveIdentityTypeEnum.OPERATE.getValue(), userVo.getIdentityType())) {
            return Results.success(chatMessageService.queryPublicLivePage(userVo.getId()));
        }
        Long liveId = userVo.getId();
        if (Objects.equals(LiveIdentityTypeEnum.HELPER.getValue(), userVo.getIdentityType())) {
            liveId = userVo.getBelongLive();
        }
        return Results.success(chatMessageService.queryLiveOnlineById(liveId));
    }

    @PostMapping("/privateUserByLivePage")
    @Operation(summary = "消息-私聊回复：根据主播查询用户列表(用户信息刷新)")
    @MLog
    public Result<PageResponse<LiveChatMessageVO>> queryPrivateUserByLivePage(@Validated @RequestBody UserByLivePageRequest pageRequest) {
        SotokenBGUserVo userVo = SotokenUtil.getBGTokenUserInfo();
        if (pageRequest.getAnchorId() == null) {
            if (IdentityType.ANCHOR.getCode().equals(userVo.getIdentityType())) {
                pageRequest.setAnchorId(userVo.getId());
            } else if (IdentityType.ASSISTANT.getCode().equals(userVo.getIdentityType())) {
                pageRequest.setAnchorId(userVo.getBelongLive());
            } else {
                return Results.failure("非主播/助手用户请传入主播ID信息");
            }
        }
        return Results.success(chatMessageService.queryPrivateUserByLivePage(userVo.getId(), pageRequest));
    }

    @PostMapping("/history")
    @Operation(summary = "历史消息查询")
    @MLog
    public Result<List<ChatMessageDO>> getHistoryMessage(@Validated @RequestBody MessageSlidePageReqDto messageSlidePageReqDto) {
        List<ChatMessageDO> result = chatMessageService.getHistoryMessage(messageSlidePageReqDto, Boolean.TRUE);
        if (Strings.isNotBlank(messageSlidePageReqDto.getUserId())) {
            pushMessageService.pushUnreadMessageTotalCount(messageSlidePageReqDto.getUserId(), messageSlidePageReqDto.getSearchId(), IdentityType.ANCHOR.getCode());
        }
        return Results.success(result);
    }

    @PostMapping("/historyNotSetRead")
    @Operation(summary = "历史消息查询(不设置已读)")
    @MLog
    public Result<List<ChatMessageDO>> getHistoryNotSetReadMessage(@Validated @RequestBody MessageSlidePageReqDto messageSlidePageReqDto) {
        List<ChatMessageDO> result = chatMessageService.getHistoryMessage(messageSlidePageReqDto,Boolean.FALSE);
        return Results.success(result);
    }

    @PostMapping("/readMessage")
    @Operation(summary = "消息批量已读")
    @MLog
    public Result readMessage(@Validated @RequestBody MessageReadRequest readRequest) {
        chatMessageService.anchorReadMessage(readRequest);
        if (Objects.nonNull(readRequest.getAnchorId()) && Objects.nonNull(readRequest.getUserId())) {
            pushMessageService.pushUnreadMessageTotalCount(String.valueOf(readRequest.getAnchorId()), String.valueOf(readRequest.getUserId()), IdentityType.ANCHOR.getCode());
        }
        return Results.success();
    }
    @PostMapping("/privateLatestMessage")
    @Operation(summary = "私聊最新消息列表")
    @MLog
    public Result<List<ChatMessageDO>> latestMessage(@Validated @RequestBody MessageQueryRequest messageQueryRequest) {
        List<ChatMessageDO> chatMessageDOS = chatMessageService.privateLatestMessage(messageQueryRequest);
        pushMessageService.pushUnreadMessageTotalCount(String.valueOf(messageQueryRequest.getAnchorId()), String.valueOf(messageQueryRequest.getUserId()), IdentityType.ANCHOR.getCode());
        return Results.success(chatMessageDOS);
    }

    @PostMapping("/privateEarliestUnread")
    @Operation(summary = "私聊最早未读消息")
    @MLog
    public Result<ChatMessageDO> privateEarliestUnread(@Validated @RequestBody MessageQueryRequest2 messageQueryRequest) {
        return Results.success( chatMessageService.privateEarliestUnread(messageQueryRequest));
    }

}
