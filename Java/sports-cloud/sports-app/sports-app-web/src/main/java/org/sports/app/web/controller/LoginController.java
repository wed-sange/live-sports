package org.sports.app.web.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.service.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.sports.admin.manage.dao.req.AppUserLoginRequest;
import org.sports.admin.manage.service.enums.UserLoginTypeEnum;
import org.sports.admin.manage.service.service.ILoginService;
import org.sports.springboot.starter.base.utils.ValidationUtils;
import org.sports.springboot.starter.convention.exception.ServiceException;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.log.annotation.MLog;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.web.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

/**
 * APP用户登录入口
 *

 * @since 2023-07-20
 */
@RestController
@RequestMapping("/common")
@Tag(name = "APP用户登录入口")
public class LoginController {

    @Autowired
    private ILoginService loginService;

    @Resource
    private CaptchaService captchaService;

    @GetMapping("/getKaptchaImage")
    @Operation(summary = "获取验证码")
    @MLog
    public void getKaptchaImage(@RequestParam(name = "类型：math算法,char字符串") String type, @RequestParam(name="账号") String account, HttpServletResponse response) {
        ServletOutputStream out = null;
        try {
            response.setDateHeader("Expires", 0);
            response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
            response.addHeader("Cache-Control", "post-check=0, pre-check=0");
            response.setHeader("Pragma", "no-cache");
            response.setContentType("image/jpeg");
            if(!ValidationUtils.isMobile(account) && !ValidationUtils.isEmail(account)){
                throw new ServiceException("账号格式有误");
            }
            account = UserChannelEnum.APP.getValue() + ":" + account;
            BufferedImage bi = loginService.buildKaptchaImage(type, account);
            out = response.getOutputStream();
            ImageIO.write(bi, "jpg", out);
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @PostMapping("/login")
    @Operation(summary = "APP用户登录注册【区分登陆来源】")
    @MLog
    public Result<String> appLogin(@Validated @RequestBody AppUserLoginRequest appUserLoginRequest) {
        if(!Objects.equals(UserLoginTypeEnum.TEL.getValue(), appUserLoginRequest.getType()) && !Objects.equals(UserLoginTypeEnum.EMAIL.getValue(), appUserLoginRequest.getType())){
            return Results.failure("登录方式有误");
        }
        if (Objects.nonNull(appUserLoginRequest.getCaptchaVO())){
            ResponseModel response = captchaService.verification(appUserLoginRequest.getCaptchaVO());
            if (!"0000".equals(response.getRepCode())){
                return Results.failure(response.getRepMsg());
            }
        }
        return loginService.appLogin(appUserLoginRequest);
    }

}
