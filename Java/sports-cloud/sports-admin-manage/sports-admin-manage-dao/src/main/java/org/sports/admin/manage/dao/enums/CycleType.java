package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CycleType {
    CURRENT_MONTH(1, "本月"),
    NEARLY_THREE_MONTH(2, "近三个月"),
    ALL(3, "全部"),
    ;

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
    public static CycleType getEnum(Integer type) {
        CycleType[] values = CycleType.values();
        for (CycleType value : values) {
            if (value.getCode().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
