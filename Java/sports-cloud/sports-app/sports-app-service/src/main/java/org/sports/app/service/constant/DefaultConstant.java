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

package org.sports.app.service.constant;

/**
 * @描述: 默认常量，目的减少魔法值
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/20
 * @创建时间: 11:37
 */
public class DefaultConstant {

    /**
     * edi 返回成功code码
     */
    public final static Integer EDI_SUCCEED_CODE = Integer.valueOf(0);
    /**
     * 前端查询已完成比赛列表状态值
     */
    public final static Integer QUERY_FINISH_MATCH = Integer.valueOf(99);

    /**
     * 热门比赛取最近几天的数据
     */
    public final static Integer HOT_MATCH_DAYS = Integer.valueOf(2);

    /**
     * 热门比赛TOP
     */
    public final static Integer HOT_MATCH_TOP = Integer.valueOf(5);

    /**
     * 最新比赛获取数量
     */
    public final static Integer LATEST_MATCH_COUNT = Integer.valueOf(20);

    /**
     * 截止时间到明天
     */
    public final static Integer TOMORROW = Integer.valueOf(5);
}
