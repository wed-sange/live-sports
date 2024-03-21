package org.sports.admin.manage.dao.enums;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "消息类型，25:开播，26：关播，27：主播更新播放地址，28：反馈通知", example = "1")
public enum MessageType {
    ANCHOR_OPEN(25, "开播"),
    ANCHOR_CLOSE(26, "关播"),
    ANCHOR_UPDATE_PLAY_URL(27, "主播更新播放地址"),
    FEEDBACK_REPLY(28, "反馈通知"),
    USER_UNREAD_MSG_COUNT(29, "用户未读消息总数"),
    MATCH_SCORE(30, "比赛实时分数"),
    FORBIDDEN_APP_USER(31, "封禁APP用户"),
    UNFORBIDDEN_APP_USER(32, "解禁APP用户"),
    START_MATCH(33, "开赛通知"),
    FINISHED_MATCH(34, "完赛通知"),
    NEWS_REFRESH(35, "新闻更新通知"),
    KICK_OUT_USER(36, "用户被踢出直播间"),
    LIVE_USER_CACHE_REFRESH(99, "直播端用户缓存更新提醒"),
    ;

    private final Integer command;
    private final String info;
}
