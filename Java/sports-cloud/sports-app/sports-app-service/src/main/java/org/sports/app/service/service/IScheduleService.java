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

package org.sports.app.service.service;


import com.google.common.collect.Table;
import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.service.vo.LiveVo;
import org.sports.admin.manage.service.vo.MatchCountVO;
import org.sports.app.service.req.MatchCountRequest;
import org.sports.app.service.req.MatchPageRequest;
import org.sports.app.service.req.MatchRequest;
import org.sports.app.service.req.OngoingMatchRequest;
import org.sports.app.service.vo.HotMatchVo;
import org.sports.app.service.vo.MatchIdType;
import org.sports.app.service.vo.MatchVO;
import org.sports.app.service.vo.live.LiveUserVO;
import org.sports.springboot.starter.convention.page.PageRequest;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.time.LocalDate;
import java.util.List;

public interface IScheduleService {

    PageResponse<MatchVO> getMatchPage(Long userId, MatchPageRequest matchType);


    MatchVO getMatchInfoByMatchId(Integer matchId, MatchType matchType);

    Table<Integer, MatchType, MatchVO> getBatchMatchInfoByMatchId(List<MatchIdType> matchIdList);

    List<MatchVO> getLatestMatch(MatchRequest req);

    List<LiveVo> getOngoingMatch(OngoingMatchRequest req);

    PageResponse<MatchVO> getMyFollowMatchList(Long userId, PageRequest pageRequest);

    List<HotMatchVo> getHotMatchList(LocalDate startTime, LocalDate endTime, MatchType matchType,Integer status);

    List<MatchCountVO> getMatchTimeCount(MatchCountRequest request);

    List<LiveUserVO> getAnchorListByMatchId(Integer matchId, MatchType anEnum);
}
