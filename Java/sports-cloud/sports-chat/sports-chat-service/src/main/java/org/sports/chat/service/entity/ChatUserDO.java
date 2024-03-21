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
import org.jim.core.packets.UserStatusType;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;


/**
 * @描述: 用户DAO
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
@TableName("t_chat_user")
public class ChatUserDO extends MysqlBaseDO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 昵称
     */
    private String nick;
    
    /**
     * 头像
     */
    private String avatar;

    /**
     * 等级名称
     */
    private String lvName;

    /**
     * 等级
     */
    private Integer lvNum;

    /**
     * 用户身份：0-普通用户   1-主播   2-主播助理
     */
    private Integer identity;
    
    /**
     * 在线状态 0 在线 1 离线
     */
    private Integer status = UserStatusType.OFFLINE.getNumber();
    /**
     * 个性签名;
     */
    private String sign;
    /**
     * 用户所属终端;(ws、tcp、http、android、ios等)
     */
    private String terminal;
}
