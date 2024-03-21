package org.sports.live.manage.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.service.dto.LoginDTO;
import org.sports.admin.manage.service.service.ILoginService;
import org.sports.admin.manage.service.vo.UserBackgroundLoginVO;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Live用户登录入口
 *

 * @since 2023-07-20
 */
@RestController
@RequestMapping("/common")
@Tag(name = "live用户登录入口")
public class LoginController {

    @Autowired
    private ILoginService loginService;


    @PostMapping("/login")
    @Operation(summary = "live用户登录")
    @MLog
    public Result<LoginDTO> login(@Validated @RequestBody UserBackgroundLoginVO userBackgroundLoginVO) {
        Result<LoginDTO> loginDTOResult = loginService.backgroundLogin(userBackgroundLoginVO, UserChannelEnum.LIVE.getFlag(), null);
        if(Result.SUCCESS_CODE == loginDTOResult.getCode()) {
            return loginDTOResult;
        }
        return Results.failure(loginDTOResult.getMsg());
    }
    @PostMapping("/anchor/login")
    @Operation(summary = "主播登录")
    @MLog
    public Result<LoginDTO> anchorLogin(@Validated @RequestBody UserBackgroundLoginVO userBackgroundLoginVO) {
        Result<LoginDTO> loginDTOResult = loginService.backgroundLogin(userBackgroundLoginVO, UserChannelEnum.LIVE.getFlag(), IdentityType.ANCHOR);
        if(Result.SUCCESS_CODE == loginDTOResult.getCode()) {
            return loginDTOResult;
        }
        return Results.failure(loginDTOResult.getMsg());
    }
    @PostMapping("/assistant/login")
    @Operation(summary = "助理登录")
    @MLog
    public Result<LoginDTO> assistantLogin(@Validated @RequestBody UserBackgroundLoginVO userBackgroundLoginVO) {
        Result<LoginDTO> loginDTOResult = loginService.backgroundLogin(userBackgroundLoginVO, UserChannelEnum.LIVE.getFlag(), IdentityType.OPERATOR);
        if(Result.SUCCESS_CODE == loginDTOResult.getCode()) {
            return loginDTOResult;
        }
        return Results.failure(loginDTOResult.getMsg());
    }

}
