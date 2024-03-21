package org.sports.app.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.dao.entity.AppUserNoticeDO;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.service.service.AppUserNoticeService;
import org.sports.admin.manage.service.service.IPushMessageService;
import org.sports.admin.manage.service.vo.AppUserNoticeVO;
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
 * APP用户-系统通知入口

 */
@RestController
@RequestMapping("/user/notice")
@Tag(name = "系统通知入口-new")
@SaCheckLogin
public class AppUserNoticeController {

    @Resource
    private AppUserNoticeService appUserNoticeService;
    @Resource
    private IPushMessageService pushMessageService;

    @PostMapping("/getInfos")
    @Operation(summary = "获取当前APP用户系统通知信息")
    @MLog
    public Result<PageResponse<AppUserNoticeVO>> getInfos(@Validated @RequestBody PageRequest pageRequest){
        Long userId = SotokenUtil.getCheckUserId();
        appUserNoticeService.readAllNoticeInfo(userId);
        pushMessageService.pushUnreadMessageTotalCount(null, userId.toString(), IdentityType.APP_USER.getCode());
        return Results.success(appUserNoticeService.getNoticeInfosByUserId(pageRequest, userId));
    }

    @PutMapping("/read/{id}")
    @Operation(summary = "读取系统通知")
    @MLog
    public Result switchConfig(@PathVariable("id") Long id) {
        Long userId = SotokenUtil.getCheckUserId();
        AppUserNoticeDO appUserNoticeDO = appUserNoticeService.getById(id);
        if(appUserNoticeDO == null || !userId.equals(appUserNoticeDO.getUserId())){
            return Results.failure("读取通知无效");
        }
        boolean readFlag = appUserNoticeService.update(new LambdaUpdateWrapper<AppUserNoticeDO>()
                .eq(AppUserNoticeDO::getId, id)
                .set(AppUserNoticeDO::getReadFlag, true));
        if(readFlag){
            pushMessageService.pushUnreadMessageTotalCount(null, userId.toString(), IdentityType.APP_USER.getCode());
            return Results.success();
        }
        return Results.failure("读取通知失败");
    }

}
