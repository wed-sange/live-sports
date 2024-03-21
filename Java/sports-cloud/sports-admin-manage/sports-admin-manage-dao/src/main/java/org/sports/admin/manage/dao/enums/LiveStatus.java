package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "直播状态,1:未开始，2：直播中，3：已关播")
public enum LiveStatus {
    NOT_START(1, "未开始"),
    LIVING(2, "直播中"),
    CLOSED(3, "已关播"),

    ;
    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
}
