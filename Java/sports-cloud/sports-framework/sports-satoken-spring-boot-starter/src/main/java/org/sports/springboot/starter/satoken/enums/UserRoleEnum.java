package org.sports.springboot.starter.satoken.enums;

import lombok.Getter;

/**
 * 用户权限枚举
 */
@Getter
public enum UserRoleEnum {

    ADMIN_SUPER("admin_super","admin超级管理员"),
    ADMIN_COMMON("admin_common","admin普通用户"),
    LIVE_ANCHOR("live_anchor","live主播用户"),
    LIVE_HELPER("live_helper","live助手用户"),
    LIVE_OPERATE("live_operate","live运营人员");

    private String name;
    private String descp;

    UserRoleEnum(String name, String descp){
        this.name = name;
        this.descp = descp;
    }

}
