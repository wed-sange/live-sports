/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.sports.admin.manage.dao.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.sports.admin.manage.dao.enums.CancelType;
import org.sports.admin.manage.dao.enums.ForbiddenType;
import org.sports.admin.manage.dao.enums.IdentityType;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;

import java.time.LocalDateTime;


/**
 * @描述: 主播用户DO
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/18
 * @创建时间: 14:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_live_user")
public class LiveUserDO extends MysqlBaseDO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 账号
     */
    private String account;
    /**
     * 密码
     */
    private String passwd;
    
    /**
     * 身份
     * @see IdentityType
     */
    private Integer identityType;

    /**
     * 所属主播
     */
    private Long belongLive;

    /**
     * 拥有主播（运营特有)
     */
    @TableField( updateStrategy = FieldStrategy.IGNORED)
    private String possessLive;
    
    /**
     * 名字
     */
    private String name;
    /**
     * 昵称
     */
    private String nickName;
    
    /**
     * 头像
     */
    private String head;
    /**
     * 公告
     */
    private String notice;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 是否封禁 0否 1普通封禁 2永久封禁
     * @see ForbiddenType
     */
    private Integer ynForbidden;
    /**
     * 封禁天数
     */
    private Integer forbiddenDay;
    /**
     * 封禁原因
     */
    private String forbiddenDescp;
    /**
     * 封禁期限
     */
    private LocalDateTime forbiddenTime;
    /**
     * 是否注销
     * @see CancelType
     */
    private Integer ynCancel;

    /**
     * 上次登录日期
     */
    private LocalDateTime lastLoginTime;
    /**
     * 是否配置过开播信息
     * @see org.sports.admin.manage.service.enums.YnEnum
     */
    private Integer setOpenInfo;
}
