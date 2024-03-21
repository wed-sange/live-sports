package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "反馈类型：1：更新问题，2：直播相关，3：产品体验，4：聊天相关，5：比赛相关，6：其他")
public enum FeedbackType {
    //更新问题
    UPDATE_QUESTION(1, "更新问题"),
    //直播相关
    LIVE_RELATED(2, "直播相关"),
    //产品体验
    PRODUCT_EXPERIENCE(3, "产品体验"),
    //聊天相关
    CHAT_RELATED(4, "聊天相关"),
    //比赛相关
    COMPETITION_RELATED(5, "比赛相关"),
    //其他
    OTHERS(6, "其他");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
}
