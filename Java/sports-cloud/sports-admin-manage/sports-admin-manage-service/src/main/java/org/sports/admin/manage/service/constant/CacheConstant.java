package org.sports.admin.manage.service.constant;

/**
 * @描述: 缓存key
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/13
 * @创建时间: 11:37
 */
public interface CacheConstant {

    String APP_PREFIX = "sports:";

    String FOOTBALL = "football:";

    String BASKETBALL = "basketball:";
    /**
     * 文章明细
     */
    String ARTICLE_DETAIL = APP_PREFIX + "article_detail";

    /**
     * 赛事明细
     */
    String TOUR_DETAIL = APP_PREFIX + "tour_detail";

    /**
     * 赛事类型
     */
    String MATCH_TYPE = "match:type:";

    /**
     * 球队信息
     */
    String MATCH_TEAM = APP_PREFIX + "match:team:";
    /**
     * 赛事信息
     */
    String MATCH_COMPETITION_DETAIL = APP_PREFIX + "match:competition:detail:";
    /**
     * 赛事搜索
     */
    String COMPETITION_SEARCH = APP_PREFIX + "competition:search:";

    /**
     * 赛事列表
     */
    String MATCH_COMPETITION_LIST = APP_PREFIX + "match:competition:list:";

    /**
     * 后台配置缓存前缀
     */
    String ADMIN_CONFIG = "config:";

    /**
     * APP用户缓存前缀
     */
    String APP_USERVO = "app:uservo:";

    /**
     * 直播热度
     */
    String LIVE_HOT = "live:hot:";

    /**
     * 足球日历比赛数缓存KEY
     */
    String CALENDAR_MATCH_COUNT = APP_PREFIX + "calendar:";

    /**
     * 热门赛事ID列表缓存key
     */
    String HOT_COMPETITION_ID_LIST = APP_PREFIX +  "hot_competition_id_list:";

    /**
     * 比赛列表
     */
    String MATCH_LIST = APP_PREFIX + "match:list:";


    /**
     * 比赛详情
     */
    String MATCH_DETAILS = APP_PREFIX + "match:details:";

    /**
     * 新闻明细缓存KEY
     */
    String NEWS = APP_PREFIX +  "news:";

    /**
     * 国家列表
     */
    String COUNTRIES = APP_PREFIX +  "countries";

    /**
     * 新闻明细缓存KEY
     */
    String ACTIVITY = APP_PREFIX +  "activity:";
    /**
     * app版本
     */
    String VERSION = APP_PREFIX +  "version:";
    String HOT_COMPETITION_LIST = APP_PREFIX + "hot_competition_list:";
    /**
     * 抓取新闻同步锁
     */
    String GRAB_NEWS_LOCK_IP = APP_PREFIX + "refresh:lock:IP:grab_news";

    String GRAB_NEWS_TARGET = APP_PREFIX + "grab_news:target";

    /**
     * 裁判信息
     */
    String REFEREE = APP_PREFIX + "referee:";

    String HOT_MATCH_LIVE_LIST = APP_PREFIX + "hot_match_live_list:";

    /** 用户观看直播阻塞队列缓存 */
    String UserWatchTimePer = "chat:user:watch:time";
    String LIVE_USER_FANS_COUNT =  APP_PREFIX + "live_user:fans:";


    //足球比赛开始上半场或者下半场开始时间
    String FOOTBALL_START_TIME = APP_PREFIX + FOOTBALL + "starttime:";
    String SDK_BASKETBALL_MATCH_LIST = APP_PREFIX + BASKETBALL + "sdk:matchList:";
    String SDK_FOOTBALL_MATCH_LIST = APP_PREFIX + FOOTBALL + "sdk:matchList:";

    String SDK_BASKETBALL_MATCH = APP_PREFIX + BASKETBALL + "sdk:match:";
    String SDK_FOOTBALL_MATCH = APP_PREFIX + FOOTBALL + "sdk:match:";
    String SDK_FOOTBALL_MATCH_LIVE_HISTORY = APP_PREFIX + FOOTBALL + "sdk:match:live:history";
    String SDK_BASKETBALL_MATCH_LIVE_HISTORY = APP_PREFIX + BASKETBALL + "sdk:match:live:history";
    String SDK_FOOTBALL_MATCH_PAGE = "sdk:matchPage:";
    String SDK_BASKETBALL_MATCH_PAGE = APP_PREFIX + BASKETBALL + "sdk:matchPage:";

    String SDK_BASKETBALL_RECENT_MATCH_TIME = APP_PREFIX + BASKETBALL + "sdk:recent_match_time";
    String SDK_FOOTBALL_RECENT_MATCH_TIME= APP_PREFIX + FOOTBALL + "sdk:recent_match_time";

    /**
     * 定时刷新比赛分数
     */
    String RECENT_MATCH_LOCK_IP = APP_PREFIX + "refresh:lock:IP:recent_match";
    /**
     * 篮球比赛最新分数
     */
    String APP_BASKETBALL_MATCH_NEWEST_SCORE = APP_PREFIX + BASKETBALL + "sdk:match:newest:score:";
    /**
     * 足球比赛最新分数
     */
    String APP_FOOTBALL_MATCH_NEWEST_SCORE = APP_PREFIX + FOOTBALL + "sdk:match:newest:score:";


    String PUSH_USER_START_MATCH = APP_PREFIX  + "push:user:start_match:";

    String PUSH_USER_FINISHED_MATCH = APP_PREFIX  + "push:user:finished_match:";

    String USER_FOLLOW_ANCHOR = APP_PREFIX + "user_follow_anchor:";

    String USER_FOLLOW_MATCH = APP_PREFIX + "user_follow_match:";


    //踢出用户
    String KICK_OUT_USER_KEY = APP_PREFIX + "kick_out_users:";

    /**
     * 短信发送的厂商ID
     */
    String APP_SMS_CONFIGID = "app:sms:config_id";

    /**
     * 短信发送异常次数
     */
    String APP_SMS_ERROR_COUNT = "app:sms:error:count";

    String FOOTBALL_VIDEO_LINES = APP_PREFIX + FOOTBALL + "lines:";
}
