package org.sports.springboot.starter.satoken.Vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * APP:token用户信息
 */
@Data
public class SotokenAppUserVo {

   //"用户id"
    private Long id;

   //"手机号"
    private String tel;

   //"邮箱"
    private String email;

   //"昵称"
    private String name;

   //"头像"
    private String head;

   //"等级"
    private Integer lvNum;

   //"等级名称"
    private String lvName;

   //"成长值"
    private Integer growthValue;

   //"下一阶段成长值"
    private Integer growthValueNext;

   //"注册地址"
    private String registerAddr;

   //"是否禁言 0否 1普通禁言 2永久禁言"
    private Integer ynForbidden;

   //"禁言期限"
    private LocalDateTime forbiddenTime;

   //"禁言原因"
    private String forbiddenDescp;

    // 来源 1IOS 2android 3H5 4小程序
    private String source;

    // IP地址
    private String ipAddress;

    //"渠道"
    private String channel;

}
