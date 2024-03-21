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
 * @描述: 篮球比赛状态
 * @版权: Copyright (c) 2023


 * @版本: 1.0
 * @创建日期: 2023/7/20
 * @创建时间: 13:39
 */
public enum BasketballStatusType {
    ABNORMAL(0, "异常比赛"),
    NOT_START(1, "未开赛"),
    FIRST_QUARTER(2, "第一节"),
    FIRST_QUARTER_FINISHED(3, "第一节完"),
    SECOND_QUARTER(4, "第二节"),
    SECOND_QUARTER_FINISHED(5, "第二节完"),
    THIRD_QUARTER(6, "第三节"),
    THIRD_QUARTER_FINISHED(7, "第三节完"),
    FOURTH_QUARTER(8, "第四节"),
    EXTRA_TIME(9, "加时"),
    FINISHED(10, "完场"),
    BREAK_OFF(11, "中断"),
    CANCELED(12, "取消"),
    POSTPONE(13, "延期"),
    CUTTING(14, "腰斩"),
    UNDETERMINED(15, "待定"),

    ;

    private final Integer code;
    private final String info;

    BasketballStatusType(Integer code, String info) {
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

    /**
     * 其他类型比赛不包含已完赛的
     * @return
     */
    public static List<Integer> getOtherStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(BasketballStatusType.FINISHED.code);
        list.add(BasketballStatusType.ABNORMAL.code);
        list.add(BasketballStatusType.BREAK_OFF.code);
        list.add(BasketballStatusType.CANCELED.code);
        list.add(BasketballStatusType.POSTPONE.code);
        list.add(BasketballStatusType.CUTTING.code);
        list.add(BasketballStatusType.UNDETERMINED.code);
        return list;
    }

    public static List<Integer> getFinishedStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(BasketballStatusType.FINISHED.code);
        return list;
    }

    public static List<Integer> getUnFinishedStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(BasketballStatusType.NOT_START.code);
        list.add(BasketballStatusType.FIRST_QUARTER.code);
        list.add(BasketballStatusType.FIRST_QUARTER_FINISHED.code);
        list.add(BasketballStatusType.SECOND_QUARTER.code);
        list.add(BasketballStatusType.SECOND_QUARTER_FINISHED.code);
        list.add(BasketballStatusType.THIRD_QUARTER.code);
        list.add(BasketballStatusType.THIRD_QUARTER_FINISHED.code);
        list.add(BasketballStatusType.FOURTH_QUARTER.code);
        list.add(BasketballStatusType.EXTRA_TIME.code);
        list.add(BasketballStatusType.ABNORMAL.code);
        list.add(BasketballStatusType.BREAK_OFF.code);
        list.add(BasketballStatusType.CANCELED.code);
        list.add(BasketballStatusType.POSTPONE.code);
        list.add(BasketballStatusType.CUTTING.code);
        list.add(BasketballStatusType.UNDETERMINED.code);
        return list;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }

    /**
     * 进行中比赛状态 2-9
     *
     * @return
     */
    public static List<Integer> getRunningStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(BasketballStatusType.FIRST_QUARTER.code);
        list.add(BasketballStatusType.FIRST_QUARTER_FINISHED.code);
        list.add(BasketballStatusType.SECOND_QUARTER.code);
        list.add(BasketballStatusType.SECOND_QUARTER_FINISHED.code);
        list.add(BasketballStatusType.THIRD_QUARTER.code);
        list.add(BasketballStatusType.THIRD_QUARTER_FINISHED.code);
        list.add(BasketballStatusType.FOURTH_QUARTER.code);
        list.add(BasketballStatusType.EXTRA_TIME.code);
        return list;
    }

    /**
     * 未开始比赛状态 1
     *
     * @return
     */
    public static List<Integer> getUpcomingStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(BasketballStatusType.NOT_START.code);
        return list;
    }

    /**
     * 进行中和未开始比赛状态 1-9
     *
     * @return
     */
    public static List<Integer> getRunningAndUpcomingStatus() {
        List<Integer> list = Lists.newArrayList();
        list.add(BasketballStatusType.NOT_START.code);
        list.add(BasketballStatusType.FIRST_QUARTER.code);
        list.add(BasketballStatusType.FIRST_QUARTER_FINISHED.code);
        list.add(BasketballStatusType.SECOND_QUARTER.code);
        list.add(BasketballStatusType.SECOND_QUARTER_FINISHED.code);
        list.add(BasketballStatusType.THIRD_QUARTER.code);
        list.add(BasketballStatusType.THIRD_QUARTER_FINISHED.code);
        list.add(BasketballStatusType.FOURTH_QUARTER.code);
        list.add(BasketballStatusType.EXTRA_TIME.code);
        return list;
    }
}
