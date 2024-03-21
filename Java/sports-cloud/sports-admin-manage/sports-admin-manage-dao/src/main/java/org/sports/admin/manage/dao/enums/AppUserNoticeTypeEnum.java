package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * APP用户通知分类枚举

 */
@Getter
@AllArgsConstructor
@Schema(description = "通知类型(0系统通知 1反馈结果 2禁言通知 3解禁通知)", example = "1")
public enum AppUserNoticeTypeEnum {
    SYSTEM(0, "系统通知"),
    FEEDBACK(1, "反馈结果"),
    FORBIDDEN(2, "禁言通知"),
    UNFORBIDDEN(3, "解禁通知"),
    KICK_OUT(4, "踢出直播间");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
}
