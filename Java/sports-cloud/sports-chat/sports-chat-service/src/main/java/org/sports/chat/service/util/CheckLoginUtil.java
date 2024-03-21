package org.sports.chat.service.util;

import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.sports.admin.manage.service.vo.AppUserVO;
import org.sports.chat.service.constant.CacheConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
public class CheckLoginUtil {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 根据token获取用户信息  无效时候返回null
     * @param token
     * @return
     */
    public AppUserVO getUserByToken(String token){
        try {
            Object tokenJson = redissonClient.getBucket(CacheConstant.SPORTSTOKEN_LOGIN_TOKEN_SESSION + token, StringCodec.INSTANCE).get();
            if(Objects.isNull(tokenJson)){
                return null;
            }
            JSONObject jsonObject1 = JSONObject.parseObject(String.valueOf(tokenJson));
            JSONObject jsonObject2 = JSONObject.parseObject(String.valueOf(jsonObject1.get("dataMap")));
            return JSONObject.parseObject(String.valueOf(jsonObject2.get("userObject")), AppUserVO.class);
        }catch (Exception e){
            log.error("token解析用户信息失败"+e.getMessage(), e);
            return null;
        }
    }

    /**
     * 校验token是否有效
     * @param token
     * @return
     */
    public boolean checkToken(String token) {
        try{
            Object tokenJson = redissonClient.getBucket(CacheConstant.SPORTSTOKEN_LOGIN_TOKEN + token, StringCodec.INSTANCE).get();
            return Objects.nonNull(tokenJson);
        }catch (Exception e){
            log.error("用户是否登录校验异常"+e.getMessage(), e);
            return false;
        }
    }

    /**
     * 用户是否登录校验[废弃]
     * @param userId 用户ID
     * @param channelFlag 渠道标记 参照UserChannelEnum
     * @param token
     * @param source 1IOS 2android 3H5 4小程序
     * @return
     */
//    public boolean checkLogin(String userId, String channelFlag, String source, String token) {
//        try{
//            /**
//             * 根据key获取token 并进行校验token是否匹配
//             */
//            String tokenKey = CacheConstant.SPORTSTOKEN_LOGIN_SESSION + userId + channelFlag;
//            if(Strings.isNotBlank(source)){
//                tokenKey += source;
//            }
//            Object tokenJson = redissonClient.getBucket(tokenKey, StringCodec.INSTANCE).get();
//            if(Objects.isNull(tokenJson)){
//                return false;
//            }
//            JSONObject jsonObject1 = JSONObject.parseObject(tokenJson.toString());
//            JSONArray jsonArray1 = JSONArray.parseArray(String.valueOf(jsonObject1.get("tokenSignList")));
//            JSONArray jsonArray2 = JSONArray.parseArray(String.valueOf(jsonArray1.get(1)));
//            JSONObject jsonObject2 = JSONObject.parseObject(String.valueOf(jsonArray2.get(0)));
//            if(token.equals(String.valueOf(jsonObject2.get("value")))){
//                return true;
//            }
//            return false;
//        }catch (Exception e){
//            log.error("用户是否登录校验异常"+e.getMessage(), e);
//            return false;
//        }
//    }



}
