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

package org.sports.admin.manage.service.enums;

import com.google.common.collect.Lists;
import org.sports.admin.manage.dao.enums.MatchStatus;

import java.util.List;

/**
 * @描述: 足球比赛状态
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/20
 * @创建时间: 13:39
 */
public enum FootballStatusType {
    ABNORMAL(0, "异常比赛"),
    NOT_START(1, "未开赛"),
    FIRST_HALF(2, "上半场"),
    HALFTIME(3, "中场"),
    SECOND_HALF(4, "下半场"),
    EXTRA_TIME(5, "加时赛"),
    EXTRA_TIME_OLD(6, "加时赛（弃用）"),
    PK(7, "点球决战"),
    FINISHED(8, "完场"),
    PUT_OFF(9, "推迟"),
    BREAK_OFF(10, "中断"),
    CUTTING(11, "腰斩"),
    CANCELED(12, "取消"),
    UNDETERMINED(13, "待定"),

    ;

    private final Integer code;
    private final String info;

    FootballStatusType(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public static final List<Integer> RUNNING = getRunningStatus();
    public static final List<Integer> UPCOMING = getUpcomingStatus();

    public static MatchStatus getMatchStatus(Integer statusId) {
        if (RUNNING.contains(statusId)) {
            return MatchStatus.RUNNING;
        }
        if (UPCOMING.contains(statusId)) {
            return MatchStatus.UN_RUNNING;
        }
        return MatchStatus.FINISHED;
    }

    public static List<Integer> getUnFinishedStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(FootballStatusType.NOT_START.code);
        list.add(FootballStatusType.FIRST_HALF.code);
        list.add(FootballStatusType.HALFTIME.code);
        list.add(FootballStatusType.SECOND_HALF.code);
        list.add(FootballStatusType.EXTRA_TIME_OLD.code);
        list.add(FootballStatusType.EXTRA_TIME.code);
        list.add(FootballStatusType.PK.code);
        list.add(FootballStatusType.ABNORMAL.code);
        list.add(FootballStatusType.BREAK_OFF.code);
        list.add(FootballStatusType.CANCELED.code);
        list.add(FootballStatusType.PUT_OFF.code);
        list.add(FootballStatusType.CUTTING.code);
        list.add(FootballStatusType.UNDETERMINED.code);
        return list;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    /**
     * 其他类型比赛不包含已完赛的
     * @return
     */
    public static List<Integer> getOtherStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(FootballStatusType.FINISHED.code);
        list.add(FootballStatusType.ABNORMAL.code);
        list.add(FootballStatusType.BREAK_OFF.code);
        list.add(FootballStatusType.CANCELED.code);
        list.add(FootballStatusType.PUT_OFF.code);
        list.add(FootballStatusType.CUTTING.code);
        list.add(FootballStatusType.UNDETERMINED.code);
        return list;
    }
    public static List<Integer> getFinishedStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(FootballStatusType.FINISHED.code);
        return list;
    }
    /**
     * 进行中比赛状态 2-7
     *
     * @return
     */
    public static List<Integer> getRunningStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(FootballStatusType.FIRST_HALF.code);
        list.add(FootballStatusType.HALFTIME.code);
        list.add(FootballStatusType.SECOND_HALF.code);
        list.add(FootballStatusType.EXTRA_TIME_OLD.code);
        list.add(FootballStatusType.EXTRA_TIME.code);
        list.add(FootballStatusType.PK.code);
        return list;
    }

    /**
     * 未开始比赛状态 1
     *
     * @return
     */
    public static List<Integer> getUpcomingStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(FootballStatusType.NOT_START.code);
        return list;
    }

    /**
     * 进行中和未开始比赛状态 1-7
     *
     * @return
     */
    public static List<Integer> getRunningAndUpcomingStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(FootballStatusType.NOT_START.code);
        list.add(FootballStatusType.FIRST_HALF.code);
        list.add(FootballStatusType.HALFTIME.code);
        list.add(FootballStatusType.SECOND_HALF.code);
        list.add(FootballStatusType.EXTRA_TIME_OLD.code);
        list.add(FootballStatusType.EXTRA_TIME.code);
        list.add(FootballStatusType.PK.code);
        return list;
    }
}
