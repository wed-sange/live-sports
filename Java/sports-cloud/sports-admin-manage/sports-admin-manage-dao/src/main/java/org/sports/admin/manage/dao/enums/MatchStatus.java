package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "比赛状态 1：未开始，2：已开始，3：已完成", example = "1")
public enum MatchStatus {
    /**
     * 比赛中
     */
    UN_RUNNING(1, "未开始"),
    RUNNING(2, "已开始"),
    FINISHED(3, "已完成"),

    ;

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
}
