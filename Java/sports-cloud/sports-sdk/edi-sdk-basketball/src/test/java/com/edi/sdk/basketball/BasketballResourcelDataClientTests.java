package com.edi.sdk.basketball;


import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;

/**

 */
@Slf4j
public class BasketballResourcelDataClientTests {
    private final String user = "scinf";
    private final String secret = "8a9e44252deb727299984577263b809b";
    private BasketballClient client;

    @BeforeAll
    public void init() {
        client = new BasketballClient(user, secret, "https://nami.slscore.cn");
    }

    private void printJson(Object obj) {
        System.out.println(JSON.toJSONString(obj));
    }

//    @Test
//    public void matchListTest() {
//        MatchListRequest request = new MatchListRequest();
//        request.setId(100);
//        // request.setLimit(10);
//        // request.setTime(946656000);
//        EdiResponse<List<Match>> execute = client.matchList(request);
//        printJson(execute);
//        String json = client.execute(request);
//        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<Match>>>() {
//        }),  JSONWriter.Feature.MapSortField));
//    }
//
//    @Test
//    public void matchScheduleSeaSonParam() {
//        MatchScheduleSeaSonRequest request = new MatchScheduleSeaSonRequest();
//        request.setId(1012);
////        EdiResponse<List<Match>> execute = client.matchScheduleSeaSon(request);
////        printJson(execute);
//
//        String json = client.execute(request);
//        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<Match>>>() {
//        }),  JSONWriter.Feature.MapSortField));
//    }
//
//    @Test
//    public void seasonTableDetail() {
//        SeasonTableDetailRequest request = new SeasonTableDetailRequest();
//        request.setId(1012);
////        EdiResponse<List<SeasonTableDetail>> execute = client.seasonTableDetail(request);
////        printJson(execute);
//
//        String json = client.execute(request);
//        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<SeasonTableDetail>>>() {
//        }),  JSONWriter.Feature.MapSortField));
//    }
//
//    @Test
//    public void seasonStatsDetail() {
//        SeasonStatsDetailRequest request = new SeasonStatsDetailRequest();
//        request.setId(1012);
////        EdiResponse<List<SeasonStatsDetail>> execute = client.seasonStatsDetail(request);
////        printJson(execute);
//        String json = client.execute(request);
//        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<SeasonStatsDetail>>>() {
//        }),  JSONWriter.Feature.MapSortField));
//    }
//
//    @Test
//    public void archive() {
//        ArchiveRequest request = new ArchiveRequest();
////        EdiResponse<Archive> execute = client.archive(request);
////        printJson(execute);
//
//        String json = client.execute(request);
//        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
//        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<Archive>>() {
//        }),  JSONWriter.Feature.MapSortField));
//    }
}
