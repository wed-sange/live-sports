package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "比赛类型 1：足球，2：篮球", example = "1")
public enum MatchType {
    FOOTBALL(1, "足球"),
    BASKETBALL(2, "篮球"),
    ;

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;

    public static MatchType getEnum(Integer type) {
        MatchType[] values = MatchType.values();
        for (MatchType value : values) {
            if (value.getCode().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
