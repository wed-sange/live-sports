package org.sports.springboot.starter.satoken.enums;

import lombok.Getter;

/**
 * 用户渠道枚举 admin/live/app
 */
@Getter
public enum UserChannelEnum {

    ADMIN("admin", "1"),LIVE("live", "2"),APP("app", "3");

    private String value;
    private String flag;

    UserChannelEnum(String value, String flag){
        this.value = value;
        this.flag = flag;
    }
}
