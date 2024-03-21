package org.sports.springboot.starter.web.enums;

import lombok.Getter;

/**
 * 语言枚举
 */
@Getter
public enum LanguageEnum {

    ZHCN("ZHCN"),ENUS("ENUS");

    private String value;

    LanguageEnum(String value){
        this.value = value;
    }
}
