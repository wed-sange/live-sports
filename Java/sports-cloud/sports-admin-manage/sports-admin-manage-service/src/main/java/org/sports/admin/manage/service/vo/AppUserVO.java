package org.sports.admin.manage.service.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;


/**
 * APP用户信息
 *

 * @since 2023-07-20
 */
@Data
@Schema(description = "APP用户信息")
public class AppUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键id")
    private Long id;

    @Schema(description = "手机号")
    private String tel;

    @Schema(description = "手机号区号")
    private String areaCode;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "昵称")
    private String name;

    @Schema(description = "头像")
    private String head;

    @Schema(description = "等级")
    private Integer lvNum;

    @Schema(description = "等级名称")
    private String lvName;

    @Schema(description = "成长值")
    private Integer growthValue;

    @Schema(description = "下一阶段成长值")
    private Integer growthValueNext;

    @Schema(description = "注册地址")
    private String registerAddr;

    @Schema(description = "是否禁言 0否 1普通禁言 2永久禁言")
    private Integer ynForbidden;

    @Schema(description = "禁言天数")
    private Integer forbiddenDay;

    @Schema(description = "禁言期限")
    private LocalDateTime forbiddenTime;

    @Schema(description = "禁言原因")
    private String forbiddenDescp;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间")
    private LocalDateTime updateTime;

    @Schema(description = "剩余时长")
    private Integer leaveDay;
    @Schema(description = "来源 1IOS 2android 3H5 4小程序")
    private String source;

    @Schema(description = "IP地址")
    private String ipAddress;

    @Schema(description = "渠道")
    private String channel;

}
