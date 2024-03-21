package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "推送类型", example = "1")
public enum PushType {
    MATCH_START(1, "比赛开赛"),
    MATCH_FINISHED(2, "比赛完赛"),
    ANCHOR_OPEN_LIVE(3, "主播开播"),
    ;

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;

    public static PushType getEnum(Integer type) {
        PushType[] values = PushType.values();
        for (PushType value : values) {
            if (value.getCode().equals(type)) {
                return value;
            }
        }
        return null;
    }
}
