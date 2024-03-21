package org.sports.admin.manage.web.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.req.FeedbackIgnoreRequest;
import org.sports.admin.manage.dao.req.FeedbackListByAdminPageRequest;
import org.sports.admin.manage.dao.req.FeedbackReplyRequest;
import org.sports.admin.manage.service.service.IFeedbackService;
import org.sports.admin.manage.service.vo.FeedbackDetailByAdminVO;
import org.sports.admin.manage.service.vo.FeedbackListByAdminVO;
import org.sports.springboot.starter.convention.page.PageResponse;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(name = "意见反馈")
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("feedback")
@MLog
public class FeedbackController {
    private final IFeedbackService service;

    @PostMapping("admin/ignore")
    @Operation(summary = "管理员忽略")
    @SaCheckPermission("operate:feedback:ignore")
    @MLog
    public Result<Void> ignore(@Validated @RequestBody FeedbackIgnoreRequest request) {
        service.ignore(SotokenUtil.getCheckUserId(), SotokenUtil.getBGUserName(), request);
        return Results.success();
    }

    @PostMapping("admin/reply")
    @Operation(summary = "管理员回复")
    @SaCheckPermission("operate:feedback:reply")
    @MLog
    public Result<Void> reply(@Validated @RequestBody FeedbackReplyRequest request) {
        service.reply(SotokenUtil.getCheckUserId(), SotokenUtil.getBGUserName(), request);
        return Results.success();
    }

    @PostMapping("page/admin")
    @Operation(summary = "管理员分页查询")
    @SaCheckPermission("operate:feedback:query")
    @MLog
    public Result<PageResponse<FeedbackListByAdminVO>> listByAdmin(@Validated @RequestBody FeedbackListByAdminPageRequest request) {
        return Results.success(service.listByAdmin(request));
    }

    @GetMapping("detail/admin/{id}")
    @Operation(summary = "管理员查看反馈详情")
    @SaCheckPermission("operate:feedback:query")
    @MLog
    public Result<FeedbackDetailByAdminVO> detailByAdmin(@PathVariable("id") Long id) {
        return Results.success(service.detailByAdmin(id));
    }
}
