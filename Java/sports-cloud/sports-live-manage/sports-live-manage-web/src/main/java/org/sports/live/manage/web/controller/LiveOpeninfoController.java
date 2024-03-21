package org.sports.live.manage.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.dao.entity.LiveOpeninfoDO;
import org.sports.admin.manage.service.converter.LiveOpeninfoConvert;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.ILiveOpeninfoService;
import org.sports.admin.manage.service.service.ILiveUserService;
import org.sports.admin.manage.service.vo.LiveOpeninfoVO;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.distributedid.SnowflakeIdUtil;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;

/**
 * 直播开播信息 前端控制器
 *

 * @since 2023-07-28
 */
@RestController
@RequestMapping("/live/openinfo")
@Tag(name = "直播开播信息管理")
public class LiveOpeninfoController {

    @Autowired
    private ILiveOpeninfoService liveOpeninfoService;

    @Resource
    private ILiveUserService liveUserService;

    @PutMapping("/insertOrUpdate")
    @Operation(summary = "新增或者修改开播信息")
    @MLog
    public Result insertOrUpdate(@Validated @RequestBody LiveOpeninfoVO liveOpeninfoVO) {
        Long userId = SotokenUtil.getCheckUserId();
        LiveOpeninfoDO liveOpeninfoDO = LiveOpeninfoConvert.INSTANCE.convertToDo(liveOpeninfoVO);
        if (Objects.isNull(liveOpeninfoVO.getId())) {
            liveOpeninfoDO.setId(SnowflakeIdUtil.nextId());
            liveOpeninfoDO.setUsed(YnEnum.ZERO.getValue());
            long count = liveOpeninfoService.countByAnchorId(userId);
            if (count >= 10) {
                return Results.failure("主播最多可配置10条开播信息");
            }
            if(count == 0){
                liveOpeninfoDO.setUsed(YnEnum.ONE.getValue());
            }
        }
        if(Objects.isNull(liveOpeninfoVO.getAnchorId())){
            liveOpeninfoDO.setAnchorId(userId);
        }else {
            liveOpeninfoDO.setAnchorId(liveOpeninfoVO.getAnchorId());
        }
        liveOpeninfoDO.setUpdateTime(LocalDateTime.now(ZoneOffset.UTC));
        liveOpeninfoService.saveOrUpdate(liveOpeninfoDO);
        liveUserService.setOpenInfoFlag(userId);
        if(Objects.nonNull(liveOpeninfoDO.getUsed())&&liveOpeninfoDO.getUsed().equals(YnEnum.ONE.getValue())){
            liveOpeninfoService.setUsedOpenInfo(liveOpeninfoDO.getId(), liveOpeninfoDO.getAnchorId());
        }
        return Results.success();
    }

    @GetMapping("/getOpenInfo/{id}")
    @Operation(summary = "开播信息明细")
    @MLog
    public Result<LiveOpeninfoVO> getOpenInfoById(@PathVariable("id") Long id) {
        Long userId = SotokenUtil.getCheckUserId();
        return Results.success(liveOpeninfoService.getOpenInfoById(id, userId));
    }

    @DeleteMapping("/delOpenInfo/{id}")
    @Operation(summary = "删除开播信息配置")
    @MLog
    public Result<LiveOpeninfoVO> delOpenInfo(@PathVariable("id") Long id) {
        Long userId = SotokenUtil.getCheckUserId();
        liveOpeninfoService.delOpenInfoById(id, userId);
        return Results.success();
    }

    @GetMapping("/getOpenInfoList")
    @Operation(summary = "查询主播开播信息列表")
    @MLog
    public Result<List<LiveOpeninfoVO>> getOpenInfoById() {
        Long anchorId = SotokenUtil.getCheckUserId();
        return Results.success(liveOpeninfoService.getOpenInfoListByAnchorId(anchorId));
    }

    @GetMapping("/getUsedOpenInfo/{anchorId}")
    @Operation(summary = "主播正在使用的开播信息")
    @MLog
    public Result<LiveOpeninfoVO> getUsedOpenInfo(@PathVariable("anchorId") Long anchorId) {
        return Results.success(liveOpeninfoService.getUsedOpenInfo(anchorId));
    }


}
