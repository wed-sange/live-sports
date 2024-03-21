package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DateType {
    CHAT_MESSAGE(1, "普通消息"),
    FEEDBACK(2, "系统通知"),
    ;

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
    public static DateType getEnum(Integer type) {
        DateType[] values = DateType.values();
        for (DateType value : values) {
            if (value.getCode().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
