package org.sports.admin.manage.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.service.dto.LoginDTO;
import org.sports.admin.manage.service.service.ILoginService;
import org.sports.admin.manage.service.vo.UserBackgroundLoginVO;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.web.Results;
import org.sports.springboot.starter.web.util.GoogleAuthenticator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * admin用户登录入口
 *

 * @since 2023-07-20
 */
@RestController
@RequestMapping("/common")
@Tag(name = "admin用户登录入口")
public class LoginController {

    @Resource
    private ILoginService loginService;
    @Resource
    private RedissonClient redissonClient;

    @PostMapping("/login")
    @Operation(summary = "admin用户登录")
    @MLog
    public Result<String> login(@Validated @RequestBody UserBackgroundLoginVO userBackgroundLoginVO) {
        /**
         * 私钥可以为空 不为空时候就更新缓存
         */
//        if(userBackgroundLoginVO.getCode() == null){
//            return Results.failure("验证码不能为空");
//        }
//        boolean isSaveCache = true;
//        if(Strings.isBlank(userBackgroundLoginVO.getSecret())){
//            Object secret = redissonClient.getMap(RedisCacheConstant.GOOGLE_CHECK_CODE_CACHE).get(userBackgroundLoginVO.getAccount());
//            if(Objects.isNull(secret)){
//                return Results.failure("私钥不能为空");
//            }
//            userBackgroundLoginVO.setSecret(String.valueOf(secret));
//            isSaveCache = false;
//        }
//        if(!GoogleAuthenticator.checkCode(userBackgroundLoginVO.getCode(), userBackgroundLoginVO.getSecret())){
//            return Results.failure("google验证失败");
//        }
//        if(isSaveCache){
//            redissonClient.getMap(RedisCacheConstant.GOOGLE_CHECK_CODE_CACHE).put(userBackgroundLoginVO.getAccount(), userBackgroundLoginVO.getSecret());
//        }
        Result<LoginDTO> loginDTOResult = loginService.backgroundLogin(userBackgroundLoginVO, UserChannelEnum.ADMIN.getFlag(), IdentityType.ANCHOR);
        if(Result.SUCCESS_CODE == loginDTOResult.getCode()) {
            return Results.success(loginDTOResult.getData().getToken());
        }
        return Results.failure(loginDTOResult.getMsg());
    }


    @GetMapping("/getQRBarcodeURL/{account}")
    @Operation(summary = "根据账号获取身份验证信息【二维码/私钥】")
    @MLog
    public Result<Map<String, String>> getQRBarcodeURL(@PathVariable("account") String account) {
        return Results.success(GoogleAuthenticator.getQRBarcodeURL(account));
    }

//    @PutMapping("/checkCode")
//    @Operation(summary = "google身份验证")
//    @MLog
//    public Result checkCode(@Validated @RequestBody GoogleCheckCodeRequest request){
//        if(GoogleAuthenticator.checkCode(request.getCode(), request.getSecret())){
//            return Results.success("验证成功");
//        }
//        return Results.failure("验证失败");
//    }

}
