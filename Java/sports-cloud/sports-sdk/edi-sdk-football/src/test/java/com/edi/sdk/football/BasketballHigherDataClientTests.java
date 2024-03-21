package com.edi.sdk.football;

import com.alibaba.fastjson2.JSON;
import com.edi.sdk.core.EdiResponse;
import com.edi.sdk.football.domain.*;
import com.edi.sdk.football.request.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;

/**
 2023年6月27日 下午14:10:30
 */
public class BasketballHigherDataClientTests {
    private final String user = "scinf";
    private final String secret = "8a9e44252deb727299984577263b809b";
    private FootballClient client;

    @BeforeAll
    public void init() {
        client = new FootballClient(user, secret, "https://nami.gte28.com");
    }

    private void printJson(Object obj) {
//        System.out.println("对象："+obj);
        System.out.println("===========================================");
        System.out.println("自己："+ JSON.toJSONString(obj));
    }


    @Test
    public void intelligenceListTest() {
        IntelligenceListRequest request = new IntelligenceListRequest();
//		request.setId(101921);
         request.setLimit(1);
        // request.setTime(946656000);
        EdiResponse<List<Intelligence>> execute = client.intelligence(request);
        printJson(execute);
    }

    @Test
    public void seasonBestLineupTest() {
        SeasonBestLineupListRequest request = new SeasonBestLineupListRequest();
//		request.setId(100);
         request.setLimit(1);
        // request.setTime(946656000);
        EdiResponse<List<SeasonBestLineup>> execute = client.seasonBestLineup(request);
        printJson(execute);
    }

    @Test
    public void teamSquadTest() {
        TeamSquadListRequest request = new TeamSquadListRequest();
//		request.setId(100);
         request.setLimit(1);
        // request.setTime(946656000);
        EdiResponse<List<TeamSquad>> execute = client.teamSquad(request);
        printJson(execute);
    }

    @Test
    public void teamInjuryTest() {
        TeamInjuryListRequest request = new TeamInjuryListRequest();
//		request.setId(100);
         request.setLimit(1);
        // request.setTime(946656000);
        EdiResponse<List<TeamInjury>> execute = client.teamInjury(request);
        printJson(execute);
    }

    @Test
    public void teamHonorTest() {
        TeamHonorListRequest request = new TeamHonorListRequest();
//		request.setId(100);
         request.setLimit(1);
        // request.setTime(946656000);
        EdiResponse<List<TeamHonor>> execute = client.teamHonor(request);
        printJson(execute);
    }

    @Test
    public void playerTransferTest() {
        PlayerTransferListRequest request = new PlayerTransferListRequest();
//		request.setId(100);
         request.setLimit(1);
        // request.setTime(946656000);
        EdiResponse<List<PlayerTransfer>> execute = client.playerTransfer(request);
        printJson(execute);
    }

    @Test
    public void playerHonorTest() {
        PlayerHonorListRequest request = new PlayerHonorListRequest();
//		request.setId(100);
         request.setLimit(1);
        // request.setTime(946656000);
        EdiResponse<List<PlayerHonor>> execute = client.playerHonor(request);
        printJson(execute);
    }

    @Test
    public void coachHistoryTest() {
        CoachHistoryListRequest request = new CoachHistoryListRequest();
//		request.setId(100);
         request.setLimit(1);
        // request.setTime(946656000);
        EdiResponse<List<CoachHistory>> execute = client.coachHistory(request);
        printJson(execute);
    }

    @Test
    public void coachHonorTest() {
        CoachHonorListRequest request = new CoachHonorListRequest();
//		request.setId(100);
         request.setLimit(1);
        // request.setTime(946656000);
        EdiResponse<List<CoachHonor>> execute = client.coachHonor(request);
        printJson(execute);
    }

    @Test
    public void rankingFifaMenTest() {
        RankingFifaMenRequest request = new RankingFifaMenRequest();
        // request.setPub_time(946656000);
        EdiResponse<RankingFifaMen> execute = client.rankingFifaMen(request);
        printJson(execute);
    }

    @Test
    public void rankingFifaWomenTest() {
        RankingFifaWomenRequest request = new RankingFifaWomenRequest();
        // request.setPub_time(946656000);
        EdiResponse<RankingFifaWomen> execute = client.rankingFifaWomen(request);
        printJson(execute);
    }

    @Test
    public void rankingClubTest() {
        RankingClubRequest request = new RankingClubRequest();
        EdiResponse<List<RankingClub>> execute = client.rankingClub(request);
        printJson(execute);
    }

    // =========================== 高阶数据包 结束 =============================


    // =========================== 指数数据包 开始 =============================

    @Test
    public void oddsLiveTest() {
        OddsLiveRequest request = new OddsLiveRequest();
        EdiResponse<HashMap<String,List<List<Object>>>> execute = client.oddsLive(request);
        printJson(execute);
    }

    @Test
    public void oddsHistoryTest() {
        OddsHistoryRequest request = new OddsHistoryRequest();
		request.setId(106933);
        EdiResponse<OddsHistory> execute = client.oddsHistory(request);
        printJson(execute);
    }

    @Test
    public void oddsUpdateTest() {
        OddsUpdateRequest request = new OddsUpdateRequest();
        EdiResponse<List<OddsUpdate>> execute = client.oddsUpdate(request);
        printJson(execute);
    }

    @Test
    public void oddsEuropeCompanyTest() {
        OddsEuropeCompanyListRequest request = new OddsEuropeCompanyListRequest();
        EdiResponse<List<OddsEuropeCompany>> execute = client.oddsEuropeCompany(request);
        printJson(execute);
    }

    @Test
    public void oddsEuropeLiveTest() {
        OddsEuropeLiveRequest request = new OddsEuropeLiveRequest();
        EdiResponse<OddsEuropeLive> execute = client.oddsEuropeLive(request);
        printJson(execute);
    }

    @Test
    public void oddsEuropeHistoryTest() {
        OddsEuropeHistoryRequest request = new OddsEuropeHistoryRequest();
		request.setId(3554509);
        EdiResponse<OddsEuropeHistory> execute = client.oddsEuropeHistory(request);
        printJson(execute);
    }

    // =========================== 指数数据包 结束 =============================


}
