package org.sports.admin.manage.service.constant;

public interface RedisCacheConstant {

    //app用户被系统禁言缓存
    String APP_USER_FORBIDDEN = "app:user:forbidden";

    //直播端用户被系统禁言缓存
    String LIVE_USER_FORBIDDEN = "live:user:forbidden";
    //app用户被主播禁言缓存
    String APP_ANCHOR_USER_FORBIDDEN = "app:user:forbidden_by_anchor:";

    //增加用户成长值锁
    String ADD_USER_GROWTH_VALUE_LOCK = "addUserGrowthValue:lock";

    //live主播信息缓存
    String LIVE_ANCHOR_INFO_CACHE = "cache:live:anchorInfo";

    //google验证密钥
    String GOOGLE_CHECK_CODE_CACHE = "cache:google:checkCode";

    //系统用户权限缓存
    String USER_PERMISSIONS_CACHE = "cache:user:permissions";

    String LIVEID_ANCHORID_MAP = "cache:liveId_anchorId:map";
    //短信变更更新通知key
    String SMS_UPDATE_MSG_TOPC = "sms_update_msg_topc";

}
