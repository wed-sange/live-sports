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
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;


/**
 * @描述: 用户置顶操作
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2024/3/15
 * @创建时间: 14:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_app_pinned_user")
public class AppPinnedUserDO extends MysqlBaseDO {
    
    /**
     * id
     */
    private Long id;
    /**
     * 助理ID
     */
    private Long OperateId;
    /**
     * 主播ID
     */
    private Long AnchorId;
    
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 置顶类型：0 用户 1主播
     */
    private Integer type;


}
