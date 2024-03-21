package org.sports.springboot.starter.satoken.util;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson2.JSONObject;
import org.apache.logging.log4j.util.Strings;
import org.sports.springboot.starter.satoken.Vo.SotokenAppUserVo;
import org.sports.springboot.starter.satoken.Vo.SotokenBGUserVo;
import org.sports.springboot.starter.satoken.constant.SotokenConstant;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;

import java.util.List;

/**
 * Sotoken工具

 * @since 2023-07-21
 */
public class SotokenUtil {

    /**======================公用部分===============================/
    /**
     * 获取当前用户ID 未登录返回null
     */
    public static Long getUserId() {
        try {
            String loginIdStr = StpUtil.getLoginIdAsString();
            if(loginIdStr.length() > 19){
                return Long.parseLong(loginIdStr.substring(0, 19));
            }
            return Long.parseLong(loginIdStr.substring(0, loginIdStr.length() -1));
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取当前用户ID 未登录抛出异常
     */
    public static Long getCheckUserId() {
        try {
            String loginIdStr = StpUtil.getLoginIdAsString();
            if(loginIdStr.length() > 19){
                return Long.parseLong(loginIdStr.substring(0, 19));
            }
            return Long.parseLong(loginIdStr.substring(0, loginIdStr.length() -1));
        }catch (Exception e){
            throw new NotLoginException("User not logged in",NotLoginException.INVALID_TOKEN, NotLoginException.INVALID_TOKEN_MESSAGE);
        }
    }

    /**
     * 当前用户退出
     * @return
     */
    public static void logout(){
        try{
            StpUtil.checkLogin();
            StpUtil.logout();
        }catch (Exception e){ }
    }

    /**
     * 指定用户退出
     * @param loginId 用户ID
     * @param channelEnum 登陆渠道
     * @param source APP渠道专用 登陆来源
     */
    public static void logout(Object loginId, UserChannelEnum channelEnum, String source){
        if(Strings.isBlank(source)){
            StpUtil.logout(loginId + channelEnum.getFlag());
        }else{
            StpUtil.logout(loginId + channelEnum.getFlag() + source);
        }
    }

    /**
     * 指定批量用户退出
     * @param objectList 用户ID集合
     * @param channelEnum 登陆渠道
     * @param source APP渠道专用 登陆来源
     */
    public static void logoutBatch(List<Long> objectList, UserChannelEnum channelEnum, String source){
        for (Long loginId : objectList) {
            if(Strings.isBlank(source)){
                StpUtil.logout(loginId + channelEnum.getFlag());
            }else{
                StpUtil.logout(loginId + channelEnum.getFlag() + source);
            }
        }
    }

    /**
     * 当前用户注销
     * @return
     */
    public static void kickout(){
        StpUtil.kickout(StpUtil.getLoginIdAsString());
    }

    /**
     * 指定用户强制注销
     * @param loginId 用户ID
     * @param channelEnum 登陆渠道
     * @param source APP渠道专用 登陆来源
     */
    public static void kickout(Object loginId, UserChannelEnum channelEnum, String source){
        if(Strings.isBlank(source)){
            StpUtil.kickout(loginId + channelEnum.getFlag());
        }else{
            StpUtil.kickout(loginId + channelEnum.getFlag() + source);
        }
    }

    /**
     * 指定批量用户强制注销
     * @param objectList 用户ID集合
     * @param channelEnum 登陆渠道
     * @param source APP渠道专用 登陆来源
     */
    public static void kickoutBatch(List<Long> objectList, UserChannelEnum channelEnum, String source){
        for (Long loginId : objectList) {
            if(Strings.isBlank(source)){
                StpUtil.kickout(loginId + channelEnum.getFlag());
            }else{
                StpUtil.kickout(loginId + channelEnum.getFlag() + source);
            }
        }
    }

    /**
     * 用户登录 创建token
     * @param loginId 用户ID
     * @param loginType 登陆方式
     * @param value 用户香港信息
     * @param source APP渠道专用 登陆来源
     * @return
     */
    public static String createToken(Object loginId, String loginType, String value, String source){
        if(Strings.isBlank(source)){
            StpUtil.login(loginId);
        }else{
            StpUtil.login(loginId + source);
        }
        StpUtil.getTokenSession().setLoginType(loginType);
        if(value != null){
            StpUtil.getTokenSession().set(SotokenConstant.TOKEN_USER_KEY, value);
        }
        return StpUtil.getTokenValue();
    }

    /**======================admin/live专用部分===============================/
    /**
     * 获取当前后台用户信息[admin/live] 未登录 返回null
     * @return
     */
    public static SotokenBGUserVo getBGTokenUserInfo(){
        try{
            StpUtil.checkLogin();
            return JSONObject.parseObject(String.valueOf(StpUtil.getTokenSession().get(SotokenConstant.TOKEN_USER_KEY)), SotokenBGUserVo.class);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取当前后台用户名[admin/live] 未登录 返回null
     * @return
     */
    public static String getBGUserName(){
        try{
            StpUtil.checkLogin();
            return JSONObject.parseObject(String.valueOf(StpUtil.getTokenSession().get(SotokenConstant.TOKEN_USER_KEY)), SotokenBGUserVo.class).getName();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取当前后台用户账号[admin/live] 未登录 返回null
     * @return
     */
    public static String getBGUserAccount(){
        try{
            StpUtil.checkLogin();
            return JSONObject.parseObject(String.valueOf(StpUtil.getTokenSession().get(SotokenConstant.TOKEN_USER_KEY)), SotokenBGUserVo.class).getAccount();
        }catch (Exception e){
            return null;
        }
    }

    /**======================app专用部分===============================/
    /**
     * 获取当前APP用户信息 未登录 返回null
     * @return
     */
    public static SotokenAppUserVo getAppTokenUserInfo(){
        try{
            StpUtil.checkLogin();
            return JSONObject.parseObject(String.valueOf(StpUtil.getTokenSession().get(SotokenConstant.TOKEN_USER_KEY)), SotokenAppUserVo.class);
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取当前APP用户名 未登录 返回null
     * @return
     */
    public static String getAppUserName(){
        try{
            StpUtil.checkLogin();
            return JSONObject.parseObject(String.valueOf(StpUtil.getTokenSession().get(SotokenConstant.TOKEN_USER_KEY)), SotokenAppUserVo.class).getName();
        }catch (Exception e){
            return null;
        }
    }

    /**
     * 获取当前APP用户Tel 未登录 返回null
     * @return
     */
    public static String getAppUserTel(){
        try{
            StpUtil.checkLogin();
            return JSONObject.parseObject(String.valueOf(StpUtil.getTokenSession().get(SotokenConstant.TOKEN_USER_KEY)), SotokenAppUserVo.class).getTel();
        }catch (Exception e){
            return null;
        }
    }

}
