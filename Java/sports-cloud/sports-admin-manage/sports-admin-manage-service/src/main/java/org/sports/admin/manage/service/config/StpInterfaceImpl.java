package org.sports.admin.manage.service.config;

import cn.dev33.satoken.stp.StpInterface;
import org.redisson.api.RedissonClient;
import org.sports.admin.manage.service.constant.RedisCacheConstant;
import org.sports.springboot.starter.satoken.Vo.SotokenBGUserVo;
import org.sports.springboot.starter.satoken.enums.LiveIdentityTypeEnum;
import org.sports.springboot.starter.satoken.enums.PlatRoleEnum;
import org.sports.springboot.starter.satoken.enums.UserChannelEnum;
import org.sports.springboot.starter.satoken.enums.UserRoleEnum;
import org.sports.springboot.starter.satoken.util.SotokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 自定义权限加载接口实现类

 */
@Component
public class StpInterfaceImpl implements StpInterface {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 返回一个账号所拥有的权限码集合
     * 对应注解 StpUtil.checkPermission("user.add");
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        String loginIdStr = loginId.toString();
        String flag = loginIdStr.substring(loginIdStr.length() -1);
        List<String> list = new ArrayList<>();
        //目前只对系统用户进行处理
        if(UserChannelEnum.ADMIN.getFlag().equals(flag)){
            Object permissions = redissonClient.getMap(RedisCacheConstant.USER_PERMISSIONS_CACHE).get(loginIdStr);
            if(!Objects.isNull(permissions)){
                Collections.addAll(list, String.valueOf(permissions).split(","));
            }
        }
        return list;
    }

    /**
     * 返回一个账号所拥有的角色标识集合 (权限与角色可分开校验)
     * 对应注释 StpUtil.checkRole("super-admin");
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {
        // 本 list 仅做模拟，实际项目中要根据具体业务逻辑来查询权限
        String loginIdStr = loginId.toString();
        String lastStr = loginIdStr.substring(loginIdStr.length() -1);
        List<String> list = new ArrayList<String>();
        if(UserChannelEnum.ADMIN.getFlag().equals(lastStr)){
            list.add(PlatRoleEnum.ADMIN_ROLE.getName());
            SotokenBGUserVo bgUserVo = SotokenUtil.getBGTokenUserInfo();
            if("spadmin".equals(bgUserVo.getAccount())){
                list.add(UserRoleEnum.ADMIN_SUPER.getName());
            }else{
                list.add(UserRoleEnum.ADMIN_COMMON.getName());
            }
        }else if(UserChannelEnum.LIVE.getFlag().equals(lastStr)){
            list.add(PlatRoleEnum.LIVE_ROLE.getName());
            SotokenBGUserVo bgUserVo = SotokenUtil.getBGTokenUserInfo();
            if(LiveIdentityTypeEnum.ANCHOR.getValue().equals(bgUserVo.getIdentityType())){
                list.add(UserRoleEnum.LIVE_ANCHOR.getName());
            }else if(LiveIdentityTypeEnum.HELPER.getValue().equals(bgUserVo.getIdentityType())){
                list.add(UserRoleEnum.LIVE_HELPER.getName());
            }else if(LiveIdentityTypeEnum.OPERATE.getValue().equals(bgUserVo.getIdentityType())){
                list.add(UserRoleEnum.LIVE_OPERATE.getName());
            }
        }else if(UserChannelEnum.APP.getFlag().equals(lastStr)){
            list.add(PlatRoleEnum.APP_ROLE.getName());
            //APP 暂时未使用
//            SotokenAppUserVo appUserVo = SotokenUtil.getAppTokenUserInfo();
        }
        return list;
    }
}
