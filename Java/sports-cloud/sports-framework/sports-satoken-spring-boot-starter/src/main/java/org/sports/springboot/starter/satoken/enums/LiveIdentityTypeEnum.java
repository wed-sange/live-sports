package org.sports.springboot.starter.satoken.enums;

import lombok.Getter;

/**
 * 直播身份枚举 1主播 2助手 3运营
 */
@Getter
public enum LiveIdentityTypeEnum {

    ANCHOR(1),HELPER(2),OPERATE(3);

    private Integer value;

    LiveIdentityTypeEnum(Integer value){
        this.value = value;
    }
}
