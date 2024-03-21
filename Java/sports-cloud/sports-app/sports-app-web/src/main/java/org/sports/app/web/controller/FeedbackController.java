package org.sports.app.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.sports.admin.manage.dao.req.FeedbackSubmitRequest;
import org.sports.admin.manage.service.service.IFeedbackService;
import org.sports.admin.manage.service.vo.FeedbackDetailByUserVO;
import org.sports.admin.manage.service.vo.FeedbackListByUserVO;
import org.sports.springboot.starter.convention.page.PageRequest;
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
    private final IFeedbackService feedbackService;

    @PostMapping("submit")
    @Operation(summary = "用户提交反馈")
    @MLog
    public Result<Void> submit(@Validated @RequestBody FeedbackSubmitRequest request) {
        feedbackService.submit(SotokenUtil.getCheckUserId(), SotokenUtil.getAppUserName(), request);
        return Results.success();
    }

    @GetMapping("detail/user/{id}")
    @Operation(summary = "用户查看反馈详情")
    @MLog
    public Result<FeedbackDetailByUserVO> detailByUser(@PathVariable("id") Long id) {
        return Results.success(feedbackService.detailByUser(SotokenUtil.getCheckUserId(),id));
    }

    @PostMapping("page/user")
    @Operation(summary = "用户查询反馈列表")
    @MLog
    public Result<PageResponse<FeedbackListByUserVO>> pageByUser(@Validated @RequestBody PageRequest request) {
        Long userId = SotokenUtil.getCheckUserId();
        return Results.success(feedbackService.queryFeedbackByPage(request, userId));
    }

}
