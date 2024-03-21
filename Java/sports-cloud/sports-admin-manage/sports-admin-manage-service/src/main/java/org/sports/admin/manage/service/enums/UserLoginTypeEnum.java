package org.sports.admin.manage.service.enums;

import lombok.Getter;

/**
 * 用户登录方式枚举 1手机号 2邮箱
 */
@Getter
public enum UserLoginTypeEnum {
    TEL(1),EMAIL(2);

    private Integer value;

    UserLoginTypeEnum(Integer value){
        this.value = value;
    }
}
