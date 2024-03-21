package org.sports.app.web.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.dao.entity.AppNoticeConfigDO;
import org.sports.admin.manage.service.service.AppNoticeConfigService;
import org.sports.admin.manage.service.vo.AppNoticeConfigVO;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * APP用户推送设置入口

 */
@RestController
@RequestMapping("/notice/config")
@Tag(name = "推送设置入口-new")
@SaCheckLogin
public class AppNoticeConfigController {

    @Resource
    private AppNoticeConfigService appNoticeConfigService;

    @GetMapping("/getInfo")
    @Operation(summary = "获取当前APP用户推送设置信息")
    @MLog
    public Result<AppNoticeConfigVO> getInfo(){
        Long userId = SotokenUtil.getCheckUserId();
        AppNoticeConfigVO appNoticeConfigVO = appNoticeConfigService.getConfigInfo(userId);
        return Results.success(appNoticeConfigVO);
    }

    @PutMapping("/switch/{name}/{status}")
    @Operation(summary = "推送通知设置切换[name参数 liveOpen:切换主播开播通知 followMatch:切换关注比赛通知; status参数 1开 0关]")
    @MLog
    public Result switchConfig(@PathVariable("name") String name, @PathVariable("status") Integer status) {
        if(!"liveOpen".equals(name) && !"followMatch".equals(name)){
            return Results.failure("切换类型有误");
        }
        if(status < 0 || status > 1){
            return Results.failure("切换状态有误");
        }
        Long userId = SotokenUtil.getCheckUserId();
        appNoticeConfigService.update(new LambdaUpdateWrapper<AppNoticeConfigDO>()
                .eq(AppNoticeConfigDO::getUserId, userId)
                .set("liveOpen".equals(name), AppNoticeConfigDO::getYnLiveOpen, status)
                .set("followMatch".equals(name), AppNoticeConfigDO::getYnFollowMatch, status)
                .set(AppNoticeConfigDO::getUpdateTime, LocalDateTime.now(ZoneOffset.UTC)));
        return Results.success();
    }

}
