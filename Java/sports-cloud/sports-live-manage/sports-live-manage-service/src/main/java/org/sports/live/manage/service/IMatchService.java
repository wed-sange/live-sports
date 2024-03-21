package org.sports.live.manage.service;

import org.sports.admin.manage.dao.enums.MatchType;
import org.sports.admin.manage.dao.req.CompetitionListRequest;
import org.sports.admin.manage.dao.req.MatchListRequest;
import org.sports.live.manage.vo.CompetitionListVo;
import org.sports.live.manage.vo.MatchListVo;
import org.sports.live.manage.vo.MatchLiveVO;
import org.sports.springboot.starter.convention.page.PageResponse;

import java.util.List;

public interface IMatchService {
    PageResponse<MatchListVo> list(Long userId,MatchListRequest request);

    List<CompetitionListVo> competitionByTime(CompetitionListRequest request);

    MatchLiveVO getMatchInfoByMatchId(Integer matchId, MatchType matchType);
}
