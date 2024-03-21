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
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;


/**
 * @描述: 我的关注和订阅DAO
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/24
 * @创建时间: 10:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_app_follow")
public class MyFollowDO extends MysqlBaseDO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 业务ID:主播ID或者比赛ID
     */
    private Long bizId;

    /**
     * 比赛类型：1：足球 2：篮球
     */
    private MatchType matchType;

    /**
     * @see org.sports.admin.manage.dao.enums.FollowType
     * 关注类型：1：主播：2比赛
     */
    private Integer followType;

    /**
     * @see org.sports.admin.manage.dao.enums.IsFriend
     * 是否好友：1：是：0：不是
     */
    private Integer isFriend;

    /**
     * @see org.sports.admin.manage.dao.enums.IsFocus
     * 是否关注：1：是：0：不是
     */
    private Integer isFocus;

}
