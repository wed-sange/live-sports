package com.edi.sdk.football;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import com.edi.sdk.core.EdiResponse;
import com.edi.sdk.football.domain.*;
import com.edi.sdk.football.request.*;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;
import java.util.OptionalInt;

/**
 * 统计数据包
 */
public class FootballStatsClientTests extends FootballClient {
    private static final String user = "scinf";
    private static final String secret = "8a9e44252deb727299984577263b809b";

    public FootballStatsClientTests() {
        super(user, secret, "https://nami.gte28.com");
    }

    @Test
    public void MatchAnalysisRequest() {
        MatchAnalysisRequest request = new MatchAnalysisRequest();
        request.setId(3949008);
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<MatchAnalysis>>() {
        }), JSONWriter.Feature.MapSortField));

    }

    @Test
    public void MatchTeamStatsDetailRequest() {
        MatchTeamStatsDetailRequest request = new MatchTeamStatsDetailRequest();
        request.setId(1);
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<MatchTeamStatsDetail>>>() {
        })));
    }


    @Test
    public void MatchPlayerStatsDetailRequest() {
        MatchPlayerStatsDetailRequest request = new MatchPlayerStatsDetailRequest();
        request.setId(1010);
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<MatchPlayerStatsDetail>>>() {
        })));
    }

    @Test
    public void MatchLiveHistoryRequest() {
        MatchLiveHistoryRequest request = new MatchLiveHistoryRequest();
        request.setId(3677734);
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<MatchLiveHistory>>() {
        }), JSONWriter.Feature.MapSortField));
    }

    @Test
    public void CompensationListRequest() {
        CompensationListRequest request = new CompensationListRequest();
        request.setId(3677734);
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<Compensation>>>() {
        }), JSONWriter.Feature.MapSortField));
    }

    @Test
    public void SeasonBracketRequest() {
        SeasonBracketRequest request = new SeasonBracketRequest();
        request.setId(1010);
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<SeasonBracket>>() {
        })));
    }

    @Test
    public void CompetitionTableDetailRequest() {
        CompetitionTableDetailRequest request = new CompetitionTableDetailRequest();
        request.setId(1);
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<CompetitionTableDetail>>() {
        }), JSONWriter.Feature.MapSortField));
    }


    @Test
    public void CompetitionStatsDetailRequest() {
        CompetitionStatsDetailRequest request = new CompetitionStatsDetailRequest();
        request.setId(2);
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<CompetitionStatsDetail>>() {
        }), JSONWriter.Feature.MapSortField));
    }

    @Test
    public void OddsBfLiveRequest() {
        OddsBfLiveRequest request = new OddsBfLiveRequest();
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<OddsBfLive>>>() {
        }), JSONWriter.Feature.MapSortField));
    }

    @Test
    public void OddsBfHistoryRequest() {
        OddsBfHistoryRequest request = new OddsBfHistoryRequest();
        request.setId(2);
        String json = execute(request);
        System.out.println(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<OddsBfHistory>>() {
        }), JSONWriter.Feature.MapSortField));
    }

    @Test
    public void matchList() {
        RecentMatchListRequest request = new RecentMatchListRequest();
        request.setTime((int) (System.currentTimeMillis() / 1000 - 1 * 60 * 60));
        EdiResponse<List<RecentMatch>> response = recentMatchList(request);
        OptionalInt max = response.getResults().stream().mapToInt(RecentMatch::getMatchTime).max();
        OptionalInt min = response.getResults().stream().mapToInt(RecentMatch::getMatchTime).min();
        System.out.println(new Date(max.getAsInt() * 1000L));
        System.out.println(new Date(min.getAsInt() * 1000L));
        System.out.println(response.getResults().size());
    }
}
