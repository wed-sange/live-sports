package org.sports.springboot.starter.satoken.enums;

import lombok.Getter;

/**
 * 平台权限
 */
@Getter
public enum PlatRoleEnum {

    ADMIN_ROLE("plat_admin_role","admin平台权限"),
    LIVE_ROLE("plat_live_role","live平台权限"),
    APP_ROLE("plat_app_role","app平台权限");

    private String name;
    private String descp;

    PlatRoleEnum(String name, String descp){
        this.name = name;
        this.descp = descp;
    }

}
