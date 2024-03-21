package org.sports.admin.manage.service.service;

import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.req.AppUserLoginRequest;
import org.sports.admin.manage.service.dto.LoginDTO;
import org.sports.admin.manage.service.vo.UserBackgroundLoginVO;
import org.sports.springboot.starter.convention.result.Result;

import java.awt.image.BufferedImage;

/**
 * 登录service入口
 */
public interface ILoginService {

    /**
     * 生成验证码图片
     * @param type 验证码类型
     * @param bucketKey 缓存key
     * @return
     */
    BufferedImage buildKaptchaImage(String type, String bucketKey);

    /**
     * 是否用户渠道
     * @param channel 渠道
     * @return
     */
    boolean ynUserChannel(String channel);

    /**
     * 后台用户登录【admin、live】
     *
     * @param userBackgroundLoginVO
     * @param channel               渠道
     * @param identityType
     * @return
     */
    Result<LoginDTO> backgroundLogin(UserBackgroundLoginVO userBackgroundLoginVO, String channel, IdentityType identityType);

    /**
     * app用户登录
     * @param appUserLoginRequest
     * @return
     */
    Result<String> appLogin(AppUserLoginRequest appUserLoginRequest);

}
