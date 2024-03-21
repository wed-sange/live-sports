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

package org.sports.app.service.enums;

/**
 * @描述: 查询比赛状态
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/8/16
 * @创建时间: 14:54
 */
public enum QueryMatchStatus {
    RUNNING(1, "进行中"),
    UPCOMING(2, "未开始"),
    RUNNING_AND_UPCOMING(3, "进行中和未开始"),
    FINISHED(99, "已完成"),
    UNFINISHED(100, "非已完赛");;

    private final Integer code;
    private final String info;

    QueryMatchStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    public static QueryMatchStatus getQueryMatchStatusByCode(Integer code) {
        for (QueryMatchStatus matchStatus : QueryMatchStatus.values()) {
            if (matchStatus.getCode().equals(code)) {
                return matchStatus;
            }
        }
        return QueryMatchStatus.UNFINISHED;
    }
}
