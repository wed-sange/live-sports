package com.edi.sdk.basketball;

import org.junit.jupiter.api.BeforeAll;

/**
 2023年5月23日 下午1:34:39
 */
public class BasketballStatsClientTests {
    private static final String user = "scinf";
    private static final String secret = "8a9e44252deb727299984577263b809b";
    private BasketballClient client;

    @BeforeAll
    public void init() {
        client = new BasketballClient(user, secret, "https://nami.slscore.cn");
    }

//    @Test
//    public void MatchAnalysisRequest() {
//        MatchAnalysisRequest request = new MatchAnalysisRequest();
//        request.setId(3677734);
//        String json = client.execute(request);
//        System.out.println(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<MatchAnalysis>>() {
//        }),  JSONWriter.Feature.MapSortField));
//
//    }
//
//    @Test
//    public void MatchLiveHistoryRequest() {
//        MatchLiveHistoryRequest request = new MatchLiveHistoryRequest();
//        request.setId(3677734);
//        String json = client.execute(request);
//        System.out.println(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<MatchLiveHistory>>() {
//        }),  JSONWriter.Feature.MapSortField));
//    }
//
//    @Test
//    public void CompensationListRequest() {
//        CompensationListRequest request = new CompensationListRequest();
//        request.setId(3677734);
//        String json = client.execute(request);
//        System.out.println(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<CompensationList>>>() {
//        }),  JSONWriter.Feature.MapSortField));
//    }
//
//
//    @Test
//    public void CompetitionStatsDetailRequest() {
//        CompetitionStatsDetailRequest request = new CompetitionStatsDetailRequest();
//        request.setId(2);
//        String json = client.execute(request);
//        System.out.println(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<CompetitionStatsDetail>>() {
//        }),  JSONWriter.Feature.MapSortField));
//    }
//
//    @Test
//    public void CompetitionTableDetailRequest() {
//        CompetitionTableDetailRequest request = new CompetitionTableDetailRequest();
//        request.setId(1);
//        String json = client.execute(request);
//        System.out.println(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<CompetitionTableDetail>>() {
//        }),  JSONWriter.Feature.MapSortField));
//    }
//
//    @Test
//    public void SeasonBracketRequest() {
//        SeasonBracketRequest request = new SeasonBracketRequest();
//        request.setId(1010);
//        String json = client.execute(request);
//        System.out.println(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        System.out.println(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<SeasonBracket>>() {
//        }, Feature.SortFeidFastMatch)));
//    }
}
