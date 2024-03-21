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

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.sports.admin.manage.dao.enums.ActivityStatusType;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;


/**
 * @描述: 版本DAO
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/27
 * @创建时间: 13:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_admin_version")
public class VersionDO extends MysqlBaseDO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 渠道
     * @see org.sports.admin.manage.dao.enums.ChannelType
     */
    private Integer channel;
    
    /**
     * 版本
     */
    private String version;
    
    /**
     * 版本说明
     */
    private String remarks;
    
    /**
     * 是否强制更新
     * @see org.sports.admin.manage.dao.enums.ForcedUpdateType
     */
    private Integer forcedUpdate;

    /**
     * 资源路径
     */
    private String sourceUrl;

}
