package com.edi.sdk.basketball;

import java.util.List;
import java.util.Map;


import com.alibaba.fastjson2.JSON;
import com.edi.sdk.basketball.domain.*;
import com.edi.sdk.basketball.request.*;
import com.edi.sdk.core.EdiResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 2023年5月23日 下午1:34:39
 */
public class BasketballBaseDataClientTests {
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
	public void categoryList() {
		CategoryListRequest request = new CategoryListRequest();
		EdiResponse<List<Category>> execute = client.categoryList(request);
		printJson(execute);
	}

	@Test
	public void competitionList() {
		CompetitionListRequest request = new CompetitionListRequest();
		// request.setId(0);
		// request.setLimit(10);
		// request.setTime(946656000);
		EdiResponse<List<Competition>> execute = client.competitionList(request);
		printJson(execute);
	}

	@Test
	public void teamList() {
		TeamListRequest request = new TeamListRequest();
		// request.setId(0);
		// request.setLimit(10);
		// request.setTime(946656000);
		EdiResponse<List<Team>> execute = client.teamList(request);
		printJson(execute);
	}

	@Test
	public void playerList() {
		PlayerListRequest request = new PlayerListRequest();
		// request.setId(0);
		// request.setLimit(10);
		// request.setTime(946656000);
		EdiResponse<List<Player>> execute = client.playerList(request);
		printJson(execute);
	}

	@Test
	public void coachList() {
		CoachListRequest request = new CoachListRequest();
		// request.setId(0);
		// request.setLimit(10);
		// request.setTime(946656000);
		EdiResponse<List<Coach>> execute = client.coachList(request);
		printJson(execute);
	}

	@Test
	public void venueList() {
		VenueListRequest request = new VenueListRequest();
		// request.setId(0);
		// request.setLimit(10);
		// request.setTime(946656000);
		EdiResponse<List<Venue>> execute = client.venueList(request);
		printJson(execute);
	}

	@Test
	public void honorList() {
		HonorListRequest request = new HonorListRequest();
		// request.setId(0);
		// request.setLimit(10);
		// request.setTime(946656000);
		EdiResponse<List<Honor>> execute = client.honorList(request);
		printJson(execute);
	}

	@Test
	public void seasonList() {
		SeasonListRequest request = new SeasonListRequest();
		// request.setId(0);
		// request.setLimit(10);
		// request.setTime(946656000);
		EdiResponse<List<Season>> execute = client.seasonList(request);
		printJson(execute);
	}

	@Test
	public void stageList() {
		StageListRequest request = new StageListRequest();
		// request.setId(0);
		// request.setLimit(10);
		request.setTime(1685593210);
		EdiResponse<List<Stage>> execute = client.stageList(request);
		printJson(execute);
	}

	@Test
	public void dataMoreUpdate() {
		DataMoreUpdateRequest request = new DataMoreUpdateRequest();
		EdiResponse<Map<String, List<DataUpdate>>> execute = client.dataMoreUpdate(request);
		printJson(execute);
	}
}
