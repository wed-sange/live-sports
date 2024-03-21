package org.sports.springboot.starter.satoken.Vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 后台token用户信息（admin/live）
 */
@Data
public class SotokenBGUserVo {

    //"用户id"
    private Long id;

    //"账号"
    private String account;

    //"昵称"
    private String name;

    //"头像"
    private String head;

    //"备注"
    private String remarks;

    //"渠道"
    private String channel;

    //"创建时间"
    private LocalDateTime createTime;

    //"是否有效 1是 0否[admin特有]"
    private Integer ynValid;

    //"身份(1主播 2助手 3运营)[live特有]"
    private Integer identityType;

    //"主播公告[live特有]"
    private String notice;

    //"是否禁言[live特有]"
    private Integer ynForbidden;

    //"所属主播[live特有]"
    private Long belongLive;

    //"是否注销[live特有]"
    private Integer ynCancel;

    // IP地址
    private String ipAddress;
}
