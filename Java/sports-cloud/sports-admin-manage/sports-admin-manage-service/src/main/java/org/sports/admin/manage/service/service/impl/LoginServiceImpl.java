package org.sports.admin.manage.service.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.google.code.kaptcha.Producer;
import org.apache.logging.log4j.util.Strings;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.dao.entity.AppUserDO;
import org.sports.admin.manage.dao.entity.AppUserNoticeDO;
import org.sports.admin.manage.dao.entity.LiveUserDO;
import org.sports.admin.manage.dao.entity.SysUser;
import org.sports.admin.manage.dao.enums.AppUserNoticeTypeEnum;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.admin.manage.dao.req.AppUserLoginRequest;
import org.sports.admin.manage.service.constant.UserConstant;
import org.sports.admin.manage.service.dto.LoginDTO;
import org.sports.admin.manage.service.enums.UserLoginTypeEnum;
import org.sports.admin.manage.service.enums.UserLvEnum;
import org.sports.admin.manage.service.enums.YnEnum;
import org.sports.admin.manage.service.service.*;
import org.sports.admin.manage.service.vo.UserBackgroundLoginVO;
import org.sports.springboot.starter.base.exception.ErrorCode;
import org.sports.springboot.starter.base.exception.enums.GlobalErrorCodeConstants;
import org.sports.springboot.starter.base.exception.enums.ServiceErrorCodeRange;
import org.sports.springboot.starter.base.utils.RandomUtils;
import org.sports.springboot.starter.base.utils.ValidationUtils;
import org.sports.springboot.starter.convention.result.Result;
import org.sports.springboot.starter.satoken.Vo.SotokenAppUserVo;
import org.sports.springboot.starter.satoken.Vo.SotokenBGUserVo;
import org.sports.springboot.starter.satoken.constant.SotokenConstant;
import org.sports.springboot.starter.satoken.enums.KaptchaTypeEnum;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.sports.springboot.starter.web.Results;
import org.sports.springboot.starter.web.util.IpUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 登录service实现类
 */
@Service
public class LoginServiceImpl implements ILoginService {

    @Resource(name = "captchaProducer")
    private Producer captchaProducer;
    @Resource(name = "captchaProducerMath")
    private Producer captchaProducerMath;
    @Resource
    private RedissonClient redissonClient;
    @Resource
    private ISysMenuService sysMenuService;
    @Resource
    private ILiveUserService liveUserService;
    @Resource
    private IAppUserService appUserService;
    @Resource
    private ISysUserService sysUserService;
    @Resource
    private AppUserNoticeService appUserNoticeService;

    @Value("${spring.profiles.active}")
    private String env;

    @Override
    public BufferedImage buildKaptchaImage(String type, String bucketKey){
        BufferedImage bImage = null;
        String capStr;
        String code = null;
        if (KaptchaTypeEnum.MATH.getValue().equals(type)) {
            String capText = captchaProducerMath.createText();
            capStr = capText.substring(0, capText.lastIndexOf("@"));
            code = capText.substring(capText.lastIndexOf("@") + 1);
            bImage = captchaProducerMath.createImage(capStr);
        } else if (KaptchaTypeEnum.CHAR.getValue().equals(type)) {
            capStr = code = captchaProducer.createText();
            bImage = captchaProducer.createImage(capStr);
        }
        if(code != null){
            redissonClient.getBucket(SotokenConstant.TOKEN_KAPTCHA_PRE + bucketKey).set(code,60, TimeUnit.SECONDS);
        }
        return bImage;
    }

    @Override
    public boolean ynUserChannel(String channel){
        return UserChannelEnum.ADMIN.getValue().equals(channel) || UserChannelEnum.LIVE.getValue().equals(channel) || UserChannelEnum.APP.getValue().equals(channel);
    }

    @Override
    public Result<LoginDTO> backgroundLogin(UserBackgroundLoginVO userBackgroundLoginVO, String channel, IdentityType identityType){
        LoginDTO loginDTO = new LoginDTO();
        String loginId = "";
        SotokenBGUserVo bgUserVo = new SotokenBGUserVo();
        bgUserVo.setChannel(channel);
        if(UserChannelEnum.ADMIN.getFlag().equals(channel)){
            //后台用户登录
//            AdminUserDO adminUser = adminUserService.getByAccount(userBackgroundLoginVO.getAccount());
//            if(adminUser == null || !DigestUtil.md5Hex(userBackgroundLoginVO.getPasswd()).equals(adminUser.getPasswd())){
//                return Results.failure(ServiceErrorCodeRange.ACCOUNT_PASSWD_FAIL);
//            }
//            if(Objects.equals(YnEnum.ZERO.getValue(), adminUser.getYnValid())){
//                return Results.failure(ServiceErrorCodeRange.ACCOUNT_CANCELD);
//            }
//            loginId = adminUser.getId() + UserChannelEnum.ADMIN.getFlag();
//            BeanUtils.copyProperties(adminUser, bgUserVo);
            SysUser sysUser = sysUserService.selectUserByUserName(userBackgroundLoginVO.getAccount());
            if(sysUser == null || !DigestUtil.md5Hex(userBackgroundLoginVO.getPasswd()).equals(sysUser.getPassword())){
                return Results.failure(ServiceErrorCodeRange.ACCOUNT_PASSWD_FAIL);
            }
            if(!YnEnum.ONE.getValue().toString().equals(sysUser.getStatus())){
                return Results.failure(ServiceErrorCodeRange.ACCOUNT_DISABLED);
            }
            if(!YnEnum.ZERO.getValue().toString().equals(sysUser.getDelFlag())){
                return Results.failure(ServiceErrorCodeRange.ACCOUNT_CANCELD);
            }
            loginId = sysUser.getUserId() + UserChannelEnum.ADMIN.getFlag();
            bgUserVo.setId(sysUser.getUserId());
            bgUserVo.setAccount(sysUser.getUserName());
            bgUserVo.setName(sysUser.getNickName());
            bgUserVo.setHead(sysUser.getAvatar());
            bgUserVo.setRemarks(sysUser.getRemark());
            bgUserVo.setYnValid(Integer.parseInt(sysUser.getStatus()));
            bgUserVo.setYnCancel(Integer.parseInt(sysUser.getDelFlag()));
            //缓存系统用户接口权限
            sysMenuService.cacheUserPerms(sysUser.getUserId());
        }else if(UserChannelEnum.LIVE.getFlag().equals(channel)){
            //主播用户登录
            LiveUserDO liveUser = liveUserService.getByAccount(userBackgroundLoginVO.getAccount());
            if(Objects.nonNull(identityType)){
                if(IdentityType.ANCHOR.equals(identityType)&& !IdentityType.ANCHOR.getCode().equals(liveUser.getIdentityType())){
                    return Results.failure("此平台只允许主播登录！");
                }
                if(IdentityType.OPERATOR.equals(identityType)&& !IdentityType.OPERATOR.getCode().equals(liveUser.getIdentityType())){
                    return Results.failure("此平台只允许助理登录！");
                }
            }
            if(liveUser == null || !DigestUtil.md5Hex(userBackgroundLoginVO.getPasswd()).equals(liveUser.getPasswd())){
                return Results.failure(ServiceErrorCodeRange.ACCOUNT_PASSWD_FAIL);
            }
            ErrorCode errorCode = liveUserService.checkLiveUser(liveUser);
            if(!Objects.isNull(errorCode)){
                return Results.failure(errorCode);
            }
            loginId = liveUser.getId() + UserChannelEnum.LIVE.getFlag();
            bgUserVo.setName(liveUser.getNickName());
            BeanUtils.copyProperties(liveUser, bgUserVo);
            if(IdentityType.ANCHOR.getCode().equals(liveUser.getIdentityType())) {
                if (Objects.isNull(liveUser.getLastLoginTime())) { //是否第一次登录
                    loginDTO.setIsFirstLogin(Boolean.TRUE);
                } else {
                    loginDTO.setIsFirstLogin(Boolean.FALSE);
                }
            }
            liveUserService.update(new LambdaUpdateWrapper<LiveUserDO>()
                    .eq(LiveUserDO::getId, liveUser.getId())
                    .set(LiveUserDO::getLastLoginTime, LocalDateTime.now(ZoneOffset.UTC)));
        }
        bgUserVo.setIpAddress(IpUtils.getIpAddress());
        //用户创建token
        String token = SotokenUtil.createToken(loginId, channel, JSONObject.toJSONString(bgUserVo),null);
        loginDTO.setToken(token);
        return Results.success(loginDTO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result<String> appLogin(AppUserLoginRequest request){
        String bucketKey = SotokenConstant.TOKEN_KAPTCHA_PRE + UserChannelEnum.APP.getValue() + ":" + request.getAccount();
        if(Objects.nonNull(request.getAreaCode()) && Objects.equals(request.getType(),1)){
            //短信处理
            if(!request.getAreaCode().contains("+")){
                request.setAreaCode("+"+request.getAreaCode());
            }
            bucketKey = SotokenConstant.TOKEN_KAPTCHA_PRE + UserChannelEnum.APP.getValue() + ":" +request.getAreaCode() +request.getAccount();
        }

        String code = (String) redissonClient.getBucket(bucketKey).get();
        if(Objects.equals(env,"prod")){
            //生产环境才校验验证码
            if(code == null){
                return Results.failure(ServiceErrorCodeRange.VERIFICATION_EXPIRE);
            }
            if(!code.equals(request.getCode())){
                return Results.failure(ServiceErrorCodeRange.VERIFICATION_INCORRECT);
            }
        }
        AppUserDO appUser = null;
        String tel = null;
        String email = null;
        if(Objects.equals(UserLoginTypeEnum.TEL.getValue(), request.getType())){
            if(!ValidationUtils.isMobile(request.getAccount())){
                return Results.failure(ServiceErrorCodeRange.VERIFICATION_TEL_ERROE);
            }
            appUser = appUserService.getByTel(request.getAccount());
            tel = request.getAccount();
            if(Strings.isBlank(request.getAreaCode())){
                request.setAreaCode(UserConstant.TEL_AREA_CODE);
            }
        }else if(Objects.equals(UserLoginTypeEnum.EMAIL.getValue(), request.getType())){
            if(!ValidationUtils.isEmail(request.getAccount())){
                return Results.failure(ServiceErrorCodeRange.VERIFICATION_EMAIL_ERROR);
            }
            appUser = appUserService.getByEmail(request.getAccount());
            email = request.getAccount();
        }
        if(appUser == null){
            String key = "appLogin:" + request.getAccount();
            RLock rLock = redissonClient.getLock(key);
            if (!rLock.tryLock()){
                return Results.failure(GlobalErrorCodeConstants.TOO_MANY_REQUESTS);
            }
            try{
                LocalDateTime nowTime = LocalDateTime.now(ZoneOffset.UTC);
                appUser = new AppUserDO();
                appUser.setTel(tel);
                appUser.setAreaCode(request.getAreaCode());
                appUser.setEmail(email);
                appUser.setName(RandomUtils.buildNickName());
//                appUser.setPasswd(DigestUtil.md5Hex(request.getPasswd()));
                appUser.setLvNum(UserLvEnum.ONE.getLv());
                appUser.setLvName(UserLvEnum.ONE.getName());
                appUser.setGrowthValue(UserLvEnum.ONE.getScore());
                appUser.setGrowthValueNext(UserLvEnum.TWO.getScore());
                appUser.setRegisterAddr(request.getRegisterAddr());
                appUser.setYnCancel(YnEnum.ZERO.getValue());
                appUser.setYnForbidden(YnEnum.ZERO.getValue());
                appUser.setCreateTime(nowTime);
                appUserService.save(appUser);
                //保存信息-系统通知
                AppUserNoticeDO appUserNoticeDO = new AppUserNoticeDO();
                appUserNoticeDO.setType(AppUserNoticeTypeEnum.SYSTEM.getCode());
                appUserNoticeDO.setUserId(appUser.getId());
                appUserNoticeDO.setTitle("欢迎加入体育直播大家庭！让我们一起享受比赛的乐趣，领略竞技体育的魅力！祝您在我们的平台上度过愉快的时光！");
                appUserNoticeDO.setReadFlag(false);
                appUserNoticeDO.setCreateTime(nowTime);
                appUserNoticeService.save(appUserNoticeDO);
            }finally {
                if (rLock.isLocked()) {
                    rLock.unlock();
                }
            }
        }else if(Objects.equals(appUser.getYnCancel(), YnEnum.ONE.getValue())){
            return Results.failure(ServiceErrorCodeRange.ACCOUNT_CANCELD);
        }
//        else if(!DigestUtil.md5Hex(request.getPasswd()).equals(appUser.getPasswd())){
//                return Results.failure(ServiceErrorCodeRange.ACCOUNT_PASSWD_FAIL);
//        }
        //用户创建token
        String loginId = appUser.getId() + UserChannelEnum.APP.getFlag();
        SotokenAppUserVo appUserVo = new SotokenAppUserVo();
        BeanUtils.copyProperties(appUser, appUserVo);
        if(Strings.isNotBlank(request.getSource())){
            appUserVo.setSource(request.getSource());
        }
        appUserVo.setIpAddress(IpUtils.getIpAddress());
        appUserVo.setChannel(UserChannelEnum.APP.getFlag());
        String token = SotokenUtil.createToken(loginId, UserChannelEnum.APP.getFlag(), JSONObject.toJSONString(appUserVo), request.getSource());
        return Results.success(token);
    }

}
