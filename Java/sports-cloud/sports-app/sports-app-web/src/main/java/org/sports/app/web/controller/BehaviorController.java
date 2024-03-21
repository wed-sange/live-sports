package org.sports.app.web.controller;

import com.anji.captcha.model.common.ResponseModel;
import com.anji.captcha.model.vo.CaptchaVO;
import com.anji.captcha.service.CaptchaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
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

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 行为验证码
 *

 * @since 2023-07-20
 */
@RestController
@RequestMapping("/captcha")
@Tag(name = "行为验证码")
public class BehaviorController {

    @Resource
    private CaptchaService captchaService;

//    /**
//     * 登陆验证
//     * @param captchaVO
//     * @return
//     */
//    @Operation(summary = "登陆验证")
//    @PostMapping("/login")
//    public ResponseModel get(@RequestBody CaptchaVO captchaVO) {
//        //必传参数：captchaVO.captchaVerification
//        ResponseModel response = captchaService.verification(captchaVO);
//            //验证码校验失败，返回信息告诉前端
//            //repCode  0000  无异常，代表成功
//            //repCode  9999  服务器内部异常
//            //repCode  0011  参数不能为空
//            //repCode  6110  验证码已失效，请重新获取
//            //repCode  6111  验证失败
//            //repCode  6112  获取验证码失败,请联系管理员
//            //repCode  6113  底图未初始化成功，请检查路径
//            //repCode  6201  get接口请求次数超限，请稍后再试!
//            //repCode  6206  无效请求，请重新获取验证码
//            //repCode  6202  接口验证失败数过多，请稍后再试
//            //repCode  6204  check接口请求次数超限，请稍后再试!
//        return response;
//    }

    /**
     * 获取验证码
     * @param captchaVO
     * @return
     */
    @Operation(summary = "获取文字验证码")
    @PostMapping("/get/image") // 获取图片
    public ResponseModel get(@RequestBody CaptchaVO data, HttpServletRequest request) {
        //滑动拼图 blockPuzzle 2）文字点选 clickWord
        assert request.getRemoteHost()!=null;
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.get(data);
    }

    /**
     * 校验验证码
     * @param captchaVO
     * @return
     */
    @Operation(summary = "校验验证码")
    @PostMapping("/check/captcha") // 进行校验
    public ResponseModel check(@RequestBody CaptchaVO data, HttpServletRequest request) {
        data.setBrowserInfo(getRemoteId(request));
        return captchaService.check(data);
    }

    /**
     * 二次校验
     * @param captchaVO
     * @return
     */
    @Operation(summary = "二次校验")
    @PostMapping("/verify/captcha") // 二次校验
    public ResponseModel verify(@RequestBody CaptchaVO data, HttpServletRequest request) {
        return captchaService.verification(data);
    }

    public static final String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StringUtils.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StringUtils.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StringUtils.trim(ipList[0]);
        }
        return null;
    }


}
