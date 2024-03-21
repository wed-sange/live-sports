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

package org.sports.chat.service.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;


/**
 * @描述: 群组DAO
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
@TableName("t_chat_group")
public class GroupDO extends MysqlBaseDO {
    /**
     * id
     */
    private Long id;
    /**
     * 群组ID
     */
    private String groupId;
    
    /**
     * 群组名称
     */
    private String name;

    /**
     * 群组头像
     */
    private String avatar;

}
