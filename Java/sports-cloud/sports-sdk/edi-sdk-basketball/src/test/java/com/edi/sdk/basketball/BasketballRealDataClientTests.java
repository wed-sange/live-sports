package com.edi.sdk.basketball;

import java.util.List;

import com.alibaba.fastjson2.JSON;
import com.edi.sdk.basketball.domain.*;
import com.edi.sdk.basketball.request.*;
import com.edi.sdk.core.EdiResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 2023年5月23日 下午1:34:39
 */
public class BasketballRealDataClientTests {
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

	@Test
	public void recentMatchListTest() {
		RecentMatchListRequest request = new RecentMatchListRequest();
		request.setId(100);
		// request.setLimit(10);
		// request.setTime(946656000);
		EdiResponse<List<Match>> execute = client.recentMatchList(request);
		printJson(execute);
	}

	@Test
	public void scheduleDiaryTest() {
		MatchScheduleDiaryRequest request = new MatchScheduleDiaryRequest();
		// request.setDate("20230101");
		EdiResponse<Schedule> execute = client.scheduleDiary(request);
		printJson(execute);
	}

	@Test
	public void scheduleParamTest() {
		MatchScheduleParamRequest request = new MatchScheduleParamRequest();
		request.setCompetitionId(1);
		request.setLotteryId(201);
		EdiResponse<List<Match>> execute = client.scheduleParam(request);
		printJson(execute);
	}
	
	@Test
	public void matchLive() {
		MatchLiveRequest request = new MatchLiveRequest();
		EdiResponse<List<MatchLive>> execute = client.matchLive(request);
		printJson(execute);
	}
	
	@Test
	public void matchTrendDetail() {
		MatchTrendDetailRequest request = new MatchTrendDetailRequest();
		request.setId(1);
		EdiResponse<TrendDetail> execute = client.matchTrendDetail(request);
		printJson(execute);
	}
	
	@Test
	public void matchShootPoint() {
		MatchShootPointRequest request = new MatchShootPointRequest();
		request.setId(1);
		EdiResponse<List<List<Object>>> execute = client.matchShootPoint(request);
		printJson(execute);
	}
	
	@Test
	public void matchStreamUrlsFree() {
		MatchStreamUrlsFreeRequest request = new MatchStreamUrlsFreeRequest();
		EdiResponse<List<UrlsFree>> execute = client.matchStreamUrlsFree(request);
		printJson(execute);
	}
	
	@Test
	public void matchVideoCollection() {
		MatchStreamVideoCollectionRequest request = new MatchStreamVideoCollectionRequest();
		request.setId(1);
		EdiResponse<List<VideoCollection>> execute = client.matchVideoCollection(request);
		printJson(execute);
	}
	
	@Test
	public void basketballDeleted() {
		DeletedDataListRequest request = new DeletedDataListRequest();
		EdiResponse<List<DeletedData>> execute = client.basketballDeleted(request);
		printJson(execute);
	}
}
