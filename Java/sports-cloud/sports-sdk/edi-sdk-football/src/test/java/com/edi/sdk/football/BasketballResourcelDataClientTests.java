package com.edi.sdk.football;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.TypeReference;
import com.edi.sdk.core.EdiResponse;
import com.edi.sdk.football.domain.*;
import com.edi.sdk.football.request.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

/**

 */
@Slf4j
public class BasketballResourcelDataClientTests  extends FootballClient {
    private final String user = "scinf";
    private final String secret = "8a9e44252deb727299984577263b809b";
    private FootballClient client;

    @BeforeAll
    public void init() {
        client = new FootballClient(user, secret, "https://nami.slscore.cn");
    }

    private void printJson(Object obj) {
        System.out.println(JSON.toJSONString(obj));
    }

    @Test
    public void matchListTest() {
        MatchListRequest request = new MatchListRequest();
        request.setId(100);
        // request.setLimit(10);
        // request.setTime(946656000);
        EdiResponse<List<Match>> execute = matchList(request);
        printJson(execute);
        String json = execute(request);
        log.info(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<Match>>>() {
        }),  JSONWriter.Feature.MapSortField));
    }

    @Test
    public void matchScheduleSeaSonParam() {
        MatchScheduleSeaSonRequest request = new MatchScheduleSeaSonRequest();
        request.setId(1012);
//        BasketballResponse<List<Match>> execute = matchScheduleSeaSon(request);
//        printJson(execute);

        String json = execute(request);
        log.info(JSON.toJSONString(JSON.parseObject(json), JSONWriter.Feature.MapSortField));
        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<Match>>>() {
        }),  JSONWriter.Feature.MapSortField));
    }

    @Test
    public void seasonTableDetail() {
        SeasonTableDetailRequest request = new SeasonTableDetailRequest();
        request.setId(1012);
//        BasketballResponse<List<SeasonTableDetail>> execute = seasonTableDetail(request);
//        printJson(execute);

        String json = execute(request);
        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<SeasonTableDetail>>>() {
        }),  JSONWriter.Feature.MapSortField));
    }

    @Test
    public void seasonStatsDetail() {
        SeasonStatsDetailRequest request = new SeasonStatsDetailRequest();
        request.setId(1012);
//        BasketballResponse<List<SeasonStatsDetail>> execute = seasonStatsDetail(request);
//        printJson(execute);
        String json = execute(request);
        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<SeasonStatsDetail>>>() {
        }),  JSONWriter.Feature.MapSortField));
    }

    @Test
    public void archive() {
        ArchiveRequest request = new ArchiveRequest();
//        BasketballResponse<Archive> execute = archive(request);
//        printJson(execute);

        String json = execute(request);
        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<Archive>>() {
        }),  JSONWriter.Feature.MapSortField));
    }



    @Test
    public void lotteryMatchRequestTest() {
        LotteryMatchListRequest request = new LotteryMatchListRequest();
        EdiResponse<List<LotteryMatch>> execute = lotteryMatchList(request);
        printJson(execute);
        String json = execute(request);
        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<LotteryMatch>>>() {
        }),  JSONWriter.Feature.MapSortField));
    }

    @Test
    public void bdSfResultTest() {
        BdSfResultRequest request = new BdSfResultRequest();
        EdiResponse<List<BdSfResult>> execute = bdSfResult(request);
        printJson(execute);
        String json = execute(request);
        log.info(JSON.toJSONString(JSON.parseObject(json),  JSONWriter.Feature.MapSortField));
        log.info(JSON.toJSONString(JSON.parseObject(json, new TypeReference<EdiResponse<List<BdSfResult>>>() {
        }),  JSONWriter.Feature.MapSortField));
    }
}
