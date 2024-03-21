package org.sports.springboot.starter.satoken.enums;

import lombok.Getter;

/**
 * 验证码类型：math算法,char字符串
 */
@Getter
public enum KaptchaTypeEnum {

    MATH("math"),CHAR("char");

    private String value;

    KaptchaTypeEnum(String value){
        this.value = value;
    }
}
