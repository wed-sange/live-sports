package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "反馈状态 1:未处理,2:已处理")
public enum FeedbackStatus {
    //未处理
    UNPROCESSED(1, "未处理"),
    //已处理
    PROCESSED(2, "已处理"),
    ;
    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
}
