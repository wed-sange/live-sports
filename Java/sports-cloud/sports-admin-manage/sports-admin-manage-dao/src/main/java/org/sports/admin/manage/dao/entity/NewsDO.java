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
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.sports.admin.manage.dao.enums.ActivityStatusType;
import org.sports.springboot.starter.database.mybatisplus.MysqlBaseDO;

import java.time.LocalDateTime;


/**
 * @描述: 新闻
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/19
 * @创建时间: 15:37
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("t_app_news")
public class NewsDO extends MysqlBaseDO {
    
    /**
     * id
     */
    private Long id;
    
    /**
     * 标题
     */
    private String title;
    /**
     * 图片
     */
    private String pic;
    
    /**
     * 联赛名称
     */
    private String tournament;
    
    /**
     * 新闻内容
     */
    private String content;
    
    /**
     * 新闻源地址
     */
    private String sourceUrl;
    
    /**
     * 新闻发布时间
     */
    private LocalDateTime publishTime;

}
