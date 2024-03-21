package org.sports.admin.manage.dao.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @描述: 关注类型
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/18
 * @创建时间: 14:54
 */
@Getter
@AllArgsConstructor
public enum FollowType {
    ANCHOR(1, "主播"),
    MATCH(2, "比赛");

    @EnumValue
    @JsonValue
    private final Integer code;
    private final String info;
}
