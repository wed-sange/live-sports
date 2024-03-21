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
public class BasketballHigherDataClientTests {
	private final String user = "scinf";
	private final String secret = "8a9e44252deb727299984577263b809b";
	private BasketballClient client;

	@BeforeAll
	public void init() {
		client = new BasketballClient(user, secret, "https://nami.slscore.cn");
	}

	private void printJson(Object obj) {
		System.out.println(obj);
		System.out.println(JSON.toJSONString(obj));
	}

	@Test
	public void intelligenceListTest() {
		IntelligenceListRequest request = new IntelligenceListRequest();
//		request.setId(100);
		// request.setLimit(10);
		// request.setTime(946656000);
		EdiResponse<List<Intelligence>> execute = client.intelligence(request);
		printJson(execute);
	}


	@Test
	public void teamSquadListTest() {
		TeamSquadListRequest request = new TeamSquadListRequest();
//		request.setId(100);
		 request.setLimit(2);
		// request.setTime(946656000);
		EdiResponse<List<TeamSquad>> execute = client.teamSquad(request);
		printJson(execute);
	}

	@Test
	public void teamInjuryListTest() {
		TeamInjuryListRequest request = new TeamInjuryListRequest();
//		request.setId(100);
		request.setLimit(2);
		// request.setTime(946656000);
		EdiResponse<List<TeamInjury>> execute = client.teamInjury(request);
		printJson(execute);
	}

	@Test
	public void teamHonorListTest() {
		TeamHonorListRequest request = new TeamHonorListRequest();
//		request.setId(100);
		request.setLimit(2);
		// request.setTime(946656000);
		EdiResponse<List<TeamHonor>> execute = client.teamHonor(request);
		printJson(execute);
	}

	@Test
	public void playerHonorListTest() {
		PlayerHonorListRequest request = new PlayerHonorListRequest();
//		request.setId(100);
		request.setLimit(2);
		// request.setTime(946656000);
		EdiResponse<List<PlayerHonor>> execute = client.playerHonor(request);
		printJson(execute);
	}

	@Test
	public void playerCareerListTest() {
		PlayerCareerListRequest request = new PlayerCareerListRequest();
//		request.setId(100);
		request.setLimit(2);
		// request.setTime(946656000);
		EdiResponse<List<PlayerCareer>> execute = client.playerCareer(request);
		printJson(execute);
	}

	@Test
	public void rankingFibaMenTest() {
		RankingFibaMenRequest request = new RankingFibaMenRequest();
		// request.setPubTime(946656000);
		EdiResponse<RankingFibaMen> execute = client.rankingFibaMen(request);
		printJson(execute);
	}

	@Test
	public void rankingFibaWomenTest() {
		RankingFibaWomenRequest request = new RankingFibaWomenRequest();
		// request.setPubTime(946656000);
		EdiResponse<RankingFibaWomen> execute = client.rankingFibaWomen(request);
		printJson(execute);
	}


}
