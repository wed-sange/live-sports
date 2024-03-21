package org.sports.chat.service.constant;

/**
 * 缓存相关常量定义
 */
public class CacheConstant {

    /** sotoken-存储位置<根据key获取token> */
    public final static String SPORTSTOKEN_LOGIN_SESSION = "sportstoken:login:session:";
    /** sotoken-存储位置 <根据token获取key>*/
    public final static String SPORTSTOKEN_LOGIN_TOKEN = "sportstoken:login:token:";
    /** sotoken-存储位置 <根据token获取用户信息>*/
    public final static String SPORTSTOKEN_LOGIN_TOKEN_SESSION = "sportstoken:login:token-session:";

    public final static String CHAT_LIVE_GROUP_USER_SESSION = "sportschat:live:group:user:session:";
    public final static String MESSAGE_HAVE_READ_ID = "sportschat:message:have:read:"; //消息已读未修改数据库状态
    public final static String ANCHOR = "anchor:";
    public final static String OPERATOR = "operator";

    public final static String PUSH_USER_FOLLOW_ANCHOR_MAX_HOT = "sports:push:user:anchor:maxHotLiving:";
    public final static String JPUSH_ANCHOR_LIVING = "sports:jpush:user:anchor:living:";
    public final static String CHAT_GROUPIDS = "chat:groupIds:";

}
