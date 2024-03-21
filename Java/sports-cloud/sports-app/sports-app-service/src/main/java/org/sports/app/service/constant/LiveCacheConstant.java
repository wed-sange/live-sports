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
 * 直播缓存key
 */
public interface LiveCacheConstant {

    String LIVE_PREFIX = "live:";

    /**
     * 文字直播
     * 参数1：比赛ID
     */
    String LIVE_TEXT_LIVE = LIVE_PREFIX + "tLive:{}";

    /**
     * 比赛事件
     * 参数1：比赛ID
     */
    String LIVE_INCIDENTS = LIVE_PREFIX + "incidents:{}";

    /**
     * 比赛技术统计
     * 参数1：比赛ID
     */
    String LIVE_MATCH_STATS = LIVE_PREFIX + "team:stats:{}";

    /**
     * 得分
     * 参数1：比赛ID
     */
    String LIVE_MATCH_SCORE = LIVE_PREFIX + "score:{}";

    /**
     * 球员阵容及得分
     * 参数1：比赛ID
     */
    String LIVE_PLAYER_SCORE = LIVE_PREFIX + "player:score:{}";

    /**
     * 比赛指数
     * 参数1：比赛ID
     */
    String ODDS = "odds:{}";

    /**
     * 比赛指数
     * 参数1：比赛ID
     * 参数2：数据类型
     * 参数1：公司ID
     */
    String ODDS_HISTORY = "odds-history:{}:{}:{}";

    /**
     * 指数类型-胜平负
     */
    String ODDS_EU = "eu";

    /**
     * 让球
     */
    String ODDS_ASIA = "asia";

    /**
     * 进球数
     */
    String ODDS_BS = "bs";

    /**
     * 角球
     */
    String ODDS_CR = "cr";


}
