package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 活动状态
 */
@Getter
@AllArgsConstructor
@Schema(description = "活动状态,0:下架,1:上架")
public enum ActivityStatusType {
    UNPUBLISHED(0, "下架"),
    PUBLISHED(1, "上架");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
}
